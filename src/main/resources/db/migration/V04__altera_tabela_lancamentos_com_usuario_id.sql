INSERT INTO usuario (id, guid, nome, email, senha) VALUES
(2, '49E212211355438F88ACF6F859A769C0', 'Alberto da Silva', 'alberto.silva@test.com', '$2y$12$NSsM4gEOR7MKogflKR7GMeYugkttjNhAJMvFdHrBLaLp2HzlggP5W');

ALTER TABLE lancamento ADD COLUMN usuario_id BIGINT;

ALTER TABLE lancamento ADD CONSTRAINT fk_lancamento_usuario
FOREIGN KEY (usuario_id) REFERENCES usuario (id);

UPDATE lancamento SET usuario_id = 2;

ALTER TABLE lancamento MODIFY COLUMN usuario_id BIGINT NOT NULL;