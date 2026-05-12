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

    @Column(name = "arquivo_id", nullable = false)
    private String arquivoId; // UUID retornado pelo storage-module

    @Column(name = "nome_original", nullable = false)
    private String nomeOriginal; // Cópia do nome para exibição rápida

    @Column(name = "registro_diario_id", nullable = false)
    private Integer registroDiarioId;
}
