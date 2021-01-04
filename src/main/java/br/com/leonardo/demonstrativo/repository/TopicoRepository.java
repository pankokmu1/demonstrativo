package br.com.leonardo.demonstrativo.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import br.com.leonardo.demonstrativo.model.Topico;

public interface TopicoRepository extends JpaRepository<Topico,Long> {

	Page<Topico> findByCurso_Nome(String nomeCurso,Pageable paginacao);
	
}
