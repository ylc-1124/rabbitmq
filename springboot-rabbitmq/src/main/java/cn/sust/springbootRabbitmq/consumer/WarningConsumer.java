package cn.sust.springbootRabbitmq.consumer;

import cn.sust.springbootRabbitmq.config.ConfirmConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * 报警消费者
 */
@Slf4j
@Component
public class WarningConsumer {

    //接收报警消息
    @RabbitListener(queues = ConfirmConfig.WARNING_QUEUE_NAME)
    public void receiveWarningQueue(Message message) {
        String msg = new String(message.getBody());
        //打印错误信息
        log.error("报警发现不可路由消息:{}",msg);
    }
}
