package com.uxin.commons.alimq;

import com.aliyun.openservices.ons.api.*;
import com.uxin.commons.alimq.common.MQEntry;
import com.uxin.commons.alimq.serialize.Hessian2Serialization;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import java.util.Properties;

/**
 * @author ellis.luo
 * @date 16/11/4 下午5:15
 * @description MQ生产者
 */
public class MQProducer
{
    private static final Logger logger = LoggerFactory.getLogger(MQProducer.class);

    private static Producer producer;

    private Consumer consumer;
//    @Value("${producerId}")
//    private static String producerId;
//    @Value("${accessKey}")
//    private static String accessKey;
//    @Value("${secretKey}")
//    private static String secretKey;

     private static String producerId = "PID_UXINDEV";
     private static String accessKey = "LTAI1F5YB8LVyiOi";
     private static String secretKey = "I7wtbvphZbgrcHl4xmhw19U48hFxFA";

    static
    {
        Properties properties = new Properties();
        properties.put(PropertyKeyConst.ProducerId, producerId);// 您在MQ控制台创建的Producer ID
        properties.put(PropertyKeyConst.AccessKey, accessKey);// 鉴权用AccessKey，在阿里云服务器管理控制台创建
        properties.put(PropertyKeyConst.SecretKey, secretKey);// 鉴权用SecretKey，在阿里云服务器管理控制台创建
        producer = ONSFactory.createProducer(properties);
        // 在发送消息前，必须调用start方法来启动Producer，只需调用一次即可
        producer.start();
    }

    /**
     * (不丢失) 原理：同步发送是指消息发送方发出数据后，会在收到接收方发回响应之后才发下一个数据包的通讯方式。 应用场景：此种方式应用场景非常广泛，例如重要通知邮件、报名短信通知、营销短信系统等。
     *
     * @param entry
     */
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
                entry.getTopic(), entry.getTags(), Hessian2Serialization.serialize(entry.getBody()));

        SendResult sendResult = producer.send(msg);
        logger.info("Send Message success. Message ID is: " + sendResult.getMessageId());
    }

    /**
     * (不丢失) 原理：异步发送是指发送方发出数据后，不等接收方发回响应，接着发送下个数据包的通讯方式。MQ
     * 的异步发送，需要用户实现异步发送回调接口（SendCallback），在执行消息的异步发送时，应用不需要等待服务器响应即可直接返回，通过回调接口接收务器响应，并对服务器的响应结果进行处理。
     * 应用场景：异步发送一般用于链路耗时较长，对 RT 响应时间较为敏感的业务场景，例如用户视频上传后通知启动转码服务，转码完成后通知推送转码结果等。
     *
     * @param entry
     */
    public static void sendAsync(MQEntry entry)
    {
        Message msg = new Message( //
                entry.getTopic(), entry.getTags(), Hessian2Serialization.serialize(entry.getBody()));

        producer.sendAsync(msg, new SendCallback()
        {
            @Override
            public void onSuccess(final SendResult sendResult)
            {
                // 消费发送成功
                System.out.println("send async message success. topic=" + sendResult.getTopic() + ", msgId="
                        + sendResult.getMessageId());
            }

            @Override
            public void onException(OnExceptionContext context)
            {
                // 消息发送失败
                System.out.println(
                        "send async message failed. topic=" + context.getTopic() + ", msgId=" + context.getMessageId());
            }
        });
        // 在callback返回之前即可取得msgId。
        System.out.println("send message async. topic=" + msg.getTopic() + ", msgId=" + msg.getMsgID());
    }

    /**
     * （可能丢失） 原理：单向（Oneway）发送特点为只负责发送消息，不等待服务器回应且没有回调函数触发，即只发送请求不等待应答。此方式发送消息的过程耗时非常短，一般在微秒级别。
     * 应用场景：适用于某些耗时非常短，但对可靠性要求并不高的场景，例如日志收集。
     *
     * @param entry
     */
    public static void sendOneway(MQEntry entry)
    {
        Message msg = new Message( //
                entry.getTopic(), entry.getTags(), Hessian2Serialization.serialize(entry.getBody()));

        producer.sendOneway(msg);
        logger.info("Send Message sendOneway. msg is: " + msg.getBody());
    }

}
