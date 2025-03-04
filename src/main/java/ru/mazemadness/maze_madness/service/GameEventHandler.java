package ru.mazemadness.maze_madness.service;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.OnEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class GameEventHandler implements GameEvents {
    @Autowired
    private SocketIOServer socketIOServer;

    @Override
    @OnEvent("ping")
    public void onPingReceived(SocketIOClient client, String message){
        log.info("Ping received: {}", message);
        socketIOServer.getBroadcastOperations().sendEvent("pong", message);
        log.info("Pong sent: {}", message);
    }

    @Override
    @OnEvent("playerMove")
    public void onMoveReceived(SocketIOClient client, String moveData) {
        log.info("Client {} sent moveData: {}", client.getSessionId(), moveData);
        socketIOServer.getBroadcastOperations().sendEvent("playerMoved", moveData);
    }

    @Override
    @OnEvent("playerCreate")
    public void onPlayerCreate(SocketIOClient client, String playerData) {
        log.info("Client {} sent playerCreate: {}", client.getSessionId(), playerData);
        socketIOServer.getBroadcastOperations().sendEvent("playerCreated", playerData);
    }
}
