package br.com.escola.feiraciencias.users.domain.model;

import br.com.escola.feiraciencias.shared.domain.enums.TipoUsuario;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Professor extends Usuario {
    private Boolean isAdm;
    private String materia;

    public Professor() {
        this.setTipoUsuario(TipoUsuario.PROFESSOR);
    }
}
