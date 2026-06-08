-- Remove Anuncio entity, move categoria directly to Prestador
-- and link Solicitacao directly to Prestador

BEGIN;

-- Step 1: Add categoria FK to prestador
ALTER TABLE prestador ADD COLUMN id_categoria INTEGER REFERENCES categoria(id_categoria);

-- Step 2: Copy categoria from existing anuncios
UPDATE prestador p
SET id_categoria = sub.id_categoria
FROM (
    SELECT DISTINCT ON (a.id_prestador) a.id_prestador, a.id_categoria
    FROM anuncio a
    WHERE a.status = 'ativo'
) sub
WHERE p.id_prestador = sub.id_prestador;

-- Step 3: Add prestador FK to solicitacao
ALTER TABLE solicitacao ADD COLUMN id_prestador INTEGER REFERENCES prestador(id_prestador);

-- Step 4: Copy prestador from anuncio to solicitacao
UPDATE solicitacao s
SET id_prestador = a.id_prestador
FROM anuncio a
WHERE a.id_anuncio = s.id_anuncio;

-- Step 5: Make id_prestador NOT NULL
ALTER TABLE solicitacao ALTER COLUMN id_prestador SET NOT NULL;

-- Step 6: Drop old FK and column
ALTER TABLE solicitacao DROP COLUMN id_anuncio;

-- Step 7: Drop anuncio_foto and anuncio tables
DROP TABLE IF EXISTS anuncio_foto;
DROP TABLE IF EXISTS anuncio;

-- Step 8: Drop sequence
DROP SEQUENCE IF EXISTS anuncio_id_anuncio_seq;

COMMIT;
