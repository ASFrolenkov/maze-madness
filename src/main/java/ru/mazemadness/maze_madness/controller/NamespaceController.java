package ru.mazemadness.maze_madness.controller;

import com.corundumstudio.socketio.SocketIONamespace;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.mazemadness.maze_madness.dto.NamespaceRequest;
import ru.mazemadness.maze_madness.service.NamespaceService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/socket/v1/rooms")
public class NamespaceController {

    private final NamespaceService namespaceService;

    @PostMapping
    public ResponseEntity<String> createNamespace(@RequestBody NamespaceRequest request) {
        SocketIONamespace namespace = namespaceService.createNamespace(request.getName());
        return ResponseEntity.ok("Namespace created: " + namespace.getName());
    }

    @DeleteMapping("/{name}")
    public ResponseEntity<String> deleteNamespace(@PathVariable String name) {
        namespaceService.deleteNamespace(name);
        return ResponseEntity.ok("Namespace deleted: " + name);
    }

    @GetMapping
    public ResponseEntity<List<String>> listNamespaces() {
        return ResponseEntity.ok(namespaceService.getAllNamespaces());
    }
}
