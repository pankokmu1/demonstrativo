package br.com.leonardo.demonstrativo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.leonardo.demonstrativo.model.Curso;

public interface CursoRepository extends JpaRepository<Curso,Long> {

	Curso findByNome(String nomeCurso);

}
