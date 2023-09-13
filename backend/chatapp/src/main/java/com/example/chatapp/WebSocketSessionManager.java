package com.example.chatapp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import lombok.Data;

@Component
@Data
public class WebSocketSessionManager {

    private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();
    private final Map<String, List<WebSocketSession>> chatRooms = new ConcurrentHashMap<>();
    private final List<String> chatRoomList = new ArrayList<>();
    private final List <WebSocketSession> chatRoomSessions = new ArrayList<>();
    
    public void addSession(String sessionId, WebSocketSession session) {
        sessions.put(sessionId, session);
    
    }

    public WebSocketSession getSession(String sessionId) {
        return sessions.get(sessionId);
    }


    public void removeSession(String sessionId) {
        sessions.remove(sessionId);
    }

    public void removeFirstCreatedSession(String chatRoomName){
        chatRooms.get(chatRoomName).remove(0);
    }

    public void createChatRoom(String chatRoomName){
        chatRooms.put(chatRoomName, new ArrayList<>());
    }

    public void joinChatRoom(String chatRoomName, WebSocketSession session){
       chatRoomSessions.addAll(chatRooms.get(chatRoomName));
        if(chatRoomSessions != null){
            chatRoomSessions.add(session);
        }
    }

    public List<WebSocketSession> getChatRoomMemebers(String chatRoomName){
        return chatRooms.get(chatRoomName);
    }

    public void sendMessageToChatroom(String chatRoomName, TextMessage message) throws IOException {
         chatRoomSessions.addAll(chatRooms.get(chatRoomName));
        if (chatRoomSessions != null) {
            for (WebSocketSession chatSession : chatRoomSessions) {
                chatSession.sendMessage(message);
            }
        }
    }

    public boolean checkIfChatRoomNameExist(String chatRoomName){
        if(chatRoomList.contains(chatRoomName)){
            return true;
        }else{
            return false;
        }
    }

    public void addChatRoomName(String chatRoomName){
        chatRoomList.add(chatRoomName);
    }
    
}