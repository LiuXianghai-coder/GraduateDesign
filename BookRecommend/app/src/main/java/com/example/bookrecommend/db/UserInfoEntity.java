package com.example.bookrecommend.db;

import com.example.bookrecommend.pojo.Feature;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import lombok.Data;

/**
 * 存储本地登录的的记录数据实体对象
 *
 * @Author: Administrator
 * @Date: 2021/03/24 18:07
 * @Project: BookRecommend
 **/
@Data
public class UserInfoEntity implements Serializable {
    /*
        登录的记录 ID
     */
    private Long recordId = 0L;

    /*
        用户登录 ID
     */
    private Long userId = 0L;

    /*
        用户手机号
     */
    private String userPhone = "";

    /*
        用户邮箱
     */
    private String userEmail = "";

    /*
        用户名
     */
    private String userName = "";

    /*
        用户性别
     */
    private String userSex = "";

    /*
        头像地址链接
     */
    private String headImage = "";

    /*
        用户登录密码， 经加密
     */
    private String userPassword = "";

    /*
        上次登录时间
     */
    private String lastLoginTime = "";

    /*
        预计本次登录状态到期时间
     */
    private String maturityTime = "";

    /*
        该用户是否更新过相关的信息
     */
    private Short isUpdate = 0;

    /*
        用户的兴趣爱好集合
     */
    private Set<Feature> features = new HashSet<>();
}
