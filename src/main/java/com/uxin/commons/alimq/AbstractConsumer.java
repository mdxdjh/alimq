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
    private String consumerId;
    private String accessKey;
    private String secretKey;

    private String topic;
    private String tag;

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

            logger.info(getClass().getSimpleName() + " init end, properties:" + properties);
        }
        catch (Exception e)
        {
            logger.error("MQ Consumer " + getClass().getSimpleName() + " init error ", e);
            throw new RuntimeException("MQ Consumer " + getClass().getSimpleName() + " init error ", e);
        }
    }

    public void destroy(){
        consumer.shutdown();
    }
    public void todo()
    {
        if (consumer == null)
        {
            init();
        }

        consumer.subscribe(topic, tag, new MessageListener()
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

    public String getConsumerId() {
        return consumerId;
    }

    public void setConsumerId(String consumerId) {
        this.consumerId = consumerId;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
