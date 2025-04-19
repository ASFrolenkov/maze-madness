package ru.mazemadness.maze_madness.controller;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.OnConnect;
import com.corundumstudio.socketio.annotation.OnDisconnect;
import com.corundumstudio.socketio.annotation.OnEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import ru.mazemadness.maze_madness.service.socket.SocketEventHandler;

@Controller
@Slf4j
@RequiredArgsConstructor
public class SocketIOController {
    private final SocketIOServer server;
    private final SocketEventHandler socketEventHandler;

    @OnConnect
    public void onConnect(SocketIOClient client){
        socketEventHandler.onClientConnected(client);
    }
    @OnDisconnect
    public void onDisconnect(SocketIOClient client){
        socketEventHandler.onClientDisconnected(client);
    }

    @OnEvent("roomMessageSendClient")
    public void onUserMessageSent(SocketIOClient client, String chatMessageRequest) throws JsonProcessingException {
        socketEventHandler.onUserMessageSend(client, chatMessageRequest);
    }

    @OnEvent("roomGameStartClient")
    public void onRoomGameStart(SocketIOClient client){
        socketEventHandler.onRoomGameStart(client);
    }

//    @OnEvent("playerMove")
//    public void onMoveReceived(SocketIOClient client, String roomId, String moveData){
//        gameEventService.onMoveReceived(client, roomId, moveData);
//    }
//    @OnEvent("playerCreate")
//    public void onPlayerCreate(SocketIOClient client, String roomId, String playerName) {
//        gameEventService.onPlayerCreate(client, roomId, playerName);
//    }

    @OnEvent("roomUserKickClient")
    public void onRoomUserKick(SocketIOClient client, String username){
        socketEventHandler.onRoomUserKick(client, username);
    }

    @OnEvent("roomGetUsersClient")
    public void onRoomGetAllUsers(SocketIOClient client){
        socketEventHandler.onRoomGetPlayers(client);
    }

    @PreDestroy
    public void stop(){
        server.stop();
    }

}
