package ru.mazemadness.maze_madness.config.socket;

import com.corundumstudio.socketio.SocketIONamespace;
import com.corundumstudio.socketio.SocketIOServer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class SocketServerInitConfig {
    private final SocketIOServer socketIOServer;
    @Bean
    CommandLineRunner initMainNamespace(){
        return args -> {
            SocketIONamespace mainNamespace = socketIOServer.getNamespace("");

            mainNamespace.addConnectListener(client -> {
                log.info("Client connected: {}", client.getSessionId());
            });

            mainNamespace.addDisconnectListener(client -> {
                log.info("Client disconnected: {}", client.getSessionId());
            });

            socketIOServer.start();
        };
    }
}
