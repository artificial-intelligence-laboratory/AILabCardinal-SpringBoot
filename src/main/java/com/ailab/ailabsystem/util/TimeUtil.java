package com.ailab.ailabsystem.util;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import org.apache.commons.lang3.time.DateUtils;

import java.util.Date;

/**
 * @author xiaozhi
 * @description 时间工具类
 * @create 2022-11-2022/11/12 22:40
 */
public class TimeUtil {

    /**
     * 获取当前时间的小时
     */
    public static Integer getCurrentHour(Date currentTime) {
        DateTime date = cn.hutool.core.date.DateUtil.date(currentTime);
        // 获取当前时间
        return date.getField(DateField.HOUR_OF_DAY);
    }


    /**
     * 获取增加的时间
     * @param currentTime   当前时间
     * @param count         添加多少小时
     * @param maxHour       最大小时
     * @return 超过最大小时就返回最大小时
     */
    public static Date getAddDate(Date currentTime, Integer count, Integer maxHour) {
        // 获取增加后的时间
        Date date = DateUtils.addHours(currentTime, count);
        // 增加后的时间的小时
        int hours = DateUtil.date(date).getField(DateField.HOUR_OF_DAY);
        return hours < maxHour ? date : DateUtils.addHours(currentTime, maxHour - hours - 1);
    }
}
