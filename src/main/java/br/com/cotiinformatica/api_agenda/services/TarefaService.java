package br.com.cotiinformatica.api_agenda.services;

import br.com.cotiinformatica.api_agenda.dtos.ConsultarTarefaResponse;
import br.com.cotiinformatica.api_agenda.dtos.CriarTarefaRequest;
import br.com.cotiinformatica.api_agenda.dtos.CriarTarefaResponse;
import br.com.cotiinformatica.api_agenda.dtos.EditarTarefaRequest;
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
import java.util.List;

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

    public Tarefa obterTarefaPorId(Integer tarefaId) {
        return tarefaRepository.findById(tarefaId)
                .orElseThrow(() -> new RegistroNaoEncontradoException("Tarefa não encontrada. Verifique o ID informado."));
    }

    public ConsultarTarefaResponse obterTarefaPorIdResponse(Integer tarefaId) {
        return mapearParaConsulta(obterTarefaPorId(tarefaId));
    }

    public List<Tarefa> consultarTarefas(LocalDate dataInicio, LocalDate dataFim) {
        if (dataInicio == null || dataFim == null) {
            throw new IllegalArgumentException("As datas de início e fim do período são obrigatórias.");
        }

        if (dataInicio.isAfter(dataFim)) {
            throw new IllegalArgumentException("A data de início não pode ser maior que a data de fim.");
        }

        return tarefaRepository.consultarPorPeriodo(dataInicio, dataFim);
    }

    public List<Tarefa> consultarTarefas(String dataInicio, String dataFim) {
        return consultarTarefas(LocalDate.parse(dataInicio), LocalDate.parse(dataFim));
    }

    public List<Tarefa> consultarTarefasPorPeriodo(LocalDate dataInicio, LocalDate dataFim) {
        return consultarTarefas(dataInicio, dataFim);
    }

    public List<ConsultarTarefaResponse> consultarTarefasPorPeriodoResponse(LocalDate dataInicio, LocalDate dataFim) {
        return consultarTarefas(dataInicio, dataFim).stream()
                .map(this::mapearParaConsulta)
                .toList();
    }

    public Tarefa editarTarefa(Integer tarefaId, CriarTarefaRequest request) {
        var tarefa = obterTarefaPorId(tarefaId);

        var categoria = categoriaRepository.findById(request.categoriaId())
                .orElseThrow(() -> new RegistroNaoEncontradoException("Categoria não encontrada. Verifique o ID informado."));

        tarefa.setNome(request.nome());
        tarefa.setDataInicio(LocalDate.parse(request.dataInicio()));
        tarefa.setDataFim(LocalDate.parse(request.dataFim()));
        tarefa.setHoraInicio(LocalTime.parse(request.horaInicio()));
        tarefa.setHoraFim(LocalTime.parse(request.horaFim()));
        tarefa.setPrioridade(Prioridade.valueOf(request.prioridade()));
        tarefa.setCategoria(categoria);

        return tarefaRepository.save(tarefa);
    }

    public Tarefa editarTarefa(Integer tarefaId, EditarTarefaRequest request) {
        return editarTarefa(tarefaId, new CriarTarefaRequest(
                request.nome(),
                request.dataInicio(),
                request.horaInicio(),
                request.dataFim(),
                request.horaFim(),
                request.prioridade(),
                request.categoriaId()
        ));
    }

    public Tarefa editarTarefa(Tarefa tarefaAtualizada) {
        var tarefa = obterTarefaPorId(tarefaAtualizada.getId());
        tarefa.setNome(tarefaAtualizada.getNome());
        tarefa.setDataInicio(tarefaAtualizada.getDataInicio());
        tarefa.setDataFim(tarefaAtualizada.getDataFim());
        tarefa.setHoraInicio(tarefaAtualizada.getHoraInicio());
        tarefa.setHoraFim(tarefaAtualizada.getHoraFim());
        tarefa.setPrioridade(tarefaAtualizada.getPrioridade());
        tarefa.setCategoria(tarefaAtualizada.getCategoria());
        tarefa.setUsuario(tarefaAtualizada.getUsuario());

        return tarefaRepository.save(tarefa);
    }

    public void excluirTarefa(Integer tarefaId) {
        var tarefa = obterTarefaPorId(tarefaId);
        tarefaRepository.delete(tarefa);
    }

    private ConsultarTarefaResponse mapearParaConsulta(Tarefa tarefa) {
        return new ConsultarTarefaResponse(
                tarefa.getId(),
                tarefa.getNome(),
                tarefa.getDataInicio(),
                tarefa.getHoraInicio(),
                tarefa.getDataFim(),
                tarefa.getHoraFim(),
                tarefa.getPrioridade() != null ? tarefa.getPrioridade().name() : null,
                tarefa.getCategoria() != null ? tarefa.getCategoria().getId() : null,
                tarefa.getUsuario() != null ? tarefa.getUsuario().getId() : null
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
