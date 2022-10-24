package com.mpush.common.message;

import java.util.List;

/**
 * @Auther: hqlv
 * @Date: 2022/10/24 09:44
 * @Description: 业务消息
 */
public class BizMessage {

    private List<String> userIds;

    public byte[] content;

    private String messageId;

    public List<String> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<String> userIds) {
        this.userIds = userIds;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }
}
