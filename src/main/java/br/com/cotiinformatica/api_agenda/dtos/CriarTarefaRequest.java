package br.com.cotiinformatica.api_agenda.dtos;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record CriarTarefaRequest(

        @NotEmpty(message = "O nome da tarefa é obrigatório.")
        String nome,

        @NotEmpty(message = "A data de início da tarefa é obrigatória.")
        String dataInicio,

        @NotEmpty(message = "A hora de início da tarefa é obrigatória.")
        String horaInicio,

        @NotEmpty(message = "A data de fim da tarefa é obrigatória.")
        String dataFim,

        @NotEmpty(message = "A hora de fim da tarefa é obrigatória.")
        String horaFim,

        @NotEmpty(message = "A prioridade da tarefa é obrigatória.")
        String prioridade,

        @NotNull(message = "O id da categoria é obrigatório.")
        Integer categoriaId
) {
}
