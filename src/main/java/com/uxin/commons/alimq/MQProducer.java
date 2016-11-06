package com.uxin.commons.alimq;

import com.aliyun.openservices.ons.api.*;
import com.uxin.commons.alimq.serialize.Hessian2Serialization;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

/**
 * @author ellis.luo
 * @date 16/11/4 下午5:15
 * @description MQ 生产者
 */
public class MQProducer
{
    private static final Logger logger = LoggerFactory.getLogger(MQProducer.class);

    private static Producer producer;

    private static String producerId = "PID_uxin2016";
    private static String accessKey = "LTAI1F5YB8LVyiOi";
    private static String secretKey = "I7wtbvphZbgrcHl4xmhw19U48hFxFA";

    static
    {
        Properties properties = new Properties();
        properties.put(PropertyKeyConst.ProducerId, producerId);// ����MQ����̨������Producer ID
        properties.put(PropertyKeyConst.AccessKey, accessKey);// ��Ȩ��AccessKey���ڰ����Ʒ������������̨����
        properties.put(PropertyKeyConst.SecretKey, secretKey);// ��Ȩ��SecretKey���ڰ����Ʒ������������̨����
        producer = ONSFactory.createProducer(properties);
        // �ڷ�����Ϣǰ���������start����������Producer��ֻ�����һ�μ���
        producer.start();
    }

    public static void send(MQEntry entry)
    {
        // Message msg = new Message( //
        // // Message Topic
        // "TopicTestMQ",
        // // Message Tag,
        // // 可理解为Gmail中的标签，对消息进行再归类，方便Consumer指定过滤条件在MQ服务器过滤
        // "TagA",
        // // Message Body
        // // 任何二进制形式的数据， MQ不做任何干预，
        // // 需要Producer与Consumer协商好一致的序列化和反序列化方式
        // "Hello MQ".getBytes());
        // 设置代表消息的业务关键属性，请尽可能全局唯一，以方便您在无法正常收到消息情况下，可通过MQ控制台查询消息并补发
        // 注意：不设置也不会影响消息正常收发
        // msg.setKey("ORDERID_100");
        // 发送消息，只要不抛异常就是成功
        // 打印Message ID，以便用于消息发送状态查询

        Message msg = new Message( //
                entry.getTopic(),
                entry.getTags(),
                Hessian2Serialization.serialize(entry.getBody()));

        SendResult sendResult = producer.send(msg);
        logger.info("Send Message success. Message ID is: " + sendResult.getMessageId());
    }
}
