package com.activiti.modules.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 系统用户
 */
@Data
@TableName("sys_user")
public class SysUserEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 用户ID
	 */
	@TableId
	private String userId;

	/**
	 * 账号
	 */
	private String username;
	/**
	 * 姓名
	 */
	private String account;
	/**
	 * 密码
	 */
	private String password;

	/**
	 * 邮箱
	 */
	private String email;

	/**
	 * 手机号
	 */
	private String mobile;

	/**
	 * 状态  0：禁用   1：正常
	 */
	private Integer status;

	/**
	 * 创建时间
	 */
	private Date createTime;
	/**
	 * 修改者ID
	 */
	private String updateUser;

	/**
	 * 修改时间
	 */
	private Date updateTime;
}
