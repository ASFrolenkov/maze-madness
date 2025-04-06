package ru.mazemadness.maze_madness.service.socket;

import com.corundumstudio.socketio.SocketIOClient;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ClientConnectionService {

    private final ConcurrentHashMap<String, SocketIOClient> connectedClients = new ConcurrentHashMap<>();

    public void registerClient(String userId, SocketIOClient client){
        connectedClients.put(userId, client);
    }

    public void unregisterClient(String userId){
        connectedClients.remove(userId);
    }
    public Optional<SocketIOClient> getClient(String userId){
        return Optional.ofNullable(connectedClients.get(userId));
    }
}
