package ru.mazemadness.maze_madness.events;

import com.corundumstudio.socketio.SocketIOClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.stereotype.Component;

@Component
public interface GameEvents {
    void onClientDisconnected(SocketIOClient client);
    void onClientConnected(SocketIOClient client);
//    void onMoveReceived(SocketIOClient client, String roomId, MoveDto moveData);
//    void onPlayerCreate(SocketIOClient client, String roomId, String playerName);
}
