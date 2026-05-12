package br.com.escola.feiraciencias.storage.domain;

/**
 * Abstração de baixo nível para persistência binária de arquivos.
 * O StorageUseCase usa esta interface — nunca as implementações diretamente.
 *
 * Trocar o backend (banco, disco, S3) = criar uma nova implementação desta interface
 * e ajustar a propriedade 'storage.backend' no application.properties.
 */
public interface BlobStorage {

    /**
     * Persiste os bytes do arquivo e retorna um URI opaco que identifica
     * onde o conteúdo foi armazenado (ex: "file:///uploads/abc.png" ou "db://uuid").
     */
    String salvar(String arquivoId, String nomeOriginal, byte[] conteudo);

    /**
     * Recupera os bytes brutos a partir do URI opaco retornado por {@link #salvar}.
     */
    byte[] recuperar(String storageUri);

    /**
     * Remove o conteúdo binário associado ao URI.
     */
    void deletar(String storageUri);
}
