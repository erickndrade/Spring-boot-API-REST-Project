package br.com.alura.forum.controller.repository;

import br.com.alura.forum.modelo.src.main.java.br.com.alura.forum.modelo.Curso;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CursoRepository extends JpaRepository<Curso, Long> {

    Curso findByNome(String nomeCurso);
}
