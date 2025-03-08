package ru.mazemadness.maze_madness.service;

import com.corundumstudio.socketio.SocketIOClient;
import org.springframework.stereotype.Service;

/***
 * onPingReceived:
 * from: ping
 * to: pong
 * <p>
 * onMoveReceived
 * from: playerMove
 * to: playerMoved
 * <p>
 * onPlayerCreate
 * from: playerCreate
 * to: playerCreated
 */
@Service
public interface GameEvents {
    void onPingReceived(SocketIOClient client, String message);
    void onMoveReceived(SocketIOClient client, String moveData);
    void onPlayerCreate(SocketIOClient client, String playerData);
}
