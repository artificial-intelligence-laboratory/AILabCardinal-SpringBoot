package com.ailab.ailabsystem.model.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentStyle;
import com.alibaba.excel.annotation.write.style.HeadStyle;
import com.alibaba.excel.enums.poi.HorizontalAlignmentEnum;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author xiaozhi
 */
@Data
@HeadStyle(horizontalAlignment = HorizontalAlignmentEnum.CENTER) // 表头样式：居中
@ContentStyle(horizontalAlignment = HorizontalAlignmentEnum.CENTER) // 内容样式：居中
@ColumnWidth(20)    // 宽度
public class InOutRegistrationVo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     *  签到时间
     */
    @ExcelProperty("签到时间")
    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    private Date signInTime;

    /**
     * 签到者姓名
     */
    @ExcelProperty("姓名")
    private String signInUserRealName;

    /**
     * 签到者班级
     */
    @ExcelProperty("班级")
    private String signInUserClass;

    /**
     * 学号
     */
    @ExcelProperty("学号")
    private String studentNumber;

    /**
     * 任务
     */
    @ExcelProperty("任务")
    private String task;

    /**
     * 签出时间
     */
    @ExcelProperty("签出时间")
    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    private Date checkOutTime;

    /**
     * 备注
     */
    @ExcelProperty("备注")
    private String remark;

}
