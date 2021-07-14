package cn.sust.springbootRabbitmq.consumer;

import cn.sust.springbootRabbitmq.config.ConfirmConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Slf4j
public class Consumer {

    @RabbitListener(queues = ConfirmConfig.CONFIRM_QUEUE_NAME)
    public void receiveQueue(Message message) {
        String msg = new String(message.getBody());
        //打印日志
        log.info("当前时间:{},收到一条消息:{}",new Date().toString(),msg);
    }
}
