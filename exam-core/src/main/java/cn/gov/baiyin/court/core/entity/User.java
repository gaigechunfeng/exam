package cn.gov.baiyin.court.core.entity;

/**
 * 登录用户（考生和管理员）
 * Created by WK on 2017/3/25.
 */
public class User extends BaseEntity {

    @Override
    public String getTableName() {
        return "user";
    }

    public static enum UserType {
        //考生
        EXAMINEE(1),
        //管理员
        ADMIN(2);

        private int typeVal;

        UserType(int i) {
            typeVal = i;
        }

        public int getTypeVal() {
            return typeVal;
        }
    }

    public static enum UserSex {
        //男
        MALE(1),
        //女
        FEMALE(2);

        int sexVal;

        UserSex(int i) {

            sexVal = i;
        }

        public int getSexVal() {
            return sexVal;
        }
    }

    private String username;//用户名，考生就是准考证号，管理员就是登录账户
    private String password;//密码
    private String photo;//头像，图片路径
    private String name;//真实姓名
    private String idcard;//身份证号
    private Integer permission;//权限值（保留）
    private Integer type;//类型
    private Integer sex;//性别
    private String pos;//岗位代码

    public String getIdcard() {
        return idcard;
    }

    public void setIdcard(String idcard) {
        this.idcard = idcard;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPermission() {
        return permission;
    }

    public void setPermission(Integer permission) {
        this.permission = permission;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public String getPos() {
        return pos;
    }

    public void setPos(String pos) {
        this.pos = pos;
    }
}
