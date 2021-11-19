package br.com.alura.forum.controller;


import br.com.alura.forum.controller.dto.TopicoDto;
import br.com.alura.forum.modelo.src.main.java.br.com.alura.forum.modelo.Curso;
import br.com.alura.forum.modelo.src.main.java.br.com.alura.forum.modelo.Topico;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
public class TopicosController {

    @RequestMapping("/topicos")
    public List<TopicoDto> lista() {
        Topico topico = new Topico("Dúvidas", "Dúvidas com Spring", new Curso("Spring", "programação"));
        return TopicoDto.converter(Arrays.asList(topico, topico, topico));
    }
}
