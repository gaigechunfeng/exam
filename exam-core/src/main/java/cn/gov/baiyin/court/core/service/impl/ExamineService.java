package cn.gov.baiyin.court.core.service.impl;

import cn.gov.baiyin.court.core.dao.IExamineDAO;
import cn.gov.baiyin.court.core.entity.*;
import cn.gov.baiyin.court.core.exception.ServiceException;
import cn.gov.baiyin.court.core.service.*;
import cn.gov.baiyin.court.core.util.PageInfo;
import org.apache.commons.lang.ArrayUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

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

//        examineTopicService.removeByEid(examine.getId());
//        esessionService.resetByEid(examine.getId());
//
//        Examine persist = examineDAO.findById(examine.getId());
//
//        BeanUtils.copyProperties(persist, examine, "name", "score");
//
//        int r = examineDAO.edit(examine);
//        if (r == 0) {
//            throw new ServiceException("操作结果为空！");
//        }
//
//        addTopicRelation(examine.getId(), topicIds);
//        addEsessionRelation(examine.getId(), esessionIds);

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
                for (int i = 0; i < newEsessionIds.length; i++) {
                    Integer id = Integer.parseInt(newEsessionIds[i]);
                    if (ArrayUtils.contains(originIds, id)) {//EDIT
                        //ignore...
                    } else {//ADD
                        addEsessionRelation(examine.getId(), id + "");
                    }
                }
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
}
