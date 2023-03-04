package com.ailab.ailabsystem.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.ailab.ailabsystem.common.CommonConstant;
import com.ailab.ailabsystem.common.R;
import com.ailab.ailabsystem.enums.ResponseStatusEnum;
import com.ailab.ailabsystem.exception.CustomException;
import com.ailab.ailabsystem.model.entity.InOutRegistration;
import com.ailab.ailabsystem.model.vo.InOutRegistrationVo;
import com.ailab.ailabsystem.service.SignInService;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.write.style.column.SimpleColumnWidthStyleStrategy;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @author xiaozhi
 */
@Slf4j
@Api(value = "文件服务接口", tags = "文件服务接口")
@RestController
@RequestMapping("/fs")
public class FileController {

    @Resource
    private SignInService signInService;

    public static final String BASIC_DIR = "A:\\avatar\\";

//    http://localhost:8080/fs/exportSignInRecode?isRandom=true&startTime=%222022-4-17%22&endTime=%222022-4-29%22&num=2

    @ApiOperation(value = "导出签到记录接口", notes = "导出签到记录接口")
    @GetMapping("/exportSignInRecode")
    public void userLogin(HttpServletResponse response,
                          @ApiParam(value = "是否是随机生成的") @RequestParam(required = false) Boolean isRandom,
                          @ApiParam(value = "开始时间") @RequestParam(required = false) Date startTime,
                          @ApiParam(value = "结束时间") @RequestParam(required = false) Date endTime,
                          @ApiParam(value = "人数") @RequestParam(required = false) Integer num){
        isRandom = ObjectUtils.defaultIfNull(isRandom, true);
        String filename = "SignInRecode" + CommonConstant.EXCEL_SUFFIX;
        // 文件的类型
        response.setContentType("application/vnd.ms-excel");
        // 设置浏览器的编码
        response.setCharacterEncoding("utf-8");
        // 告诉浏览器以附件的形式下载
        response.setHeader("Content-disposition","attachment;filename*=utf-8''" + filename);
        List<InOutRegistrationVo> inOutRegistrationVos = null;
        if (isRandom) {
            inOutRegistrationVos = signInService.generateSignInRecord(startTime, endTime, num);
        } else {
            // 获取所有签到数据
            List<InOutRegistration> inOutRegistrations = signInService.list();
            inOutRegistrationVos = BeanUtil.copyToList(inOutRegistrations, InOutRegistrationVo.class);
        }

        // 生成Excel
        try {
            EasyExcel.write(response.getOutputStream(), InOutRegistrationVo.class)
                    .registerWriteHandler(new SimpleColumnWidthStyleStrategy(20))
                    .sheet("签到记录")
                    .doWrite(inOutRegistrationVos);
        } catch (IOException e) {
            throw new CustomException(ResponseStatusEnum.EXPORE_EXCEL_ERROR);
        }
    }


    @ApiOperation(value = "上传图片", notes = "上传用户头像")
    @PostMapping("/getImageUrl")
    public R uploadImage(@RequestParam("file") MultipartFile image) {
        if (ObjectUtil.isNull(image)) {
            throw new CustomException(ResponseStatusEnum.PARAMS_ERROR);
        }
        try {
            // 获取原始文件名称
            String originalFilename = image.getOriginalFilename();
            // 生成新文件名
            String fileName = createNewFileName(originalFilename);
            // 保存文件
            image.transferTo(new File(fileName));
            // 返回结果
            log.debug("文件上传成功，{}", fileName);
            return R.success(fileName);
        } catch (IOException e) {
            throw new RuntimeException("文件上传失败", e);
        }
    }

    private String createNewFileName(String originalFilename) {
        // 获取后缀
        String suffix = StrUtil.subAfter(originalFilename, ".", true);
        // 生成目录
        String name = UUID.randomUUID().toString();
        // 判断目录是否存在
        File dir = new File(BASIC_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        // 生成文件名
        return BASIC_DIR + name + "." + suffix;
    }

}
