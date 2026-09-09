package br.com.cotiinformatica.api_agenda.controllers;

import br.com.cotiinformatica.api_agenda.dtos.CriarTarefaRequest;
import br.com.cotiinformatica.api_agenda.dtos.EditarTarefaRequest;
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

    @PutMapping("editar/{id}")
    public ResponseEntity<?> editar(@PathVariable Integer id, @RequestBody @Valid EditarTarefaRequest request) {
        try {
            var response = tarefaService.editarTarefa(id, request);
            return ResponseEntity.status(200).body(response);
        }
        catch(RegistroNaoEncontradoException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
        catch(IllegalArgumentException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
        catch(Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    @DeleteMapping("excluir/{id}")
    public ResponseEntity<?> excluir(@PathVariable Integer id) {
        try {
            tarefaService.excluirTarefa(id);
            return ResponseEntity.status(200).body("Tarefa excluída com sucesso.");
        }
        catch(RegistroNaoEncontradoException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
        catch(Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    @GetMapping("consultar")
    public ResponseEntity<?> consultar(
            @RequestParam String dataInicio,
            @RequestParam String dataFim
    ) {
        try {
            var response = tarefaService.consultarTarefasPorPeriodoResponse(java.time.LocalDate.parse(dataInicio), java.time.LocalDate.parse(dataFim));
            return ResponseEntity.status(200).body(response);
        }
        catch(IllegalArgumentException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
        catch(Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    @GetMapping("consultar/{id}")
    public ResponseEntity<?> obterPorId(@PathVariable Integer id) {
        try {
            var response = tarefaService.obterTarefaPorIdResponse(id);
            return ResponseEntity.status(200).body(response);
        }
        catch(RegistroNaoEncontradoException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
        catch(Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }
}
