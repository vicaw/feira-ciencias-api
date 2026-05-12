package br.com.escola.feiraciencias.storage.application.dto;

public record ArquivoUpload(
        String nomeOriginal,
        String mimeType,
        long tamanho,
        byte[] conteudo,
        Integer criadoPorId
) {}
