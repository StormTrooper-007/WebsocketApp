package com.example.chatapp;

import java.util.Map;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UsernameHandshakeInterceptor implements HandshakeInterceptor{



    
    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        String username = getUserNameFromUri(request.getURI().getQuery());
        String chatRoomName = getChatroomNameFromUri(request.getURI().getQuery());
        attributes.put("chatRoom", chatRoomName);
        attributes.put("username", username);
        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception ex) {
        // After handshake, no specific action needed
    
    }

    public String getUserNameFromUri(String queryUrl){
        String[] stringParts = queryUrl.split("\\?");
        String[] userNameParts = stringParts[1].split("=");
            //System.out.println(userNameParts[1]);
            return userNameParts[1];
    }

    private String getChatroomNameFromUri(String queryUrl) {
                String[] stringParts = queryUrl.split("//?");
                String[] roomNameParts = stringParts[0].split("=");
                String[] secondSplit = roomNameParts[1].split("u");
                return secondSplit[0];
    }
    
}

