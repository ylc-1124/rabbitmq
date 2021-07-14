package cn.sust.springbootRabbitmq.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 发布确认高级篇 配置类
 */
@Configuration
public class ConfirmConfig {
    //定义名称
    public static final String CONFIRM_EXCHANGE_NAME = "confirm.exchange";
    public static final String CONFIRM_QUEUE_NAME = "confirm.queue";
    public static final String CONFIRM_ROUTING_KEY = "key1";
    //备份交换机
    public static final String BACKUP_EXCHANGE_NAME = "backup.exchange";
    //备份队列
    public static final String BACKUP_QUEUE_NAME = "backup.queue";
    //报警队列
    public static final String WARNING_QUEUE_NAME = "warning.queue";

    //声明交换机
    @Bean
    public DirectExchange confirmExchange() {
        return ExchangeBuilder.directExchange(CONFIRM_EXCHANGE_NAME)
                .durable(true)     //持久化
                .withArgument("alternate-exchange",BACKUP_EXCHANGE_NAME) //可选择的交换机设置上备份交换机
                .build(); //构造
    }

    //声明备份交换机
    @Bean
    public FanoutExchange backupExchange() {
        return new FanoutExchange(BACKUP_EXCHANGE_NAME);
    }

    //声明队列
    @Bean
    public Queue confirmQueue() {
        return QueueBuilder.durable(CONFIRM_QUEUE_NAME).build();
    }

    //声明备份队列
    @Bean
    public Queue backupQueue() {
        return QueueBuilder.durable(BACKUP_QUEUE_NAME).build();
    }
    //声明报警队列
    @Bean
    public Queue warningQueue() {
        return QueueBuilder.durable(WARNING_QUEUE_NAME).build();
    }

    //绑定
    @Bean
    public Binding confirmQueueBindingConfirmExchange(
            @Qualifier("confirmExchange") DirectExchange confirmExchange,
            @Qualifier("confirmQueue") Queue confirmQueue) {

        return BindingBuilder.bind(confirmQueue).to(confirmExchange).with(CONFIRM_ROUTING_KEY);
    }
    //绑定备份交换机和备份队列
    @Bean
    public Binding backupQueueBindingBackupExchange(
            @Qualifier("backupExchange") FanoutExchange backupExchange,
            @Qualifier("backupQueue") Queue backupQueue) {

        return BindingBuilder.bind(backupQueue).to(backupExchange);
    }
    //绑定备份交换机和报警队列
    @Bean
    public Binding warningQueueBindingBackupExchange(
            @Qualifier("backupExchange") FanoutExchange backupExchange,
            @Qualifier("warningQueue") Queue warningQueue) {

        return BindingBuilder.bind(warningQueue).to(backupExchange);
    }
}
