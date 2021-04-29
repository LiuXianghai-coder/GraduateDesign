package org.graduate.service.data_entity;

import javax.persistence.*;
import java.time.OffsetDateTime;
import java.util.Objects;

/**
 * @author : LiuXianghai on 2021/3/27
 * @Created : 2021/03/27 - 12:20
 * @Project : GetDataService
 */
@Entity
@Table(name = "js_content", schema = "public", catalog = "recommend_system")
public class JsContent {
    private short contentId;
    private String content;
    private OffsetDateTime updateTime;

    @Id
    @Column(name = "content_id")
    public short getContentId() {
        return contentId;
    }

    public void setContentId(short contentId) {
        this.contentId = contentId;
    }

    @Basic
    @Column(name = "content")
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Basic
    @Column(name = "update_time")
    public OffsetDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(OffsetDateTime updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        JsContent jsContent = (JsContent) o;

        if (contentId != jsContent.contentId) return false;
        if (!Objects.equals(content, jsContent.content)) return false;
        return Objects.equals(updateTime, jsContent.updateTime);
    }

    @Override
    public int hashCode() {
        int result = (int) contentId;
        result = 31 * result + (content != null ? content.hashCode() : 0);
        result = 31 * result + (updateTime != null ? updateTime.hashCode() : 0);
        return result;
    }
}
