package org.graduate.usershare.data;

import javax.persistence.*;

/**
 * @author : LiuXianghai on 2021/2/23
 * @Created : 2021/02/23 - 9:50
 * @Project : usershare
 */
@Entity
@Table(name = "user_info", schema = "public", catalog = "recommend_system")
public class UserInfo {
    private long userId;

    private String userPhone;

    private String userEmail;

    private String userName;

    private String userSex;

    private String headImage;

    private String userPassword;

    @Id
    @Column(name = "user_id")
    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    @Basic
    @Column(name = "user_phone")
    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    @Basic
    @Column(name = "user_email")
    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    @Basic
    @Column(name = "user_name")
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Basic
    @Column(name = "user_sex")
    public String getUserSex() {
        return userSex;
    }

    public void setUserSex(String userSex) {
        this.userSex = userSex;
    }

    @Basic
    @Column(name = "head_image")
    public String getHeadImage() {
        return headImage;
    }

    public void setHeadImage(String headImage) {
        this.headImage = headImage;
    }

    @Basic
    @Column(name = "user_password")
    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserInfo userInfo = (UserInfo) o;

        if (userId != userInfo.userId) return false;
        if (userPhone != null ? !userPhone.equals(userInfo.userPhone) : userInfo.userPhone != null) return false;
        if (userEmail != null ? !userEmail.equals(userInfo.userEmail) : userInfo.userEmail != null) return false;
        if (userName != null ? !userName.equals(userInfo.userName) : userInfo.userName != null) return false;
        if (userSex != null ? !userSex.equals(userInfo.userSex) : userInfo.userSex != null) return false;
        if (headImage != null ? !headImage.equals(userInfo.headImage) : userInfo.headImage != null) return false;
        if (userPassword != null ? !userPassword.equals(userInfo.userPassword) : userInfo.userPassword != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (userId ^ (userId >>> 32));
        result = 31 * result + (userPhone != null ? userPhone.hashCode() : 0);
        result = 31 * result + (userEmail != null ? userEmail.hashCode() : 0);
        result = 31 * result + (userName != null ? userName.hashCode() : 0);
        result = 31 * result + (userSex != null ? userSex.hashCode() : 0);
        result = 31 * result + (headImage != null ? headImage.hashCode() : 0);
        result = 31 * result + (userPassword != null ? userPassword.hashCode() : 0);
        return result;
    }
}
