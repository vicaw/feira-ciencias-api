package br.com.escola.feiraciencias.storage.infrastructure.persistence.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "blobs")
@Getter
@Setter
@NoArgsConstructor
public class BlobJpaEntity {

    @Id
    @Column(name = "storage_uri", nullable = false)
    private String storageUri; // "db://<arquivoId>"

    @Column(columnDefinition = "TEXT", nullable = false)
    private String conteudo; // Base64 do arquivo
}
