package org.graduate.user_service;

import org.graduate.user_service.repository.UserInfoRepo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author : LiuXianghai on 2021/3/19
 * @Created : 2021/03/19 - 15:35
 * @Project : user_service
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class RepoTest {
    @Autowired
    private UserInfoRepo userInfoRepo;

    @Test
    public void repoTest(){
        System.out.println("result: " + Math.ceil((double) 35 / 20));
    }
}
