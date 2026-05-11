-- =============================================================
--  Storage Module - V1.3.0
--  Gerenciamento de arquivos e anexos da plataforma.
--  Os outros módulos referenciam arquivos apenas pelo 'arquivo_id' (UUID),
--  sem conhecer como o conteúdo é armazenado internamente.
-- =============================================================

CREATE TABLE arquivos (
    id          UUID PRIMARY KEY DEFAULT gen_random_uuid(), -- Identificador único retornado aos outros módulos
    nome_original VARCHAR(255) NOT NULL,                    -- Nome original do arquivo (ex: "foto.png")
    mime_type   VARCHAR(100) NOT NULL,                      -- Tipo do arquivo (ex: "image/png", "application/pdf")
    tamanho     BIGINT NOT NULL,                            -- Tamanho em bytes
    conteudo    TEXT NOT NULL,                              -- Conteúdo em Base64
    criado_por_id INTEGER NOT NULL,                         -- ID do usuário que fez o upload (ref. sem FK para desacoplar)
    data_upload TIMESTAMP NOT NULL DEFAULT NOW()
);
