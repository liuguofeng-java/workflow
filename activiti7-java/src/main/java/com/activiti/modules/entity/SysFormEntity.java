package com.activiti.modules.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;


/**
 * activiti相关->流程表单
 *
 * @author liuguofeng
 * @email liuguofeng-java@qq.com
 * @date 2023-10-27 09:44:39
 */
@Data
@TableName("sys_form")
public class SysFormEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 表单id
     */
    @TableId
    private String formId;
    /**
     * 表单名称
     */
    private String formName;
    /**
     * 表单数据
     */
    private String formData;

    /**
     * 是否是系统内置,1:是,0:否
     */
    private Integer isSys;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm",timezone = "GMT+8")
    private Date createTime;
    /**
     * 修改时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm",timezone = "GMT+8")
    private Date updateTime;

}
