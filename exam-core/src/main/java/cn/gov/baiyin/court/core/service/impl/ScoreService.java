package cn.gov.baiyin.court.core.service.impl;

import cn.gov.baiyin.court.core.authc.Authc;
import cn.gov.baiyin.court.core.dao.IScoreDAO;
import cn.gov.baiyin.court.core.entity.*;
import cn.gov.baiyin.court.core.exception.ServiceException;
import cn.gov.baiyin.court.core.service.*;
import cn.gov.baiyin.court.core.util.CosineSimilarAlgorithm;
import cn.gov.baiyin.court.core.util.PageInfo;
import cn.gov.baiyin.court.core.util.Utils;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by WK on 2017/3/30.
 */
@Service
public class ScoreService implements IScoreService {

    private static final String LISTEN_ANSWER_PATTERN = "[^0-9a-zA-Z\\u4E00-\\u9FA5\\uF900-\\uFA2D]+";
    private IScoreDAO scoreDAO;
    private IExamineTopicService examineTopicService;
    private IUserService userService;
    //    private TextSimilarity textSimilarity = new SimpleTextSimilarity();
    private ITopicService topicService;
    private IExamineService examineService;
    private IEsessionService esessionService;


    private static final int REF_COUNT = Utils.getApp().getInt("reference.count", 120);

    @Autowired
    public void setEsessionService(IEsessionService esessionService) {
        this.esessionService = esessionService;
    }

    @Autowired
    public void setExamineService(IExamineService examineService) {
        this.examineService = examineService;
    }

    @Autowired
    public void setTopicService(ITopicService topicService) {
        this.topicService = topicService;
    }

//    @Autowired
//    public void setTextSimilarity(TextSimilarity textSimilarity) {
//        this.textSimilarity = textSimilarity;
//    }

