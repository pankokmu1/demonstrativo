package br.com.leonardo.demonstrativo.controller.dto;

import org.springframework.data.domain.Page;

import br.com.leonardo.demonstrativo.model.Curso;

public class CursoDto {
	private long id;
	private String nome;
	private String categoria;
	
	
	public CursoDto(Curso curso) {
		this.id = curso.getId();
		this.nome = curso.getNome();
		this.categoria = curso.getCategoria();
	}


	public long getId() {
		return id;
	}


	public String getNome() {
		return nome;
	}


	public String getCategoria() {
		return categoria;
	}


	public static Page<CursoDto> convert(Page<Curso> cursos) {
		return cursos.map(CursoDto::new);
	}
	
	
	
	
}
