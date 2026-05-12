package br.com.escola.feiraciencias.storage.application.dto;

import java.time.LocalDateTime;

public record ArquivoInfo(
        String id,
        String nomeOriginal,
        String mimeType,
        long tamanho,
        LocalDateTime dataUpload
) {}
