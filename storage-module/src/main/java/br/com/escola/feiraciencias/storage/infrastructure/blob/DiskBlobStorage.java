package br.com.escola.feiraciencias.storage.infrastructure.blob;

import br.com.escola.feiraciencias.shared.domain.exceptions.BusinessRuleException;
import br.com.escola.feiraciencias.storage.domain.BlobStorage;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Backend de disco para armazenamento binário.
 * Ativado pelo BlobStorageProducer quando storage.backend=disk.
 */
@ApplicationScoped
@StorageBackend("disk")
public class DiskBlobStorage implements BlobStorage {

    private static final String URI_PREFIX = "file://";

    @ConfigProperty(name = "storage.upload.dir", defaultValue = "./uploads")
    String uploadDir;

    @Override
    public String salvar(String arquivoId, String nomeOriginal, byte[] conteudo) {
        try {
            Path dir = Paths.get(uploadDir);
            Files.createDirectories(dir);

            Path destino = dir.resolve(arquivoId);
            Files.write(destino, conteudo);

            return URI_PREFIX + destino.toAbsolutePath().toString();
        } catch (IOException e) {
            throw new RuntimeException("Falha ao salvar arquivo em disco: " + arquivoId, e);
        }
    }

    @Override
    public byte[] recuperar(String storageUri) {
        try {
            Path caminho = resolverCaminho(storageUri);
            if (!Files.exists(caminho)) {
                throw new BusinessRuleException("Arquivo não encontrado no disco: " + storageUri);
            }
            return Files.readAllBytes(caminho);
        } catch (IOException e) {
            throw new RuntimeException("Falha ao ler arquivo do disco: " + storageUri, e);
        }
    }

    @Override
    public void deletar(String storageUri) {
        try {
            Path caminho = resolverCaminho(storageUri);
            Files.deleteIfExists(caminho);
        } catch (IOException e) {
            throw new RuntimeException("Falha ao deletar arquivo do disco: " + storageUri, e);
        }
    }

    private Path resolverCaminho(String storageUri) {
        String path = storageUri.startsWith(URI_PREFIX)
                ? storageUri.substring(URI_PREFIX.length())
                : storageUri;
        return Paths.get(path);
    }
}
