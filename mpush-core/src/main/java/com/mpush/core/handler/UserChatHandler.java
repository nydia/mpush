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

package com.mpush.core.handler;

import com.mpush.api.connection.Connection;
import com.mpush.api.protocol.Command;
import com.mpush.api.protocol.JsonPacket;
import com.mpush.api.protocol.Packet;
import com.mpush.api.spi.Spi;
import com.mpush.api.spi.common.Json;
import com.mpush.api.spi.handler.BindValidator;
import com.mpush.api.spi.handler.BindValidatorFactory;
import com.mpush.common.handler.BaseMessageHandler;
import com.mpush.common.message.ChatMessage;
import com.mpush.core.MPushServer;
import com.mpush.core.mqpubsub.RocketMQProductFactory;
import com.mpush.core.mqpubsub.RocketMQUtils;
import com.mpush.core.push.PushCenter;
import com.mpush.tools.log.Logs;

/**
 * Created by ohun on 2015/12/23.
 *
 * @author ohun@live.cn
 */
public final class UserChatHandler extends BaseMessageHandler<ChatMessage> {
    private final BindValidator bindValidator = BindValidatorFactory.create();

    private PushCenter pushCenter;

    public UserChatHandler(MPushServer mPushServer) {
        this.pushCenter = mPushServer.getPushCenter();
        this.bindValidator.init(mPushServer);
    }

    @Override
    public ChatMessage decode(Packet packet, Connection connection) {
        ChatMessage chatMessage = new ChatMessage(packet, connection);
        if (chatMessage.getPacket() != null) {
            JsonPacket jsonPacket = ((JsonPacket) chatMessage.getPacket());
            String content = (String) jsonPacket.getBody().getOrDefault("content", "");
            chatMessage.setContent(content);
            chatMessage.setUserId((String) jsonPacket.getBody().getOrDefault("userId", ""));
            chatMessage.setToUserType((String) jsonPacket.getBody().getOrDefault("toUserType", ""));
            chatMessage.setToken((String) jsonPacket.getBody().getOrDefault("token", ""));
            chatMessage.setFromUserId((String) jsonPacket.getBody().getOrDefault("fromUserId", ""));
            chatMessage.setFromUserType((String) jsonPacket.getBody().getOrDefault("fromUserType", ""));

            chatMessage.setMessageCategory("1");
            chatMessage.setContentStr(content);
        } else {
            //TODO 返回失败信息
        }
        return chatMessage;
    }

    @Override
    public void handle(ChatMessage message) {
        if (message.getPacket().cmd == Command.CHAT.cmd) {
            ChatMessage chatMessage = decode(message.getPacket(), message.getConnection());
            receive(chatMessage);
        } else {
            // TODO
        }
    }

    private void receive(ChatMessage message) {
        try {
            //1-用户/2-拓展员/3-商户/98-客服消息/99-系统(系统消息,用户id为sys)
            if (message.getToUserType() != null
                    && ("1".equals(message.getToUserType())
                    || "2".equals(message.getToUserType())
                    || "3".equals(message.getToUserType()))
                    ) {
                pushCenter.push(message);
            }

            message.sendRaw();
        } catch (Exception e) {
            Logs.Console.error("接受消息后推送到客户端处理失败:" + e.getMessage(), e);
        }

        try {
            RocketMQUtils.sendRecvMessage(RocketMQProductFactory.getProducer(), message);
        } catch (Exception e) {
            Logs.Console.error("接受消息后发送MQ处理失败:" + e.getMessage(), e);
        }
    }

    @Spi(order = 1)
    public static class DefaultBindValidatorFactory implements BindValidatorFactory {
        private final BindValidator validator = (userId, data) -> true;

        @Override
        public BindValidator get() {
            return validator;
        }
    }
}
