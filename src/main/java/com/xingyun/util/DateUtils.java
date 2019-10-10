package com.xingyun.util;

import lombok.extern.slf4j.Slf4j;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 功能:日期解析实体类
 * @author 星云
 * 时间: 2019/9/6 12:41
 */
@Slf4j
public class DateUtils {

    private DateUtils(){}

    private static final SimpleDateFormat SDF_YYYY_MM_DD = new SimpleDateFormat("yyyy-MM-dd");

    public static synchronized Date parseDateYYYYMMDD(String date) {
        try {
            return SDF_YYYY_MM_DD.parse(date);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            log.error(e.getMessage(), e);
        }
        return null;
    }
}
