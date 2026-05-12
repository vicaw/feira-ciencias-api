package br.com.escola.feiraciencias.storage.application.usecases;

import br.com.escola.feiraciencias.shared.domain.exceptions.BusinessRuleException;
import br.com.escola.feiraciencias.storage.application.dto.ArquivoInfo;
import br.com.escola.feiraciencias.storage.application.dto.ArquivoUpload;
import br.com.escola.feiraciencias.storage.domain.BlobStorage;
import br.com.escola.feiraciencias.storage.infrastructure.persistence.entities.ArquivoJpaEntity;
import br.com.escola.feiraciencias.storage.infrastructure.persistence.repositories.ArquivoPanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.Base64;
import java.util.UUID;

@ApplicationScoped
public class StorageUseCase {

    @Inject
    BlobStorage blobStorage;

    @Inject
    ArquivoPanacheRepository arquivoRepository;

    @Transactional
    public ArquivoInfo salvarArquivo(ArquivoUpload upload) {
        String arquivoId = UUID.randomUUID().toString();

        String storageUri = blobStorage.salvar(arquivoId, upload.nomeOriginal(), upload.conteudo());

        ArquivoJpaEntity entity = new ArquivoJpaEntity();
        entity.setId(arquivoId);
        entity.setNomeOriginal(upload.nomeOriginal());
        entity.setMimeType(upload.mimeType());
        entity.setTamanho(upload.tamanho());
        entity.setStorageUri(storageUri);
        entity.setCriadoPorId(upload.criadoPorId());
        entity.setDataUpload(LocalDateTime.now());

        arquivoRepository.persist(entity);

        return toInfo(entity);
    }

    public ArquivoInfo recuperarMetadados(String arquivoId) {
        return arquivoRepository.findByIdOptional(arquivoId)
                .map(this::toInfo)
                .orElseThrow(() -> new BusinessRuleException("Arquivo não encontrado: " + arquivoId));
    }

    public String recuperarBase64(String arquivoId) {
        ArquivoJpaEntity entity = arquivoRepository.findByIdOptional(arquivoId)
                .orElseThrow(() -> new BusinessRuleException("Arquivo não encontrado: " + arquivoId));

        byte[] bytes = blobStorage.recuperar(entity.getStorageUri());
        return Base64.getEncoder().encodeToString(bytes);
    }

    private ArquivoInfo toInfo(ArquivoJpaEntity e) {
        return new ArquivoInfo(e.getId(), e.getNomeOriginal(), e.getMimeType(), e.getTamanho(), e.getDataUpload());
    }
}
