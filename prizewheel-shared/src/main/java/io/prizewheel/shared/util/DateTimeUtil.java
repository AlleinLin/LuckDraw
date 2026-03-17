package io.prizewheel.shared.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 日期时间工具类
 * 
 * @author Allein
 * @since 1.0.0
 */
public final class DateTimeUtil {

    private static final DateTimeFormatter DEFAULT_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final DateTimeFormatter COMPACT_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private DateTimeUtil() {}

    public static String formatNow() {
        return LocalDateTime.now().format(DEFAULT_FORMATTER);
    }

    public static String format(LocalDateTime dateTime) {
        if (dateTime == null) {
            return null;
        }
        return dateTime.format(DEFAULT_FORMATTER);
    }

    public static String formatCompact(LocalDateTime dateTime) {
        if (dateTime == null) {
            return null;
        }
        return dateTime.format(COMPACT_FORMATTER);
    }

    public static String formatDate(LocalDateTime dateTime) {
        if (dateTime == null) {
            return null;
        }
        return dateTime.format(DATE_FORMATTER);
    }

    public static LocalDateTime parse(String text) {
        if (text == null || text.isEmpty()) {
            return null;
        }
        return LocalDateTime.parse(text, DEFAULT_FORMATTER);
    }

    public static boolean isInRange(LocalDateTime target, LocalDateTime start, LocalDateTime end) {
        if (target == null) {
            return false;
        }
        boolean afterStart = start == null || !target.isBefore(start);
        boolean beforeEnd = end == null || !target.isAfter(end);
        return afterStart && beforeEnd;
    }
}
