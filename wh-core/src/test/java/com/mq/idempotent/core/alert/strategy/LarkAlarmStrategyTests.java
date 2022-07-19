package com.mq.idempotent.core.alert.strategy;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

import com.mq.idempotent.core.alert.AlertDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.when;


/**
 *@author : wh
 *@date : 2022/7/19 14:22
 *@description:
 */
@ExtendWith(MockitoExtension.class)
public class LarkAlarmStrategyTests {
	
	@Mock
	private Method method;

	@Test
	public void sendMsg() throws Exception{
		AlertStrategy lark = AlertStrategyFactory.newInstance("lark");
		when(method.getName()).thenReturn("testName");
		AlertDTO alertDTO = new AlertDTO();
		alertDTO.setKey("testKey");
		alertDTO.setMethod(method);
		alertDTO.setThrowable(new Throwable("test error"));
		alertDTO.setWebHook("");
		lark.sendMsg(alertDTO);
		TimeUnit.SECONDS.sleep(3);
	}
}