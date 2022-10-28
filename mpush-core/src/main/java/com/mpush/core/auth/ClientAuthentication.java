package com.mpush.core.auth;

import com.mpush.api.spi.common.CacheManager;
import com.mpush.api.spi.common.CacheManagerFactory;
import com.mpush.common.message.ChatMessage;
import com.mpush.tools.common.JWTUtils;
import com.mpush.tools.log.Logs;
import org.apache.commons.lang3.StringUtils;

/**
 * @Description client authentication
 * @Date 2022/10/28 14:07
 * @Created by <a href="mailto:nydia_lhq@hotmail.com">lvhuaqiang</a>
 */
public class ClientAuthentication {

    private static CacheManager cacheManager = CacheManagerFactory.create();

    //TODO 目前只对 cmd=19的聊天鉴权
    public static boolean authentication(ChatMessage chatMessage) {
        return jwtAuth(chatMessage);
    }

    public static boolean jwtAuth(ChatMessage chatMessage) {
        boolean result = true;
        //校验参数
        if (StringUtils.isBlank(chatMessage.getFromUserType())
                || (!"1".equals(chatMessage.getFromUserType())
                    && !"2".equals(chatMessage.getFromUserType())
                    && !"3".equals(chatMessage.getFromUserType()))
                || StringUtils.isBlank(chatMessage.getToUserType())
                || StringUtils.isBlank(chatMessage.getUserId())
                || StringUtils.isBlank(chatMessage.getFromUserId())
                || StringUtils.isBlank(chatMessage.getContentStr())
                ) {
            return false;
        }

        String payloadUserId = "userId";
        String cacheKey = "";
        if ("1".equals(chatMessage.getFromUserType()) || "2".equals(chatMessage.getFromUserType())) {
            cacheKey = "Token:user:" + chatMessage.getFromUserId();
        } else if ("3".equals(chatMessage.getFromUserType())) {
            cacheKey = "Token:mcht:" + chatMessage.getFromUserId();
        }

        String token = chatMessage.getToken();
        //对token鉴权
        try {
            JWTUtils.verify(token);

            String userId = JWTUtils.getPayload(payloadUserId, token);
            if (userId == null || StringUtils.isBlank(userId))
                return false;
        } catch (Exception e) {
            Logs.PUSH.info("对Client端鉴权失败: " + e.getMessage(), e);
            return false;
        }
        //对redis里面的userId
        String userId = cacheManager.get(cacheKey, String.class);
        if (StringUtils.isBlank(userId)) {
            return false;
        }
        return result;
    }

}
