package br.com.escola.feiraciencias.storage.infrastructure.persistence.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "arquivos")
@Getter
@Setter
@NoArgsConstructor
public class ArquivoJpaEntity {

    @Id
    @Column(columnDefinition = "uuid", updatable = false, nullable = false)
    private String id;

    @Column(name = "nome_original", nullable = false)
    private String nomeOriginal;

    @Column(name = "mime_type", nullable = false)
    private String mimeType;

    @Column(nullable = false)
    private Long tamanho;

    @Column(name = "storage_uri", nullable = false, length = 500)
    private String storageUri; // URI opaco: "file:///...", "db://...", "s3://..."

    @Column(name = "criado_por_id", nullable = false)
    private Integer criadoPorId;

    @Column(name = "data_upload", nullable = false)
    private LocalDateTime dataUpload;
}
