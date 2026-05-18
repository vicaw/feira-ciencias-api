package br.com.escola.feiraciencias.projects.api.dto.responses;

public record RegistroDiarioArquivoResponse(
    Integer id,
    String arquivoId,
    String nomeOriginal
) {}
