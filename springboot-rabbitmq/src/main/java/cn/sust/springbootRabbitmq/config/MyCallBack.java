package cn.sust.springbootRabbitmq.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@Slf4j
public class MyCallBack implements RabbitTemplate.ConfirmCallback,RabbitTemplate.ReturnsCallback {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    //将此实现类注入到 rabbitTemplate的 ConfirmCallback接口中
    @PostConstruct
    public void init() {
        rabbitTemplate.setConfirmCallback(this);
        rabbitTemplate.setReturnsCallback(this);
    }
    /**
     * 交换机确认回调方法
     * 1.发消息 交换机收到消息 回调
     * 1.1  correlationData保存回调消息的ID和相关信息
     * 1.2  交换机收到消息 ack=true
     * 1.3  cause null
     * 2.发消息 交换机接受失败 回调
     * 2.1  correlationData保存回调消息的ID和相关信息
     * 2.2  交换机没有收到消息 ack=false
     * 2.3  cause  失败的原因
     */
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        String id = correlationData != null ? correlationData.getId() : "";
        if (ack) {
            log.info("交换机接收到ID:{}的消息", id);
        } else {
            log.info("交换机未收到ID:{}的消息,由于:{}",id,cause);
        }
    }

    /**
     * 交换机退回消息的回调
     * @param returned
     */
    @Override
    public void returnedMessage(ReturnedMessage returned) {
        log.error("消息:{}被交换机:{}退回,退回原因:{},路由Key:{}",
                new String(returned.getMessage().getBody()),
                returned.getExchange(),
                returned.getReplyText(),
                returned.getRoutingKey());
    }
}
