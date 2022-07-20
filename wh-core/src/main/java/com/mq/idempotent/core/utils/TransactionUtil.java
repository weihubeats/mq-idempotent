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
		}
		catch (Exception e) {
			transactionManager.rollback(status);
			log.error("transact error", e);
			return false;
		}

	}

	public <T> T transact(Supplier<T> supplier, TransactionDefinition transactionDefinition) {
		TransactionStatus status = transactionManager.getTransaction(transactionDefinition);
		try {
			T t = supplier.get();
			transactionManager.commit(status);
			return t;
		}
		catch (Exception e) {
			transactionManager.rollback(status);
			log.error("transact error", e);
			return null;
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
