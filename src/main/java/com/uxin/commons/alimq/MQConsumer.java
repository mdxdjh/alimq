package com.uxin.commons.alimq;

import com.aliyun.openservices.ons.api.*;
import com.uxin.commons.alimq.common.ConsumerApi;
import com.uxin.commons.alimq.common.MQEntry;
import com.uxin.commons.alimq.serialize.Hessian2Serialization;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;
import java.util.Properties;

/**
 * @author ellis.luo
 * @date 16/11/4 下午6:40
 * @description MQ消费者抽象类
 */
public class MQConsumer
{
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private Consumer consumer;
    @Value("${consumerId}")
    private String consumerId;
    @Value("${accessKey}")
    private String accessKey;
    @Value("${secretKey}")
    private String secretKey;

    public void initConsumerProperties()
    {
        try
        {
            Properties properties = new Properties();
            properties.put(PropertyKeyConst.ConsumerId, consumerId);// 您在MQ控制台创建的Consumer ID
            properties.put(PropertyKeyConst.AccessKey, accessKey);// 鉴权用AccessKey，在阿里云服务器管理控制台创建
            properties.put(PropertyKeyConst.SecretKey, secretKey);// 鉴权用SecretKey，在阿里云服务器管理控制台创建
            consumer = ONSFactory.createConsumer(properties);

            logger.info(getClass().getSimpleName() + " init end, properties:" + properties);
        }
        catch (Exception e)
        {
            logger.error("MQ Consumer " + getClass().getSimpleName() + " init error ", e);
            throw new RuntimeException("MQ Consumer " + getClass().getSimpleName() + " init error ", e);
        }
    }

    public void initConsumerProcess(List<ConsumerApi> consumers)
    {
        if (consumer == null)
        {
            initConsumerProperties();
        }

        for (final ConsumerApi api : consumers)
        {
            logger.info("MQ subscribe Topic:{}, tag:{}", api.getTopic(), api.getTag());
            consumer.subscribe(api.getTopic(), api.getTag(), new MessageListener()
            {
                public Action consume(Message message, ConsumeContext context)
                {
                    logger.info("MQ " + api.getClass().getSimpleName() + " Receive: " + message);
                    MQEntry entry = new MQEntry(message.getTopic(), message.getTag(),
                            Hessian2Serialization.deserialize(message.getBody(), Object.class));
                    try
                    {
                        api.process(entry);
                    }
                    catch (Throwable t)
                    {
                        logger.error("MQ Consumer process error ", t);
                        return Action.ReconsumeLater;
                    }

                    return Action.CommitMessage;
                }
            });
        }

        consumer.start();
    }
}
