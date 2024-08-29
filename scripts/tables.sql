CREATE TABLE vinhos.usuarios (
	nome VARCHAR(255) NOT NULL,
	email VARCHAR(255),
	senha VARCHAR(255) NOT NULL,
	data_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	CONSTRAINT pk_email PRIMARY KEY (email)
);

CREATE TABLE vinhos.vinhos (
	nome VARCHAR(255),
	ano INT,
	descricao TEXT,
	uva VARCHAR(50),
	vinicula VARCHAR(50),
	regiao VARCHAR(50),
	preco DECIMAL(10, 2) NOT NULL,
	quantidade_estoque INT DEFAULT 0,
	img_path VARCHAR(255),
	CONSTRAINT pk_nome PRIMARY KEY (nome)
);

CREATE TABLE vinhos.vinho_tinto (
	nome_vinho VARCHAR(255),
	estilo VARCHAR(50) NOT NULL,
	CONSTRAINT ck_estilo CHECK (estilo IN ('Cabernet Sauvignon', 'Merlot', 'Pinot Noir', 'Syrah')),
	CONSTRAINT fk_nome_vinho FOREIGN KEY (nome_vinho)
		REFERENCES vinhos.vinhos(nome) ON DELETE CASCADE,
	CONSTRAINT pk_vinho_tinto PRIMARY KEY (nome_vinho)
);

CREATE TABLE vinhos.vinho_branco (
	nome_vinho VARCHAR(255),
	estilo VARCHAR(50) NOT NULL,
	CONSTRAINT ck_estilo CHECK (estilo IN ('Chardonnay', 'Sauvignon Blanc', 'Riesling', 'Pinot Grigio')),
	CONSTRAINT fk_nome_vinho FOREIGN KEY (nome_vinho)
		REFERENCES vinhos.vinhos(nome) ON DELETE CASCADE,
	CONSTRAINT pk_vinho_branco PRIMARY KEY (nome_vinho)
);

CREATE TABLE vinhos.vinho_espumante (
	nome_vinho VARCHAR(255),
	estilo VARCHAR(50) NOT NULL,
	CONSTRAINT ck_estilo CHECK (estilo IN ('Champagne', 'Prosecco', 'Cava', 'Espumante Rosé')),
	CONSTRAINT fk_nome_vinho FOREIGN KEY (nome_vinho)
		REFERENCES vinhos.vinhos(nome) ON DELETE CASCADE,
	CONSTRAINT pk_vinho_espumante PRIMARY KEY (nome_vinho)
);

CREATE TABLE vinhos.vinho_rose (
	nome_vinho VARCHAR(255),
	estilo VARCHAR(50) NOT NULL,
	CONSTRAINT ck_estilo CHECK (estilo IN ('Provence Rosé', 'Tempranillo Rosé', 'Zinfandel Rosé', 'Grenache Rosé')),
	CONSTRAINT fk_nome_vinho FOREIGN KEY (nome_vinho)
		REFERENCES vinhos.vinhos(nome) ON DELETE CASCADE,
	CONSTRAINT pk_vinho_rose PRIMARY KEY (nome_vinho)
);

CREATE TABLE vinhos.vinho_sobremesa (
	nome_vinho VARCHAR(255),
	estilo VARCHAR(50) NOT NULL,
	CONSTRAINT ck_estilo CHECK (estilo IN ('Sauternes', 'Porto Branco', 'Moscato d''Asti', 'Tokaji')),
	CONSTRAINT fk_nome_vinho FOREIGN KEY (nome_vinho)
		REFERENCES vinhos.vinhos(nome) ON DELETE CASCADE,
	CONSTRAINT pk_vinho_sobremesa PRIMARY KEY (nome_vinho)
);

CREATE TABLE vinhos.vinho_fortificado (
	nome_vinho VARCHAR(255),
	estilo VARCHAR(50) NOT NULL,
	CONSTRAINT ck_estilo CHECK (estilo IN ('Porto (Ruby)', 'Porto (Tawny)', 'Xerez (Sherry)', 'Madeira')),
	CONSTRAINT fk_nome_vinho FOREIGN KEY (nome_vinho)
		REFERENCES vinhos.vinhos(nome) ON DELETE CASCADE,
	CONSTRAINT pk_vinho_fortificado PRIMARY KEY (nome_vinho)
);

CREATE TABLE vinhos.carrinho (
	numero SERIAL,
	usuario_email VARCHAR(255),
	valor_total DECIMAL(10, 2) DEFAULT 0,
	CONSTRAINT fk_usuario_email FOREIGN KEY (usuario_email)
		REFERENCES vinhos.usuarios(email) ON DELETE CASCADE,
	CONSTRAINT pk_carrinho PRIMARY KEY (numero)
);

CREATE TABLE vinhos.carrinho_produto (
	numero_carrinho INT,
	nome_vinho VARCHAR(255),
	quantidade INT DEFAULT 0,
	subtotal DECIMAL(10, 2) DEFAULT 0,
	CONSTRAINT fk_numero_carrinho FOREIGN KEY (numero_carrinho)
		REFERENCES vinhos.carrinho(numero) ON DELETE CASCADE,
	CONSTRAINT fk_nome_vinho FOREIGN KEY (nome_vinho)
		REFERENCES vinhos.vinhos(nome) ON DELETE CASCADE,
	CONSTRAINT pk_carriho_produto PRIMARY KEY (numero_carrinho, nome_vinho)
);

CREATE TABLE vinhos.compras (
	numero INT,
	valor_total DECIMAL(10, 2) DEFAULT 0,
	CONSTRAINT pk_compra PRIMARY KEY (numero)
);

CREATE TABLE vinhos.compra_carrinho_produto (
	numero_compra INT,
	numero_carrinho INT,
	nome_vinho VARCHAR(255),
	quantidade INT DEFAULT 0,
	subtotal DECIMAL(10, 2) DEFAULT 0,
	CONSTRAINT fk_numero_compra FOREIGN KEY (numero_compra)
		REFERENCES vinhos.compras(numero) ON DELETE CASCADE,
	CONSTRAINT fk_carrinho_vinho FOREIGN KEY (numero_carrinho, nome_vinho)
		REFERENCES vinhos.carrinho_produto(numero_carrinho, nome_vinho) ON DELETE CASCADE,
	CONSTRAINT pk_compra_carrinho_produto PRIMARY KEY (numero_compra, numero_carrinho, nome_vinho)
);