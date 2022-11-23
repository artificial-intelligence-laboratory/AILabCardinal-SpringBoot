package com.ailab.ailabsystem.util;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;

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
     * 获取前面时间到后面时间之间的秒数
     *
     * @param preTime   前面时间
     * @param afterTime 后面时间
     * @return 返回秒数
     */
    public static long getAddTime(Date preTime, Date afterTime) {
        long time = afterTime.getTime() - preTime.getTime();
        return time / 1000;
    }

}
