package com.uxin.test;

import com.uxin.commons.alimq.MQConsumer;
import com.uxin.commons.alimq.common.ConsumerApi;
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
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private static boolean isStart = false;

    @Autowired
    private List<ConsumerApi> consumers;

    @Autowired
    private MQConsumer main;

    public void onApplicationEvent(ApplicationEvent event)
    {
        if (!isStart)
        {
            main.initConsumerProcess(consumers);
        }
    }

}
