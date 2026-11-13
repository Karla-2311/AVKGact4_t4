package com.escuela.act4.service;

import com.escuela.act4.dto.EscuelaRequest;
import com.escuela.act4.dto.EscuelaResponse;
import com.escuela.act4.model.Escuela;
import com.escuela.act4.repository.EscuelaRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EscuelaService {

    private final EscuelaRepository repository;

    public EscuelaService(EscuelaRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<EscuelaResponse> listar() {
        return repository.findAll().stream().map(this::toResponse).toList();
    }

    @Transactional
    public EscuelaResponse crear(EscuelaRequest request) {
        Escuela escuela = new Escuela();
        escuela.setNombre(request.nombre().trim());
        escuela.setCiudad(request.ciudad().trim());
        return toResponse(repository.save(escuela));
    }

    private EscuelaResponse toResponse(Escuela e) {
        return new EscuelaResponse(e.getId(), e.getNombre(), e.getCiudad());
    }
}
