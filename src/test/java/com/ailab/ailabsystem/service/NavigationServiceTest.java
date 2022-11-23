package com.ailab.ailabsystem.service;

import com.ailab.ailabsystem.model.entity.SiteType;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author xiaozhi
 * @description
 * @create 2022-11-2022/11/22 19:48
 */
@SpringBootTest
public class NavigationServiceTest {

    @Resource
    private NavigationService navigationService;

    @Test
    public void testQueryLabelTypeList(){
        List<SiteType> siteTypes = navigationService.queryLabelTypeList();
        System.out.println(siteTypes);

    }
}
