package br.com.escola.feiraciencias.users.domain.model;

import br.com.escola.feiraciencias.shared.domain.enums.TipoUsuario;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Aluno extends Usuario {
    private String matricula;
    private String anoEscolar;

    public Aluno() {
        this.setTipoUsuario(TipoUsuario.ALUNO);
    }
}
