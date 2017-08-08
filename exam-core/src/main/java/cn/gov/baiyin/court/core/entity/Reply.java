package cn.gov.baiyin.court.core.entity;

/**
 * 答题人写的答案
 * Created by WK on 2017/3/27.
 */
public class Reply extends BaseEntity {

    private Integer etid;//考试-题目 ID
    private Integer uid;//答题人 ID
    private String answer;//答题人写的答案

    public Reply() {
    }

    public Reply(Integer examineTopicId, Integer userId, String answer) {
        this.etid = examineTopicId;
        this.uid = userId;
        this.answer = answer;
    }

    public Integer getEtid() {
        return etid;
    }

    public void setEtid(Integer etid) {
        this.etid = etid;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    @Override
    public String getTableName() {
        return "reply";
    }
}
