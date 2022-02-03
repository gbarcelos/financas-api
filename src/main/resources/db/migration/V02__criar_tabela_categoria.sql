CREATE TABLE categoria (
	id BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
	descricao VARCHAR(30) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO categoria (id, descricao) VALUES
(1, 'Outras'),
(2, 'Alimentação'),
(3, 'Saúde'),
(4, 'Moradia'),
(5, 'Transporte'),
(6, 'Educação'),
(7, 'Lazer'),
(8, 'Imprevistos');

ALTER TABLE lancamento ADD COLUMN categoria_id BIGINT;

ALTER TABLE lancamento ADD CONSTRAINT fk_lancamento_categoria
FOREIGN KEY (categoria_id) REFERENCES categoria (id);

update lancamento set categoria_id = 1 where tipo = 'DESPESA';