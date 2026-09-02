package br.com.cotiinformatica.api_agenda.controllers;

import br.com.cotiinformatica.api_agenda.dtos.AutenticarUsuarioRequest;
import br.com.cotiinformatica.api_agenda.dtos.CriarUsuarioRequest;
import br.com.cotiinformatica.api_agenda.exceptions.AcessoNegadoException;
import br.com.cotiinformatica.api_agenda.exceptions.EmailJaCadastradoException;
import br.com.cotiinformatica.api_agenda.services.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    /*
        ENDPOINT para autenticação do usuário na API
     */
    @PostMapping("autenticar")
    public ResponseEntity<?> autenticar(@RequestBody @Valid AutenticarUsuarioRequest request) {
        try {
            //Executar a autenticação do usuário e obter a resposta
            var response = usuarioService.autenticarUsuario(request);
            //HTTP 200 (OK)
            return ResponseEntity.status(200).body(response);
        }
        catch (AcessoNegadoException e) {
            //HTTP 401 (UNAUTHORIZED)
            return ResponseEntity.status(401).body(e.getMessage());
        }
        catch (Exception e) {
            //HTTP 500 (INTERNAL SERVER ERROR)
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    /*
        ENDPOINT para criação de usuário na API
     */
    @PostMapping("criar")
    public ResponseEntity<?> criar(@RequestBody @Valid CriarUsuarioRequest request) {
        try {
            //Executar o cadastro do usuário e obter a resposta
            var response = usuarioService.criarUsuario(request);
            //HTTP 201 (CREATED)
            return ResponseEntity.status(201).body(response);
        }
        catch(EmailJaCadastradoException e) {
            //HTTP 409 (CONFLITO)
            return ResponseEntity.status(409).body(e.getMessage());
        }
        catch(Exception e) {
            //HTTP 500 (INTERNAL SERVER ERROR)
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }
}
