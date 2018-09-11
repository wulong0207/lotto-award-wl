package com.hhly.award.config;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.amqp.SimpleRabbitListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @desc RabbitMq配置类
 * @author wulong
 * @date 2018年7月26日
 * @company 益彩网络
 * @version v1.0
 */
@Configuration
public class RabbitMqConfig {

	/**
	 * 消费者预取消费数
	 */
	@Value("${rabbitmq.prefetchCount}")
	private int prefetchCount;

	/**
	 * 消费者并发数
	 */
	@Value("${rabbitmq.concurrent}")
	private int concurrent;

	/**
	 * 并发设置的线程池线程数
	 */
	@Value("${rabbitmq.threadNum}")
	private int threadNum;

	/**
	 * @desc Rabbit监听容器工厂
	 * @author huangb
	 * @date 2018年6月8日
	 * @param configurer
	 * @param connectionFactory mq连接工厂,自动注入
	 * @return
	 */
	@Bean("rabbitmqContainerFactory")
	public SimpleRabbitListenerContainerFactory rabbitmqContainerFactory(
			SimpleRabbitListenerContainerFactoryConfigurer configurer, ConnectionFactory connectionFactory) {
		SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
		factory.setPrefetchCount(prefetchCount);
		factory.setConcurrentConsumers(concurrent);
		// 线程的线程池
		ExecutorService executorService = Executors.newFixedThreadPool(threadNum);
		factory.setTaskExecutor(executorService);
		configurer.configure(factory, connectionFactory);
		return factory;
	}
}
