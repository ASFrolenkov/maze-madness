package ru.mazemadness.maze_madness.service.socket;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.mazemadness.maze_madness.dto.socket.*;
import ru.mazemadness.maze_madness.events.GameEvents;
import ru.mazemadness.maze_madness.events.RoomEvents;
import ru.mazemadness.maze_madness.utils.RandomData;
import ru.mazemadness.maze_madness.utils.ValidationUtils;

import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
@Slf4j
public class SocketEventHandler implements GameEvents, RoomEvents {
    private final SocketIOServer server;
    private final RoomService roomService;
    private final ConcurrentHashMap<String, PlayerDto> connectedPlayers = new ConcurrentHashMap<>();
    private final ConnectedClientService clientService;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final RandomData randomData = new RandomData();
    private final ValidationUtils validationUtils = new ValidationUtils();

//    @Override
//    public void onMoveReceived(SocketIOClient client, String roomId, String moveData) {
//        log.debug("Client {} sent moveData: {}", client.getSessionId(), moveData);
//        try {
//            if (connectedPlayers.get(client.getSessionId().toString()) != null){
//                PlayerDto currentPlayer = connectedPlayers.get(client.getSessionId().toString());
//                currentPlayer.setSpawnX(objectMapper.readValue(moveData, MoveDto.class).getX());
//                currentPlayer.setSpawnY(objectMapper.readValue(moveData, MoveDto.class).getY());
//                connectedPlayers.put(client.getSessionId().toString(), currentPlayer);
//                String connectedPlayersJson = objectMapper.writeValueAsString(connectedPlayers);
//                server.getBroadcastOperations().sendEvent("playerMoved", client, connectedPlayersJson);
//                log.debug("Server {} sent moveData: {}", client.getSessionId(), connectedPlayersJson);
//            } else {
//                log.error("Cannot find player for {} session id!", client.getSessionId().toString());
//            }
//        } catch (JsonProcessingException e){
//            log.error("Error processing json: {}, {}", e, e.getMessage());
//        } catch (Exception e){
//            log.error("Unknown exception while player moving: {}, {}", e, e.getMessage());
//        }
//    }
//
//    @Override
//    public void onPlayerCreate(SocketIOClient client, String roomId, String playerName) {
//        log.info("Client {} sent playerCreate: {}", client.getSessionId(), playerName);
//        try {
//            if (!validationUtils.isValidName(playerName)){
//                client.sendEvent("playerCreateDecline");
//                return;
//            }
//
//            PlayerDto newPlayer = new PlayerDto(
//                    client.getSessionId().toString(),
//                    playerName,
//                    randomData.getRandomPosition(100,400),
//                    randomData.getRandomPosition(100,400),
//                    randomData.getRandomColor()
//            );
//
//            connectedPlayers.put(client.getSessionId().toString(), newPlayer);
//
//            // Sending alreadyConnectedPlayers to client
//            String connectedPlayersJson = objectMapper.writeValueAsString(connectedPlayers);
//            log.info("connectedPlayerJson: {}", connectedPlayersJson);
//            client.sendEvent("playerCreateAccept", connectedPlayersJson);
//
//            // Sending to alreadyConnectedPlayers new player
//            String newPlayerJson = objectMapper.writeValueAsString(newPlayer);
//            log.info("newPlayerJson: {}", newPlayerJson);
//            server.getRoomOperations(roomId).sendEvent("roomPlayerConnected", client, newPlayerJson);
//        } catch (JsonProcessingException e){
//            log.error("Error processing json: {}, {}", e, e.getMessage());
//        } catch (Exception e){
//            log.error("Exception while creating new player. Session id: {}, PlayerData: {}, Error message: {}, {}",
//                    client.getSessionId().toString(), playerName, e, e.getMessage());
//        }
//    }

    @Override
    public void onUserMessageSend(SocketIOClient client, String clientChatMessage) {
        String roomName = client.getHandshakeData().getSingleUrlParam("roomName");
        // TODO: Проверка содержимого сообщения
//        ChatMessage chatMessageResponse = objectMapper.readValue(clientChatMessage, ChatMessage.class);

        roomService.sendMessageToRoom(roomName, "roomMessageSendServer", clientChatMessage);
        // TODO: гарантия упорядоченности, очередь (?), история сообщений (?)
        log.info("onUserMessageSent: {}", clientChatMessage);
    }

    @Override
    public void onClientDisconnected(SocketIOClient client) {
        if (clientService.getAllConnectedClients().contains(client)){
            String roomName = client.getHandshakeData().getSingleUrlParam("roomName");
            String username = client.getHandshakeData().getSingleUrlParam("username");
//            roomService.sendMessageToRoom(roomName, "roomUserLeaveServer", roomService.getRoomUsernames(roomName));

            roomService.leaveRoom(roomName, client);
            clientService.unregisterClient(client);

            roomService.sendMessageToRoom(roomName, "roomUserLeaveServer", roomService.getRoomUsernames(roomName));
            log.info("Client {} disconnected", client.getSessionId());
        }
    }

    @Override
    public void onClientConnected(SocketIOClient client) {
        log.info("Client handshakeData: {}", client.getHandshakeData().toString());
        String roomName = client.getHandshakeData().getSingleUrlParam("roomName");
        String username = client.getHandshakeData().getSingleUrlParam("username");

        if (roomName != null && username != null && roomService.findRoomByName(roomName) != null) {
            if (!roomService.findRoomByName(roomName).getIsFull()) {
                clientService.registerClient(client, username);
                roomService.joinRoom(roomName, client);
                log.info("Client {} joined room {}", client.getSessionId(), roomName);
            } else {
                log.error("Client {} not joined room {}. Room {} is full.", client.getSessionId(), roomName, roomName);
                client.disconnect();
            }
        } else {
            log.error("Connection error.");
            client.disconnect();
        }
    }

    @Override
    public void onRoomGameStart(SocketIOClient client){
        String roomName = client.getHandshakeData().getSingleUrlParam("roomName");
        String username = client.getHandshakeData().getSingleUrlParam("username");
        RoomDto room = roomService.findRoomByName(roomName);

        if (room.getPlayers() >= 2 && room.getCreatedBy().equals(username)){
            roomService.sendMessageToRoom(roomName, "roomGameStartServer", "Game started!");
        } else {
            client.sendEvent("roomGameStartFailureServer",
                    "Game cannot be started with 1 player / Only host can start the game");
        }
    }

    @Override
    public void onRoomUserKick(SocketIOClient client, String otherUsername) {
        String roomName = client.getHandshakeData().getSingleUrlParam("roomName");
        String username = client.getHandshakeData().getSingleUrlParam("username");
        RoomDto roomDto = roomService.findRoomByName(roomName);
        SocketIOClient otherClient = clientService.getClientByUsername(otherUsername);
        if (roomDto.getCreatedBy().equals(username)
                && !roomDto.getCreatedBy().equals(otherUsername)
                && otherClient != null){
            otherClient.disconnect();
//            roomService.sendMessageToRoom(roomName, "roomUserKickServer", clientService.getClientByUsername(otherUsername).getSessionId());
        }
    }

    @Override
    public void onRoomGetPlayers(SocketIOClient client){
        String roomName = client.getHandshakeData().getSingleUrlParam("roomName");

        roomService.sendMessageToRoom(roomName, "roomGetUsersServer", roomService.getRoomUsernames(roomName));
    }
}
