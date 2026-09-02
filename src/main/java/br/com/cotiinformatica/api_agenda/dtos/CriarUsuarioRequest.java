package br.com.cotiinformatica.api_agenda.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CriarUsuarioRequest(

        @Size(min = 8, message = "O nome do usuário deve ter pelo menos 8 caracteres.")
        @NotEmpty(message = "O nome do usuário é obrigatório.")
        String nome,

        @Email(message = "Informe um endereço de email válido.")
        @NotEmpty(message = "O email do usuário é obrigatório.")
        String email,

        @Pattern(
                regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
                message = "A senha deve ter pelo menos 1 letra minúscula, 1 letra maiúscula, 1 número, 1 caractere especial e no mínimo 8 caracteres."
        )
        @NotEmpty(message = "A senha do usuário é obrigatória.")
        String senha
) {
}
