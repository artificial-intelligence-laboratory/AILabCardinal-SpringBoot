package com.ailab.ailabsystem.controller;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.ailab.ailabsystem.common.CommonConstant;
import com.ailab.ailabsystem.common.R;
import com.ailab.ailabsystem.common.RedisKey;
import com.ailab.ailabsystem.model.dto.SingInRequest;
import com.ailab.ailabsystem.model.entity.InOutRegistration;
import com.ailab.ailabsystem.model.entity.User;
import com.ailab.ailabsystem.service.SignInService;
import com.ailab.ailabsystem.util.RedisOperator;
import com.ailab.ailabsystem.util.TimeUtil;
import com.ailab.ailabsystem.util.UserHolder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @author xiaozhi
 * @description
 * @create 2022-11-2022/11/12 10:35
 */
@Api(value = "用户服务接口", tags = "用户服务接口")
@RequestMapping("/user")
@RestController
public class UserController {

    @Resource
    private RedisOperator redis;

    @Resource
    private SignInService signInService;


    @ApiOperation(value = "签到接口", notes = "签到接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "task", value = "任务", dataType = "String", required = false, paramType = "body"),
            @ApiImplicitParam(name = "remark", value = "备注", dataType = "String", required = false, paramType = "body"),
            @ApiImplicitParam(name = "checkOutTime", value = "签出时间", dataType = "int", required = false, paramType = "body"),
    })
    @PostMapping("/signIn")
    public R singIn(@ApiIgnore @RequestBody SingInRequest singInRequest) {
        // 设置默认值
        String task = ObjectUtil.defaultIfBlank(singInRequest.getTask(), CommonConstant.TASK_DEFAULT_VALUE);
        Integer time = ObjectUtil.defaultIfNull(singInRequest.getCheckOutTime(), CommonConstant.DEFAULT_CHECK_OUT_TIME);
        String remark = ObjectUtil.defaultIfNull(singInRequest.getRemark(), "");

        // 限定签到时间： 8:00 - 22:00
        Date currentTime = new Date();
        // 获取当前小时
        int hour = DateUtil.date(currentTime).getField(DateField.HOUR_OF_DAY);
        if (hour < 8 || hour > 22) {
            return R.error("请在规定时间内签到");
        }
        User user = UserHolder.getUser();
        if (redis.keyIsExist(RedisKey.getUserSignIn(user.getUserId()))) {
            return R.success("您已签到，请勿重复");
        }
        // 签到放入redis中，指定时间后过期
        redis.set(RedisKey.getUserSignIn(user.getUserId()), "1", time * 60 * 60);
        // 获取签出时间
        Date checkOutTime = TimeUtil.getAddDate(currentTime, time, CommonConstant.MAX_CHECKOUT_TIME);

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
}
