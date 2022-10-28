package com.mpush.core.mqpubsub;

import com.alibaba.fastjson.JSON;
import com.mpush.common.message.ChatMessage;
import com.mpush.tools.config.CC;
import com.mpush.tools.log.Logs;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description rockermq utils
 * @Date 2022/10/25 19:19
 * @Created by <a href="mailto:nydia_lhq@hotmail.com">lvhuaqiang</a>
 */
public class RocketMQUtils {

    public static void sendRecvMessage(DefaultMQProducer producer, ChatMessage chatMessage) {
        String topic = CC.mp.rocketmq.recvMsgTopic;
        String tag = CC.mp.rocketmq.recvMsgTag;
        String msg = JSON.toJSONString(chatMessage);
        sendMessage(producer, msg, topic, tag);
    }

    public static void sendRecvMessage(DefaultMQProducer producer, String msg) {
        String topic = CC.mp.rocketmq.recvMsgTopic;
        String tag = CC.mp.rocketmq.recvMsgTag;
        sendMessage(producer, msg, topic, tag);
    }

    public static void sendMessage(DefaultMQProducer producer, String msg, String topic, String tags) {
        try {
            Logs.PUSH.info("MQ发送的消息内容为: {},topic: {},tags: {}", msg, topic, tags);
            Message message = new Message();
            message.setTopic(topic);
            message.setTags(tags);
            message.setBody(msg.getBytes(Charset.forName("UTF-8")));
            SendResult sendResult = producer.send(message);
            Logs.PUSH.info("MQ发送的消结果为: " + sendResult.getSendStatus());
        } catch (Exception e) {
            Logs.PUSH.error("MQ发送消息失败: " + e.getMessage(), e);
        }
    }

    public static void main(String[] args) {
        Map<String, Object> map = new HashMap<>();
        map.put("3", "在你好ss");
        String str = JSON.toJSONString(map);
        System.out.println(str);
    }

}
