package br.com.escola.feiraciencias.users.api.dto.requests;

import br.com.escola.feiraciencias.users.domain.enums.AnoEscolar;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record GerarConviteAlunoRequest(
    @NotBlank(message = "O nome é obrigatório.")
    @Size(min = 3, max = 100, message = "O nome deve ter entre 3 e 100 caracteres.")
    String nome,

    @NotBlank(message = "A matrícula é obrigatória.")
    String matricula,

    @NotNull(message = "O ano escolar é obrigatório.")
    AnoEscolar anoEscolar
) {}
