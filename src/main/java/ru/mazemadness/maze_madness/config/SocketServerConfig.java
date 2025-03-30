package ru.mazemadness.maze_madness.config;

import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.SpringAnnotationScanner;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import ru.mazemadness.maze_madness.jsonTypes.PlayerData;

import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Configuration
@ComponentScan
@PropertySource("classpath:application.properties")
public class SocketServerConfig {
    @Value("${server.host}")
    private String serverHost;
    @Value("${socket.port}")
    private int socketPort;

    private boolean isSocketConnected;

    @Bean
    public SocketIOServer socketIOServer(){
        com.corundumstudio.socketio.Configuration config = new com.corundumstudio.socketio.Configuration();
        config.setHostname(serverHost);
        config.setPort(socketPort);

        return new SocketIOServer(config);
    }

    @Bean
    public SpringAnnotationScanner springAnnotationScanner(SocketIOServer socketIOServer) {
        return new SpringAnnotationScanner(socketIOServer);
    }

    @Bean
    CommandLineRunner initSocketServer(SocketIOServer socketIOServer){
        return args -> {
            socketIOServer.addConnectListener(client -> {
                log.info("Client connected: {}", client.getSessionId());
            });

            socketIOServer.addDisconnectListener(client -> {
                log.info("Client disconnected: {}", client.getSessionId());
            });

            socketIOServer.start();
        };
    }
    @Bean
    public ConcurrentHashMap<String,PlayerData> connectedPlayers(){
        return new ConcurrentHashMap<>();
    }
}
