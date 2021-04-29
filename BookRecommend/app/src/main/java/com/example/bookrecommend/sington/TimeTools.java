package com.example.bookrecommend.sington;

import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import org.threeten.bp.LocalDateTime;
import org.threeten.bp.OffsetDateTime;
import org.threeten.bp.format.DateTimeFormatter;

/**
 * @Author: Administrator
 * @Date: 2021/03/25 11:41
 * @Project: BookRecommend
 **/
public final class TimeTools {
    private static final Long sessionDays = 7L;

    /**
     * 获取当前的时间的字符串表示形式，时间格式为 yyyy-MM-dd HH:mm:ss
     * @return : 当前时间的字符串表示格式
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String currentDateTimeString() {
        return OffsetDateTime.now().atZoneSameInstant(ZoneIdTool.DEFAULT_ZONE_ID.getVal())
                .format(DateTimeFormatter.ofPattern(DateFormatTool.DEFAULT_DATE_FORMAT.getVal()));
    }

    /**
     * 获取当前时间的 Unix 时间戳
     * @return ： 当前的 Unix 时间戳， 使用 Long 表示
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static Long currentDateTimeUnixTimeStamp() {
        return OffsetDateTime.now().atZoneSameInstant(ZoneIdTool.DEFAULT_ZONE_ID.getVal()).toEpochSecond();
    }

    /**
     * 得到当前的 LocalDateTime 本地时间对象
     * @return ： 当前 LocalDateTime 对象
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static OffsetDateTime currentDateTime() {
        return OffsetDateTime.now();
    }

    /**
     * 将传入的 LocalDateTime 对象的字符串表示转变为对应的 LocalDateTime 对象
     * @param time ： 传入的 LocalDateTime 字符串表示形式, 格式应当为 {@code DateFormatTool} 的默认格格式
     * @return ： 解析得到的 LocalDateTime 对象
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static OffsetDateTime parseLocalDateTime(@NonNull String time) {
        return OffsetDateTime.parse(time, DateTimeFormatter.ofPattern(DateFormatTool.DEFAULT_DATE_FORMAT.getVal()));
    }

    /**
     * 将传入的 LocalDateTime 对象的字符串表示得到对应的 Unix 时间戳
     * @param time ： 传入的 LocalDateTime 字符串表示形式, 格式应当为 {@code DateFormatTool} 的默认格格式
     * @return ： 解析得到的 Unix 时间戳
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static Long parseLocalDateTimeToUnixTimeStamp(@NonNull String time) {
        LocalDateTime ldt = LocalDateTime.parse(time, DateTimeFormatter.ofPattern(DateFormatTool.DEFAULT_DATE_FORMAT.getVal()));
        return OffsetDateTime.of(ldt, ZoneIdTool.DEFAULT_ZONE_ID.getVal().getRules().getOffset(ldt)).toEpochSecond();
    }

    /**
     * 获取预期到期时间的字符串表示
     * @return ： 预计到期时间
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String maturityTimeString() {
        return OffsetDateTime.now().plusDays(sessionDays)
                .format(DateTimeFormatter.ofPattern(DateFormatTool.DEFAULT_DATE_FORMAT.getVal()));
    }
}
