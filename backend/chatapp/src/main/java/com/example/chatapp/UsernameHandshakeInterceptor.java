package com.example.chatapp;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

public class UsernameHandshakeInterceptor implements HandshakeInterceptor{

    
    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        String username = getUserNameFromUri("ws://localhost:8080/api/chat?chatroom=oldboys?username=collins");// Extract username from query parameter
        attributes.put("username", username); // Store username in session attributes
        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception ex) {
        // After handshake, no specific action needed
    }

    public String getUserNameFromUri(String queryUrl){
        String[] stringParts = queryUrl.split("\\?");
        String[] userNameParts = stringParts[2].split("=");
            //System.out.println(userNameParts[1]);
            return userNameParts[1];
    }
    
}
