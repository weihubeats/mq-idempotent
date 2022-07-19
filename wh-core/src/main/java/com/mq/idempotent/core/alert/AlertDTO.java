package com.mq.idempotent.core.alert;

import java.lang.reflect.Method;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *@author : wh
 *@date : 2022/7/19 14:27
 *@description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AlertDTO {

	/**
	 * 唯一key
	 */
	private String key;

	/**
	 * 方法
	 */
	private Method method;

	/**
	 * 异常
	 */
	private Throwable throwable;

	/**
	 * 报警url
	 */
	private String webHook;

}
