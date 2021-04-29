package org.graduate.user_service.repository;

import org.graduate.user_service.entity.UserInfo;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author : LiuXianghai on 2021/3/19
 * @Created : 2021/03/19 - 14:27
 * @Project : user_service
 */
@Repository
public interface UserInfoRepo extends CrudRepository<UserInfo, Long> {
    /**
     * 通过输入的用户手机号来查找对应的 UserInfo 对象，
     * 这是为了保证注册时一个手机号对应一个 User
     * @param userPhone ： 传入的用户手机号
     * @return ： 查找到的 UserInfo 对象
     */
    Optional<UserInfo> findUserInfoByUserPhone(@Param("userPhone") String userPhone);

    /**
     * 通过输入的用户邮箱来查找对应的 UserInfo 对象， 与查找手机号的目的一样，
     * 这是为了保证再注册时一个邮箱对应一个 UserInfo 对象
     * @param userEmail ： 传入的用户邮箱
     * @return ： 查找道德 UserInfo 对象
     */
    Optional<UserInfo> findUserInfoByUserEmail(@Param("userEmail") String userEmail);

    /**
     * 可能这不是必须的， 但是这里也是为了保证用户的昵称是唯一的。
     * @param userName ： 输入查找的用户名
     * @return ： 查找道德 UserInfo 对象
     */
    Optional<UserInfo> findUserInfoByUserName(@Param("userName") String userName);

    /**
     * 通过用户输入的邮箱和密码的匹配来认证用户的登录信息
     * @param userEmail ： 用户输入的邮箱地址
     * @param password ： 经过加密之后的密码
     * @return ： 是否存在对应的 UserInfo 对象
     */
    Optional<UserInfo> findUserInfoByUserEmailAndUserPassword(@Param("userEmail") String userEmail,
                                                              @Param("userPassword") String password);

    /**
     * 通过用户输入的手机号和密码对来验证用户登录
     * @param userPhone ： 用户传入的手机号
     * @param password ： 用户传入的经过加密后的密码
     * @return ： 是否存在对应的 UserInfo 对象
     */
    Optional<UserInfo> findUserInfoByUserPhoneAndUserPassword(@Param("userPhone") String userPhone,
                                                              @Param("userPassword") String password);
}
