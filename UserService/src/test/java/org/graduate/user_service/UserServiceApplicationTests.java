package org.graduate.user_service;

import org.graduate.user_service.repository.UserInfoRepo;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class UserServiceApplicationTests {
    @Resource
    private UserInfoRepo userInfoRepo;

    @Test
    void contextLoads() {
        System.out.println(userInfoRepo.findUserInfoByUserName("刘湘海"));
    }
}
