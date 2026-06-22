package br.com.escola.feiraciencias.users.api.dto.requests;

import br.com.escola.feiraciencias.users.domain.enums.AnoEscolar;

public record AtualizarUsuarioRequest(
        String nome,
        String email,
        String matricula,
        AnoEscolar anoEscolar,
        String materia
) {}
