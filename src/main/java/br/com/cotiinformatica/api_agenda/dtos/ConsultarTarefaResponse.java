package br.com.cotiinformatica.api_agenda.dtos;

import java.time.LocalDate;
import java.time.LocalTime;

public record ConsultarTarefaResponse(
        Integer id,
        String nome,
        LocalDate dataInicio,
        LocalTime horaInicio,
        LocalDate dataFim,
        LocalTime horaFim,
        String prioridade,
        Integer categoriaId,
        Integer usuarioId
) {
}
