package org.graduate.user_service.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * @author : LiuXianghai on 2021/3/19
 * @Created : 2021/03/19 - 14:26
 * @Project : user_service
 */
@Data
@Entity
@Table(name = "user_info", schema = "public", catalog = "recommend_system")
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false)
    private long userId;

    @Basic
    @Column(name = "user_phone")
    private String userPhone;

    @Basic
    @Column(name = "user_email")
    private String userEmail;

    @Basic
    @Column(name = "user_name")
    private String userName;

    @Basic
    @Column(name = "user_sex")
    private String userSex;

    @Basic
    @Column(name = "head_image")
    private String headImage;

    @Basic
    @Column(name = "user_password")
    private String userPassword;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_feature_holding",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "feature_id")
    )
    private Set<Feature> holdingFeatures = new HashSet<>();

    @OneToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_book_browse",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "isbn")
    )
    private Set<Book> bookSet = new HashSet<>();

    @OneToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_share_browse",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "share_id")
    )
    private Set<UserShare> shareSet = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserInfo userInfo = (UserInfo) o;

        if (userId != userInfo.userId) return false;
        if (!Objects.equals(userPhone, userInfo.userPhone)) return false;
        if (!Objects.equals(userEmail, userInfo.userEmail)) return false;
        if (!Objects.equals(userName, userInfo.userName)) return false;
        if (!Objects.equals(userSex, userInfo.userSex)) return false;
        if (!Objects.equals(headImage, userInfo.headImage)) return false;
        return Objects.equals(userPassword, userInfo.userPassword);
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
