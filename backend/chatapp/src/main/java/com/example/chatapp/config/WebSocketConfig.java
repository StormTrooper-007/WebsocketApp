package com.example.chatapp.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import com.example.chatapp.UsernameHandshakeInterceptor;
import com.example.chatapp.WebSocketService;

import lombok.RequiredArgsConstructor;




@Configuration
@EnableWebSocket
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketConfigurer{

private final WebSocketService webSocketService;
@Override
public void registerWebSocketHandlers(WebSocketHandlerRegistry registry){
    registry.addHandler(webSocketService, "/api/chat")
    .addInterceptors(new UsernameHandshakeInterceptor())
    .setAllowedOrigins("*");
}

}
