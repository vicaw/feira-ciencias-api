package br.com.escola.feiraciencias.users.domain.model;

import br.com.escola.feiraciencias.shared.domain.enums.TipoUsuario;
import br.com.escola.feiraciencias.shared.domain.validation.DomainValidator;
import lombok.Getter;

@Getter
public class Aluno extends Usuario {
    private String matricula;
    private String anoEscolar;

    public Aluno() {
        this.setTipoUsuario(TipoUsuario.ALUNO);
    }

    /**
     * Define os dados escolares do aluno, garantindo que não sejam vazios.
     */
    public void definirDadosEscolares(String matricula, String anoEscolar) {
        DomainValidator.notBlank(matricula, "A matrícula do aluno é obrigatória.");
        DomainValidator.notBlank(anoEscolar, "O ano escolar do aluno é obrigatório.");
        
        this.matricula = matricula;
        this.anoEscolar = anoEscolar;
    }
}
