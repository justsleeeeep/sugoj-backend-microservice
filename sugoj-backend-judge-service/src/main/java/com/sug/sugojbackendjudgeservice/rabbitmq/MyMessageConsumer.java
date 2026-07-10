package com.sug.sugojbackendjudgeservice.rabbitmq;

import com.rabbitmq.client.Channel;
import com.sug.sugbackendmodel.model.entity.QuestionSubmit;
import com.sug.sugbackendmodel.model.enums.QuestionSubmitStatusEnum;
import com.sug.sugojbackendjudgeservice.judge.JudgeService;
import com.sug.sugojbackendserviceclient.service.QuestionFeignClient;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
@Slf4j
public class MyMessageConsumer {
    @Resource
    JudgeService judgeService;

    @Resource
    QuestionFeignClient questionFeignClient;

    // 指定程序监听的消息队列和确认机制
    @SneakyThrows
    @RabbitListener(queues = {"code_queue"}, ackMode = "MANUAL")
    public void receiveMessage(String message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) {
        long questionSubmitId=Long.parseLong(message);
        log.info("receiveMessage message = {}", message);
        try {
            judgeService.doJudge(questionSubmitId);
            channel.basicAck(deliveryTag, false);
        }
        catch (Exception e)
        {
            log.error("判题消费异常, questionSubmitId={}", questionSubmitId, e);
            // 标记为 FAILED，防止 status 卡在 RUNNING
            QuestionSubmit failUpdate = new QuestionSubmit();
            failUpdate.setId(questionSubmitId);
            failUpdate.setStatus(QuestionSubmitStatusEnum.FAILED.getValue());
            failUpdate.setJudgeInfo("{\"message\": \"" + e.getMessage() + "\"}");
            questionFeignClient.updateQuestionSubmitById(failUpdate);
            channel.basicNack(deliveryTag, false, false);
        }

    }

}
