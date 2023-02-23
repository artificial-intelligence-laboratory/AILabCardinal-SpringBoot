package com.ailab.ailabsystem.service.impl;

import com.ailab.ailabsystem.mapper.ProjectMapper;
import com.ailab.ailabsystem.model.entity.Project;
import com.ailab.ailabsystem.service.ProjectService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
* @author Y
* @description 针对表【project】的数据库操作Service实现
* @createDate 2023-02-23 17:40:41
*/
@Service
public class ProjectServiceImpl extends ServiceImpl<ProjectMapper, Project>
    implements ProjectService {

}




