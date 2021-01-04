package br.com.leonardo.demonstrativo.controller;

import java.net.URI;
import java.util.Optional;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.leonardo.demonstrativo.controller.dto.CursoDto;
import br.com.leonardo.demonstrativo.controller.form.CursoForm;
import br.com.leonardo.demonstrativo.model.Curso;
import br.com.leonardo.demonstrativo.repository.CursoRepository;

@RestController
@RequestMapping("/cursos")
public class CursosController {
	
	@Autowired
	private CursoRepository cursoRepository;
	
	@GetMapping
	@Cacheable(value = "listaCursos")
	public Page<CursoDto> listaCursos(@PageableDefault(sort="nome",direction = Direction.DESC,page = 0,size = 4) Pageable paginacao){
		Page<Curso> cursos = cursoRepository.findAll(paginacao);
		return CursoDto.convert(cursos);
	}
	
	@PostMapping
	@Transactional
	@CacheEvict(value = "listaCursos",allEntries = true)
	public ResponseEntity<CursoDto> CadastrarCurso(@RequestBody @Valid CursoForm formCurso,UriComponentsBuilder uriBuilder) {
		Curso curso = formCurso.convert();
		cursoRepository.save(curso);
		URI uri = uriBuilder.path("/cursos/{id}").buildAndExpand(curso.getId()).toUri();
		
		return ResponseEntity.created(uri).body(new CursoDto(curso));
	}
	
	@PutMapping("/{id}")
	@Transactional
	@CacheEvict(value = "listaCursos",allEntries = true)
	public ResponseEntity<CursoDto> atualizarTopico(@PathVariable Long id,@Valid @RequestBody CursoForm formCurso ){
		Optional<Curso> optionalTopico = cursoRepository.findById(id);
		if(optionalTopico.isPresent()) {
			Curso topico = formCurso.atualizar(id, cursoRepository);
			return ResponseEntity.ok(new CursoDto(topico));	
		}
		return ResponseEntity.notFound().build();
	}
}
