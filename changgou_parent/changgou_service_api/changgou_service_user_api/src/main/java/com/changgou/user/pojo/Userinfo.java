package com.changgou.user.pojo;

import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * address实体类
 *
 * @author 黑马架构师2.5
 */
@Table(name = "tb_userinfo")
public class Userinfo implements Serializable {


    @Id
    private String id;
    private String nickname;
    private String sex;
    private String proviced;
    private String cities;
    private String areas;
    private String zhiye;
    private String img;
    private String username;

    public Userinfo(String id, String nickname, String sex, String proviced, String cities, String areas, String zhiye, String img, String username) {
        this.id = id;
        this.nickname = nickname;
        this.sex = sex;
        this.proviced = proviced;
        this.cities = cities;
        this.areas = areas;
        this.zhiye = zhiye;
        this.img = img;
        this.username = username;
    }

    @Override
    public String toString() {
        return "Userinfo{" +
                "id='" + id + '\'' +
                ", nickname='" + nickname + '\'' +
                ", sex='" + sex + '\'' +
                ", proviced='" + proviced + '\'' +
                ", cities='" + cities + '\'' +
                ", areas='" + areas + '\'' +
                ", zhiye='" + zhiye + '\'' +
                ", img='" + img + '\'' +
                ", username='" + username + '\'' +
                '}';
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
/*`id` varchar(255) NOT NULL COMMENT 'id',
            `nickname` varchar(255) DEFAULT NULL COMMENT '昵称',
            `sex` varchar(255) DEFAULT NULL COMMENT '性别',
            `proviced` varchar(255) DEFAULT NULL COMMENT '省',
            `cities` varchar(255) DEFAULT NULL COMMENT '市',
            `areas` varchar(255) DEFAULT NULL COMMENT '区',
            `zhiye` varchar(255) DEFAULT NULL COMMENT '职业',
            img
*/

    public Userinfo() {
    }

    public Userinfo(String id, String nickname, String sex, String proviced, String cities, String areas, String zhiye, String img) {
        this.id = id;
        this.nickname = nickname;
        this.sex = sex;
        this.proviced = proviced;
        this.cities = cities;
        this.areas = areas;
        this.zhiye = zhiye;
        this.img = img;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getProviced() {
        return proviced;
    }

    public void setProviced(String proviced) {
        this.proviced = proviced;
    }

    public String getCities() {
        return cities;
    }

    public void setCities(String cities) {
        this.cities = cities;
    }

    public String getAreas() {
        return areas;
    }

    public void setAreas(String areas) {
        this.areas = areas;
    }

    public String getZhiye() {
        return zhiye;
    }

    public void setZhiye(String zhiye) {
        this.zhiye = zhiye;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
