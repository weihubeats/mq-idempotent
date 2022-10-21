/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
        } catch (Exception e) {
            log.error("插入数据库异常", e);
            throw new RuntimeException(e);
        }
        System.out.println("插入数据库成功");
        return true;
    }
    
}