    @Autowired
    public void setUserService(IUserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setExamineTopicService(IExamineTopicService examineTopicService) {
        this.examineTopicService = examineTopicService;
    }

    @Autowired
    public void setScoreDAO(IScoreDAO scoreDAO) {
        this.scoreDAO = scoreDAO;
    }

    @PostConstruct
    public void initTextSimilarity() {

//        textSimilarity.similarScore("", "");
    }

    @Override
    @Transactional
    public Score submitAnswer(String answer, Integer speed, Float accuracy, Integer tid, Integer eid) throws ServiceException {

        if (answer == null || tid == null || eid == null) {
            throw new ServiceException("参数错误！");
        }
        ExamineTopic examineTopic = examineTopicService.findByEidAndTid(eid, tid);
        if (examineTopic == null) {
            throw new ServiceException("试卷与题目不匹配！");
        }
        Topic topic = topicService.findById(tid);
        User user = userService.findByUserName(Authc.getCurrUserName());

        Reply reply = new Reply(examineTopic.getId(), user.getId(), answer);
        addReply(reply);

        Score score;
        if (topic.getType() == Topic.Type.LOOK.getTypeVal()) {
            if (speed == null || accuracy == null) {
                throw new ServiceException("参数错误！");
            }
            float s = Math.min(accuracy * speed * topic.getScore() / REF_COUNT / 100, topic.getScore());
            score = new Score(reply.getId(), accuracy, s);
        } else {
            accuracy = CosineSimilarAlgorithm.levenshtein(answer.replaceAll(LISTEN_ANSWER_PATTERN, ""),
                    topic.getAnswer().replaceAll(LISTEN_ANSWER_PATTERN, ""));
            score = new Score(reply.getId(), accuracy, Math.min(topic.getScore() * accuracy, topic.getScore()));
        }
        boolean r = scoreDAO.add(score);
        if (!r) {
            throw new ServiceException("保存分数失败！");
        }
        return score;
    }

    private void addReply(Reply reply) throws ServiceException {

        Reply r = findReplyByEtidAndUid(reply.getEtid(), reply.getUid());
        if (r != null) {
//            throw new ServiceException("此题答案已经提交，不能重复提交");
            scoreDAO.removeByEtidAndUid(reply.getEtid(), reply.getUid());
        }

        scoreDAO.addReply(reply);
        reply.setId(scoreDAO.findReplyByEtidAndUid(reply.getEtid(), reply.getUid()).getId());
    }

    @Override
    public Reply findReplyByEtidAndUid(Integer etid, Integer uid) {

        return scoreDAO.findReplyByEtidAndUid(etid, uid);
    }

    @Override
    public PageInfo listPagination(PageInfo pageInfo, String pos, Integer eid) {

        Examine examine = examineService.findById(eid);
        boolean isRandom = examine.getType() == 2;

        pageInfo = scoreDAO.listPagination(pageInfo, pos, eid);
        List<Topic> topics = topicService.listFieldsByEid(eid);

        List<Integer> topicIds;
        if (isRandom) {
            topicIds = new ArrayList<>();
            topicIds.add(1);
            topicIds.add(2);
        } else {
            topicIds = topics.stream().map(Topic::getId).collect(Collectors.toList());
        }

        List<Map<String, Object>> list = new ArrayList<>();

        Map<String, Object> scoreMap = isRandom ? scoreDAO.queryRandomScoreMap(eid) : scoreDAO.queryScoreMap(eid);
        for (Object o : pageInfo.getList()) {
            User user = (User) o;
            Map<String, Object> m = new HashMap<>();

            m.put("id", user.getId());
            m.put("name", user.getName());
            m.put("username", user.getUsername());
            m.put("pos", user.getPos());

            float count = 0;
            for (int i = 0; i < topicIds.size(); i++) {
                Integer key = topicIds.get(i);
//                float score = findScoreInMap(key, scoreMap);
                float score = MapUtils.getFloat(scoreMap, key + "-" + user.getId(), 0F);

                count += score;
                m.put("score_" + i, score);
            }
            m.put("allscore", new BigDecimal(count).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue());
            m.put("fieldSize", topicIds.size());
            list.add(m);
        }

        list.sort((o1, o2) -> ((Float) o2.get("allscore")).compareTo((Float) o1.get("allscore")));
        pageInfo.setList(list);
        return pageInfo;
    }

    @Override
    public Map<String, Object> detail(Integer uid, Integer eid) throws ServiceException {

        List<Map<String, Object>> list = scoreDAO.detail(uid, eid);
        if (CollectionUtils.isEmpty(list)) {
            throw new ServiceException("未查到分数信息！");
        }
        Map<String, Object> obj = new HashMap<>();
        Map<String, Object> scoreMap = new HashMap<>();

        float allscore = 0;
        for (int i = 0; i < list.size(); i++) {
            Map<String, Object> m = list.get(i);

            if (i == 0) {
                obj.put("uid", m.get("uid"));
                obj.put("eid", m.get("eid"));
                obj.put("name", m.get("name"));
                obj.put("username", m.get("username"));
                obj.put("startTime", m.get("startTime"));
                obj.put("photo", m.get("photo"));
            }
            float score = new BigDecimal(MapUtils.getFloatValue(m, "score", 0F))
                    .setScale(2, BigDecimal.ROUND_HALF_UP)
                    .floatValue();
            allscore += score;

            scoreMap.put(m.get("topicname").toString(), score);
        }
        obj.put("scores", scoreMap);
        obj.put("allscore", allscore);
        return obj;
    }

    @Override
    public Map<String, Double> statistic(Integer eid, String pos) {

//        List<Map<String, Object>> list = scoreDAO.statistic(eid);
//        Map<String, Object> map = new HashMap<>();
//
//        if (!CollectionUtils.isEmpty(list)) {
//            for (Map<String, Object> m : list) {
//                map.put(m.get("name").toString(), new BigDecimal((Double) m.get("score")).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue());
//            }
//        }
//        return map;
        return scoreDAO.statistic(eid, pos);
    }

    @Override
    public Map<String, Object> frontDetail(Integer eid) throws ServiceException {
        User currUser = userService.findByUserName(Authc.getCurrUserName());
        return detail(currUser.getId(), eid);
    }

    @Override
    @Transactional
    public void reExam(Integer uid, Integer eid) throws ServiceException {

        if (uid == null || uid == 0 || eid == null || eid == 0) {
            throw new ServiceException("uid或eid错误");
        }
        scoreDAO.removeByUidAndEid(uid, eid);

        scoreDAO.removeReplyByUidAndEid(uid, eid);

        esessionService.clearExamInfo(eid, uid);
    }

    @Override
    public String exportDb2Sql() {
        return scoreDAO.exportDb2Sql();
    }

    private float findScoreInMap(String s, Map<String, Object> scoreMap) {

        float score = 0;

        Object obj = null;
        for (Map.Entry<String, Object> entry : scoreMap.entrySet()) {
            if (s.equalsIgnoreCase(entry.getKey())) {
                obj = entry.getValue();
                break;
            }
        }
        if (obj != null) {
            score = (Float) obj;
        }

        return score;
    }
}
