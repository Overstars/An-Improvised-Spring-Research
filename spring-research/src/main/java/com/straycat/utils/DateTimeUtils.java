package com.straycat.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalQueries;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DateTimeUtils {
    private static final Logger logger = LoggerFactory.getLogger(DateTimeUtils.class);
    static String dateTimeFormat[] = {
            "uuuu-MM-dd'T'HH:mm:ss.SSSSSS",
            "uuuu-MM-dd'T'HH:mm:ss.SSS",
            "yyyy-MM-dd'T'HH:mm:ss",
            "yyyy-MM-dd HH:mm:ss.SSS",
            "yyyy-MM-dd HH:mm:ss",
            "yyyy年-M月-d日 HH:mm:ss.SSS",
            "yyyy年-M月-d日 HH:mm:ss",
            "yyyy年-M月-d日 HH时mm分ss秒"
    };
    /**
     * @description:
     *  传入一个包含年月日时分秒的String，会尝试枚举几种时间格式并解析为LocalDateTime格式
     *  使用LocalDateTime格式可以自行转化为ISO标准格式
     *  若无法解析最后会返回null
     * @param dateStr:
     * @return: java.time.LocalDateTime
     * @author: Overstars
     * @date: 2023/11/10 1:13
     */
    public static LocalDateTime parseDateTimeFromStr(String dateStr) {
        LocalDateTime result = null;
        List<DateTimeFormatter> formatterList = new ArrayList<>();
        // 使用
        for (String format : dateTimeFormat) {
            formatterList.add(DateTimeFormatter.ofPattern(format));
        }
        for (DateTimeFormatter formatter : formatterList) {
            try {
                TemporalAccessor ta = formatter.parse(dateStr);
                result = LocalDateTime.from(ta);
                break;
//                date = LocalDateTime.parse(dateStr, formatter);
//                date = formatter.parseLocalDateTime(stringDate).toLocalDate();
            } catch (DateTimeParseException e) {
                logger.info("TemporalAccessor 解析出错: {}，尝试其他解析格式", e.getMessage());
            } catch (Exception e) {
                logger.info("解析时间时其他异常{}", e.getMessage());
            }
        }
//        if (result != null) {
//            logger("解析{}结果为{}", dateStr, result.toString());
//        }
        return result;
    }
    public static LocalDateTime parseBest(String dateStr) {
        if (dateStr == null) {
            throw new NullPointerException("dateStr参数不能为null");
        };
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy hh:mm:ss");
        TemporalAccessor ta = formatter.parseBest(dateStr, TemporalQueries.localDate(), TemporalQueries.localTime());
        return LocalDateTime.from(ta);
    }
    /**
     * @description: 获取某个月的实际最大天数, 如2016-02, 最大天数为29
     * @param date:
     * @return: int
     * @author: Overstars
     * @date: 2023/11/10 9:51
     */
    public static int getMaximum(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.getActualMaximum(Calendar.DAY_OF_MONTH);
    }
    public static void main(String[] args) {
        try {
            String query[] = {
                    "2023-11-10 00:00:06.234",
                    "2023-11-10 00:00:06",
                    "2023-11-10T00:43:22",
                    "2023-11-10T00:43:22.585",
                    "2023年-11月-10日 23:45:53",
                    "2023年-2月-28日 23时59分59秒"
                };
            for (String s : query) {
                logger.info(s + "转换格式为ISO-8601格式 -> " + parseDateTimeFromStr(s).toString());
            }
        } catch (Exception e) {
            logger.info("Date解析异常：{}", e.getMessage());
        }
    }
}
