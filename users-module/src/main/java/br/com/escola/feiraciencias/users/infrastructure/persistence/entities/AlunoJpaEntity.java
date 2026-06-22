package br.com.escola.feiraciencias.users.infrastructure.persistence.entities;

import br.com.escola.feiraciencias.users.domain.enums.AnoEscolar;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "alunos")
@Getter
@Setter
@NoArgsConstructor
public class AlunoJpaEntity extends UsuarioJpaEntity {
    @Column(nullable = false, unique = true, length = 50)
    private String matricula;

    @Enumerated(EnumType.STRING)
    @Column(name = "ano_escolar", nullable = false, length = 30)
    private AnoEscolar anoEscolar;

    // Getters and Setters omitted
}
