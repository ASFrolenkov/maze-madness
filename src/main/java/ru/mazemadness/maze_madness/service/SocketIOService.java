package ru.mazemadness.maze_madness.service;

import com.corundumstudio.socketio.SocketIOServer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.mazemadness.maze_madness.jsonTypes.PlayerData;

import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
public class SocketIOService {

    @Autowired
    private SocketIOServer socketIOServer;
    private Boolean isConnected = false;
    @Autowired
    private ConcurrentHashMap<String, PlayerData> connectedPlayers;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @PostConstruct
    public void start(){
        socketIOServer.addConnectListener(client -> {
            if(!isConnected){
                isConnected = true;
                connectedPlayers.put(client.getSessionId().toString(), new PlayerData());
                log.info("Client connected: {}", client.getSessionId());
            }
        });

        socketIOServer.addDisconnectListener(client -> {
            if(isConnected){
                isConnected = false;
                try {
                    socketIOServer.getBroadcastOperations().sendEvent("playerDisconnected", client, objectMapper.writeValueAsString(connectedPlayers.get(client.getSessionId().toString())));
                    connectedPlayers.remove(client.getSessionId().toString());
                    log.info("Disconnected session {} removed from connectedPlayers. ConnectedPlayers: {}", client.getSessionId().toString(), connectedPlayers);
                    log.info("Client disconnected: {}", client.getSessionId());
                } catch (JsonProcessingException e){
                    log.error("Error while removing player. Json processing exception: {}, {}", e, e.getMessage());
                } catch (Exception e){
                    log.error("Unknown exception: {}, {}", e, e.getMessage());
                }
            }
        });
        socketIOServer.start();
    }

    @PreDestroy
    public void stop(){
        socketIOServer.stop();
    }
}
