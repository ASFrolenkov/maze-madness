package ru.mazemadness.maze_madness.service;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIONamespace;
import com.corundumstudio.socketio.SocketIOServer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.mazemadness.maze_madness.dto.ChatMessage;
import ru.mazemadness.maze_madness.dto.JoinRoomRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class NamespaceService {

    private final SocketIOServer server;
    private final ConcurrentHashMap<String, SocketIONamespace> activeNamespaces = new ConcurrentHashMap<>();


    public List<String> getAllNamespaces(){
        return new ArrayList<>(activeNamespaces.keySet());
    }
    public SocketIONamespace createNamespace(String namespaceName) {
        if (activeNamespaces.containsKey(namespaceName)) {
            return activeNamespaces.get(namespaceName);
        }

        SocketIONamespace namespace = server.addNamespace("/" + namespaceName);
        activeNamespaces.put(namespaceName, namespace);

        // Add default event listeners
        namespace.addEventListener("join_room", JoinRoomRequest.class, this::handleJoinRoom);
        namespace.addEventListener("leave_room", String.class, this::handleLeaveRoom);
        namespace.addEventListener("message", ChatMessage.class, this::handleMessage);

        return namespace;
    }

    public void deleteNamespace(String namespaceName) {
        SocketIONamespace namespace = activeNamespaces.get(namespaceName);
        if (namespace != null) {
            namespace.getBroadcastOperations().sendEvent("namespace_closing");
            server.removeNamespace(namespaceName);
            activeNamespaces.remove(namespaceName);
        }
    }

    private void handleJoinRoom(SocketIOClient client, JoinRoomRequest request, AckRequest ack) {
        client.joinRoom(request.getRoomId());
        ack.sendAckData("success", "Joined room " + request.getRoomId());
    }

    private void handleLeaveRoom(SocketIOClient client, String roomId, AckRequest ack) {
        client.leaveRoom(roomId);
        ack.sendAckData("success", "Left room " + roomId);
    }

    private void handleMessage(SocketIOClient client, ChatMessage message, AckRequest ack) {
        client.getNamespace().getRoomOperations(message.getRoomId())
                .sendEvent("new_message", message);
        ack.sendAckData("success");
    }
}
