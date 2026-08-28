package br.com.cotiinformatica.api_agenda.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record AutenticarUsuarioRequest(

        @Email(message = "Informe um endereço de email válido.")
        @NotEmpty(message = "O campo email é obrigatório")
        String email,

        @Size(min = 8, message = "Informe a senha com pelo menos 8 caracteres.")
        @NotEmpty(message = "O campo senha é obrigatório")
        String senha

) {
}
