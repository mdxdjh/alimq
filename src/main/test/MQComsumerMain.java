package src.main.test;

import com.uxin.commons.alimq.AbstractConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MQComsumerMain implements ApplicationListener<ApplicationEvent>
{
    private static final Logger logger = LoggerFactory.getLogger(MQComsumerMain.class);

    private static boolean isStart = false;

    @Autowired
    private List<AbstractConsumer> mqs;

    public void onApplicationEvent(ApplicationEvent event)
    {
        if (!isStart)
        {
            for (AbstractConsumer mq : mqs)
            {
                mq.todo();
            }
        }
    }
}
