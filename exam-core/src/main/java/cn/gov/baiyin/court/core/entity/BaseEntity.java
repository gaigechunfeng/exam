package cn.gov.baiyin.court.core.entity;

/**
 * Created by WK on 2017/3/24.
 */
public abstract class BaseEntity {

    private Integer id;//ID,主键，自增
    private String crtime;//创建时间
    private String cruser;//创建用户

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCrtime() {
        return crtime;
    }

    public void setCrtime(String crtime) {
        this.crtime = crtime;
    }

    public String getCruser() {
        return cruser;
    }

    public void setCruser(String cruser) {
        this.cruser = cruser;
    }

    public abstract String getTableName();
}
