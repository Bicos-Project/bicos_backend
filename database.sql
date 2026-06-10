-- =====================================================
-- Bicos Database - Script completo de criação e seed
-- Execute uma única vez no banco "bicos"
-- =====================================================

-- Categorias
CREATE SEQUENCE categoria_id_categoria_seq;

CREATE TABLE categoria (
    id_categoria INTEGER PRIMARY KEY DEFAULT nextval('categoria_id_categoria_seq'),
    nome VARCHAR(100) NOT NULL,
    descricao VARCHAR(255)
);

-- Endereços
CREATE SEQUENCE endereco_id_endereco_seq;

CREATE TABLE endereco (
    id_endereco INTEGER PRIMARY KEY DEFAULT nextval('endereco_id_endereco_seq'),
    cep VARCHAR(9) NOT NULL,
    logradouro VARCHAR(150),
    numero VARCHAR(10) NOT NULL,
    complemento VARCHAR(100),
    bairro VARCHAR(100),
    cidade VARCHAR(100),
    estado VARCHAR(2),
    latitude DOUBLE PRECISION,
    longitude DOUBLE PRECISION
);

-- Prestadores
CREATE SEQUENCE prestador_id_prestador_seq;

CREATE TABLE prestador (
    id_prestador INTEGER PRIMARY KEY DEFAULT nextval('prestador_id_prestador_seq'),
    nome VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    senha_hash VARCHAR(255) NOT NULL,
    cpf VARCHAR(14) UNIQUE,
    telefone VARCHAR(20),
    descricao VARCHAR(255),
    especialidade VARCHAR(100),
    avaliacao NUMERIC(3,1),
    id_categoria INTEGER REFERENCES categoria(id_categoria),
    id_endereco INTEGER REFERENCES endereco(id_endereco)
);

-- Fotos dos prestadores
CREATE SEQUENCE prestador_foto_id_foto_seq;

CREATE TABLE prestador_foto (
    id_foto INTEGER PRIMARY KEY DEFAULT nextval('prestador_foto_id_foto_seq'),
    id_prestador INTEGER NOT NULL REFERENCES prestador(id_prestador),
    url VARCHAR(255) NOT NULL,
    ordem INTEGER
);

-- Clientes
CREATE SEQUENCE cliente_id_cliente_seq;

CREATE TABLE cliente (
    id_cliente INTEGER PRIMARY KEY DEFAULT nextval('cliente_id_cliente_seq'),
    nome VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    senha_hash VARCHAR(255) NOT NULL,
    telefone VARCHAR(20),
    cpf VARCHAR(14) UNIQUE,
    foto_url VARCHAR(255),
    avaliacao NUMERIC(3,1) DEFAULT 0.0,
    id_endereco INTEGER REFERENCES endereco(id_endereco)
);

-- Solicitações
CREATE SEQUENCE solicitacao_id_solicitacao_seq;

CREATE TABLE solicitacao (
    id_solicitacao INTEGER PRIMARY KEY DEFAULT nextval('solicitacao_id_solicitacao_seq'),
    descricao VARCHAR(500),
    data_solicitacao DATE,
    data_estimada DATE,
    valor_sugerido DECIMAL(10,2),
    status VARCHAR(30) NOT NULL,
    prestador_confirmou_pagamento BOOLEAN NOT NULL DEFAULT false,
    cliente_confirmou_pagamento BOOLEAN NOT NULL DEFAULT false,
    id_cliente INTEGER NOT NULL REFERENCES cliente(id_cliente),
    id_prestador INTEGER NOT NULL REFERENCES prestador(id_prestador)
);

-- Avaliações
CREATE SEQUENCE avaliacao_id_avaliacao_seq;

CREATE TABLE avaliacao (
    id_avaliacao INTEGER PRIMARY KEY DEFAULT nextval('avaliacao_id_avaliacao_seq'),
    nota INTEGER NOT NULL,
    comentario VARCHAR(500),
    avaliador_tipo VARCHAR(10) NOT NULL,
    id_solicitacao INTEGER NOT NULL REFERENCES solicitacao(id_solicitacao),
    UNIQUE (id_solicitacao, avaliador_tipo)
);

-- Mensagens
CREATE SEQUENCE mensagem_id_mensagem_seq;

CREATE TABLE mensagem (
    id_mensagem INTEGER PRIMARY KEY DEFAULT nextval('mensagem_id_mensagem_seq'),
    id_solicitacao INTEGER NOT NULL,
    remetente_id INTEGER NOT NULL,
    tipo_remetente VARCHAR(20) NOT NULL,
    texto TEXT NOT NULL,
    data_hora TIMESTAMP NOT NULL,
    imagem_url VARCHAR(255)
);

-- Favoritos
CREATE TABLE favorito (
    id_cliente INTEGER NOT NULL REFERENCES cliente(id_cliente) ON DELETE CASCADE,
    id_prestador INTEGER NOT NULL REFERENCES prestador(id_prestador) ON DELETE CASCADE,
    PRIMARY KEY (id_cliente, id_prestador)
);

-- =====================================================
-- Seed: Categorias
-- =====================================================
INSERT INTO categoria (nome, descricao) VALUES
    ('Beleza e Estética', 'Salões, barbearias, maquiagem e cuidados pessoais'),
    ('Serviços Domésticos', 'Faxina, passar roupa, organização e limpeza'),
    ('Reparos Rápidos', 'Pequenos consertos, montagem de móveis e reparos em geral'),
    ('Alimentação', 'Cozinheiros, confeiteiros e preparo de refeições'),
    ('Obras e Reformas', 'Pedreiros, pintores, eletricistas e encanadores'),
    ('Logística Local', 'Entregas, fretes e transporte de objetos'),
    ('Manutenção Eletrônica', 'Conserto de celulares, computadores e eletrônicos'),
    ('Cuidadores', 'Cuidado de idosos, crianças e pets');
