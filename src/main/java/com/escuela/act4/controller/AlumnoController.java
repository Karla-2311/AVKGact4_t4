package com.escuela.act4.controller;

import com.escuela.act4.dto.AlumnoRequest;
import com.escuela.act4.dto.AlumnoResponse;
import com.escuela.act4.dto.PageResponse;
import com.escuela.act4.service.AlumnoService;
import jakarta.validation.Valid;
import java.net.URI;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/alumnos")
public class AlumnoController {

    private final AlumnoService service;

    public AlumnoController(AlumnoService service) {
        this.service = service;
    }

    // GET /api/alumnos?page=0&size=10
    @GetMapping
    public ResponseEntity<PageResponse<AlumnoResponse>> listar(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        int safePage = Math.max(page, 0);
        int safeSize = Math.min(Math.max(size, 1), 100);
        return ResponseEntity.ok(service.listar(PageRequest.of(safePage, safeSize, Sort.by("id"))));
    }

    // GET /api/alumnos/{id}
    @GetMapping("/{id}")
    public ResponseEntity<AlumnoResponse> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(service.obtener(id));
    }

    // POST /api/alumnos -> 201 Created + header Location
    @PostMapping
    public ResponseEntity<AlumnoResponse> crear(@Valid @RequestBody AlumnoRequest request) {
        AlumnoResponse creado = service.crear(request);
        return ResponseEntity.created(URI.create("/api/alumnos/" + creado.id())).body(creado);
    }

    // PUT /api/alumnos/{id}
    @PutMapping("/{id}")
    public ResponseEntity<AlumnoResponse> actualizar(@PathVariable Long id,
                                                     @Valid @RequestBody AlumnoRequest request) {
        return ResponseEntity.ok(service.actualizar(id, request));
    }

    // DELETE /api/alumnos/{id} -> 204 No Content
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
