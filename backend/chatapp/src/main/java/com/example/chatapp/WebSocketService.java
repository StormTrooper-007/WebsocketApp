package com.example.chatapp;

import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;
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
       //System.out.println(UsernameHandshakeInterceptor.class);

       String userName = (String)session.getAttributes().get("username");
       String chatRoomName = (String) session.getAttributes().get("chatRoom");
      

       if (chatRoomName != null) {
        if(sessionManager.checkIfChatRoomNameExist(chatRoomName)==true){
            sessionManager.joinChatRoom(chatRoomName, session);
            session.sendMessage(new TextMessage(userName + " just joined " + chatRoomName));
        }else{
           sessionManager.createChatRoom(chatRoomName);
           session.sendMessage(new TextMessage("chatRoom " + chatRoomName + " created"));
           sessionManager.addChatRoomName(chatRoomName);
           sessionManager.removeFirstCreatedSession(chatRoomName);
        }
      }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {
        
        String receivedMessage = message.getPayload();
        String chatRoomName = (String) session.getAttributes().get("chatRoom");
        if(chatRoomName != null){
            sessionManager.sendMessageToChatroom(chatRoomName, new TextMessage(receivedMessage));
        }else{
        handleMissingChatroomName(session);
        }
    }
     

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        String username = (String)session.getAttributes().get("username");
        List<WebSocketSession> activeSessions = sessionManager.getChatRoomSessions();
        
        handleLeavingChat(activeSessions, username);
    }

    private void handleMissingChatroomName(WebSocketSession session) throws IOException {
        String errorMessage = "Chatroom name is missing. Cannot send the message.";
        session.sendMessage(new TextMessage(errorMessage));
        session.close();
    }

    private void handleLeavingChat(List<WebSocketSession> sessions, String username) throws IOException{
          for(WebSocketSession s : sessions){
          if(s.getAttributes().get(username)==null){
          s.sendMessage(new TextMessage("user " + username + " just left"));
        }
      }
    }

 }
    

   

