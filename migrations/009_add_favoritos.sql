CREATE TABLE favorito (
    id_cliente INTEGER NOT NULL REFERENCES cliente(id_cliente) ON DELETE CASCADE,
    id_prestador INTEGER NOT NULL REFERENCES prestador(id_prestador) ON DELETE CASCADE,
    PRIMARY KEY (id_cliente, id_prestador)
);
