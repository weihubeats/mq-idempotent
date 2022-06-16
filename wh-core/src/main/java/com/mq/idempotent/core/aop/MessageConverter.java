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

package com.mq.idempotent.core.aop;

import java.lang.reflect.Method;

/**
 * @author : wh
 * @date : 2021/11/15 13:51
 * @description: 消息转换器
 */
public interface MessageConverter<T> {

    /**
     * 
     * @param t 消息
     * @param field 注解 Idempotent 中的 field值 
     * @param method 被拦截方法
     * @param args 被拦截方法参数
     * @return
     */
    String getUniqueKey(T t, String field, Method method, Object[] args);

}
