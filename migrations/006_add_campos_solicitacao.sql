BEGIN;

ALTER TABLE solicitacao ADD COLUMN data_estimada DATE;
ALTER TABLE solicitacao ADD COLUMN valor_sugerido DECIMAL(10,2);

COMMIT;
