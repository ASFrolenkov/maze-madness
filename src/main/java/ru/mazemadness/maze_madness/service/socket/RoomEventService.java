package ru.mazemadness.maze_madness.service.socket;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.mazemadness.maze_madness.dto.MoveDto;
import ru.mazemadness.maze_madness.dto.PlayerDto;
import ru.mazemadness.maze_madness.interfaces.GameEvents;
import ru.mazemadness.maze_madness.utils.RandomData;
import ru.mazemadness.maze_madness.utils.ValidationUtils;

import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoomEventService implements GameEvents {

    private final SocketIOServer server;
    private final ClientConnectionService clientService;
    private final ConcurrentHashMap<String, PlayerDto> connectedPlayers = new ConcurrentHashMap<>();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final RandomData randomData = new RandomData();
    private final ValidationUtils validationUtils = new ValidationUtils();

    @Override
    public void onPingReceived(SocketIOClient client, String message) {
        log.info("Client Ping received: {}", message);
        client.sendEvent("pong", message);
        log.info("Client Pong sent: {}", message);
    }

    @Override
    public void onMoveReceived(SocketIOClient client, String roomId, String moveData) {
        log.debug("Client {} sent moveData: {}", client.getSessionId(), moveData);
        try {
            if (connectedPlayers.get(client.getSessionId().toString()) != null){
                PlayerDto currentPlayer = connectedPlayers.get(client.getSessionId().toString());
                currentPlayer.setSpawnX(objectMapper.readValue(moveData, MoveDto.class).getX());
                currentPlayer.setSpawnY(objectMapper.readValue(moveData, MoveDto.class).getY());
                connectedPlayers.put(client.getSessionId().toString(), currentPlayer);
                String connectedPlayersJson = objectMapper.writeValueAsString(connectedPlayers);
                server.getBroadcastOperations().sendEvent("playerMoved", client, connectedPlayersJson);
                log.debug("Server {} sent moveData: {}", client.getSessionId(), connectedPlayersJson);
            } else {
                log.error("Cannot find player for {} session id!", client.getSessionId().toString());
            }
        } catch (JsonProcessingException e){
            log.error("Error processing json: {}, {}", e, e.getMessage());
        } catch (Exception e){
            log.error("Unknown exception while player moving: {}, {}", e, e.getMessage());
        }
    }

    @Override
    public void onPlayerCreate(SocketIOClient client, String roomId, String playerName) {
        log.info("Client {} sent playerCreate: {}", client.getSessionId(), playerName);
        try {
            if (!validationUtils.isValidName(playerName)){
                client.sendEvent("playerCreateDecline");
                return;
            }

            PlayerDto newPlayer = new PlayerDto(
                    client.getSessionId().toString(),
                    playerName,
                    randomData.getRandomPosition(100,400),
                    randomData.getRandomPosition(100,400),
                    randomData.getRandomColor()
            );

            connectedPlayers.put(client.getSessionId().toString(), newPlayer);

            // Sending alreadyConnectedPlayers to client
            String connectedPlayersJson = objectMapper.writeValueAsString(connectedPlayers);
            log.info("connectedPlayerJson: {}", connectedPlayersJson);
            client.sendEvent("playerCreateAccept", connectedPlayersJson);

            // Sending to alreadyConnectedPlayers new player
            String newPlayerJson = objectMapper.writeValueAsString(newPlayer);
            log.info("newPlayerJson: {}", newPlayerJson);
            server.getRoomOperations(roomId).sendEvent("roomPlayerConnected", client, newPlayerJson);
        } catch (JsonProcessingException e){
            log.error("Error processing json: {}, {}", e, e.getMessage());
        } catch (Exception e){
            log.error("Exception while creating new player. Session id: {}, PlayerData: {}, Error message: {}, {}",
                    client.getSessionId().toString(), playerName, e, e.getMessage());
        }
    }

    @Override
    public void onPlayerDisconnected(SocketIOClient client) {
        try {
            String userId = client.getHandshakeData().getSingleUrlParam("userId");
            if (userId != null) {
                clientService.unregisterClient(userId);
            }

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
    public void onPlayerConnected(SocketIOClient client) {
        log.info("Client handshakeData: {}", client.getHandshakeData().toString());
        String userId = client.getHandshakeData().getSingleUrlParam("userId");
        if (userId != null) {
            clientService.registerClient(userId, client);
            client.sendEvent("serverConnected", "Connected to main namespace.");
        }
        connectedPlayers.put(client.getSessionId().toString(), new PlayerDto());
    }
}
