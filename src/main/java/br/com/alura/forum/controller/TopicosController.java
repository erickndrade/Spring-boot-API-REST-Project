package br.com.alura.forum.controller;


import br.com.alura.forum.controller.dto.DetailsTopicoDto;
import br.com.alura.forum.controller.dto.TopicoDto;
import br.com.alura.forum.controller.form.AttTopicoForm;
import br.com.alura.forum.controller.form.TopicoForm;
import br.com.alura.forum.controller.repository.CursoRepository;
import br.com.alura.forum.modelo.src.main.java.br.com.alura.forum.modelo.Topico;
import br.com.alura.forum.repository.TopicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("/topicos")
public class TopicosController {

    /**
     * A ideia é você não utilizar cache em todos os lugares, mas sim onde fizer sentido. Geralmente, utilizamos cache
     * naquelas tabelas que nunca ou são raramente atualizadas. É preciso olhar a aplicação e escolher uma tabela que
     * seja mais estável, sem muitas alterações, onde consulta uma única vez, guarda o resultado em memória, e ali fica
     * armazenado por um bom tempo.
     */

    @Autowired
    private TopicoRepository topicoRepository;
    @Autowired
    private CursoRepository cursoRepository;

    @GetMapping
    @Cacheable(value = "listaDeTopicos")
    public Page<TopicoDto> lista(@RequestParam(required = false) String nomeCurso,
                                 @PageableDefault(sort = "id", direction = Sort.Direction.DESC, page = 0, size = 10)
                                         Pageable paginacao) {
        if (nomeCurso == null) {
            Page<Topico> topicos = topicoRepository.findAll(paginacao);
            return TopicoDto.converter(topicos);
        } else {
            Page<Topico> topicos = topicoRepository.findByCursoNome(nomeCurso, paginacao);
            return TopicoDto.converter(topicos);
        }
    }

    @PostMapping
    @Transactional
    @CacheEvict(value = "listaDeTopicos", allEntries = true)
    public ResponseEntity<TopicoDto> cadastrar(@RequestBody @Valid TopicoForm topicoForm, UriComponentsBuilder uriComponentsBuilder) {
        Topico topico = topicoForm.converter(cursoRepository);
        topicoRepository.save(topico);
        URI uri = uriComponentsBuilder.path("/topicos/{id}").buildAndExpand(topico.getId()).toUri();
        return ResponseEntity.created(uri).body(new TopicoDto(topico));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> detalhar(@PathVariable Long id) {
        Optional<Topico> topico = topicoRepository.findById(id);
        return topico.<ResponseEntity<Object>>map(value -> ResponseEntity.ok(new DetailsTopicoDto(value))).orElseGet(()
                -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    @Transactional
    @CacheEvict(value = "listaDeTopicos", allEntries = true)
    public ResponseEntity<TopicoDto> atualizar(@PathVariable Long id, @RequestBody @Valid AttTopicoForm topicoForm) {
        Optional<Topico> topico = topicoRepository.findById(id);
        return topico.<ResponseEntity<TopicoDto>>map(value -> ResponseEntity.ok(new TopicoDto(value))).orElseGet(()
                -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Transactional
    @CacheEvict(value = "listaDeTopicos", allEntries = true)
    public ResponseEntity<?> remover(@PathVariable Long id) {
        Optional<Topico> topico = topicoRepository.findById(id);
        if (topico.isPresent()) {
            topicoRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
