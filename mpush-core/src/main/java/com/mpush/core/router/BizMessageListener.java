package com.mpush.core.router;

import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.Subscribe;
import com.mpush.api.event.RouterChangeEvent;
import com.mpush.api.push.*;
import com.mpush.api.spi.common.Json;
import com.mpush.api.spi.common.MQClient;
import com.mpush.api.spi.common.MQMessageReceiver;
import com.mpush.common.message.BizMessage;
import com.mpush.tools.event.EventConsumer;
import com.mpush.tools.log.Logs;

import java.util.concurrent.FutureTask;

/**
 * Created by nydia 2022/10/21
 *
 * @author nydia_lhq@hotmail.com
 */
public final class BizMessageListener implements MQMessageReceiver {

    private MQClient mqClient;

    private String topic;

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public BizMessageListener() {

    }

    @Subscribe
    @AllowConcurrentEvents
    void on(RouterChangeEvent event) {
        //send
    }

    @Override
    public void receive(String topic, Object message) {
        //receive
        PushSender sender = PushSender.create();
        sender.start().join();

        BizMessage bizMessage = Json.JSON.fromJson(message.toString(), BizMessage.class);
        PushMsg msg = PushMsg.build(MsgType.MESSAGE, new String(bizMessage.getContentBytes()));
        msg.setMsgId("msgId_" + bizMessage.getMessageId());

        PushContext context = PushContext.build(msg)
                .setAckModel(AckModel.AUTO_ACK)
                .setBroadcast(false)
                .setUserIds(bizMessage.getToUserIds())
                .setTimeout(2000)
                .setCallback(new PushCallback() {
                    @Override
                    public void onResult(PushResult result) {
                        Logs.Console.info("\n\n" + result.toString());
                    }
                });
        FutureTask<PushResult> future = sender.send(context);
    }

}
