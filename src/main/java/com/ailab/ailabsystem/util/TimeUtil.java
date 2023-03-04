package com.ailab.ailabsystem.util;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import com.ailab.ailabsystem.common.CommonConstant;
import lombok.extern.slf4j.Slf4j;

import java.lang.invoke.ConstantCallSite;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author xiaozhi
 * @description 时间工具类
 * @create 2022-11-2022/11/12 22:40
 */
@Slf4j
public class TimeUtil {

    public static final String GRADUATE_MONTH_DAY = "-06-15";

    /**
     * 获取当前时间的小时
     */
    public static Integer getCurrentHour(Date currentTime) {
        DateTime date = cn.hutool.core.date.DateUtil.date(currentTime);
        // 获取当前时间
        return date.getField(DateField.HOUR_OF_DAY);
    }

    public static int getPrefixEnrollmentYear() {
        return Integer.parseInt(CommonConstant.PREFIX_ENROLLMENT_YEAR);
    }

    public static int getSysYear() {
        Calendar date = Calendar.getInstance();
        return date.get(Calendar.YEAR);
    }

    public static Date getGraduateTime(String year){

        Date date = new Date();
        try {
            SimpleDateFormat formatter = new SimpleDateFormat( "yyyy-MM-dd ");
            String graduateTime = year + GRADUATE_MONTH_DAY;
            date = formatter.parse(graduateTime);
        } catch (ParseException e) {
            log.error("转换时间错误");
        }
        return date;
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
