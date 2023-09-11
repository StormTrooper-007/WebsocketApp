package com.example.chatapp;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.PongMessage;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import lombok.RequiredArgsConstructor;




@Service
@RequiredArgsConstructor
public class WebSocketService extends TextWebSocketHandler{

    private final WebSocketSessionManager sessionManager;

    //store chatRoomName

    //function to check if a chat room exists

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception{

       sessionManager.addSession(session.getId(), session);

       String chatRoomName = getChatroomNameFromURI(session.getUri().getQuery());

       if (chatRoomName != null) {
        //Create or join the specified chatroom
       //sessionManager.createChatRoom(chatRoomName);
        //sessionManager.joinChatRoom(chatRoomName, session);
        if(sessionManager.checkIfChatRoomNameExist(chatRoomName)==true){
            sessionManager.joinChatRoom(chatRoomName, session);
            System.out.println(sessionManager.getChatRoomList());
        }else{
           sessionManager.createChatRoom(chatRoomName);
           sessionManager.addChatRoomName(chatRoomName);
           sessionManager.removeFirstCreatedSession(chatRoomName);
        }
      }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {
        
        String receivedMessage = message.getPayload();
        // Broadcast the message to all connected sessions
        String chatRoomName = getChatroomNameFromURI(session.getUri().getQuery());
        if(chatRoomName != null){
            sessionManager.sendMessageToChatroom(chatRoomName, new TextMessage(receivedMessage));
        }else{
        handleMissingChatroomName(session);
        }
    }
     

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        // Remove the session from the session manager
        sessionManager.removeSession(session.getId());
    }

    private String getChatroomNameFromURI(String uriQuery) {
        if (uriQuery != null && !uriQuery.isEmpty()) {
            // Split the query string into key-value pairs
            String[] queryParams = uriQuery.split("&");
            for (String param : queryParams) {
                String[] keyValue = param.split("=");
                if (keyValue.length == 2 && "chatroom".equals(keyValue[0])) {
                    // Return the chatroom name if found
                    System.out.println(keyValue[1]);
                    return keyValue[1];
                }
            }
        }
        return null; 
    }

    private void handleMissingChatroomName(WebSocketSession session) throws IOException {
        String errorMessage = "Chatroom name is missing. Cannot send the message.";
        session.sendMessage(new TextMessage(errorMessage));
        session.close();
    }

 }
    

   

