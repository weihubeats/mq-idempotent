package com.samples.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 *@author : wh
 *@date : 2022/7/20 14:26
 *@description:
 */
@TableName("test")
@Data
public class TestDO {

	@TableId(type = IdType.AUTO)
	private Long id;
	
	private String msg;
	
}
