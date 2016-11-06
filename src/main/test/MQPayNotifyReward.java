package src.main.test;

import com.uxin.commons.alimq.AbstractConsumer;
import com.uxin.commons.alimq.MQAnnotation;
import com.uxin.commons.alimq.MQEntry;
import com.uxin.commons.alimq.MQTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author ellis.luo
 * @date 16/11/4 下午6:58
 * @description
 */
@Component
public class MQPayNotifyReward extends AbstractConsumer<String>
{
    private static final Logger logger = LoggerFactory.getLogger(MQPayNotifyReward.class);

    @Override
    @MQAnnotation(topic = MQTopic.PAY_NOTIFY_REWARD)
    public void process(MQEntry<String> entry)
    {
        if (entry.getBody().equals("hello"))
        {
            logger.info("i success");
        }
        else
        {
            throw new RuntimeException("=======xxxx======");
        }
    }
}
