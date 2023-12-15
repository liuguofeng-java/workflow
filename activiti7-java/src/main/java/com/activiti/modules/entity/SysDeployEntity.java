package com.activiti.modules.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;


/**
 * 流程部署详情
 *
 * @author liuguofeng
 * @email liuguofeng-java@qq.com
 * @date 2023-12-15 11:48:02
 */
@Data
@TableName("sys_deploy")
public class SysDeployEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 部署id
     */
    @TableId
    private String deployId;
    /**
     * 绑定数据库表的名称
     */
    private String tableName;
    /**
     * 绑定数据库表的备注
     */
    private String tableComment;
    /**
     * 创建时间
     */
    private Date createTime;

}
