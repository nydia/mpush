package com.mpush.common.message;

import java.util.List;

/**
 * @Auther: hqlv
 * @Date: 2022/10/24 09:44
 * @Description: 业务消息
 */
public class BizMessage {

    private List<String> toUserIds;

    public byte[] contentBytes;

    public String content;

    private String messageId;

    public List<String> getToUserIds() {
        return toUserIds;
    }

    public void setToUserIds(List<String> toUserIds) {
        this.toUserIds = toUserIds;
    }

    public byte[] getContentBytes() {
        return contentBytes;
    }

    public void setContentBytes(byte[] contentBytes) {
        this.contentBytes = contentBytes;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
