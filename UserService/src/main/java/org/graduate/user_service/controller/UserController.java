package org.graduate.user_service.controller;
import com.google.common.collect.Sets;

import lombok.extern.slf4j.Slf4j;
import org.graduate.user_service.entity.*;
import org.graduate.user_service.repository.UserBookBrowseRepo;
import org.graduate.user_service.repository.UserInfoRepo;
import org.graduate.user_service.repository.UserShareBrowseRepo;
import org.graduate.user_service.tools.TimeTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.Set;

/**
 * 用户信息的主体控制器， 包括注册、登录，查找等。
 *
 * @author : LiuXianghai on 2021/3/19
 * @Created : 2021/03/19 - 14:19
 * @Project : user_service
 */
@Slf4j
@RestController
@RequestMapping(path = "/user")
@CrossOrigin(origins = "*")
public class UserController {
    private final UserInfoRepo userInfoRepo;

    private final UserBookBrowseRepo userBookBrowseRepo;

    private final UserShareBrowseRepo userShareBrowseRepo;

    @Autowired
    public UserController(UserInfoRepo userInfoRepo,
                          UserBookBrowseRepo userBookBrowseRepo,
                          UserShareBrowseRepo userShareBrowseRepo) {
        this.userInfoRepo = userInfoRepo;
        this.userBookBrowseRepo = userBookBrowseRepo;
        this.userShareBrowseRepo = userShareBrowseRepo;
    }

    /**
     * 验证用户的登录信息， 首先检查用户的邮箱地址是否存在且有效，
     * 如果有效则使用邮箱与对应的加密后的密码进行配对查找。如果查找到相关的信息， 则认证成功。
     * <p>
     * 如果没有出现对应的邮箱地址， 那么将会直接对用户的手机号和加密后的密码结对进行查找，
     * 如果查找到对应的用户， 则说明认证成功。
     *
     * @param userInfo ：输入了邮箱或者手机号以及对应的加密后的密码的用户信息对象
     * @return ： 如果认证成功， 则返回得到的 UserInfo 对象， 否则返回 null.
     */
    @PostMapping(path = "/login")
    public UserInfo userLogin(@RequestBody UserInfo userInfo) {
        log.info("UserInfo: " + userInfo.toString());
        String userEmail = userInfo.getUserEmail();

        if (null != userEmail && 0 != userEmail.trim().length()) {
            Optional<UserInfo> obj = userInfoRepo
                    .findUserInfoByUserEmailAndUserPassword(userEmail,
                            userInfo.getUserPassword());
            return obj.orElse(null);
        }

        Optional<UserInfo> obj = userInfoRepo
                .findUserInfoByUserPhoneAndUserPassword(userInfo.getUserPhone(),
                        userInfo.getUserPassword());

        return obj.orElse(null);
    }

    /**
     * 注册用户的信息处理
     *
     * @param userInfo ： 要注册的用户信息对象
     * @return ： 如果当前的用户名、用户手机号、用户邮箱已经被注册了，
     * 则返回 Http 状态码 202， 如果保存成功， 则返回状态码 200
     */
    @PostMapping(path = "/register")
    public HttpStatus register(@RequestBody UserInfo userInfo) {
        /*
            按照用户姓名查找是否存在这个 UserInfo 对象， 如果存在， 则直接跳过这个请求
         */
        Optional<UserInfo> userInfo1 = userInfoRepo
                .findUserInfoByUserName(userInfo.getUserName());
        if (userInfo1.isPresent()) {
            log.debug("UserName present, ignore the request.");
            return HttpStatus.ACCEPTED;
        }

        /*
            按照用户的手机号查找是否存在这个 UserInfo 对象， 如果存在则直接跳过这个请求
         */
        userInfo1 = userInfoRepo.findUserInfoByUserEmail(userInfo.getUserEmail());
        if (userInfo1.isPresent()) {
            log.debug("UserEmail present, ignore the request.");
            return HttpStatus.ACCEPTED;
        }

        /*
            按照用户有限查找是否存在这个对象， 如果存在则直接跳过这个请求
         */
        userInfo1 = userInfoRepo.findUserInfoByUserPhone(userInfo.getUserPhone());
        if (userInfo1.isPresent()) {
            log.debug("UserPhone present, ignore the request.");
            return HttpStatus.ACCEPTED;
        }

        log.debug("Save UserInfo Object: " + userInfo);
        userInfoRepo.save(userInfo);

        return HttpStatus.OK;
    }

    /**
     * 更新用户信息
     *
     * @param userInfo ： 要更新的用户信息对象
     * @return ： 处理成功， 则返回状态码 200
     */
    @PostMapping(path = "/update")
    public HttpStatus update(@RequestBody UserInfo userInfo) {
        log.info("Save UserInfo Object: " + userInfo);

        Optional<UserInfo> optional = userInfoRepo
                .findById(userInfo.getUserId());

        if (optional.isEmpty()) return HttpStatus.ACCEPTED;

        UserInfo obj = optional.get();

        obj.setUserPhone(userInfo.getUserPhone());
        obj.setUserEmail(userInfo.getUserEmail());
        obj.setUserName(userInfo.getUserName());
        obj.setUserSex(userInfo.getUserSex());
        obj.setHeadImage(userInfo.getHeadImage());

        userInfoRepo.save(obj);

        return HttpStatus.OK;
    }

