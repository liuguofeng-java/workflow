package com.activiti.modules.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;


/**
 * 流程定义节点表单信息
 *
 * @author liuguofeng
 * @email liuguofeng-java@qq.com
 * @date 2023-11-28 14:50:18
 */
@Data
@TableName("sys_node_form")
public class SysNodeFormEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 节点id
     */
    @TableId
    private String node;
    /**
     * 流程id
     */
    private String definitionId;
    /**
     * 流程定义节点唯一标识
     */
    private String activityId;
    /**
     * 表单详情
     */
    private String formJson;
    /**
     * 是否是主表单,1:是,2:否
     */
    private Long isMainFrom;
    /**
     * 创建时间
     */
    private Date createTime;

}
