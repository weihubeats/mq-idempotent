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
