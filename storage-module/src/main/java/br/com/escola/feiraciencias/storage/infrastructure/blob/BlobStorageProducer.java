package br.com.escola.feiraciencias.storage.infrastructure.blob;

import br.com.escola.feiraciencias.storage.domain.BlobStorage;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;

/**
 * Seleciona a implementação de BlobStorage ativa em runtime
 * com base na propriedade 'storage.backend'.
 *
 * Para adicionar um novo backend (ex: S3):
 *   1. Criar S3BlobStorage implements BlobStorage com @StorageBackend("s3")
 *   2. Adicionar o case "s3" abaixo
 *   3. Setar storage.backend=s3 no application.properties
 */
@ApplicationScoped
public class BlobStorageProducer {

    @ConfigProperty(name = "storage.backend", defaultValue = "disk")
    String backend;

    // Injetados pelo qualificador concreto — sem ambiguidade
    @Inject
    @StorageBackend("disk")
    BlobStorage diskBackend;

    @Inject
    @StorageBackend("database")
    BlobStorage dbBackend;

    @Produces
    @ApplicationScoped
    public BlobStorage produceBlobStorage() {
        return switch (backend.toLowerCase().trim()) {
            case "database" -> dbBackend;
            case "disk"     -> diskBackend;
            default -> throw new IllegalStateException(
                    "Valor inválido para 'storage.backend': '" + backend + "'. Use 'disk' ou 'database'."
            );
        };
    }
}
