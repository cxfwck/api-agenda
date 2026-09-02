package br.com.cotiinformatica.api_agenda.services;

import br.com.cotiinformatica.api_agenda.dtos.ConsultarCategoriaResponse;
import br.com.cotiinformatica.api_agenda.repositories.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoriaService {

    @Autowired
    private CategoriaRepository categoriaRepository;

    /*
        Método para consultar todas as categorias cadastradas
        na tabela do banco de dados e retorna-la usando o DTO
     */
    public List<ConsultarCategoriaResponse> consultarCategorias() throws Exception {

        //Consultar todas as categorias no banco de dados
        var categorias = categoriaRepository.findAll();

        //Copiar os dados da lista de 'Categoria' para uma lista de 'ConsultarCategoriaResponse'
        var response = categorias.stream()
                .map(categoria -> {
                    var dto = new ConsultarCategoriaResponse(
                            categoria.getId(),
                            categoria.getNome()
                    );
                    return dto;
                }).toList();

        return response;
    }
}
