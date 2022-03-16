package org.graduate.usershare.data;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.OffsetDateTime;

import javax.persistence.*;

/**
 * Created by IntelliJ IDEA.
 * User: liuxianghai
 * Date: 2021/4/5
 * Time: 下午9:02
 */
@Data
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@Entity
@Table(name = "user_share_browse", schema = "public", catalog = "recommend_system")
@IdClass(UserShareBrowsePK.class)
public class UserShareBrowse {
    @Id
    @Column(name = "user_id")
    private long userId;

    @Id
    @Column(name = "share_id")
    private long shareId;

    @Basic
    @Column(name = "browse_time")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private OffsetDateTime browseTime;
}
