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

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.mq.idempotent.core.alert.AlertDTO;
import com.mq.idempotent.core.spi.Join;
import com.mq.idempotent.core.utils.ThreadFactoryImpl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.apache.commons.lang3.exception.ExceptionUtils;

import org.springframework.util.ObjectUtils;

/**
 * @author : wh
 * @date : 2022/6/15 18:05
 * @description:
 */
@Join
public class LarkAlarmStrategy implements AlertStrategy {
    
    private static final String TEMPLATE =
            "{\"msg_type\":\"interactive\",\"card\":{\"config\":{\"wide_screen_mode\":true},\"header\":{\"template\":\"greed\",\"title\":{\"content\":\"mq 幂等报警\",\"tag\":\"plain_text\"}},\"elements\":[{\"fields\":[{\"is_short\":true,\"text\":{\"content\":\"** 唯一key：** %s\",\"tag\":\"lark_md\"}},{\"is_short\":true,\"text\":{\"content\":\"** methodName：** %s\",\"tag\":\"lark_md\"}},{\"is_short\":true,\"text\":{\"content\":\"** 异常** %s\",\"tag\":\"lark_md\"}}],\"tag\":\"div\"},{\"tag\":\"hr\"}]}}";
    
    private static final String ALL = ",{\"tag\":\"div\",\"text\":{\"content\":\"<at id=all></at> \",\"tag\":\"lark_md\"}}";
    
    private static final int FEISHU_MESSAGE_HASH_MAX_LENGTH = 30 * 1024;
    
    private static final OkHttpClient OK_HTTP_CLIENT = new OkHttpClient()
            .newBuilder().connectTimeout(50L, TimeUnit.SECONDS)
            .readTimeout(60L, TimeUnit.SECONDS)
            .build();
    
    private static final ExecutorService executor = new ThreadPoolExecutor(1, 3, 60, TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(100), new ThreadFactoryImpl("feishu-"));;
    
    @Override
    public boolean sendMsg(AlertDTO alertDTO) {
        String stackTrace = ExceptionUtils.getStackTrace(alertDTO.getThrowable());
        // 格式化异常
        if (!ObjectUtils.isEmpty(stackTrace)) {
            stackTrace = stackTrace.replaceAll("\n", "\\\\n");
            stackTrace = stackTrace.replaceAll("\t", "\\\\t");
            if (stackTrace.getBytes(StandardCharsets.UTF_8).length > FEISHU_MESSAGE_HASH_MAX_LENGTH) {
                stackTrace = stackTrace.substring(0, new String(new byte[FEISHU_MESSAGE_HASH_MAX_LENGTH]).length());
            }
        }
        
        stackTrace = String.format(TEMPLATE, alertDTO.getKey(), alertDTO.getMethod().getName(), stackTrace);
        
        RequestBody body = RequestBody.create(
                MediaType.parse("application/json"), stackTrace);
        Request request = new Request.Builder()
                .url(alertDTO.getWebHook())
                .post(body)
                .build();
        try {
            OK_HTTP_CLIENT.newCall(request).execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return false;
    }
    
}
