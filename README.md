# UNAERP-Desafio-5-Etapa

Diego Manucci Bizzotto - 836156 <br />
Diego Simonaio Brino - 836054 <br /><br />

ATENÇÃO ABRIR COMO PROJETO NO ANDROID STUDIO A PASTA "app" NÃO A PASTA "UNAERP-Desafio-5-Etapa"

Requisitos Antes de prosseguir, certifique-se de que seu sistema atenda aos seguintes requisitos: 

Sistema operacional compatível (Windows, macOS, Linux). 
Instalação do PostgreSQL - https://www.postgresql.org/download/
Instalação do Node (v16.15.1 ou mais nova) - https://nodejs.org/en

Para executar o aplicativo, você precisará baixar e instalar o PostgreSQL em seu sistema. Siga as etapas abaixo para instalar o PostgreSQL: 

Acesse o site oficial do PostgreSQL em https://www.postgresql.org/download/ e faça o download do instalador apropriado para o seu sistema operacional. Execute o instalador baixado e siga as instruções fornecidas pelo assistente de instalação. Durante a instalação, será solicitada a definição de uma senha para o usuário do PostgreSQL. Certifique-se de inserir a senha 'admin' quando solicitado. Anote essa senha, pois você precisará dela posteriormente. Configuração do Projeto Após a instalação bem-sucedida do PostgreSQL, siga as etapas abaixo para configurar o projeto:

execute os seguintes comandos dentro do PGADMIN

CREATE DATABASE "desafio-5-etapa"
	WITH
	OWNER = postgres
	ENCODING = 'UTF8'
	LC_COLLATE = 'Portuguese_Brazil.1252'
	LC_CTYPE = 'Portuguese_Brazil.1252'
	TABLESPACE = pg_default
	CONNECTION LIMIT = -1
	IS_TEMPLATE = False;


—------------------------------------------------------------------

CREATE SEQUENCE IF NOT EXISTS public."USER_iduser_seq"
	INCREMENT 1
	START 1
	MINVALUE 1
	MAXVALUE 2147483647
	CACHE 1
	OWNED BY "USER".iduser;

ALTER SEQUENCE public."USER_iduser_seq"
	OWNER TO postgres;

—------------------------------------------------------------------

CREATE SEQUENCE IF NOT EXISTS public.announcement_idannouncement_seq
	INCREMENT 1
	START 1
	MINVALUE 1
	MAXVALUE 2147483647
	CACHE 1
	OWNED BY "ANNOUNCEMENT".idannouncement;

ALTER SEQUENCE public.announcement_idannouncement_seq
	OWNER TO postgres;

—------------------------------------------------------------------

CREATE TABLE IF NOT EXISTS public."USER"
(
	iduser integer NOT NULL DEFAULT nextval('"USER_iduser_seq"'::regclass),
	name character varying(100) COLLATE pg_catalog."default" NOT NULL,
	email character varying(100) COLLATE pg_catalog."default" NOT NULL,
	password character varying(100) COLLATE pg_catalog."default" NOT NULL,
	type character varying(100) COLLATE pg_catalog."default" NOT NULL,
	CONSTRAINT "USER_pkey" PRIMARY KEY (iduser),
	CONSTRAINT "USER_email_key" UNIQUE (email)
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public."USER"
	OWNER to postgres;

—------------------------------------------------------------------

-- Table: public.ANNOUNCEMENT

-- DROP TABLE IF EXISTS public."ANNOUNCEMENT";

CREATE TABLE IF NOT EXISTS public."ANNOUNCEMENT"
(
	idannouncement integer NOT NULL DEFAULT nextval('announcement_idannouncement_seq'::regclass),
	area character varying(1000) COLLATE pg_catalog."default" NOT NULL,
	remuneration numeric NOT NULL,
	email character varying(1000) COLLATE pg_catalog."default" NOT NULL,
	phone bigint NOT NULL,
	showadvertiser character varying(1) COLLATE pg_catalog."default" NOT NULL,
	startdate timestamp without time zone NOT NULL,
	enddate timestamp without time zone NOT NULL,
	idadvertiser integer,
	description character varying(1000) COLLATE pg_catalog."default",
	city character varying(1000) COLLATE pg_catalog."default",
	CONSTRAINT announcement_pkey PRIMARY KEY (idannouncement),
	CONSTRAINT fk_announcement_advertiser FOREIGN KEY (idadvertiser)
    	REFERENCES public."USER" (iduser) MATCH SIMPLE
    	ON UPDATE NO ACTION
    	ON DELETE NO ACTION
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public."ANNOUNCEMENT"
	OWNER to postgres;

—------------------------------------------------------------------

 

Para executar a api, clone o projeto e execute o comando “npm install” e depois “npm run start” na raiz do projeto, após isso a api estará rodando na porta 8888
