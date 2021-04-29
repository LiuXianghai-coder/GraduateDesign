package org.graduate.service.tools;

import java.time.OffsetDateTime;
import java.util.TimeZone;

/**
 * @author : LiuXianghai on 2021/3/21
 * @Created : 2021/03/21 - 14:47
 * @Project : service
 */
public final class TimeTool {
    public static OffsetDateTime getCurrentOffsetDateTime() {
        return OffsetDateTime.now();
    }
}
