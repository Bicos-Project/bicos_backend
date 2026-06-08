-- Permite duas avaliações por solicitação (uma do cliente, uma do prestador)

-- 1. Remover unique constraint existente em id_solicitacao
ALTER TABLE avaliacao DROP CONSTRAINT IF EXISTS uk_id_solicitacao;
ALTER TABLE avaliacao DROP CONSTRAINT IF EXISTS avaliacao_id_solicitacao_key;

-- 2. Adicionar coluna avaliador_tipo
ALTER TABLE avaliacao ADD COLUMN IF NOT EXISTS avaliador_tipo VARCHAR(10);

-- 3. Preencher avaliações existentes como 'CLIENTE' (default para dados antigos)
UPDATE avaliacao SET avaliador_tipo = 'CLIENTE' WHERE avaliador_tipo IS NULL;

-- 4. Tornar coluna NOT NULL
ALTER TABLE avaliacao ALTER COLUMN avaliador_tipo SET NOT NULL;

-- 5. Adicionar unique constraint composto (id_solicitacao + avaliador_tipo)
ALTER TABLE avaliacao ADD CONSTRAINT uk_solicitacao_avaliador_tipo UNIQUE (id_solicitacao, avaliador_tipo);
