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
    public void sendMsg() throws Exception {
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