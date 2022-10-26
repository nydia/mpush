package com.mpush.common.message;

import com.mpush.api.common.Condition;
import com.mpush.api.connection.Connection;
import com.mpush.api.protocol.Command;
import com.mpush.api.protocol.Packet;
import com.mpush.api.spi.push.IPushMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFutureListener;

import java.util.List;

/**
 * @Auther: hqlv
 * @Date: 2022/10/24 09:44
 * @Description: 业务消息
 */
public class ChatMessage extends ByteBufMessage implements IPushMessage {

    /**
     * token
     */
    private String token;

    /**
     * 消息内容
     */
    private String content;

    /**
     * 发送-用户id
     */
    private String fromUserId;

    /**
     * 发送：  1-用户/2-拓展员/3-商户/98-客服消息/99-系统(系统消息,用户id为sys)
     */
    private String fromUserType;

    /**
     * 接受-用户id
     */
    private String userId;

    /**
     * 接受：  1-用户/2-拓展员/3-商户/99-系统(系统消息,用户id为sys)
     */
    private String toUserType;

    /**
     * 发送-用户ids
     */
    private List<String> toUserIds;

    /**
     * 消息内容，字符串形式
     */
    public String contentStr;

    /**
     * 发送-用户ids
     */
    private List<String> fromUserIds;

    /**
     * 多媒体消息连接
     */
    private List<String> mediaUrls;

    /**
     * 消息发送时间
     */
    private String sendTime;

    /**
     * 消息id
     */
    private String messageId;

    /**
     * 消息类别 1-普通消息 2-订单消息 2-实名认证 99-系统消息
     */
    private String messageCategory;

    public static ChatMessage from(ChatMessage src) {
        return new ChatMessage(new Packet(Command.CHAT, src.getSessionId()), src.connection);
    }

    public ChatMessage(Packet message, Connection connection) {
        super(message, connection);
    }

    @Override
    public void decode(ByteBuf body) {
        //token = decodeString(body);
        //content = decodeString(body);
        //fromUserId = decodeString(body);
        //fromUserType = decodeString(body);
        //userId = decodeString(body);
        //toUserType = decodeString(body);
    }

    @Override
    public void encode(ByteBuf body) {
        //encodeString(body, token);
        //encodeString(body, content);
        //encodeString(body, fromUserId);
        //encodeString(body, fromUserType);
        //encodeString(body, userId);
        //encodeString(body, toUserType);
    }

    @Override
    public void send() {
        super.sendRaw();
    }

    @Override
    public void send(ChannelFutureListener listener) {
        super.sendRaw(listener);
    }

    @Override
    public String toString() {
        return "ChatMessage{" +
                "token='" + token + '\'' +
                ", toUserId=" + userId +
                ", content=" + content +
                ", packet=" + packet +
                '}';
    }

    @Override
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public byte[] getContent() {
        return content.getBytes();
    }

    @Override
    public int getClientType() {
        return 0;
    }

    @Override
    public boolean isBroadcast() {
        return false;
    }

    @Override
    public boolean isNeedAck() {
        return false;
    }

    @Override
    public byte getFlags() {
        return 0;
    }

    @Override
    public int getTimeoutMills() {
        return 0;
    }

    @Override
    public String getTaskId() {
        return null;
    }

    @Override
    public Condition getCondition() {
        return null;
    }

    @Override
    public void finalized() {

    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(String fromUserId) {
        this.fromUserId = fromUserId;
    }

    public List<String> getFromUserIds() {
        return fromUserIds;
    }

    public void setFromUserIds(List<String> fromUserIds) {
        this.fromUserIds = fromUserIds;
    }

    public String getFromUserType() {
        return fromUserType;
    }

    public void setFromUserType(String fromUserType) {
        this.fromUserType = fromUserType;
    }

    public List<String> getToUserIds() {
        return toUserIds;
    }

    public void setToUserIds(List<String> toUserIds) {
        this.toUserIds = toUserIds;
    }

    public String getToUserType() {
        return toUserType;
    }

    public void setToUserType(String toUserType) {
        this.toUserType = toUserType;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContentStr() {
        return contentStr;
    }

    public void setContentStr(String contentStr) {
        this.contentStr = contentStr;
    }

    public List<String> getMediaUrls() {
        return mediaUrls;
    }

    public void setMediaUrls(List<String> mediaUrls) {
        this.mediaUrls = mediaUrls;
    }

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getMessageCategory() {
        return messageCategory;
    }

    public void setMessageCategory(String messageCategory) {
        this.messageCategory = messageCategory;
    }
}
