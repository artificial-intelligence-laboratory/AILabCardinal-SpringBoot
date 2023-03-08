package com.ailab.ailabsystem.service.impl;

import com.ailab.ailabsystem.mapper.AwardMapper;
import com.ailab.ailabsystem.mapper.CompetitionMapper;
import com.ailab.ailabsystem.mapper.ProjectMapper;
import com.ailab.ailabsystem.mapper.UserInfoMapper;
import com.ailab.ailabsystem.model.entity.Award;
import com.ailab.ailabsystem.model.entity.Project;
import com.ailab.ailabsystem.model.entity.UserInfo;
import com.ailab.ailabsystem.model.vo.labvo.LabAchievementVo;
import com.ailab.ailabsystem.model.vo.labvo.LabAwardVo;
import com.ailab.ailabsystem.model.vo.labvo.LabProjectVo;
import com.ailab.ailabsystem.service.LabService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author huanghuiyuan
 * @ClassName
 * @Description 处理实验室信息的业务层的实现类
 * @date 2023/3/7 14:35
 */
@Service
public class LabServiceImpl implements LabService {

    @Resource
    private UserInfoMapper userInfoMapper;

    @Resource
    private ProjectMapper projectMapper;

    @Resource
    private CompetitionMapper competitionMapper;

    @Resource
    private AwardMapper awardMapper;

    @Override
    public LabAchievementVo getLabAchievement() {
        LambdaQueryWrapper<Project> projectLambdaQueryWrapper = new LambdaQueryWrapper<>();
        LambdaQueryWrapper<Award> awardLambdaQueryWrapper = new LambdaQueryWrapper<>();
        projectLambdaQueryWrapper.orderByDesc(Project::getYear);
        List<Project> projectList = projectMapper.selectList(projectLambdaQueryWrapper);
        awardLambdaQueryWrapper.orderByAsc(Award::getAwardLevel).orderByAsc(Award::getAwardId);
        List<Award> awardList = awardMapper.selectList(awardLambdaQueryWrapper);
        List<LabAwardVo> labAwardVoList = getLabAwardVoList(awardList);
        List<LabProjectVo> labProjectVoList = getLabProjectVoList(projectList);
        LabAchievementVo labAchievementVo = new LabAchievementVo();
        labAchievementVo.setLabAwardVoList(labAwardVoList);
        labAchievementVo.setLabProjectVoList(labProjectVoList);
        return labAchievementVo;
    }

    private List<LabAwardVo> getLabAwardVoList(List<Award> awardList) {
        List<LabAwardVo> labAwardVoList = new ArrayList<>();
        awardList.forEach(award -> {
            LabAwardVo labAwardVo = new LabAwardVo();
            labAwardVo.setAwardName(award.getAwardName());
            labAwardVo.setLevel(getLevel(award.getAwardLevel()));
            LambdaQueryWrapper<UserInfo> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(UserInfo::getUserId, award.getUserId());
            UserInfo userInfo = userInfoMapper.selectOne(queryWrapper);
            labAwardVo.setRole(getStrRole(award.getRole()));
            labAwardVo.setUsername(userInfo.getRealName());
            labAwardVo.setCompetitionName(competitionMapper.selectById(award.getCompetitionId()).getCompetitionName());
            labAwardVoList.add(labAwardVo);
        });
        return labAwardVoList;
    }

    public List<LabProjectVo> getLabProjectVoList(List<Project> projectList) {
        List<LabProjectVo> labProjectVoList = new ArrayList<>();
        projectList.forEach(project -> {
            LabProjectVo labProjectVo = new LabProjectVo();
            labProjectVo.setProjectName(project.getProjectName());
            labProjectVo.setLevel(getLevel(project.getLevel()));
            labProjectVo.setTeacherName(project.getTeacher());
            labProjectVo.setYear(project.getYear());
            labProjectVoList.add(labProjectVo);
        });
        return labProjectVoList;
    }

    private String getLevel(Integer level) {
        String levelStr = "";
        switch (level) {
            case 0:
                levelStr = "国家级";
                break;
            case 1:
                levelStr = "省级";
                break;
            case 2:
                levelStr = "校级";
                break;
        }
        return levelStr;
    }

    private String getStrRole(Integer role) {
        String roleStr = "";
        switch (role) {
            case 0:
                roleStr = "负责人";
                break;
            case 1:
                roleStr = "小组成员";
                break;
            case 2:
                roleStr = "个人比赛";
                break;
        }
        return roleStr;
    }
}
