package org.graduate.user_service.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Objects;

/**
 * @author : LiuXianghai on 2021/3/19
 * @Created : 2021/03/19 - 15:10
 * @Project : user_service
 */
@Data
@Entity
@Table(name = "user_feature", schema = "public", catalog = "recommend_system")
public class Feature {
    @Id
    @Column(name = "feature_id")
    private short featureId;

    @Basic
    @Column(name = "feature_name")
    private String featureName;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Feature that = (Feature) o;

        if (featureId != that.featureId) return false;
        return Objects.equals(featureName, that.featureName);
    }

    @Override
    public int hashCode() {
        int result = featureId;
        result = 31 * result + (featureName != null ? featureName.hashCode() : 0);
        return result;
    }
}
