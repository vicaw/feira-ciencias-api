-- =============================================================
--  Storage Module - V1.3.0 (Consolidada)
--  Gerenciamento de arquivos e anexos da plataforma.
-- =============================================================

-- Tabela de Metadados: Guarda informações sobre o arquivo
CREATE TABLE arquivos (
    id            UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    nome_original VARCHAR(255) NOT NULL,
    mime_type     VARCHAR(100) NOT NULL,
    tamanho       BIGINT NOT NULL,
    storage_uri   VARCHAR(500) NOT NULL, -- URI opaco: "file://...", "db://..."
    criado_por_id INTEGER NOT NULL,
    data_upload   TIMESTAMP NOT NULL DEFAULT NOW()
);

-- Tabela de Blobs: Usada apenas quando o backend é 'database'
-- Guarda o conteúdo binário em Base64
CREATE TABLE blobs (
    storage_uri  VARCHAR(500) PRIMARY KEY, -- "db://<arquivoId>"
    conteudo     TEXT         NOT NULL     -- Base64
);
