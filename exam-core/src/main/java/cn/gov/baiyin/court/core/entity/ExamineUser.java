package cn.gov.baiyin.court.core.entity;

/**
 * 考试-考生对应表
 * Created by WK on 2017/3/27.
 */
public class ExamineUser extends BaseEntity {

    private Integer eid;//考试场次编号
    private Integer uid;//考生编号

    public ExamineUser(Integer eid, Integer uid) {

        this.eid = eid;
        this.uid = uid;
    }

    public Integer getEid() {
        return eid;
    }

    public void setEid(Integer eid) {
        this.eid = eid;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    @Override
    public String getTableName() {
        return "examine_user";
    }
}
