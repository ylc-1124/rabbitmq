package cn.sust.springbootRabbitmq.controller;

import cn.sust.springbootRabbitmq.config.ConfirmConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping("/confirm")
@Slf4j
public class ProducerController {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @GetMapping("/sendMessage/{message}")
    public void sendMessage(@PathVariable("message") String message) {
        CorrelationData correlationData = new CorrelationData("1");
        //一条正确发送的消息
        rabbitTemplate.convertAndSend(
                ConfirmConfig.CONFIRM_EXCHANGE_NAME, //交换机
                ConfirmConfig.CONFIRM_ROUTING_KEY,   //routingKey
                message+ConfirmConfig.CONFIRM_ROUTING_KEY,  //发送的消息内容
                correlationData);
        //打印日志
        log.info("当前时间:{},发送了一条消息:{}", new Date().toString(),message);

        //故意写错 routingKey，测试交换机回退消息
        CorrelationData correlationData2 = new CorrelationData("2");
        //一条正确发送的消息
        rabbitTemplate.convertAndSend(
                ConfirmConfig.CONFIRM_EXCHANGE_NAME,
                "key2",  //不存在的一个路由
                message+"key2",
                correlationData2);
        //打印日志
        log.info("当前时间:{},发送了一条消息:{}", new Date().toString(),message);
    }
}
