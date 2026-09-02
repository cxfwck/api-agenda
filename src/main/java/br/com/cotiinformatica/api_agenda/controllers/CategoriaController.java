package br.com.cotiinformatica.api_agenda.controllers;

import br.com.cotiinformatica.api_agenda.services.CategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/categorias")
public class CategoriaController {

    @Autowired
    private CategoriaService categoriaService;

    @GetMapping("consultar")
    public ResponseEntity<?> consultar() {
        try {

            var response = categoriaService.consultarCategorias();

            //HTTP 200 (OK)
            return ResponseEntity.status(200).body(response);
        }
        catch(Exception e) {
            //HTTP 500 (INTERNAL SERVER ERROR)
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }
}
