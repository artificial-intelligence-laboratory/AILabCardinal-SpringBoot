package com.ailab.ailabsystem.mapper;

import com.ailab.ailabsystem.model.entity.ProjectMember;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* @author huiyuan
* @description 针对表【project_member】的数据库操作Mapper
* @createDate 2023-02-23 17:40:41
* @Entity generator.domain.ProjectMember
*/
public interface ProjectMemberMapper extends BaseMapper<ProjectMember> {

    int getLabProjectCount();
}




