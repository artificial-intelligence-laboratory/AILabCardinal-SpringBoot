package com.ailab.ailabsystem.util;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import com.ailab.ailabsystem.model.entity.User;
import com.ailab.ailabsystem.model.vo.InOutRegistrationVo;
import org.apache.commons.lang3.time.DateUtils;

import java.util.*;

/**
 * 生成时间工具类
 *
 * @author xiaozhi
 */
public class GenerateTimeUtil {

    /**
     * 早上开始时间（8:00）
     */
    private final static long AM_START_TIME = 8 * 60 * 60 * 1000;

    /**
     * 早上结束时间（12:00）
     */
    private final static long AM_END_TIME = 12 * 60 * 60 * 1000;

    /**
     * 中午开始时间（14:00）
     */
    private final static long NOON_START_TIME = 14 * 60 * 60 * 1000;

    /**
     * 中午结束时间（17:00）
     */
    private final static long NOON_END_TIME = 17 * 60 * 60 * 1000;

    /**
     * 晚上开始时间（18:00）
     */
    private final static long NIGHT_START_TIME = 18 * 60 * 60 * 1000;

    /**
     * 晚上结束时间（22:00）
     */
    private final static long NIGHT_END_TIME = 22 * 60 * 60 * 1000;

    /**
     * 工作列表
     */
    private static final List<String> WORDS = new ArrayList<>(10);

    /**
     * 签到开始时间列表
     */
    private static final List<Long> SIGN_START_LIST = new ArrayList<>(3);

    /**
     * 签到结束时间列表
     */
    private static final List<Long> CHECK_OUT_TIME_LIST = new ArrayList<>(3);


    static {
        WORDS.add("程序开发");
        WORDS.add("程序设计");
        WORDS.add("科学研究");
        WORDS.add("课程讨论");
        WORDS.add("学术讨论");

        SIGN_START_LIST.add(AM_START_TIME);
        SIGN_START_LIST.add(NOON_START_TIME);
        SIGN_START_LIST.add(NIGHT_START_TIME);
        CHECK_OUT_TIME_LIST.add(AM_END_TIME);
        CHECK_OUT_TIME_LIST.add(NOON_END_TIME);
        CHECK_OUT_TIME_LIST.add(NIGHT_END_TIME);
    }


    /**
     * 获取时间每一天的三个不同时间段随机签到和签退的列表
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @param num       人数
     * @return 签到签出时间和工作内容的列表
     */
    public static List<InOutRegistrationVo> getDateList(Date startTime,
                                                        Date endTime,
                                                        Integer num,
                                                        List<User> users) {
        List<InOutRegistrationVo> dateList = new ArrayList<>();
        // 获取到开始天结束天的天数
        long totalDays = DateUtil.betweenDay(startTime, endTime, true);
        for (int i = 0; i < totalDays + 1; i++) {
            // 天数随遍历次数增加
            Date currentTime = DateUtils.addDays(startTime, i);
            // 获取随机的三个时间段的签到时间和签出时间和工作
            List<InOutRegistrationVo> dates = getThreeTimePeriodsAndWork(currentTime, num, users);
            dateList.addAll(dates);
        }
        // 根据时间排序
        dateList.sort((o1, o2) -> {
            Date signInTime1 = o1.getSignInTime();
            Date signInTime2 = o2.getSignInTime();
            if (signInTime1.after(signInTime2)) {
                return 1;
            }
            return -1;
        });
        return dateList;
    }

    /**
     * 随机生成当前时间的三个时间段的 {@code num} 数量的签到
     * @param currentTime   当前时间
     * @param num           需要签到和签出的人数，输出不同的签到和签出时间
     * @return
     */
    private static List<InOutRegistrationVo> getThreeTimePeriodsAndWork(Date currentTime,
                                                                        Integer num,
                                                                        List<User> users) {
        List<InOutRegistrationVo> clockIns = new ArrayList<>(num * SIGN_START_LIST.size());
        // 获取不同的时间段
        for (int i = 0; i < SIGN_START_LIST.size(); i++) {
            // 去重集合，最后剩一个人也不重要，重要的是不能重复
            Set<User> userSet = new HashSet<>(num);
            for (int j = 0; j < num; j++) {
                int index = RandomUtil.randomInt(num);
                // 获取 num 不重复的用户
                userSet.add(users.get(index));
            }
            // 根据人数遍历
            for (User user : userSet) {
                InOutRegistrationVo inOutRegistrationVo = getClockIn(currentTime, i, user);
                clockIns.add(inOutRegistrationVo);
            }

        }
        return clockIns;
    }

    private static InOutRegistrationVo getClockIn(Date current,
                                                  int index,
                                                  User user) {
        // 根据索引获取签到时间和签出时间
        Long signStartTime = SIGN_START_LIST.get(index);
        Long signEndTime = CHECK_OUT_TIME_LIST.get(index);
        // 获取往上浮动半小时的签到时间
        long halfHour = DateUtils.MILLIS_PER_MINUTE * 30;
        long randomHalfAnHour = RandomUtil.randomLong(1, halfHour);
        Date signTime = DateUtils.addMilliseconds(current, (int) (randomHalfAnHour + signStartTime));
        // 获取指定时间半小时内上下浮动的签出时间
        long randomHalfAnHourForCheckOut = RandomUtil.randomLong(-halfHour, halfHour) + signEndTime;
        Date checkOutTime = DateUtils.addMilliseconds(current, (int) randomHalfAnHourForCheckOut);
        // 获取指定时间内上下浮动的签出时间
        InOutRegistrationVo clockIn = new InOutRegistrationVo();
        clockIn.setSignInUserRealName(user.getUserInfo().getRealName());
        clockIn.setSignInUserClass(user.getUserInfo().getClassNumber());
        clockIn.setStudentNumber(user.getStudentNumber());
        clockIn.setSignInTime(signTime);
        clockIn.setCheckOutTime(checkOutTime);
        clockIn.setTask(WORDS.get(RandomUtil.randomInt(WORDS.size())));

        return clockIn;
    }

    public static void main(String[] args) {
//        DateTime startTime = DateUtil.parse("2022-5-14", "yyyy-MM-dd");
//        DateTime endTime = DateUtil.parse("2022-5-18", "yyyy-MM-dd");
//        List<InOutRegistrationVo> dateList = GenerateTimeUtil.getDateList(startTime, endTime, 5);
//        dateList.forEach(System.out::println);
//        System.out.println(dateList.size());
    }
}
