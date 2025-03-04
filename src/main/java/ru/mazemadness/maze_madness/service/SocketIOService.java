package ru.mazemadness.maze_madness.service;

import com.corundumstudio.socketio.SocketIOServer;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SocketIOService {

    @Autowired
    private SocketIOServer socketIOServer;

    @PostConstruct
    public void start(){
        socketIOServer.addConnectListener(client -> {
            log.info("Client connected: {}", client.getSessionId());
        });

        socketIOServer.addDisconnectListener(client -> {
            log.info("Client disconnected: {}", client.getSessionId());
        });

        socketIOServer.start();
    }

    @PreDestroy
    public void stop(){
        socketIOServer.stop();
    }
}
