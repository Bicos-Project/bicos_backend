-- Cria endereços para os prestadores que não têm
INSERT INTO endereco (cep, logradouro, numero, complemento, bairro, cidade, estado, latitude, longitude)
VALUES
  ('52010-010', 'Rua da Aurora', '500', 'Apto 201', 'Boa Vista', 'Recife', 'PE', -8.0564, -34.8789),
  ('51020-010', 'Av. Boa Viagem', '1200', 'Loja 5', 'Boa Viagem', 'Recife', 'PE', -8.1205, -34.8985),
  ('52020-010', 'Rua do Hospício', '350', 'Sala 4', 'Boa Vista', 'Recife', 'PE', -8.0621, -34.8823),
  ('51111-010', 'Av. Conselheiro Aguiar', '800', 'Bloco B', 'Boa Viagem', 'Recife', 'PE', -8.1312, -34.9012),
  ('52030-010', 'Rua da Hora', '200', NULL, 'Espinheiro', 'Recife', 'PE', -8.0445, -34.8910),
  ('52110-010', 'Rua Real da Torre', '100', 'Casa', 'Madalena', 'Recife', 'PE', -8.0400, -34.9050),
  ('52041-010', 'Av. Norte', '1500', 'Box 12', 'Tamarineira', 'Recife', 'PE', -8.0300, -34.9150),
  ('52040-010', 'Rua Padre Inglês', '400', 'Fundos', 'Boa Vista', 'Recife', 'PE', -8.0670, -34.8780);

-- Atualiza os prestadores para apontar pros endereços criados
UPDATE prestador SET id_endereco = 5 WHERE id_prestador = 2;
UPDATE prestador SET id_endereco = 6 WHERE id_prestador = 3;
UPDATE prestador SET id_endereco = 7 WHERE id_prestador = 4;
UPDATE prestador SET id_endereco = 8 WHERE id_prestador = 5;
UPDATE prestador SET id_endereco = 9 WHERE id_prestador = 6;
UPDATE prestador SET id_endereco = 10 WHERE id_prestador = 7;
UPDATE prestador SET id_endereco = 11 WHERE id_prestador = 8;
UPDATE prestador SET id_endereco = 12 WHERE id_prestador = 9;
