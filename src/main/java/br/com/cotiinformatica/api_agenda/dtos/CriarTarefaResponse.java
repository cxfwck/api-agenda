package br.com.cotiinformatica.api_agenda.dtos;

import java.time.LocalDateTime;

public record CriarTarefaResponse(
        String mensagem,
        LocalDateTime dataHoraCadastro,
        Integer tarefaId
) {
}
