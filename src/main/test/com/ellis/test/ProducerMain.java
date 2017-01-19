package src.main.test.com.ellis.test;

import com.ellis.commons.alimq.MQProducer;
import com.ellis.commons.alimq.common.MQEntry;
import com.ellis.commons.alimq.common.MQTag;
import com.ellis.commons.alimq.common.MQTopic;

/**
 * @author ellis.luo
 * @date 16/11/6 上午11:59
 * @description
 */
public class ProducerMain
{
    public static void main(String[] args)
    {
        for (int i = 0; i < 5; i++)
        {
            MQEntry entry = new MQEntry(MQTopic.test2, MQTag.PAY, "hello");
            MQProducer.send(entry);
        }
        for (int i = 0; i < 5; i++)
        {
            MQEntry entry = new MQEntry(MQTopic.test1, MQTag.PAY, "hello");
            MQProducer.send(entry);
        }

    }
}
