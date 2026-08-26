package br.com.cotiinformatica.api_agenda.dtos;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record CriarUsuarioResponse(
        String mensagem,
        LocalDateTime dataHora,
        Integer id,
        String nome,
        String email


) {
}
