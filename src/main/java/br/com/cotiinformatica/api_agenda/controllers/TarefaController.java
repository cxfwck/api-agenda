package br.com.cotiinformatica.api_agenda.controllers;

import br.com.cotiinformatica.api_agenda.dtos.CriarTarefaRequest;
import br.com.cotiinformatica.api_agenda.exceptions.RegistroNaoEncontradoException;
import br.com.cotiinformatica.api_agenda.services.TarefaService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/tarefas")
public class TarefaController {

    @Autowired
    private TarefaService tarefaService;

    @PostMapping("cadastrar")
    public ResponseEntity<?> cadastrar(@RequestBody @Valid CriarTarefaRequest request, HttpServletRequest http) {
        try {
            var response = tarefaService.criarTarefa(request, http);
            return ResponseEntity.status(201).body(response);
        }
        catch(RegistroNaoEncontradoException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
        catch(Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }
}
