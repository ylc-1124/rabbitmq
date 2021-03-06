package cn.sust.springbootRabbitmq.controller;

import cn.sust.springbootRabbitmq.config.DelayQueueConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * 发布延迟消息
 */
@Slf4j
@RestController
@RequestMapping("/ttl")
public class SendMsgController {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    //发送消息
    @GetMapping("/sendMsg/{message}")
    public void sendMsg(@PathVariable("message") String message) {
        log.info("当前时间:{},发送了一条消息给两个TTL队列:{}", new Date().toString(), message);

        rabbitTemplate.convertAndSend("X", "XA", "消息来自延迟10s的队列:" + message);
        rabbitTemplate.convertAndSend("X", "XB", "消息来自延迟40s的队列:" + message);
    }

    @GetMapping("/sendTtlMsg/{message}/{ttlTime}")
    public void sendMsg(@PathVariable("message") String message,
                        @PathVariable("ttlTime") String ttlTime) {
        log.info("当前时间:{},发送了一条时长{}ms的TTL消息给队列QC:{}",
                new Date().toString(), ttlTime, message);
        rabbitTemplate.convertAndSend("X", "XC", message, msg -> {
            //发送消息的时候设置时长
            msg.getMessageProperties().setExpiration(ttlTime);
            return msg;
        });

    }

    //发消息 给基于插件的延迟队列
    @GetMapping("/sendDelayMsg/{message}/{delayTime}")
    public void sendMsg(@PathVariable("message") String message,
                             @PathVariable("delayTime") Integer delayTime) {

        log.info("当前时间:{},发送了一条时长{}ms的延迟消息给队列delayed.queue:{}",
                new Date().toString(), delayTime, message);

        //发送消息
        rabbitTemplate.convertAndSend(
                DelayQueueConfig.DELAY_EXCHANGE_NAME,
                DelayQueueConfig.DELAY_ROUTING_KEY,
                message,
                msg->{
                    msg.getMessageProperties().setDelay(delayTime); //设置延迟时间
                    return msg;
                } );
    }
}
