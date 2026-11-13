package com.escuela.act4.service;

import com.escuela.act4.dto.AlumnoRequest;
import com.escuela.act4.dto.AlumnoResponse;
import com.escuela.act4.dto.EscuelaResponse;
import com.escuela.act4.dto.PageResponse;
import com.escuela.act4.exception.ConflictException;
import com.escuela.act4.exception.ResourceNotFoundException;
import com.escuela.act4.model.Alumno;
import com.escuela.act4.model.Escuela;
import com.escuela.act4.repository.AlumnoRepository;
import com.escuela.act4.repository.EscuelaRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AlumnoService {

    private final AlumnoRepository alumnoRepository;
    private final EscuelaRepository escuelaRepository;

    public AlumnoService(AlumnoRepository alumnoRepository, EscuelaRepository escuelaRepository) {
        this.alumnoRepository = alumnoRepository;
        this.escuelaRepository = escuelaRepository;
    }

    @Transactional(readOnly = true)
    public PageResponse<AlumnoResponse> listar(Pageable pageable) {
        return PageResponse.from(alumnoRepository.findAllConEscuela(pageable).map(this::toResponse));
    }

    @Transactional(readOnly = true)
    public AlumnoResponse obtener(Long id) {
        return toResponse(alumnoRepository.findByIdConEscuela(id)
                .orElseThrow(() -> noExiste(id)));
    }

    @Transactional
    public AlumnoResponse crear(AlumnoRequest request) {
        String matricula = request.matricula().trim();
        if (alumnoRepository.existsByMatricula(matricula)) {
            throw new ConflictException("Ya existe un alumno con la matrícula " + matricula);
        }
        Alumno alumno = new Alumno();
        aplicar(alumno, request, matricula);
        return toResponse(alumnoRepository.save(alumno));
    }

    @Transactional
    public AlumnoResponse actualizar(Long id, AlumnoRequest request) {
        Alumno alumno = alumnoRepository.findById(id).orElseThrow(() -> noExiste(id));
        String matricula = request.matricula().trim();
        if (alumnoRepository.existsByMatriculaAndIdNot(matricula, id)) {
            throw new ConflictException("Ya existe otro alumno con la matrícula " + matricula);
        }
        aplicar(alumno, request, matricula);
        return toResponse(alumnoRepository.save(alumno));
    }

    @Transactional
    public void eliminar(Long id) {
        alumnoRepository.delete(alumnoRepository.findById(id).orElseThrow(() -> noExiste(id)));
    }

    private void aplicar(Alumno alumno, AlumnoRequest request, String matricula) {
        Escuela escuela = escuelaRepository.findById(request.escuelaId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No existe una escuela con id " + request.escuelaId()));
        alumno.setNombre(request.nombre().trim());
        alumno.setMatricula(matricula);
        alumno.setEscuela(escuela);
    }

    private ResourceNotFoundException noExiste(Long id) {
        return new ResourceNotFoundException("No existe un alumno con id " + id);
    }

    private AlumnoResponse toResponse(Alumno a) {
        Escuela e = a.getEscuela();
        return new AlumnoResponse(a.getId(), a.getNombre(), a.getMatricula(),
                new EscuelaResponse(e.getId(), e.getNombre(), e.getCiudad()));
    }
}
