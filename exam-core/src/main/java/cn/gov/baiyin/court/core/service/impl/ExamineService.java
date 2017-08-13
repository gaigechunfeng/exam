package cn.gov.baiyin.court.core.service.impl;

import cn.gov.baiyin.court.core.dao.IExamineDAO;
import cn.gov.baiyin.court.core.entity.*;
import cn.gov.baiyin.court.core.exception.ServiceException;
import cn.gov.baiyin.court.core.service.*;
import cn.gov.baiyin.court.core.util.PageInfo;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by WK on 2017/3/26.
 */
@Service
public class ExamineService implements IExamineService {

    private IExamineDAO examineDAO;
    private ITopicService topicService;
    private IExamineTopicService examineTopicService;
    private IExamineUserService examineUserService;
    private IEsessionService esessionService;

    @Autowired
    public void setEsessionService(IEsessionService esessionService) {
        this.esessionService = esessionService;
    }

    @Autowired
    public void setExamineDAO(IExamineDAO examineDAO) {
        this.examineDAO = examineDAO;
    }

    @Autowired
    public void setTopicService(ITopicService topicService) {
        this.topicService = topicService;
    }

    @Autowired
    public void setExamineTopicService(IExamineTopicService examineTopicService) {
        this.examineTopicService = examineTopicService;
    }

    @Autowired
    public void setExamineUserService(IExamineUserService examineUserService) {
        this.examineUserService = examineUserService;
    }

    @Override
    public PageInfo listPagination(PageInfo pageInfo) {

        return examineDAO.listPagination(pageInfo);
    }

    @Override
    @Transactional
    public void add(Examine examine, String topicIds, String esessionIds) throws ServiceException {

        if (StringUtils.isEmpty(examine.getName())) {
            throw new ServiceException("参数错误！");
        }

        if (examineDAO.findByName(examine.getName()) != null) {
            throw new ServiceException("名称为[" + examine.getName() + "]的考试已存在！");
        }
        autoCalculateScore(examine, topicIds);

        boolean r = examineDAO.add(examine);
        if (!r) {
            throw new ServiceException("操作结果为0！");
        }

        Examine added = examineDAO.findByName(examine.getName());
        addTopicRelation(added.getId(), topicIds);
        addEsessionRelation(added.getId(), esessionIds);
        applyTopic2Esession(examine, topicIds, esessionIds);
    }

    private void applyTopic2AddedEsession(Examine examine, String topicIds, String esessionIds, List<Integer> addEsIds) throws ServiceException {

        if (StringUtils.isEmpty(topicIds) || CollectionUtils.isEmpty(addEsIds)) {
            return;
        }
        if (examine.getType() != null && examine.getType() == 2) {

            List<Topic> topics = topicService.findByIds(topicIds);
            String[] used = StringUtils.isEmpty(esessionIds) ? new String[0] :
                    esessionService.findByIds(esessionIds).stream()
                            .map(ESession::getTopics)
                            .reduce((s1, s2) -> s1 + "," + s2)
                            .orElse("").split(",");
            List<Topic> avilable = topics.stream()
                    .filter(topic -> !ArrayUtils.contains(used, topic.getId() + ""))
                    .collect(Collectors.toList());
            if (CollectionUtils.isEmpty(avilable)) {
                throw new RuntimeException("\u6ca1\u6709\u591a\u4f59\u7684\u9898\u76ee\u53ef\u4f9b\u5206\u914d\uff01");
            }
            List<Map<String, Object>> l1 = avilable.stream()
                    .filter(topic -> topic.getType() != null && topic.getType() == 1)
                    .map(topic -> {
                        Map<String, Object> m = new HashMap<>();
                        m.put("id", topic.getId());
                        m.put("use", false);
                        return m;
                    }).collect(Collectors.toList());
            List<Map<String, Object>> l2 = avilable.stream()
                    .filter(topic -> topic.getType() != null && topic.getType() == 2)
                    .map(topic -> {
                        Map<String, Object> m = new HashMap<>();
                        m.put("id", topic.getId());
                        m.put("use", false);
                        return m;
                    }).collect(Collectors.toList());
            if (addEsIds.size() > Math.min(l1.size(), l2.size())) {
                throw new RuntimeException("\u6ca1\u6709\u8db3\u591f\u7684\u8bd5\u9898\u53ef\u4f9b\u5206\u914d\u7ed9\u8003\u573a\uff01");
            }

            List<ESession> eSessions = esessionService.findByIds(addEsIds.
                    stream()
                    .map(integer -> integer + "")
                    .reduce((i1, i2) -> i1 + "," + i2)
                    .orElse(""));

            for (int i = 0, len = eSessions.size(); i < len; i++) {
                List<Map<String, Object>> usableList1 = l1.stream()
                        .filter(m -> !MapUtils.getBooleanValue(m, "use", false))
                        .collect(Collectors.toList());
                List<Map<String, Object>> usableList2 = l2.stream()
                        .filter(m -> !MapUtils.getBooleanValue(m, "use", false))
                        .collect(Collectors.toList());
                if (StringUtils.isEmpty(usableList1) || StringUtils.isEmpty(usableList2)) {
                    throw new RuntimeException("\u53ef\u4f9b\u5206\u914d\u7684\u9898\u76ee\u4e0d\u8db3\uff01");
                }
                Map<String, Object> m1 = usableList1.get(RandomUtils.nextInt(usableList1.size()));
                Map<String, Object> m2 = usableList2.get(RandomUtils.nextInt(usableList2.size()));
                int id1 = (int) m1.get("id");
                int id2 = (int) m2.get("id");
                ESession eSession = eSessions.get(i);
                eSession.setTopics(id1 + "," + id2);
                esessionService.edit(eSession);

                m1.put("use", true);
                m2.put("use", true);
            }
        }
    }

