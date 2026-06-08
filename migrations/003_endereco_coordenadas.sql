-- Adiciona latitude e longitude à tabela endereco
ALTER TABLE endereco ADD COLUMN IF NOT EXISTS latitude DOUBLE PRECISION;
ALTER TABLE endereco ADD COLUMN IF NOT EXISTS longitude DOUBLE PRECISION;

-- Atualiza coordenadas para endereços existentes (região de Recife/PE)
UPDATE endereco SET latitude = -8.0476, longitude = -34.8770 WHERE id_endereco = 1;
UPDATE endereco SET latitude = -8.0476, longitude = -34.8770 WHERE id_endereco = 2;
UPDATE endereco SET latitude = -8.0632, longitude = -34.8712 WHERE id_endereco = 3;
UPDATE endereco SET latitude = -8.0500, longitude = -34.9000 WHERE id_endereco = 4;
