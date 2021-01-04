package br.com.leonardo.demonstrativo.controller;

import java.net.URI;
import java.util.Optional;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
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
import org.springframework.web.util.UriComponentsBuilder;

import br.com.leonardo.demonstrativo.controller.dto.TopicoDetalhesDto;
import br.com.leonardo.demonstrativo.controller.dto.TopicoDto;
import br.com.leonardo.demonstrativo.controller.form.AtualizacaoTopicoForm;
import br.com.leonardo.demonstrativo.controller.form.TopicoForm;
import br.com.leonardo.demonstrativo.model.Topico;
import br.com.leonardo.demonstrativo.repository.CursoRepository;
import br.com.leonardo.demonstrativo.repository.TopicoRepository;


@RestController
@RequestMapping("/topicos")
public class TopicosController {
	
	@Autowired
	private TopicoRepository topicoRepository;
	
	@Autowired
	private CursoRepository cursoRepository;
	
	@GetMapping
	public Page<TopicoDto> listaTopicos(@RequestParam(required = false) String nomeCurso,@PageableDefault(sort="titulo",direction = Direction.DESC,page = 0,size = 5) Pageable paginacao){
		if(nomeCurso==null) {
			Page<Topico> topicos = topicoRepository.findAll(paginacao);
			return TopicoDto.converter(topicos);
		}else {
			Page<Topico> topicos = topicoRepository.findByCurso_Nome(nomeCurso,paginacao);
			return TopicoDto.converter(topicos);
		}
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<TopicoDetalhesDto> TopicoDetalhe(@PathVariable Long id){
		
		Optional<Topico> optionalTopico = topicoRepository.findById(id);
		if(optionalTopico.isPresent()) {
			return ResponseEntity.ok(new TopicoDetalhesDto(optionalTopico.get()));	
		}
		return ResponseEntity.notFound().build();
		
	}
	
	@PostMapping
	@Transactional
	public ResponseEntity<TopicoDto> cadastrarTopico(@RequestBody @Valid TopicoForm formTopico,UriComponentsBuilder uriBuilder) {
		Topico topico = formTopico.converter(cursoRepository);
		topicoRepository.save(topico);
		URI uri = uriBuilder.path("/topicos/{id}").buildAndExpand(topico.getId()).toUri();
		return ResponseEntity.created(uri).body(new TopicoDto(topico));
	}
	
	@PutMapping("/{id}")
	@Transactional
	public ResponseEntity<TopicoDto> atualizarTopico(@PathVariable Long id,@Valid @RequestBody AtualizacaoTopicoForm formTopicoAtualizado ){
		Optional<Topico> optionalTopico = topicoRepository.findById(id);
		if(optionalTopico.isPresent()) {
			Topico topico = formTopicoAtualizado.atualizar(id, topicoRepository);
			return ResponseEntity.ok(new TopicoDto(topico));	
		}
		return ResponseEntity.notFound().build();
		
	}
	
	
	@DeleteMapping("/{id}")
	@Transactional
	public ResponseEntity<?> removerTopico(@PathVariable Long id){
		Optional<Topico> optionalTopico = topicoRepository.findById(id);
		if(optionalTopico.isPresent()) {
			topicoRepository.deleteById(id);
			return ResponseEntity.ok().build();	
		}
		return ResponseEntity.notFound().build();
		
	}
	
	
}
