package ru.mazemadness.maze_madness.service;

import com.corundumstudio.socketio.SocketIOClient;
import org.springframework.stereotype.Service;

@Service
public interface GameEvents {
    void onPingReceived(SocketIOClient client, String message);
    void onMoveReceived(SocketIOClient client, String moveData);
    void onPlayerCreate(SocketIOClient client, String playerData);
}
