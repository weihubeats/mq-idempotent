package com.mq.idempotent.core.idempotent;

/**
 * @author : wh
 * @date : 2021/11/6 14:49
 */
public interface Monitor {

    default void monitor(UnMessage unMessage){
        System.out.println("幂等插入redis失败，请手动处理,唯一key " + unMessage.getMsgUniqKey());
    }
}
