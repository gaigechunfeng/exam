package cn.gov.baiyin.court.core.entity;

import cn.gov.baiyin.court.core.util.CodeUtil;

import java.math.BigDecimal;

/**
 * 分数
 * Created by WK on 2017/3/25.
 */
public class Score extends BaseEntity {

    private Integer rid;//reply id
    private Float accuracy;//正确率，保留小数点后两位
    private Float score;//得分，

    public Score(Integer rid, float accuracy, float score) {
        this.rid = rid;
        this.accuracy = new BigDecimal(accuracy).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
        this.score = new BigDecimal(score).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
//        this.score = CodeUtil.encrypt(String.valueOf(score));
    }

    public Integer getRid() {
        return rid;
    }

    public void setRid(Integer rid) {
        this.rid = rid;
    }

    public Float getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(Float accuracy) {
        this.accuracy = accuracy;
    }

    public Float getScore() {
        return score;
    }

    public void setScore(Float score) {
        this.score = score;
    }

    @Override
    public String getTableName() {
        return "score";
    }
}
