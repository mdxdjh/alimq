package src.main.test.com.ellis.test;

import com.ellis.commons.alimq.common.ConsumerApi;
import com.ellis.commons.alimq.common.MQEntry;
import com.ellis.commons.alimq.common.MQTag;
import com.ellis.commons.alimq.common.MQTopic;
import org.springframework.stereotype.Component;

/**
 * @author ellis.luo
 * @date 16/11/4 下午6:58
 * @description
 */
@Component
public class Comsuner1 implements ConsumerApi<String>
{
    @Override
    public String getTopic()
    {
        return MQTopic.test1;
    }

    @Override
    public String getTag()
    {
        return MQTag.PAY;
    }

    @Override
    public void process(MQEntry<String> entry)
    {
        System.out.println("111111");
    }
}
