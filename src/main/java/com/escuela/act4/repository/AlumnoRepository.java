package com.escuela.act4.repository;

import com.escuela.act4.model.Alumno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AlumnoRepository extends JpaRepository<Alumno, Long> {

    boolean existsByMatricula(String matricula);

    boolean existsByMatriculaAndIdNot(String matricula, Long id);

    // JOIN FETCH evita cargar la escuela con una consulta extra por cada alumno
    @Query(value = "SELECT a FROM Alumno a JOIN FETCH a.escuela",
            countQuery = "SELECT COUNT(a) FROM Alumno a")
    org.springframework.data.domain.Page<Alumno> findAllConEscuela(org.springframework.data.domain.Pageable pageable);

    @Query("SELECT a FROM Alumno a JOIN FETCH a.escuela WHERE a.id = :id")
    java.util.Optional<Alumno> findByIdConEscuela(Long id);
}
