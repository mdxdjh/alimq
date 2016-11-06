package src.main.test;

import com.uxin.commons.alimq.MQEntry;
import com.uxin.commons.alimq.MQProducer;
import com.uxin.commons.alimq.MQTag;
import com.uxin.commons.alimq.MQTopic;

/**
 * @author ellis.luo
 * @date 16/11/6 上午11:59
 * @description
 */
public class ProducerMain
{
    public static void main(String[] args)
    {
        MQEntry entry = new MQEntry(MQTopic.PAY_NOTIFY_REWARD, MQTag.PAY, "hello");
        MQProducer.send(entry);

    }
}
