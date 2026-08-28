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
    public class UsuarioController{
    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("autenticar")
    public ResponseEntity<?> autenticar(@RequestBody @Valid AutenticarUsuarioRequest request){
     try{
         var response = usuarioService.autenticarUsuario(request);
         return ResponseEntity.status(200).body(response);
     }

     catch (AcessoNegadoException e){
         return ResponseEntity.status(401).body(e.getMessage());

     }
     catch (Exception e){
         return ResponseEntity.status(500).body(e.getMessage());
     }

    }

    @PostMapping("criar")
    public ResponseEntity<?> criar(@RequestBody @Valid CriarUsuarioRequest request){
        try{
            var response = usuarioService.criarUsuario(request);
            return ResponseEntity.status(201).body(response);
        }
        catch(EmailJaCadastradoException e){
            return ResponseEntity.status(409).body(e.getMessage());
        }
        catch (Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }
}

