package com.ailab.ailabsystem.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.ailab.ailabsystem.common.R;
import com.ailab.ailabsystem.common.RedisKey;
import com.ailab.ailabsystem.mapper.AwardMapper;
import com.ailab.ailabsystem.mapper.ProjectMemberMapper;
import com.ailab.ailabsystem.model.entity.Award;
import com.ailab.ailabsystem.model.entity.Project;
import com.ailab.ailabsystem.model.entity.User;
import com.ailab.ailabsystem.model.vo.IndexAiLabInfo;
import com.ailab.ailabsystem.service.AwardService;
import com.ailab.ailabsystem.service.ProjectService;
import com.ailab.ailabsystem.service.UserService;
import com.ailab.ailabsystem.util.RedisOperator;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Api(value = "实验室信息接口", tags = "实验室信息接口")
@RestController
@RequestMapping("/lab")
public class CommonLabController {

    @Resource
    private UserService userService;

    @Resource
    private ProjectMemberMapper projectMemberMapper;

    @Resource
    private AwardMapper awardMapper;

    @Resource
    private RedisOperator redis;

    @GetMapping("/getLabInfo")
    public R getLabInfo() {
        String labInfoJson = redis.get(RedisKey.INDEX_LAB_KEY);
        if (StrUtil.isNotBlank(labInfoJson)) {
            return R.success(JSONUtil.toBean(labInfoJson, IndexAiLabInfo.class));
        }
        IndexAiLabInfo aiLabInfo = new IndexAiLabInfo();
        List<User> userList = userService.list();
        //去掉非实验室成员
        userList = userList.stream().filter(user -> {
            return user.getUserRight() == 2 || user.getUserRight() == 3;
        }).collect(Collectors.toList());
        aiLabInfo.setMemberCount(userList.size());
        aiLabInfo.setProjectCount(projectMemberMapper.getLabProjectCount());
        aiLabInfo.setAwardCount(awardMapper.getLabAwardCount());
        String labInfoJsonStr = JSONUtil.toJsonStr(aiLabInfo);
        redis.set(labInfoJson, labInfoJsonStr, 60 * 30);
        return R.success(aiLabInfo);
    }
}
