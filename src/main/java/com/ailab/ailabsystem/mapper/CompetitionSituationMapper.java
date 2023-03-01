package com.ailab.ailabsystem.mapper;

import com.ailab.ailabsystem.model.entity.CompetitionSituation;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* @author Y
* @description 针对表【competition_situation】的数据库操作Mapper
* @createDate 2023-02-23 21:27:38
* @Entity generator.domain.CompetitionSituation
*/
public interface CompetitionSituationMapper extends BaseMapper<CompetitionSituation> {

    int queryCompetitionCount(Long userId);
}




