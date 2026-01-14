package com.team2.storyservice.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * WebSocket ?ㅼ젙
 * ?ㅼ떆媛??낅젰 ?곹깭 ?뚮┝???꾪븳 STOMP ?꾨줈?좎퐳 ?ㅼ젙
 *
 * @author ?뺤쭊??
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // ?대씪?댁뼵?몃줈 硫붿떆吏瑜??꾨떖??prefix: /topic
        config.enableSimpleBroker("/topic");

        // ?대씪?댁뼵?몄뿉???쒕쾭濡?硫붿떆吏瑜?蹂대궪 ???ъ슜??prefix: /app
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // WebSocket ?곌껐 endpoint: /ws
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("*")
                .withSockJS(); // SockJS fallback 吏??
    }
}
