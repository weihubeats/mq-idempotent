package strategy;

import com.mq.idempotent.core.alert.strategy.AlertStrategy;
import com.mq.idempotent.core.alert.strategy.AlertStrategyFactory;
import com.mq.idempotent.core.alert.strategy.LarkAlarmStrategy;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author : wh
 * @date : 2022/6/15 18:17
 * @description:
 */
public class AlertStrategyFactoryTest {

    @Test
    public void test() {
        AlertStrategy lark = AlertStrategyFactory.newInstance("lark");
        assertTrue(lark instanceof LarkAlarmStrategy);
    }
    
}
