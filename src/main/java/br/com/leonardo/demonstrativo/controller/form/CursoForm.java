package br.com.leonardo.demonstrativo.controller.form;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import br.com.leonardo.demonstrativo.model.Curso;
import br.com.leonardo.demonstrativo.repository.CursoRepository;

public class CursoForm {
	
	@NotNull @NotEmpty
	private String nome;
	
	@NotNull @NotEmpty
	private String categoria;
	
	
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getCategoria() {
		return categoria;
	}
	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}
	public Curso convert() {
		Curso curso = new Curso();
		curso.setCategoria(categoria);
		curso.setNome(nome);
		return curso;
	}
	
	public Curso atualizar(Long id, CursoRepository repositoryCurso) {
		Curso curso = repositoryCurso.getOne(id);
		curso.setNome(this.nome);
		curso.setCategoria(this.categoria);
		return curso;
	}
}
