package cn.gov.baiyin.court.core.entity;

/**
 * 题目
 * Created by WK on 2017/3/24.
 */
public class Topic extends BaseEntity {

    @Override
    public String getTableName() {
        return "topic";
    }

    public static enum Type {
        //看打题
        LOOK(1),
        //听打题
        LISTEN(2);

        int typeVal;

        Type(int i) {
            typeVal = i;
        }

        public int getTypeVal() {
            return typeVal;
        }
    }

    private Integer type;//题目类型，1：看打题目，2：听打题目
    private String name;//题目名称
    private String content;//题目内容，如果是看打题目则是文本内容，如果是听打题目则是音频文件路径
    private String answer;//题目答案
    private Integer period;//题目作答时间（分钟）
    private Integer score;//题目分数
    private Integer playtype;//听打题目播放方式，1：统一播放；2：各自播放

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getPeriod() {
        return period;
    }

    public void setPeriod(Integer period) {
        this.period = period;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPlaytype() {
        return playtype;
    }

    public void setPlaytype(Integer playtype) {
        this.playtype = playtype;
    }
}