    private void applyTopic2Esession(Examine examine, String topicIds, String esessionIds) throws ServiceException {
        if (StringUtils.isEmpty(topicIds) || StringUtils.isEmpty(esessionIds)) {
            return;
        }
        if (examine.getType() != null && examine.getType() == 2) {
            List<Topic> topics = topicService.findByIds(topicIds);
            List<Map<String, Object>> t1s = topics.stream()
                    .filter(topic -> topic.getType() != null && topic.getType() == 1)
                    .map(topic -> {
                        Map<String, Object> m = new HashMap<>();
                        m.put("id", topic.getId());
                        m.put("use", false);
                        return m;
                    }).collect(Collectors.toList());
            List<Map<String, Object>> t2s = topics.stream()
                    .filter(topic -> topic.getType() != null && topic.getType() == 2)
                    .map(topic -> {
                        Map<String, Object> m = new HashMap<>();
                        m.put("id", topic.getId());
                        m.put("use", false);
                        return m;
                    })
                    .collect(Collectors.toList());
            List<ESession> eSessions = esessionService.findByIds(esessionIds);

            for (int i = 0, len = eSessions.size(); i < len; i++) {

                List<Map<String, Object>> usableList1 = t1s.stream()
                        .filter(m -> !MapUtils.getBooleanValue(m, "use", false))
                        .collect(Collectors.toList());
                List<Map<String, Object>> usableList2 = t2s.stream()
                        .filter(m -> !MapUtils.getBooleanValue(m, "use", false))
                        .collect(Collectors.toList());
                if (StringUtils.isEmpty(usableList1) || StringUtils.isEmpty(usableList2)) {
                    throw new RuntimeException("\u53ef\u4f9b\u5206\u914d\u7684\u9898\u76ee\u4e0d\u8db3\uff01");
                }
                Map<String, Object> m1 = usableList1.get(RandomUtils.nextInt(usableList1.size()));
                Map<String, Object> m2 = usableList2.get(RandomUtils.nextInt(usableList2.size()));
                int id1 = (int) m1.get("id");
                int id2 = (int) m2.get("id");
                ESession eSession = eSessions.get(i);
                eSession.setTopics(id1 + "," + id2);
                esessionService.edit(eSession);

                m1.put("use", true);
                m2.put("use", true);
            }
        }
    }

    private void autoCalculateScore(Examine examine, String topicIds) {
        int score = 0;

        if (!StringUtils.isEmpty(topicIds)) {
            List<Topic> topics = topicService.findByIds(topicIds);
            if (!CollectionUtils.isEmpty(topics)) {
                boolean isRandom = examine.getType() != null && examine.getType() == 2;
                if (isRandom) {
                    score = topics.stream()
                            .collect(Collectors.toMap(Topic::getType, Topic::getScore, Integer::max))
                            .values().stream()
                            .reduce((v1, v2) -> v1 + v2)
                            .orElse(0);
                } else {
                    score = topics.stream()
                            .map(Topic::getScore)
                            .reduce((i1, i2) -> i1 + i2)
                            .orElse(0);
                }
            }
        }
        examine.setScore(score);
    }

    private void addEsessionRelation(int eid, String esessionIds) throws ServiceException {
        if (!StringUtils.isEmpty(esessionIds)) {
            String[] ids = esessionIds.split(",");
            for (String sid : ids) {
                esessionService.applyExamine(sid, eid);
            }
        }
    }

    private void addTopicRelation(int eid, String topicIds) throws ServiceException {
        if (!StringUtils.isEmpty(topicIds)) {
            String[] ids = topicIds.split(",");
            for (String id : ids) {
                Topic topic = topicService.findById(id);
                if (topic != null) {
                    examineTopicService.addByEidAndTid(eid, topic.getId());
                }
            }
        }
    }

