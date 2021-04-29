package org.graduate.user_service.repository;

import org.graduate.user_service.entity.Feature;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 对应的特征类型的数据访问接口
 *
 * @author : LiuXianghai on 2021/3/19
 * @Created : 2021/03/19 - 20:54
 * @Project : user_service
 */
@Repository
public interface FeatureRepo extends CrudRepository<Feature, Short> {
    /**
     * 得到用户可能的所有特征属性， 方法用于用户注册时用户选择喜欢的事物，
     *  从而得到对应的用户特征
     * @return ： 查找到的所有可能用户特征列表
     */
    @Query(value = "SELECT * FROM user_feature", nativeQuery = true)
    List<Feature> findAllFeature();
}
