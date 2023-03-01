package com.ailab.ailabsystem.mapper;

import com.ailab.ailabsystem.model.entity.Project;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
* @author huiyuan
* @description 针对表【project】的数据库操作Mapper
* @createDate 2023-02-23 17:40:41
* @Entity generator.domain.Project
*/
public interface ProjectMapper extends BaseMapper<Project> {

    List<Project> getUserProject(long userId);
}




