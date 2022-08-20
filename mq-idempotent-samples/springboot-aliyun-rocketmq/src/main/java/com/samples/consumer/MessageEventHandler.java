package com.samples.consumer;

import com.aliyun.openservices.ons.api.Message;
import com.mq.idempotent.core.annotation.Idempotent;
import com.samples.entity.TestDO;
import com.samples.mapper.TestMapper;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author : wh
 * @date : 2021/11/15 20:02
 * @description:
 */
@Service
@Slf4j
public class MessageEventHandler {

	@Autowired
	TestMapper testMapper;


	@Idempotent
	@Transactional(rollbackFor = Exception.class)
	public boolean consumer(Message message) {
		String msg = new String(message.getBody());
		System.out.println("消息id " + message.getMsgID());
		System.out.println("消息key " + message.getKey());
		System.out.println("消费成功, msg " + msg);
		TestDO testDO = new TestDO();
		testDO.setMsg("test");
		try {
			testMapper.insert(testDO);
		}
		catch (Exception e) {
			log.error("插入数据库异常", e);
			throw new RuntimeException(e);
		}
		System.out.println("插入数据库成功");
		return true;
	}


}
