package com.ailab.ailabsystem.controller;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import com.ailab.ailabsystem.common.CommonConstant;
import com.ailab.ailabsystem.common.R;
import com.ailab.ailabsystem.common.RedisKey;
import com.ailab.ailabsystem.enums.ResponseStatusEnum;
import com.ailab.ailabsystem.exception.CustomException;
import com.ailab.ailabsystem.model.dto.SingInRequest;
import com.ailab.ailabsystem.model.entity.InOutRegistration;
import com.ailab.ailabsystem.model.entity.User;
import com.ailab.ailabsystem.model.entity.UserInfo;
import com.ailab.ailabsystem.model.vo.UserInfoVo;
import com.ailab.ailabsystem.service.SignInService;
import com.ailab.ailabsystem.service.UserService;
import com.ailab.ailabsystem.util.RedisOperator;
import com.ailab.ailabsystem.util.TimeUtil;
import com.ailab.ailabsystem.util.UserHolder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author xiaozhi
 * @description 用户服务controller
 * @create 2022-11-2022/11/12 10:35
 */
@Api(value = "用户服务接口", tags = "用户服务接口")
@RequestMapping("/user")
@RestController
public class UserController {

    @Resource
    private RedisOperator redis;

    @Resource
    private UserService userService;

    @Resource
    private SignInService signInService;

    @ApiOperation(value = "判断是否已经签到接口", notes = "判断是否已经签到接口")
    @GetMapping("/isSingIn")
    public R<Object> isSingIn() {
        User user = UserHolder.getUser();
        // 判断是否已经签到
        if (redis.keyIsExist(RedisKey.getUserSignIn(user.getUserId()))) {
            throw new CustomException(ResponseStatusEnum.SGININ_ERROR);
        }
        return R.success();
    }

    @ApiOperation(value = "签到接口", notes = "签到接口")
    @PostMapping("/signIn")
    public R<Object> singIn(@ApiIgnore @RequestBody SingInRequest singInRequest) {
        Assert.notNull(singInRequest, "参数异常");
        // 设置默认值
        String task = ObjectUtil.defaultIfBlank(singInRequest.getTask(), CommonConstant.TASK_DEFAULT_VALUE);
        Integer time = ObjectUtil.defaultIfNull(singInRequest.getCheckOutTime(), CommonConstant.DEFAULT_CHECK_OUT_TIME);
        String remark = ObjectUtil.defaultIfNull(singInRequest.getRemark(), "");

        // 限定签到时间： 8:00 - 22:00
        Date currentTime = new Date();
        // 获取当前小时
        int hour = DateUtil.date(currentTime).getField(DateField.HOUR_OF_DAY);
        if (hour < 8 || hour > 22) {
            throw new CustomException(ResponseStatusEnum.SGININ_ERROR);
        }
        User user = UserHolder.getUser();
        // 判断是否已经签到
        if (redis.keyIsExist(RedisKey.getUserSignIn(user.getUserId()))) {
            throw new CustomException(ResponseStatusEnum.SGININ_ERROR);
        }
        // 获取签出时间
        Date checkOutTime = getCheckOutTime(time, currentTime, CommonConstant.MAX_CHECKOUT_TIME);
        long addTime = TimeUtil.getAddTime(currentTime, checkOutTime);
        redis.set(RedisKey.getUserSignIn(user.getUserId()), "1", addTime);

        InOutRegistration inOutRegistration = new InOutRegistration();
        inOutRegistration.setTask(task);
        inOutRegistration.setRemark(remark);
        inOutRegistration.setSignInTime(new Date());
        inOutRegistration.setCheckOutTime(checkOutTime);
        inOutRegistration.setStudentNumber(user.getStudentNumber());
        inOutRegistration.setSignInUserClass(user.getUserInfo().getClassNumber());
        inOutRegistration.setSignInUserRealName(user.getUserInfo().getRealName());

        signInService.save(inOutRegistration);
        return R.success();
    }

    /**
     * 获取签出时间
     * @param time          指定小时签出
     * @param currentTime   当前签到时间
     * @param maxHours      最大时间 - 1，比如最大时间为23，那么设置时间为22
     * @return
     */
    private Date getCheckOutTime(Integer time, Date currentTime, Integer maxHours) {
        // 结束时间加随机时间
        int endTime = time * CommonConstant.ONE_HOUR + RandomUtil.randomInt(1, 30) * CommonConstant.ONE_SECONDS;
        // 获取增加后的时间
        Date date = DateUtils.addSeconds(currentTime, endTime);
        // 增加后的时间的小时
        int hours = DateUtil.date(date).getField(DateField.HOUR_OF_DAY);
        // 签出时间大于最大时间，则设置为最大时间
        return hours < maxHours && hours > 8 ? date : DateUtils.setHours(new Date(), maxHours);
    }


    @ApiOperation(value = "获取所有学生信息接口", notes = "获取所有学生信息接口")
    @GetMapping("/getStuInfoList")
    public R getStuInfoList() {
        Map<String, List<UserInfoVo>> userInfos = userService.getUserInfoList();
        return R.success(userInfos);
    }

    @ApiOperation(value = "获取单个学生信息", notes = "获取单个学生信息")
    @GetMapping("/getStuInfo")
    public R getStuInfo(Integer userInfoId) {
        if (ObjectUtils.isEmpty(userInfoId)) {
            throw new CustomException(ResponseStatusEnum.FAILED);
        }
        UserInfo userInfo = userService.getUserInfo(userInfoId);
        return R.success(userInfo);
    }
}
