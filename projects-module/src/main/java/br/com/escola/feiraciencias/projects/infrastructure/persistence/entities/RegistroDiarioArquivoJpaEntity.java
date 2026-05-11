package br.com.escola.feiraciencias.projects.infrastructure.persistence.entities;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "registro_diario_arquivos")
@Getter
@Setter
@NoArgsConstructor
public class RegistroDiarioArquivoJpaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String base64;

    @Column(name = "registro_diario_id", nullable = false)
    private Integer registroDiarioId;
}
