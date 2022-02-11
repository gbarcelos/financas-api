create table grupo (
	id bigint not null auto_increment,
	nome varchar(60) not null,

	primary key (id)
) engine=InnoDB default charset=utf8;

create table permissao (
	id bigint not null auto_increment,
	descricao varchar(60) not null,
	nome varchar(100) not null,

	primary key (id)
) engine=InnoDB default charset=utf8;

create table grupo_permissao (
	grupo_id bigint not null,
	permissao_id bigint not null,

	primary key (grupo_id, permissao_id)
) engine=InnoDB default charset=utf8;

create table usuario (
	id bigint not null auto_increment,
	guid VARCHAR(64) NOT NULL,
	nome varchar(80) not null,
	email varchar(255) not null,
	senha varchar(255) not null,

	primary key (id)
) engine=InnoDB default charset=utf8;

create table usuario_grupo (
	usuario_id bigint not null,
	grupo_id bigint not null,

	primary key (usuario_id, grupo_id)
) engine=InnoDB default charset=utf8;

alter table grupo_permissao add constraint fk_grupo_permissao_permissao
foreign key (permissao_id) references permissao (id);

alter table grupo_permissao add constraint fk_grupo_permissao_grupo
foreign key (grupo_id) references grupo (id);

alter table usuario_grupo add constraint fk_usuario_grupo_grupo
foreign key (grupo_id) references grupo (id);

alter table usuario_grupo add constraint fk_usuario_grupo_usuario
foreign key (usuario_id) references usuario (id);

create table oauth_client_details (
  client_id varchar(255),
  resource_ids varchar(256),
  client_secret varchar(256),
  scope varchar(256),
  authorized_grant_types varchar(256),
  web_server_redirect_uri varchar(256),
  authorities varchar(256),
  access_token_validity integer,
  refresh_token_validity integer,
  additional_information varchar(4096),
  autoapprove varchar(256),

  primary key (client_id)
) engine=InnoDB default charset=utf8;

insert into grupo (id, nome) values (1, 'Administrador');

insert into permissao (id, nome, descricao) values (1, 'EDITAR_CATEGORIAS', 'Permite editar categorias');
insert into permissao (id, nome, descricao) values (2, 'CONSULTAR_USUARIOS_GRUPOS_PERMISSOES', 'Permite consultar usuários, grupos e permissões');
insert into permissao (id, nome, descricao) values (3, 'EDITAR_USUARIOS_GRUPOS_PERMISSOES', 'Permite criar ou editar usuários, grupos e permissões');

# Adiciona todas as permissoes no grupo do Administrador
insert into grupo_permissao (grupo_id, permissao_id)
select 1, id from permissao;

insert into usuario (id, guid, nome, email, senha) values
(1, 'BB0FBFB367914E87A1CD1C88533B6F99', 'Carlos da Silva', 'carlos.admin@test.com', '$2y$12$NSsM4gEOR7MKogflKR7GMeYugkttjNhAJMvFdHrBLaLp2HzlggP5W');

insert into usuario_grupo (usuario_id, grupo_id) values (1, 1);

insert into oauth_client_details (
  client_id, resource_ids, client_secret,
  scope, authorized_grant_types, web_server_redirect_uri, authorities,
  access_token_validity, refresh_token_validity, autoapprove
)
values (
  'lancamentos-web', null, '$2y$12$w3igMjsfS5XoAYuowoH3C.54vRFWlcXSHLjX7MwF990Kc2KKKh72e',
  'READ,WRITE', 'password', null, null,
  60 * 60 * 6, 60 * 24 * 60 * 60, null
);