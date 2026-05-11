package br.com.escola.feiraciencias.users.api.dto.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

public record CadastrarAlunoRequest(
    @NotBlank(message = "O nome é obrigatório.")
    @Size(min = 3, max = 100, message = "O nome deve ter entre 3 e 100 caracteres.")
    String nome,

    @NotBlank(message = "O email é obrigatório.")
    @Email(message = "O formato do email é inválido.")
    String email,

    @NotBlank(message = "A senha é obrigatória.")
    @Size(min = 6, message = "A senha deve ter pelo menos 6 caracteres.")
    String senha,

    @NotBlank(message = "A matrícula é obrigatória.")
    String matricula,

    @NotBlank(message = "O ano escolar é obrigatório.")
    String anoEscolar
) {}
