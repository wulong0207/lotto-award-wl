package com.hhly.award.service.rabbitmq.produce;

public interface MQProducer {
	/**
     * 发送消息到指定队列
     * @param queueKey
     * @param object
     */
    public void sendDataToQueue(String queueKey, String message);
    
    /**
     * 发送路由mq消息
     * @author jiangwei
     * @Version 1.0
     * @CreatDate 2018年5月3日 下午12:04:45
     * @param exchange 交换机
     * @param key 路由
     * @param message 消息
     */
    public void sendDataToRouting(String exchange,String routing,String message);
}
