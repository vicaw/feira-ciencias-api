package br.com.escola.feiraciencias.storage.infrastructure.blob;

import br.com.escola.feiraciencias.shared.domain.exceptions.BusinessRuleException;
import br.com.escola.feiraciencias.storage.domain.BlobStorage;
import br.com.escola.feiraciencias.storage.infrastructure.persistence.entities.BlobJpaEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.util.Base64;

/**
 * Backend de banco de dados para armazenamento binário.
 * Ativado pelo BlobStorageProducer quando storage.backend=database.
 */
@ApplicationScoped
@StorageBackend("database")
public class DatabaseBlobStorage implements BlobStorage,
        PanacheRepositoryBase<BlobJpaEntity, String> {

    private static final String URI_PREFIX = "db://";

    @Override
    @Transactional
    public String salvar(String arquivoId, String nomeOriginal, byte[] conteudo) {
        String uri = URI_PREFIX + arquivoId;
        String base64 = Base64.getEncoder().encodeToString(conteudo);

        BlobJpaEntity blob = new BlobJpaEntity();
        blob.setStorageUri(uri);
        blob.setConteudo(base64);
        persist(blob);

        return uri;
    }

    @Override
    public byte[] recuperar(String storageUri) {
        return findByIdOptional(storageUri)
                .map(b -> Base64.getDecoder().decode(b.getConteudo()))
                .orElseThrow(() -> new BusinessRuleException("Blob não encontrado: " + storageUri));
    }

    @Override
    @Transactional
    public void deletar(String storageUri) {
        deleteById(storageUri);
    }
}
