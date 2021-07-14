package cn.sust.springbootRabbitmq.consumer;

import cn.sust.springbootRabbitmq.config.DelayQueueConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 消费者 基于延迟插件
 */
@Component
@Slf4j
public class DelayQueueConsumer {

    //监听消息
    @RabbitListener(queues = DelayQueueConfig.DELAY_QUEUE_NAME)
    public void receiveDelayQueue(Message message) {
        String msg = new String(message.getBody());
        log.info("当前时间:{},收到来自延迟队列的消息:{}",new Date().toString(),msg);
    }
}
