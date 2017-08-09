package cn.gov.baiyin.court.core.entity;

import cn.gov.baiyin.court.core.annotations.SqlIgnore;

import java.beans.Transient;
import java.util.List;

/**
 * 考试
 * Created by WK on 2017/3/24.
 */
public class Examine extends BaseEntity {

    private String name;//考试名称

    //    private String startTime;//考试开始时间，形如：2017-3-25 15:27:20
//    private String endTime;//考试结束时间，形如：2017-3-25 15:27:29
    private Integer score;//考试总分

    private Integer type; // 出题模式 1：普通模式；2：随机模式（从不同类型的试题中随机抽取一道）

    private List<ESession> eSessions;

    private List<Topic> topics;

    public List<Topic> getTopics() {
        return topics;
    }

    @SqlIgnore
    public void setTopics(List<Topic> topics) {
        this.topics = topics;
    }

    public List<ESession> geteSessions() {
        return eSessions;
    }

    @SqlIgnore
    public void seteSessions(List<ESession> eSessions) {
        this.eSessions = eSessions;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    //    public String getStartTime() {
//        return startTime;
//    }
//
//    public void setStartTime(String startTime) {
//        this.startTime = startTime;
//    }
//
//    public String getEndTime() {
//        return endTime;
//    }
//
//    public void setEndTime(String endTime) {
//        this.endTime = endTime;
//    }


    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    @Override
    public String getTableName() {
        return "examine";
    }
}
