package com.uxin.commons.alimq;

import com.aliyun.openservices.ons.api.*;
import com.uxin.commons.alimq.serialize.Hessian2Serialization;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

/**
 * @author ellis.luo
 * @date 16/11/4 ����5:15
 * @description MQ ������
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
        // // �����ΪGmail�еı�ǩ������Ϣ�����ٹ��࣬����Consumerָ������������MQ����������
        // "TagA",
        // // Message Body
        // // �κζ�������ʽ�����ݣ� MQ�����κθ�Ԥ��
        // // ��ҪProducer��ConsumerЭ�̺�һ�µ����л��ͷ����л���ʽ
        // "Hello MQ".getBytes());
        // ���ô�����Ϣ��ҵ��ؼ����ԣ��뾡����ȫ��Ψһ���Է��������޷������յ���Ϣ����£���ͨ��MQ����̨��ѯ��Ϣ������
        // ע�⣺������Ҳ����Ӱ����Ϣ�����շ�
        // msg.setKey("ORDERID_100");
        // ������Ϣ��ֻҪ�����쳣���ǳɹ�
        // ��ӡMessage ID���Ա�������Ϣ����״̬��ѯ

        Message msg = new Message( //
                // Message Topic
                entry.getTopic(),
                // Message Tag,
                // �����ΪGmail�еı�ǩ������Ϣ�����ٹ��࣬����Consumerָ������������MQ����������
                entry.getTags(),
                // Message Body
                // �κζ�������ʽ�����ݣ� MQ�����κθ�Ԥ��
                // ��ҪProducer��ConsumerЭ�̺�һ�µ����л��ͷ����л���ʽ
                Hessian2Serialization.serialize(entry.getBody()));

        SendResult sendResult = producer.send(msg);
        logger.info("Send Message success. Message ID is: " + sendResult.getMessageId());
    }
}
