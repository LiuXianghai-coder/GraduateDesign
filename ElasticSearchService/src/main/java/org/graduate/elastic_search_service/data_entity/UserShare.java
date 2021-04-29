package org.graduate.elastic_search_service.data_entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.threeten.bp.OffsetDateTime;

import javax.persistence.*;

/**
 * 显示给用户界面的基础分享信息实体类
 *
 * @author : LiuXianghai on 2021/2/23
 * @Created : 2021/02/23 - 10:12
 * @Project : usershare
 */
@Entity
@Table(name = "user_share", schema = "public", catalog = "recommend_system")
@Data
public class UserShare {
    @Id
    @Column(name = "share_id")
    @JsonProperty("shareId")
    private long shareId;

    @Basic
    @Column(name = "share_head")
    @JsonProperty("shareHeader")
    private String shareHeader;

    @Basic
    @Column(name = "star_num")
    @JsonProperty("starNum")
    private int starNum;

    @Basic
    @Column(name = "comment_num")
    @JsonProperty("commentNum")
    private int commentNum;

    @Basic
    @Column(name = "share_date")
    @JsonProperty("shareDate")
    private OffsetDateTime shareDate;

    @Basic
    @Column(name = "user_id")
    @JsonProperty("userId")
    private long userId;

    @Basic
    @Column(name = "collections_num")
    private Integer collectionNum = 0;

    @Basic
    @Column(name = "share_content")
    private String shareContent;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserShare userShare = (UserShare) o;

        if (shareId != userShare.shareId) return false;
        if (starNum != userShare.starNum) return false;
        if (commentNum != userShare.commentNum) return false;
        if (shareHeader != null ? !shareHeader.equals(userShare.shareHeader) : userShare.shareHeader != null)
            return false;
        return shareDate != null ? shareDate.equals(userShare.shareDate) : userShare.shareDate == null;
    }

    @Override
    public int hashCode() {
        int result = (int) (shareId ^ (shareId >>> 32));
        result = 31 * result + (shareHeader != null ? shareHeader.hashCode() : 0);
        result = 31 * result + starNum;
        result = 31 * result + commentNum;
        result = 31 * result + (shareDate != null ? shareDate.hashCode() : 0);
        return result;
    }
}