    @Override
    public Map<String, Object> detail(String id) throws ServiceException {

        if (StringUtils.isEmpty(id)) {
            throw new ServiceException("参数错误！[id]");
        }
        Examine examine = examineDAO.findById(Integer.parseInt(id));
        if (examine == null) {
            throw new ServiceException("未找到试卷对象！[id:" + id + "]");
        }
        List<Topic> topics = examineDAO.findTopicsByEid(examine.getId());

        Map<String, Object> data = new HashMap<>();
        data.put("examine", examine);
        data.put("topics", topics);
        data.put("esessions", esessionService.findByEid(examine.getId()));
        return data;
    }

    @Override
    @Transactional
    public void deleteById(String id) throws ServiceException {

        if (StringUtils.isEmpty(id)) {
            throw new ServiceException("参数错误！");
        }

        int intId = Integer.parseInt(id);
        examineDAO.deleteById(intId);
        examineTopicService.removeByEid(intId);
        esessionService.removeByEid(intId);
        examineUserService.removeByEid(intId);
    }

    @Override
    @Transactional
    public void edit(Examine examine, String topicIds, String esessionIds) throws ServiceException {

        if (StringUtils.isEmpty(examine.getId()) || StringUtils.isEmpty(examine.getName())) {
            throw new ServiceException("参数错误！");
        }


        Examine persist = examineDAO.findById(examine.getId());

        BeanUtils.copyProperties(persist, examine, "name", "score", "type");

        autoCalculateScore(examine, topicIds);
        int r = examineDAO.edit(examine);
        if (r == 0) {
            throw new ServiceException("操作结果为空！");
        }

        if (!StringUtils.isEmpty(topicIds)) {
            List<Topic> originTopics = examineTopicService.findByEid(examine.getId());
            String[] newTopicIds = topicIds.split(",");
            int originSize = originTopics.size();

            Integer[] originIds = originTopics.stream().map(BaseEntity::getId).toArray(Integer[]::new);

            {
                for (int i = 0; i < newTopicIds.length; i++) {
                    Integer id = Integer.parseInt(newTopicIds[i]);
                    if (ArrayUtils.contains(originIds, id)) {//EDIT
                        //ignore...
                    } else {//ADD
                        addTopicRelation(examine.getId(), id + "");
                    }
                }
            }

            //DEL
            {

                for (int i = 0; i < originSize; i++) {
                    Integer id = originTopics.get(i).getId();
                    if (!ArrayUtils.contains(newTopicIds, id + "")) {
                        examineTopicService.removeRelationByEidAndTid(examine.getId(), id);
                    }
                }
            }
        }

        if (!StringUtils.isEmpty(esessionIds)) {
            List<ESession> originEsessions = esessionService.findByEid(examine.getId());
            String[] newEsessionIds = esessionIds.split(",");
            int originSize = originEsessions.size();
            Integer[] originIds = originEsessions.stream().map(BaseEntity::getId).toArray(Integer[]::new);

            {
                List<Integer> addList = new ArrayList<>();
                for (int i = 0; i < newEsessionIds.length; i++) {
                    Integer id = Integer.parseInt(newEsessionIds[i]);
                    if (ArrayUtils.contains(originIds, id)) {//EDIT
                        //ignore...
                    } else {//ADD
                        addEsessionRelation(examine.getId(), id + "");
                        addList.add(id);
                    }
                }
                applyTopic2AddedEsession(examine, topicIds, esessionIds, addList);
            }

            //DEL
            {

                for (int i = 0; i < originSize; i++) {
                    Integer id = originEsessions.get(i).getId();
                    if (!ArrayUtils.contains(newEsessionIds, id + "")) {
                        esessionService.resetById(id);
                    }
                }
            }
        }

//        applyTopic2Esession(examine, topicIds, esessionIds);
    }


    @Override
    public void changeKsxz(String ksxz, String kssm) throws ServiceException {

        if (StringUtils.isEmpty(ksxz) || StringUtils.isEmpty(kssm)) {
            throw new ServiceException("考试须知和考试说明不能为空！");
        }
        examineDAO.saveKsxz(ksxz, kssm);
    }

    @Override
    public ExamineInfo findKsxz() {
        return examineDAO.findKsxz();
    }

    @Override
    public ExamineUser findEUByEidAndUname(Integer eid, String username) {
        return examineDAO.findEUByEidAndUname(eid, username);
    }

    @Override
    public Examine findById(Integer eid) {
        return examineDAO.findById(eid);
    }

    @Override
    public ExamineUser findEUByEidAndUid(Integer eid, Integer uid) {
        return examineDAO.findEUByEidAndUid(eid, uid);
    }
}
