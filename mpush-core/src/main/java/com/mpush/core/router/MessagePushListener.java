/*
 * (C) Copyright 2015-2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Contributors:
 *   ohun@live.cn (夜色)
 */

package com.mpush.core.router;

import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.Subscribe;
import com.mpush.api.Constants;
import com.mpush.api.connection.Connection;
import com.mpush.api.connection.SessionContext;
import com.mpush.api.event.RouterChangeEvent;
import com.mpush.api.router.ClientLocation;
import com.mpush.api.router.Router;
import com.mpush.api.spi.common.MQClient;
import com.mpush.api.spi.common.MQClientFactory;
import com.mpush.api.spi.common.MQMessageReceiver;
import com.mpush.common.message.KickUserMessage;
import com.mpush.common.message.gateway.GatewayKickUserMessage;
import com.mpush.common.router.KickRemoteMsg;
import com.mpush.common.router.MQKickRemoteMsg;
import com.mpush.common.router.RemoteRouter;
import com.mpush.core.MPushServer;
import com.mpush.tools.Jsons;
import com.mpush.tools.config.CC;
import com.mpush.tools.config.ConfigTools;
import com.mpush.tools.event.EventConsumer;
import com.mpush.tools.log.Logs;

import java.net.InetSocketAddress;

import static com.mpush.api.Constants.KICK_CHANNEL_PREFIX;


/**
 * Created by nydia 2022/10/21
 *
 * @author nydia_lhq@hotmail.com
 */
public final class MessagePushListener extends EventConsumer implements MQMessageReceiver {
    private final boolean udpGateway = CC.mp.net.udpGateway();
    private String kick_channel;
    private MQClient mqClient;
    private MPushServer mPushServer;

    public MessagePushListener(MPushServer mPushServer) {
        this.mPushServer = mPushServer;
        if (!udpGateway) {
            mqClient = MQClientFactory.create();
            mqClient.init(mPushServer);
            //mqClient.subscribe(getKickChannel(), this);
        }
    }

    public String getKickChannel() {
        return kick_channel;
    }

    @Subscribe
    @AllowConcurrentEvents
    void on(RouterChangeEvent event) {
        String userId = event.userId;
        Router<?> r = event.router;
        if (r.getRouteType().equals(Router.RouterType.LOCAL)) {
            //sendKickUserMessage2Client(userId, (LocalRouter) r);
        } else {
            //sendKickUserMessage2MQ(userId, (RemoteRouter) r);
        }
    }

    @Override
    public void receive(String topic, Object message) {
        KickRemoteMsg msg = Jsons.fromJson(message.toString(), MQKickRemoteMsg.class);
        if (msg != null) {
            //onReceiveKickRemoteMsg(msg);
        } else {
            Logs.CONN.warn("receive an error kick message={}", message);
        }
    }
}
