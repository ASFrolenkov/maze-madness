package ru.mazemadness.maze_madness.controller;

import com.corundumstudio.socketio.SocketIOServer;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.mazemadness.maze_madness.jsonTypes.PlayerData;

import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
public class SocketIOController {
    @Autowired
    private SocketIOServer socketIOServer;
    private Boolean isConnected = false;
    @Autowired
    private ConcurrentHashMap<String, PlayerData> connectedPlayers;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @PreDestroy
    public void stop(){
        socketIOServer.stop();
    }

}
