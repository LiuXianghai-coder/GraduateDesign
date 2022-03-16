package org.graduate.user_service.controller;

import org.graduate.user_service.entity.Feature;
import org.graduate.user_service.repository.FeatureRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author : LiuXianghai on 2021/3/19
 * @Created : 2021/03/19 - 20:53
 * @Project : user_service
 */
@RestController
@RequestMapping(path = "/feature")
@CrossOrigin(origins = "*")
public class FeatureController {
    private final FeatureRepo featureRepo;

    @Autowired
    public FeatureController(FeatureRepo featureRepo) {
        this.featureRepo = featureRepo;
    }

    /**
     * 获取用户可能包含的所有特征信息
     * @return ：得到用户所有的特征信息对象列表
     */
    @GetMapping(path = "/allFeature")
    public List<Feature> allFeature() {
        return featureRepo.findAllFeature();
    }
}
