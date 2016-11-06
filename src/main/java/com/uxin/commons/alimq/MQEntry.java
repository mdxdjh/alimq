package com.uxin.commons.alimq;

/**
 * @author ellis.luo
 * @date 16/11/6 上午10:43
 * @description
 */

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * MQ消息的包体
 *
 * @author: ellis.luo
 * @date 2016年7月28日 下午6:13:27</br>
 *       <br/>
 */
public class MQEntry<T>
{
    /**
     * MQ消息的业务Topic，没有分类的情况下可以和MQ的Topic相同
     */
    public String topic;
    /**
     * MQ消息的包体信息，需要实现Serializable接口
     */
    public T body;
    /**
     * Message Tag,
     * 可理解为Gmail中的标签，对消息进行再归类，方便Consumer指定过滤条件在MQ服务器过滤
     * @param topic
     * @param tags
     * @param body
     */
    public String tags;

    public MQEntry(String topic, String tags, T body)
    {
        this.topic = topic;
        this.tags = tags;
        this.body = body;
    }

    public T getBody()
    {
        return body;
    }

    public void setBody(T body)
    {
        this.body = body;
    }

    public String getTopic()
    {
        return topic;
    }

    public void setTopic(String topic)
    {
        this.topic = topic;
    }

    public String getTags()
    {
        return tags;
    }

    public void setTags(String tags)
    {
        this.tags = tags;
    }

    @Override
    public String toString()
    {
        return ToStringBuilder.reflectionToString(this);
    }

}
