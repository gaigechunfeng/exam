package cn.gov.baiyin.court.core.entity;

/**
 * 考试-题目对应
 * Created by WK on 2017/3/24.
 */
public class ExamineTopic extends BaseEntity {

    private Integer eid;//考试
    private Integer tid;//题目

    public ExamineTopic() {

    }

    public ExamineTopic(int eid, int tid) {
        super();
        this.eid = eid;
        this.tid = tid;
    }

    public Integer getEid() {
        return eid;
    }

    public void setEid(Integer eid) {
        this.eid = eid;
    }

    public Integer getTid() {
        return tid;
    }

    public void setTid(Integer tid) {
        this.tid = tid;
    }

    @Override
    public String getTableName() {
        return "examine_topic";
    }
}
