package com.ailab.ailabsystem.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.ailab.ailabsystem.common.CommonConstant;
import com.ailab.ailabsystem.common.R;
import com.ailab.ailabsystem.common.RedisKey;
import com.ailab.ailabsystem.enums.ResponseStatusEnum;
import com.ailab.ailabsystem.exception.CustomException;
import com.ailab.ailabsystem.model.dto.SingInRequest;
import com.ailab.ailabsystem.model.dto.UserInfoDTO;
import com.ailab.ailabsystem.model.entity.InOutRegistration;
import com.ailab.ailabsystem.model.entity.User;
import com.ailab.ailabsystem.model.entity.UserInfo;
import com.ailab.ailabsystem.model.vo.UserInfoVo;
import com.ailab.ailabsystem.model.vo.UserVo;
import com.ailab.ailabsystem.service.SignInService;
import com.ailab.ailabsystem.service.UserService;
import com.ailab.ailabsystem.util.*;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author xiaozhi
 * @description 用户服务controller
 * @create 2022-11-2022/11/12 10:35
 */
@Api(value = "用户服务接口", tags = "用户服务接口")
@RequestMapping("/user")
@RestController
public class UserController {

    private static final ExecutorService SEND_EMAIL_EXECUTOR = Executors.newSingleThreadExecutor();

    @Resource
    private RedisOperator redis;

    @Resource
    private UserService userService;

    @Resource
    private SignInService signInService;

    @Autowired
    private SendEmailUtils sendEmailUtils;

    @ApiOperation(value = "判断是否已经签到接口", notes = "判断是否已经签到接口")
    @GetMapping("/isSingIn")
    public R<Object> isSingIn() {
        UserVo userVo = UserHolder.getUser();
        // 判断是否已经签到
        if (redis.keyIsExist(RedisKey.getUserSignIn(userVo.getUserId()))) {
            throw new CustomException(ResponseStatusEnum.SGININ_ERROR);
        }
        return R.success();
    }

