package com.escuela.act4.controller;

import com.escuela.act4.dto.EscuelaRequest;
import com.escuela.act4.dto.EscuelaResponse;
import com.escuela.act4.service.EscuelaService;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/escuelas")
public class EscuelaController {

    private final EscuelaService service;

    public EscuelaController(EscuelaService service) {
        this.service = service;
    }

    // GET /api/escuelas
    @GetMapping
    public ResponseEntity<List<EscuelaResponse>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    // POST /api/escuelas -> 201 Created
    @PostMapping
    public ResponseEntity<EscuelaResponse> crear(@Valid @RequestBody EscuelaRequest request) {
        EscuelaResponse creada = service.crear(request);
        return ResponseEntity.created(URI.create("/api/escuelas/" + creada.id())).body(creada);
    }
}