    /**
     * 按照用户输入的用户名， 查找是否存在已经注册的用户
     *
     * @param userName ： 输入查找的用户名参数
     * @return ： 如果存在， 则返回 Http 状态码 202, 否则， 返回状态码 200
     */
    @GetMapping(path = "/findByName")
    public HttpStatus findUserInfoByName(@NonNull @RequestParam(name = "userName") String userName) {
        Optional<UserInfo> userInfo = userInfoRepo.findUserInfoByUserName(userName);

        if (userInfo.isPresent()) return HttpStatus.ACCEPTED;

        return HttpStatus.OK;
    }

    /**
     * 按照用户输入的电话号码查找是否存在对应的注册用户
     *
     * @param userPhone ： 输入查找的电话号码
     * @return ： 如果该电话号码已经被注册了， 则返回 Http状态码 202， 否则， 返回状态码 200
     */
    @GetMapping(path = "/findByPhone")
    public HttpStatus findUserInfoByPhone(@NonNull @RequestParam(name = "userPhone") String userPhone) {
        Optional<UserInfo> userInfo = userInfoRepo.findUserInfoByUserPhone(userPhone);

        if (userInfo.isPresent()) return HttpStatus.ACCEPTED;

        return HttpStatus.OK;
    }

    /**
     * 按照用户输入的邮箱查找是否存在对应的注册用户
     *
     * @param userEmail ： 输入查找的邮箱
     * @return ： 如果该电话号码已经被注册了， 则返回 Http状态码 202， 否则， 返回状态码 200
     */
    @GetMapping(path = "/findByEmail")
    public HttpStatus findUserInfoByEmail(@NonNull @RequestParam(name = "userEmail") String userEmail) {
        Optional<UserInfo> userInfo = userInfoRepo.findUserInfoByUserEmail(userEmail);

        if (userInfo.isPresent()) return HttpStatus.ACCEPTED;

        return HttpStatus.OK;
    }

    /**
     * 更新指定用户的基本信息
     *
     * @param userInfo ： 待更新的用户信息对象
     * @return ： 如果更新成功， 返回 Http 状态码 200.
     */
    @PostMapping(path = "/update/{userId}")
    public HttpStatus updateUserInfo(@NonNull @RequestBody UserInfo userInfo,
                                     @PathVariable(name = "userId") Long userId) {
        Optional<UserInfo> optional = userInfoRepo.findById(userId);

        if (optional.isEmpty()) return HttpStatus.ACCEPTED;

        UserInfo obj = optional.get();

        String userPhone = userInfo.getUserPhone();
        String userEmail = userInfo.getUserEmail();
        String userName = userInfo.getUserName();
        String password = userInfo.getUserPassword();
        String userSex = userInfo.getUserSex();
        String headImage = userInfo.getHeadImage();

        if (null != userPhone && 0 != userPhone.trim().length()) obj.setUserPhone(userPhone);
        if (null != userEmail && 0 != userEmail.trim().length()) obj.setUserEmail(userEmail);
        if (null != userName && 0 != userName.trim().length()) obj.setUserName(userName);
        if (null != password && 0 != password.trim().length()) obj.setUserPassword(password);
        if (null != userSex && 0 != userSex.trim().length()) obj.setUserSex(userSex);
        if (null != headImage && 0 != headImage.trim().length()) obj.setHeadImage(headImage);

        userInfoRepo.save(obj);

        return HttpStatus.OK;
    }

    /**
     * 保存或更新用户对于相关书籍的浏览信息
     *
     * @param obj ： 保存或更新的用户书籍浏览对象
     * @return ： 处理成功返回 Http 状态码 200
     */
    @PostMapping(path = "/updateUserBookBrowse")
    public @ResponseBody
    HttpStatus updateUserBrowse(
            @RequestBody UserBookBrowse obj) {
        obj.setBrowseTime(OffsetDateTime.now());
        userBookBrowseRepo.save(obj);

        return HttpStatus.OK;
    }

    /**
     * 保存或更新用户对于动态信息的浏览信息对象
     *
     * @param obj ： 保存的用户浏览动态对象
     * @return ： 处理结果，成功则返回 Http 状态码 200
     */
    @PostMapping(path = "/updateUserShareBrowse")
    public @ResponseBody
    HttpStatus updateUserShareBrowse(@RequestBody UserShareBrowse obj) {
        obj.setBrowseTime(TimeTools.currentTime());
        userShareBrowseRepo.save(obj);

        return HttpStatus.OK;
    }

    /**
     * 通过用户获取对应的书籍浏览记录
     *
     * @param userId ： 输入的用户 ID
     * @return ： 得到的 Book 集合
     */
    @GetMapping(path = "/throughRecord/{userId}")
    public Set<Book> throughRecord(@PathVariable Long userId) {
        Optional<UserInfo> optional = userInfoRepo.findById(userId);
        if (optional.isPresent()) return optional.get().getBookSet();

        return SingleObject.getBookSet();
    }
}
