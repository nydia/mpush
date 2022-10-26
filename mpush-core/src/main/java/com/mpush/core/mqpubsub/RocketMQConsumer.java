package com.mpush.core.mqpubsub;

import com.mpush.api.push.*;
import com.mpush.api.spi.common.Json;
import com.mpush.common.message.ChatMessage;
import com.mpush.tools.config.CC;
import com.mpush.tools.log.Logs;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.List;
import java.util.concurrent.FutureTask;

/**
 * @Description rockermq consumer
 * @Date 2022/10/25 19:19
 * @Created by <a href="mailto:nydia_lhq@hotmail.com">lvhuaqiang</a>
 */
public class RocketMQConsumer {

    public void start() {
        String mqGroup = CC.mp.rocketmq.sendMsgGroup;
        String mqTopic = CC.mp.rocketmq.sendMsgTopic;
        String nameSvr = CC.mp.rocketmq.namesrv;
        try {
            //1.创建消费者Consumer，制定消费者组名
            DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(mqGroup);
            //2.指定Nameserver地址
            consumer.setNamesrvAddr(nameSvr);
            //3.订阅主题Topic和Tag
            consumer.subscribe(mqTopic, "*");
            //consumer.subscribe("base", "Tag1");

            //消费所有"*",消费Tag1和Tag2  Tag1 || Tag2
            //consumer.subscribe("base", "*");

            //设定消费模式：负载均衡|广播模式  默认为负载均衡
            //负载均衡10条消息，每个消费者共计消费10条
            //广播模式10条消息，每个消费者都消费10条
            //consumer.setMessageModel(MessageModel.BROADCASTING);

            //4.设置回调函数，处理消息
            consumer.registerMessageListener(new MessageListenerConcurrently() {

                //接受消息内容
                @Override
                public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
                    for (MessageExt msg : msgs) {
                        Logs.Console.info("consumeThread=" + Thread.currentThread().getName() + "," + new String(msg.getBody()));
                        sendMsg(new String(msg.getBody()));
                    }
                    return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                }
            });
            //5.启动消费者consumer
            consumer.start();
        } catch (Exception e) {
            Logs.Console.error("消费消息失败:" + e.getMessage(), e);

        }

    }

    public void sendMsg(String content) {
        try {
            PushSender sender = PushSender.create();
            sender.start().join();

            ChatMessage chatMessage = Json.JSON.fromJson(content, ChatMessage.class);
            PushMsg msg = PushMsg.build(MsgType.MESSAGE, new String(chatMessage.getContent()));
            msg.setMsgId("msgId_" + chatMessage.getMessageId());

            PushContext context = PushContext.build(msg)
                    .setAckModel(AckModel.AUTO_ACK)
                    //.setUserId("user-" + i)
                    .setBroadcast(false)
                    //.setCondition("tags&&tags.indexOf('test')!=-1")
                    .setUserIds(chatMessage.getToUserIds())
                    .setTimeout(2000)
                    .setCallback(new PushCallback() {
                        @Override
                        public void onResult(PushResult result) {
                            Logs.Console.info("推送结果为: " + result);
                        }
                    });
            FutureTask<PushResult> future = sender.send(context);
        } catch (Exception e) {
            Logs.Console.error("推送消息失败:" + e.getMessage(), e);
        }
    }

}
