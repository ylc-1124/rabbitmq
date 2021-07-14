package cn.sust.springbootRabbitmq.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class DelayQueueConfig {
    //延迟交换机名称
    public static final String DELAY_EXCHANGE_NAME = "delayed.exchange";
    //延迟队列名称
    public static final String DELAY_QUEUE_NAME = "delayed.queue";
    //routingKey
    public static final String DELAY_ROUTING_KEY = "delayed.routingkey";

    //声明延迟交换机  基于延迟插件
    @Bean
    public CustomExchange delayedExchange() {
        /**
         * 1.交换机名称
         * 2.交换机类型
         * 3.是否持久化
         * 4.是否自动删除
         * 5.参数
         */
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("x-delayed-type", "direct");
        return new CustomExchange(DELAY_EXCHANGE_NAME, "x-delayed-message", true,
                false, arguments);
    }

    //声明延迟队列
    @Bean
    public Queue delayedQueue() {
        return QueueBuilder.durable(DELAY_QUEUE_NAME).build();
    }

    //绑定延迟队列和延迟交换机
    @Bean
    public Binding delayedQueueBindingDelayedExchange(
            @Qualifier("delayedQueue") Queue delayedQueue,
            @Qualifier("delayedExchange") CustomExchange delayedExchange) {

        return BindingBuilder.bind(delayedQueue).to(delayedExchange).with(DELAY_ROUTING_KEY).noargs();
    }
}
