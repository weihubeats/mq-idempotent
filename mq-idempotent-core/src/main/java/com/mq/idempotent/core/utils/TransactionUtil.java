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

package com.mq.idempotent.core.utils;

import java.util.function.Supplier;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

/**
 *@author : wh
 *@date : 2022/7/20 11:21
 *@description:
 */
@AllArgsConstructor
@Slf4j
public class TransactionUtil {
    
    private final PlatformTransactionManager transactionManager;
    
    public boolean transact(Runnable runnable, TransactionDefinition transactionDefinition) {
        TransactionStatus status = transactionManager.getTransaction(transactionDefinition);
        try {
            runnable.run();
            transactionManager.commit(status);
            return true;
        } catch (Exception e) {
            transactionManager.rollback(status);
            throw e;
        }
        
    }
    
    public <T> T transact(Supplier<T> supplier, TransactionDefinition transactionDefinition) {
        TransactionStatus status = transactionManager.getTransaction(transactionDefinition);
        try {
            T t = supplier.get();
            transactionManager.commit(status);
            return t;
        } catch (Exception e) {
            transactionManager.rollback(status);
            throw e;
        }
        
    }
    
    /**
     *  默认创博行为 PROPAGATION_REQUIRED
     * @param runnable
     * @return
     */
    public boolean transact(Runnable runnable) {
        return transact(runnable, new DefaultTransactionDefinition());
    }
    
    public <T> T transact(Supplier<T> supplier) {
        return transact(supplier, new DefaultTransactionDefinition());
        
    }
    
}
