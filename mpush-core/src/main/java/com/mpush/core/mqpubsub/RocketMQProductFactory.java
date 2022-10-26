package com.mpush.core.mqpubsub;

import com.mpush.tools.config.CC;
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
        String group = CC.mp.rocketmq.recvMsgGroup;
        int sendMsgTimeout = 3000;
        String nameSvr = CC.mp.rocketmq.namesrv;
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
