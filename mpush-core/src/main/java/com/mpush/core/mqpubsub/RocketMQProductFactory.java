package com.mpush.core.mqpubsub;

import com.mpush.tools.log.Logs;
import org.apache.rocketmq.client.producer.DefaultMQProducer;

/**
 * @Description rockermq product
 * @Date 2022/10/25 19:19
 * @Created by <a href="mailto:nydia_lhq@hotmail.com">lvhuaqiang</a>
 */
public class RocketMQProductFactory {

    private static DefaultMQProducer producer;

    public static void init() {
        String group = "message-send-group";
        int sendMsgTimeout = 3000;
        String nameSvr = "192.168.43.61:9876;192.168.43.62:9876";
        String clientName = "mpush";
        try {

            producer = new DefaultMQProducer(group);
            producer.setSendMsgTimeout(sendMsgTimeout);
            producer.setNamesrvAddr(nameSvr);
            producer.setUnitName(clientName);
            producer.start();
        } catch (Exception e) {
            Logs.Console.error("加载生产者失败:" + e.getMessage(), e);
        }
    }

    public static DefaultMQProducer getProducer() {
        return producer;
    }
}
