package cn.gov.baiyin.court.core.entity;

/**
 * 考试须知/考试说明
 * Created by gaige on 2017/4/18.
 */
public class ExamineInfo extends BaseEntity {

    private String ksxz;
    private String kssm;

    @Override
    public String getTableName() {
        return "examine_info";
    }

    public String getKsxz() {
        return ksxz;
    }

    public void setKsxz(String ksxz) {
        this.ksxz = ksxz;
    }

    public String getKssm() {
        return kssm;
    }

    public void setKssm(String kssm) {
        this.kssm = kssm;
    }
}
