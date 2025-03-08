package ru.mazemadness.maze_madness.service;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.OnEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.mazemadness.maze_madness.jsonTypes.MoveData;
import ru.mazemadness.maze_madness.jsonTypes.PlayerData;
import ru.mazemadness.maze_madness.utils.RandomData;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class GameEventHandler implements GameEvents {
    @Autowired
    private SocketIOServer server;

    @Autowired
    private ConcurrentHashMap<String, PlayerData> connectedPlayers;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final RandomData randomData = new RandomData();

    @Override
    @OnEvent("ping")
    public void onPingReceived(SocketIOClient client, String message){
        log.info("Client Ping received: {}", message);
        client.sendEvent("pong", message);
        log.info("Client Pong sent: {}", message);
    }

    @Override
    @OnEvent("playerMove")
    public void onMoveReceived(SocketIOClient client, String moveData){
        log.debug("Client {} sent moveData: {}", client.getSessionId(), moveData);
        try {
            PlayerData currentPlayer = connectedPlayers.get(client.getSessionId().toString());
            currentPlayer.setAnim(objectMapper.readValue(moveData, MoveData.class).getAnim());
            currentPlayer.setX(objectMapper.readValue(moveData, MoveData.class).getX());
            currentPlayer.setY(objectMapper.readValue(moveData, MoveData.class).getY());
            connectedPlayers.put(client.getSessionId().toString(), currentPlayer);
            String connectedPlayersJson = objectMapper.writeValueAsString(connectedPlayers);
            server.getBroadcastOperations().sendEvent("playerMoved", client, connectedPlayersJson);
            log.debug("Server {} sent moveData: {}", client.getSessionId(), connectedPlayersJson);
        } catch (JsonProcessingException e){
            log.error("Error processing json: {}, {}", e, e.getMessage());
        } catch (Exception e){
            log.error("Unknown exception while player moving: {}, {}", e, e.getMessage());
        }
    }

    @Override
    @OnEvent("playerCreate")
    public void onPlayerCreate(SocketIOClient client, String playerData) {
        log.info("Client {} sent playerCreate: {}", client.getSessionId(), playerData);
        try {
            PlayerData newPlayer = new PlayerData(
                    randomData.getRandomId(),
                    randomData.getRandomPosition(100,400),
                    randomData.getRandomPosition(100,400),
                    "playerIdle",
                    randomData.getRandomColor()
            );

            connectedPlayers.put(client.getSessionId().toString(), newPlayer);

            // Sending alreadyConnectedPlayers to client
            String connectedPlayersJson = objectMapper.writeValueAsString(connectedPlayers);
            log.info("connectedPlayerJson: {}", connectedPlayersJson);
            client.sendEvent("playerCreated", connectedPlayersJson);

            // Sending to alreadyConnectedPlayers new player
            String newPlayerJson = objectMapper.writeValueAsString(newPlayer);
            log.info("newPlayerJson: {}", newPlayerJson);
            server.getBroadcastOperations().sendEvent("playerConnected", client, newPlayerJson);
        } catch (JsonProcessingException e){
            log.error("Error processing json: {}, {}", e, e.getMessage());
        } catch (Exception e){
            log.error("Exception while creating new player. Session id: {}, PlayerData: {}, Error message: {}, {}",
                    client.getSessionId().toString(), playerData, e, e.getMessage());
        }
    }
}
