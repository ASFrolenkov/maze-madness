package ru.mazemadness.maze_madness.service;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.OnConnect;
import com.corundumstudio.socketio.annotation.OnDisconnect;
import com.corundumstudio.socketio.annotation.OnEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.mazemadness.maze_madness.jsonTypes.MoveData;
import ru.mazemadness.maze_madness.jsonTypes.PlayerData;
import ru.mazemadness.maze_madness.utils.RandomData;
import ru.mazemadness.maze_madness.utils.Utils;

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
    private final Utils utils = new Utils();

    @OnConnect
    public void onConnect(SocketIOClient client){
        connectedPlayers.put(client.getSessionId().toString(), new PlayerData());
    }

    @OnDisconnect
    public void onDisconnect(SocketIOClient client){
        try {
            server.getBroadcastOperations().sendEvent("playerDisconnected", client, objectMapper.writeValueAsString(connectedPlayers.get(client.getSessionId().toString())));
            connectedPlayers.remove(client.getSessionId().toString());
            log.info("Disconnected session {} removed from connectedPlayers. ConnectedPlayers: {}", client.getSessionId().toString(), connectedPlayers);
//            log.info("Client disconnected: {}", client.getSessionId());
        } catch (JsonProcessingException e){
            log.error("Error while removing player. Json processing exception: {}, {}", e, e.getMessage());
        } catch (Exception e){
            log.error("Unknown exception: {}, {}", e, e.getMessage());
        }
    }

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
            if (connectedPlayers.get(client.getSessionId().toString()) != null){
                PlayerData currentPlayer = connectedPlayers.get(client.getSessionId().toString());
                currentPlayer.setX(objectMapper.readValue(moveData, MoveData.class).getX());
                currentPlayer.setY(objectMapper.readValue(moveData, MoveData.class).getY());
                connectedPlayers.put(client.getSessionId().toString(), currentPlayer);
                String connectedPlayersJson = objectMapper.writeValueAsString(connectedPlayers);
                server.getBroadcastOperations().sendEvent("playerMoved", client, connectedPlayersJson);
                log.debug("Server {} sent moveData: {}", client.getSessionId(), connectedPlayersJson);
            } else {
                log.error("Cannot find player for {} session id!", client.getSessionId().toString());
            }
//            PlayerData currentPlayer = connectedPlayers.get(client.getSessionId().toString());
//            currentPlayer.setX(objectMapper.readValue(moveData, MoveData.class).getX());
//            currentPlayer.setY(objectMapper.readValue(moveData, MoveData.class).getY());
//            connectedPlayers.put(client.getSessionId().toString(), currentPlayer);
//            String connectedPlayersJson = objectMapper.writeValueAsString(connectedPlayers);
//            server.getBroadcastOperations().sendEvent("playerMoved", client, connectedPlayersJson);
//            log.debug("Server {} sent moveData: {}", client.getSessionId(), connectedPlayersJson);
        } catch (JsonProcessingException e){
            log.error("Error processing json: {}, {}", e, e.getMessage());
        } catch (Exception e){
            log.error("Unknown exception while player moving: {}, {}", e, e.getMessage());
        }
    }

    @Override
    @OnEvent("playerCreate")
    public void onPlayerCreate(SocketIOClient client, String playerName) {
        log.info("Client {} sent playerCreate: {}", client.getSessionId(), playerName);
        try {
            if (!utils.isValidName(playerName)){
                client.sendEvent("playerCreateDeclined");
                return;
            }

            PlayerData newPlayer = new PlayerData(
                    randomData.getRandomId(),
                    playerName,
                    randomData.getRandomPosition(100,400),
                    randomData.getRandomPosition(100,400),
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
                    client.getSessionId().toString(), playerName, e, e.getMessage());
        }
    }

    @Override
    @OnEvent("playerDisconnect")
    public void onPlayerDisconnected(SocketIOClient client) {
        log.info("GET playerDisconnected request");
//        if(isSocketConnected){
//            isSocketConnected = false;
            try {
                server.getBroadcastOperations().sendEvent("playerDisconnected", client, objectMapper.writeValueAsString(connectedPlayers.get(client.getSessionId().toString())));
                connectedPlayers.remove(client.getSessionId().toString());
                log.info("Player {} disconnected and removed from connectedPlayers. ConnectedPlayers: {}", client.getSessionId().toString(), connectedPlayers);
                log.info("Client disconnected: {}", client.getSessionId());
            } catch (JsonProcessingException e){
                log.error("Error while removing player. Json processing exception: {}, {}", e, e.getMessage());
            } catch (Exception e){
                log.error("Unknown exception: {}, {}", e, e.getMessage());
            }
//        }
    }
}
