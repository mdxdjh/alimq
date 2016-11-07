package com.uxin.commons.alimq;

import com.aliyun.openservices.ons.api.*;
import com.uxin.commons.alimq.common.MQAnnotation;
import com.uxin.commons.alimq.common.MQEntry;
import com.uxin.commons.alimq.serialize.Hessian2Serialization;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.Properties;

/**
 * @author ellis.luo
 * @date 16/11/4 下午6:40
 * @description MQ消费者抽象类
 */
public abstract class AbstractConsumer<T>
{
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private Consumer consumer;
    private String consumerId = "CID_uxin2016";
    private String accessKey = "LTAI1F5YB8LVyiOi";
    private String secretKey = "I7wtbvphZbgrcHl4xmhw19U48hFxFA";

    private String topic;

    public abstract void process(MQEntry<T> entry);

    public void init()
    {
        try
        {
            Properties properties = new Properties();
            properties.put(PropertyKeyConst.ConsumerId, consumerId);// 您在MQ控制台创建的Consumer ID
            properties.put(PropertyKeyConst.AccessKey, accessKey);// 鉴权用AccessKey，在阿里云服务器管理控制台创建
            properties.put(PropertyKeyConst.SecretKey, secretKey);// 鉴权用SecretKey，在阿里云服务器管理控制台创建
            consumer = ONSFactory.createConsumer(properties);

            analysisAnnotation(); // 解析注解配置信息
            logger.info(getClass().getSimpleName() + " init end, properties:" + properties);
        }
        catch (Exception e)
        {
            logger.error("MQ Consumer " + getClass().getSimpleName() + " init error ", e);
            throw new RuntimeException("MQ Consumer " + getClass().getSimpleName() + " init error ", e);
        }
    }

    public void todo()
    {
        if (consumer == null)
        {
            init();
        }

        consumer.subscribe(topic, "*", new MessageListener()
        {
            public Action consume(Message message, ConsumeContext context)
            {
                logger.info("MQ " + getClass().getSimpleName() + " Receive: " + message);
                MQEntry entry = new MQEntry(message.getTopic(), message.getTag(),
                        Hessian2Serialization.deserialize(message.getBody(), Object.class));
                try
                {
                    process(entry);
                }
                catch (Throwable t)
                {
                    logger.error("MQ Consumer process error ", t);
                    return Action.ReconsumeLater;
                }

                return Action.CommitMessage;
            }
        });
        consumer.start();
    }

    private void analysisAnnotation() throws Exception
    {
        Method method = Class.forName(getClass().getName()).getDeclaredMethod("process", MQEntry.class);
        MQAnnotation mqAnnotation = method.getAnnotation(MQAnnotation.class);
        topic = mqAnnotation.topic();
    }

}
