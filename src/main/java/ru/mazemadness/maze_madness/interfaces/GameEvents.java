package ru.mazemadness.maze_madness.interfaces;

import com.corundumstudio.socketio.SocketIOClient;
import org.springframework.stereotype.Service;

@Service
public interface GameEvents {
    void onPingReceived(SocketIOClient client, String message);
    void onMoveReceived(SocketIOClient client, String roomId, String moveData);
    void onPlayerCreate(SocketIOClient client, String roomId, String playerName);
    void onPlayerDisconnected(SocketIOClient client);
    void onPlayerConnected(SocketIOClient client);
}
