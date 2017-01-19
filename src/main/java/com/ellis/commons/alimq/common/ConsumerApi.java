package com.ellis.commons.alimq.common;

/**
 * @author ellis.luo
 * @date 16/12/8 下午1:22
 * @description
 */
public interface ConsumerApi<T>
{
    public String getTopic();

    public String getTag();

    public void process(MQEntry<T> entry);
}
