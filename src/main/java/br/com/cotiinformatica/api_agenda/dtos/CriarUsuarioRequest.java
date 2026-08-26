package br.com.cotiinformatica.api_agenda.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CriarUsuarioRequest(

        @Size(min = 1, max = 100, message = "O nome deve conter no mínimo 1 caractere.")
        @NotEmpty(message = "O nome é obrigatório.")
        String nome,

        @Email(message = "Informe um endereço de enail válido.")
        @NotEmpty(message = "O email é obrigatório.")
        String email,

        @Pattern(
                regexp = "",
                message = "A senha deve conter pelo menos 1 letra maiúscula, " +
                        "1 letra minúscula, 1 número e 1 caractere especial."
        )
        @NotEmpty(message = "A senha é obrigatória")
        String senha


) {
}
