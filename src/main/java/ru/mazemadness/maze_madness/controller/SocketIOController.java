package ru.mazemadness.maze_madness.controller;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.OnConnect;
import com.corundumstudio.socketio.annotation.OnDisconnect;
import com.corundumstudio.socketio.annotation.OnEvent;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import ru.mazemadness.maze_madness.service.GameEventService;

@Controller
@Slf4j
@RequiredArgsConstructor
public class SocketIOController {
    private final SocketIOServer server;
    private final GameEventService gameEventService;

    @OnConnect
    public void onConnect(SocketIOClient client){
        gameEventService.onPlayerConnected(client);
    }

    @OnDisconnect
    public void onDisconnect(SocketIOClient client){
        gameEventService.onPlayerDisconnected(client);
    }
    @OnEvent("ping")
    public void onPingReceived(SocketIOClient client, String message){
        gameEventService.onPingReceived(client, message);
    }
    @OnEvent("playerMove")
    public void onMoveReceived(SocketIOClient client, String moveData){
        gameEventService.onMoveReceived(client, moveData);
    }
    @OnEvent("playerCreate")
    public void onPlayerCreate(SocketIOClient client, String playerName) {
        gameEventService.onPlayerCreate(client, playerName);
    }
    @OnEvent("playerDisconnect")
    public void onPlayerDisconnected(SocketIOClient client) {
        gameEventService.onPlayerDisconnected(client);
    }
    @PreDestroy
    public void stop(){
        server.stop();
    }

}
