package cn.gov.baiyin.court.core.entity;

import cn.gov.baiyin.court.core.annotations.SqlIgnore;

/**
 * 考试场次
 * Created by WK on 2017/3/28.
 */
public class ESession extends BaseEntity {

    private Integer eid;
    private String name;
    private String topics;
    private String startTime;
    private String endTime;
    private Examine examine;

    public Examine getExamine() {
        return examine;
    }

    @SqlIgnore
    public void setExamine(Examine examine) {
        this.examine = examine;
    }

    public Integer getEid() {
        return eid;
    }

    public void setEid(Integer eid) {
        this.eid = eid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getTopics() {
        return topics;
    }

    public void setTopics(String topics) {
        this.topics = topics;
    }

    @Override
    public String getTableName() {
        return "esession";
    }
}
