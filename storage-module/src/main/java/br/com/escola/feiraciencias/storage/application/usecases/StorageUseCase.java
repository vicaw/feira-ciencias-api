package br.com.escola.feiraciencias.storage.application.usecases;

public interface StorageUseCase {
    String salvarArquivo(String nomeArquivo, String base64Content);
    String recuperarArquivo(String idArquivo);
}
