package com.activiti.modules.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;


/**
 * 执行监听器
 *
 * @author liuguofeng
 * @email liuguofeng-java@qq.com
 * @date 2023-12-06 22:18:27
 */
@Data
@TableName("sys_listener")
public class SysListenerEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 监听器id
     */
    @TableId
    private String listenerId;
    /**
     * 监听器名称
     */
    private String listenerName;
    /**
     * java类
     */
    private String javaClass;
    /**
     * 是否是系统内置,1:是,0:否
     */
    private Integer isSys;
    /**
     * 备注
     */
    private String remark;
    /**
     * 修改时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
    private Date updateTime;
    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
    private Date createTime;

}
