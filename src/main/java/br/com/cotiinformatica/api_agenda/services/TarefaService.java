package br.com.cotiinformatica.api_agenda.services;

import br.com.cotiinformatica.api_agenda.dtos.CriarTarefaRequest;
import br.com.cotiinformatica.api_agenda.dtos.CriarTarefaResponse;
import br.com.cotiinformatica.api_agenda.entities.Tarefa;
import br.com.cotiinformatica.api_agenda.enums.Prioridade;
import br.com.cotiinformatica.api_agenda.exceptions.RegistroNaoEncontradoException;
import br.com.cotiinformatica.api_agenda.repositories.CategoriaRepository;
import br.com.cotiinformatica.api_agenda.repositories.TarefaRepository;
import br.com.cotiinformatica.api_agenda.repositories.UsuarioRepository;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Service
public class TarefaService {

    @Autowired
    private TarefaRepository tarefaRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public CriarTarefaResponse criarTarefa(CriarTarefaRequest request, HttpServletRequest http) throws Exception {

        //Verificar se a categoria informada existe no banco de dados.
        var categoria = categoriaRepository.findById(request.categoriaId())
                .orElseThrow(() -> new RegistroNaoEncontradoException
                        ("Categoria não encontrada. Verifique o ID informado."));

        //Capturar os dados da tarefa
        var tarefa = new Tarefa();

        tarefa.setNome(request.nome());
        tarefa.setDataInicio(LocalDate.parse(request.dataInicio()));
        tarefa.setDataFim(LocalDate.parse(request.dataFim()));
        tarefa.setHoraInicio(LocalTime.parse(request.horaInicio()));
        tarefa.setHoraFim(LocalTime.parse(request.horaFim()));
        tarefa.setPrioridade(Prioridade.valueOf(request.prioridade()));
        tarefa.setCategoria(categoria);

        //Associar a tarefa ao usuário autenticado
        var email = extrairEmailUsuario(http); //Extraindo o email do usuário gravado no TOKEN JWT
        var usuario = usuarioRepository.findByEmail(email); //Buscando o usuário no banco de dados

        //Associar a tarefa ao usuário
        tarefa.setUsuario(usuario);

        //Salvar a tarefa no banco de dados
        tarefaRepository.save(tarefa);

        //Retornar a resposta
        return new CriarTarefaResponse(
                "Tarefa cadastrada com sucesso",
                LocalDateTime.now(),
                tarefa.getId()
        );
    }


    /*
        Método para extrair o email do usuário gravado no TOKEN JWT
     */
    private String extrairEmailUsuario(HttpServletRequest request) throws Exception {
        //Capturar o token enviado no cabeçalho da requisição
        var authorization = request.getHeader("Authorization");

        //Verificar se o token foi enviado
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            throw new Exception("Token de autenticação não informado.");
        }

        //Remover o prefixo "Bearer "
        var token = authorization.substring(7);

        //Mesma chave utilizada para gerar o token
        var chaveAssinatura = "e2b38b1e-123a-4276-870f-32706418de8c";

        //Decodificar e validar o token
        var claims = Jwts.parser()
                .setSigningKey(chaveAssinatura)
                .parseClaimsJws(token)
                .getBody();

        //Retornar o email gravado no Subject do token
        return claims.getSubject();
    }

}