    @ApiOperation(value = "签到接口", notes = "签到接口")
    @PostMapping("/signIn")
    public R<Object> singIn(@RequestBody SingInRequest singInRequest) {
        Assert.notNull(singInRequest, "参数异常");
        // 设置默认值
        String task = ObjectUtil.defaultIfBlank(singInRequest.getTask(), CommonConstant.TASK_DEFAULT_VALUE);
        String time = ObjectUtil.defaultIfNull(singInRequest.getCheckOutTime(), CommonConstant.DEFAULT_CHECK_OUT_TIME);
        String remark = ObjectUtil.defaultIfNull(singInRequest.getRemark(), "");

        // 限定签到时间： 8:00 - 22:00
        Date currentTime = new Date();
        // 获取当前小时
        int hour = DateUtil.date(currentTime).getField(DateField.HOUR_OF_DAY);
        if (hour < 8 || hour > 22) {
            throw new CustomException(ResponseStatusEnum.SGININ_ERROR);
        }
        UserVo userVo = UserHolder.getUser();
        User user = userService.getById(userVo.getUserId());
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
        inOutRegistration.setSignInTime(currentTime);
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
     * @return 签到时间
     */
    private Date getCheckOutTime(String time, Date currentTime, Integer maxHours) {
        // 获取随机半小时的时间 - 毫秒数
        long halfHour = DateUtils.MILLIS_PER_MINUTE * 30;
        // 结束时间加随机时间
        long addTime =  (RandomUtil.randomLong(-halfHour, halfHour) + (Long.parseLong(time) * DateUtils.MILLIS_PER_HOUR));
        // 获取签出时间
        Date checkOutTime = DateUtils.addMilliseconds(currentTime, (int) addTime);
        // 获取签到时间是几点
        int hours = DateUtil.date(checkOutTime).getField(DateField.HOUR_OF_DAY);
        // 签出时间大于最大时间，则设置为最大时间
        return hours < maxHours && hours > 8 ? checkOutTime : DateUtils.setHours(currentTime, maxHours);
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

    @ApiOperation(value = "获取学生的首页信息", notes = "用于获取学生首页的基本信息")
    @GetMapping("/getIndexUserInfo")
    public R getIndexUserInfo(HttpServletRequest request) {
        //获取用户登录凭证token
        String token = RequestUtil.getAuthorization(request);
        String loginUserKey = RedisKey.getLoginUserKey(token);
        return userService.getIndexUserInfo(loginUserKey);
    }

    @ApiOperation(value = "获取自己的详情信息", notes = "获取首页侧边栏中的我的信息")
    @GetMapping("/Info/of/me")
    public R getInfoOfMe(HttpServletRequest request) {
        String authorization = RequestUtil.getAuthorization(request);
        String loginUserKey = RedisKey.getLoginUserKey(authorization);
        return userService.getInfoOfMe(loginUserKey);
    }

    @ApiOperation(value = "获取个人详情学生信息", notes = "用于获取成员页中其他成员的详细信息")
    @GetMapping("/Info/of/id")
    public R getInfoOfId(Long userId) {
        return userService.getInfoById(userId);
    }

    @ApiOperation(value = "新增个人信息", notes = "侧边栏中的新增信息")
    @PutMapping("/update/my/info")
    public R updateMyInfo(@RequestBody UserInfoDTO userInfoDTO, HttpServletRequest request) {
        //获取用户登录token
        String token = RequestUtil.getAuthorization(request);
        if (StrUtil.isBlank(token)) {
            throw new CustomException(ResponseStatusEnum.SESSION_EXPIRE);
        }
        if (userInfoDTO == null) {
            throw new CustomException(ResponseStatusEnum.PARAMS_ERROR);
        }
        return userService.updateMyInfo(userInfoDTO, token);
    }

    @ApiOperation(value = "绑定手机号", notes = "绑定手机号码，要注意手机号码格式，格式不对会报错")
    @PutMapping("/bind/phone")
    public R bindPhone(HttpServletRequest request, Long userId, String phone) {
        if (userId == null) {
            throw new CustomException(ResponseStatusEnum.PARAMS_ERROR);
        }
        if (StrUtil.isBlank(phone)) {
            throw new CustomException(ResponseStatusEnum.PARAMS_ERROR);
        }
        if (!PatternUtil.matchPhonePattern(phone)) {
            throw new CustomException(ResponseStatusEnum.PHONE_ERROR);
        }
        String token = RequestUtil.getAuthorization(request);
        UpdateWrapper<User> updateWrapper = new UpdateWrapper();
        updateWrapper.eq("user_id", userId).set("phone", phone);
        userService.update(updateWrapper);
        User user = userService.getById(userId);
        if (user == null) {
            throw new CustomException(ResponseStatusEnum.NOT_FOUND_ERROR);
        }
        UserVo userVo = BeanUtil.copyProperties(user, UserVo.class);
        String userVoJson = JSONUtil.toJsonStr(userVo);
        redis.set(RedisKey.getLoginUserKey(token),userVoJson);
        return R.success(userVo);
    }

    @ApiOperation(value = "发送验证码到邮箱", notes = "发送验证码到邮箱")
    @GetMapping("/send/code")
    public R sendCode(Long userId, String email) {
        if (userId == null) {
            throw new CustomException(ResponseStatusEnum.PARAMS_ERROR);
        }
        if (StrUtil.isBlank(email)) {
            throw new CustomException(ResponseStatusEnum.EMAIL_ERROR);
        }
        if (!PatternUtil.matchEmailPattern(email)) {
            throw new CustomException(ResponseStatusEnum.EMAIL_ERROR);
        }
        String emailCode = RandomUtil.randomNumbers(6);
        SEND_EMAIL_EXECUTOR.submit(new HandleSendEmail(email, emailCode));
        redis.set(RedisKey.getEmailCode(userId), emailCode, 60 * 5);
        return R.success();
    }

    @ApiOperation(value = "校验验证码并绑定邮箱", notes = "校验验证码并绑定邮箱")
    @PutMapping("/bind/email")
    public R bindEmail(HttpServletRequest request, String email, String code) {
        String token = RequestUtil.getAuthorization(request);
        String userVoJson = redis.get(RedisKey.getLoginUserKey(token));
        if (userVoJson == null) {
            throw new CustomException(ResponseStatusEnum.NOT_LOGIN_ERROR);
        }
        UserVo userVo = JSONUtil.toBean(userVoJson, UserVo.class);
        String emailCOde = redis.get(RedisKey.getEmailCode(userVo.getUserId()));
        if (!code.equals(emailCOde)) {
            throw new CustomException(ResponseStatusEnum.CODE_ERROR);
        }
        return userService.bindEmail(userVo.getUserId(), email, token);
    }

    @ApiOperation(value = "获取成员列表")
    @GetMapping("/getSimpleUser")
    public R getSimpleUser() {
        return userService.getSimpleUserInfoList();
    }

    private class HandleSendEmail implements Runnable {

        private final String email;

        private final String emailCode;

        public HandleSendEmail(String email, String emailCode) {
            this.email = email;
            this.emailCode = emailCode;
        }

        @Override
        public void run() {
            sendEmailUtils.sendSimpleMail(email, SendEmailUtils.subject, SendEmailUtils.getContent(emailCode));
        }
    }
}
