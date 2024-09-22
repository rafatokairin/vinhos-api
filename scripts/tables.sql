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
    CONSTRAINT ch_preco CHECK (preco >= 0),
    CONSTRAINT pk_vinho PRIMARY KEY (nome, vinicula) -- Chave primária composta por nome e vinícola
);

CREATE TABLE vinhos.vinho_tinto (
    nome_vinho VARCHAR(255),
    vinicula VARCHAR(50),
    estilo VARCHAR(50) NOT NULL,
    CONSTRAINT ck_estilo CHECK (estilo IN ('Cabernet Sauvignon', 'Merlot', 'Pinot Noir', 'Syrah')),
    CONSTRAINT fk_nome_vinho FOREIGN KEY (nome_vinho, vinicula)
        REFERENCES vinhos.vinhos(nome, vinicula) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT pk_vinho_tinto PRIMARY KEY (nome_vinho, vinicula)
);

CREATE TABLE vinhos.vinho_branco (
    nome_vinho VARCHAR(255),
    vinicula VARCHAR(50),
    estilo VARCHAR(50) NOT NULL,
    CONSTRAINT ck_estilo CHECK (estilo IN ('Chardonnay', 'Sauvignon Blanc', 'Riesling', 'Pinot Grigio')),
    CONSTRAINT fk_nome_vinho FOREIGN KEY (nome_vinho, vinicula)
        REFERENCES vinhos.vinhos(nome, vinicula) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT pk_vinho_branco PRIMARY KEY (nome_vinho, vinicula)
);

CREATE TABLE vinhos.vinho_espumante (
    nome_vinho VARCHAR(255),
    vinicula VARCHAR(50),
    estilo VARCHAR(50) NOT NULL,
    CONSTRAINT ck_estilo CHECK (estilo IN ('Champagne', 'Prosecco', 'Cava', 'Espumante Rosé')),
    CONSTRAINT fk_nome_vinho FOREIGN KEY (nome_vinho, vinicula)
        REFERENCES vinhos.vinhos(nome, vinicula) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT pk_vinho_espumante PRIMARY KEY (nome_vinho, vinicula)
);

CREATE TABLE vinhos.vinho_rose (
    nome_vinho VARCHAR(255),
    vinicula VARCHAR(50),
    estilo VARCHAR(50) NOT NULL,
    CONSTRAINT ck_estilo CHECK (estilo IN ('Provence Rosé', 'Tempranillo Rosé', 'Zinfandel Rosé', 'Grenache Rosé')),
    CONSTRAINT fk_nome_vinho FOREIGN KEY (nome_vinho, vinicula)
        REFERENCES vinhos.vinhos(nome, vinicula) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT pk_vinho_rose PRIMARY KEY (nome_vinho, vinicula)
);

CREATE TABLE vinhos.vinho_sobremesa (
    nome_vinho VARCHAR(255),
    vinicula VARCHAR(50),
    estilo VARCHAR(50) NOT NULL,
    CONSTRAINT ck_estilo CHECK (estilo IN ('Sauternes', 'Porto Branco', 'Moscato d''Asti', 'Tokaji')),
    CONSTRAINT fk_nome_vinho FOREIGN KEY (nome_vinho, vinicula)
        REFERENCES vinhos.vinhos(nome, vinicula) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT pk_vinho_sobremesa PRIMARY KEY (nome_vinho, vinicula)
);

CREATE TABLE vinhos.vinho_fortificado (
    nome_vinho VARCHAR(255),
    vinicula VARCHAR(50),
    estilo VARCHAR(50) NOT NULL,
    CONSTRAINT ck_estilo CHECK (estilo IN ('Porto (Ruby)', 'Porto (Tawny)', 'Xerez (Sherry)', 'Madeira')),
    CONSTRAINT fk_nome_vinho FOREIGN KEY (nome_vinho, vinicula)
        REFERENCES vinhos.vinhos(nome, vinicula) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT pk_vinho_fortificado PRIMARY KEY (nome_vinho, vinicula)
);

CREATE TABLE vinhos.carrinho (
    numero SERIAL,
    usuario_email VARCHAR(255),
    valor_total DECIMAL(10, 2) DEFAULT 0,
    CONSTRAINT ck_valor_total CHECK (valor_total >= 0),
    CONSTRAINT fk_usuario_email FOREIGN KEY (usuario_email)
        REFERENCES vinhos.usuarios(email) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT pk_carrinho PRIMARY KEY (numero)
);

CREATE TABLE vinhos.carrinho_produto (
    numero_carrinho INT,
    nome_vinho VARCHAR(255),
    vinicula VARCHAR(50),
    quantidade INT DEFAULT 0,
    subtotal DECIMAL(10, 2) DEFAULT 0,
    CONSTRAINT ck_subtotal CHECK (subtotal >= 0),
    CONSTRAINT fk_numero_carrinho FOREIGN KEY (numero_carrinho)
        REFERENCES vinhos.carrinho(numero) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_nome_vinho FOREIGN KEY (nome_vinho, vinicula)
        REFERENCES vinhos.vinhos(nome, vinicula) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT pk_carrinho_produto PRIMARY KEY (numero_carrinho, nome_vinho, vinicula)
);

CREATE TABLE vinhos.compras (
    numero INT,
    valor_total DECIMAL(10, 2) DEFAULT 0,
    CONSTRAINT ck_valor_total CHECK (valor_total >= 0),
    CONSTRAINT pk_compra PRIMARY KEY (numero)
);

CREATE TABLE vinhos.compra_carrinho_produto (
    numero_compra INT,
    numero_carrinho INT,
    nome_vinho VARCHAR(255),
    vinicula VARCHAR(50),
    quantidade INT DEFAULT 0,
    subtotal DECIMAL(10, 2) DEFAULT 0,
    CONSTRAINT ck_subtotal CHECK (subtotal >= 0),
    CONSTRAINT fk_numero_compra FOREIGN KEY (numero_compra)
        REFERENCES vinhos.compras(numero) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_carrinho_vinho FOREIGN KEY (numero_carrinho, nome_vinho, vinicula)
        REFERENCES vinhos.carrinho_produto(numero_carrinho, nome_vinho, vinicula) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT pk_compra_carrinho_produto PRIMARY KEY (numero_compra, numero_carrinho, nome_vinho, vinicula)
);

DROP TABLE IF EXISTS vinhos.vinho_tinto CASCADE;
DROP TABLE IF EXISTS vinhos.vinho_branco CASCADE;
DROP TABLE IF EXISTS vinhos.vinho_espumante CASCADE;
DROP TABLE IF EXISTS vinhos.vinho_rose CASCADE;
DROP TABLE IF EXISTS vinhos.vinho_sobremesa CASCADE;
DROP TABLE IF EXISTS vinhos.vinho_fortificado CASCADE;

ALTER TABLE vinhos.vinhos
ADD COLUMN categoria VARCHAR(50) NOT NULL,
ADD COLUMN estilo VARCHAR(100),
ADD COLUMN numero SERIAL;

ALTER TABLE vinhos.vinhos
ADD CONSTRAINT check_categoria_estilo CHECK (
    (categoria = 'Tinto' AND estilo IN ('Cabernet Sauvignon', 'Merlot', 'Pinot Noir', 'Syrah')) OR
    (categoria = 'Branco' AND estilo IN ('Chardonnay', 'Sauvignon Blanc', 'Riesling', 'Pinot Grigio')) OR
    (categoria = 'Espumante' AND estilo IN ('Champagne', 'Prosecco', 'Cava', 'Espumante Rosé')) OR
    (categoria = 'Rosé' AND estilo IN ('Provence Rosé', 'Tempranillo Rosé', 'Zinfandel Rosé', 'Grenache Rosé')) OR
    (categoria = 'Sobremesa' AND estilo IN ('Sauternes', 'Porto Branco', 'Moscato d''Asti', 'Tokaji')) OR
    (categoria = 'Fortificado' AND estilo IN ('Porto (Ruby)', 'Porto (Tawny)', 'Xerez (Sherry)', 'Madeira'))
);

DROP TABLE IF EXISTS vinhos.compra_carrinho_produto CASCADE;
DROP TABLE IF EXISTS vinhos.carrinho_produto CASCADE;

-- Para manter a chave primária atual (nome, vinicula) e a nova coluna 'numero', você precisará remover a chave primária existente e depois criar uma nova.
ALTER TABLE vinhos.vinhos
DROP CONSTRAINT pk_vinho;

ALTER TABLE vinhos.vinhos
ADD CONSTRAINT pk_vinho PRIMARY KEY (numero);

CREATE TABLE vinhos.carrinho_vinho (
    numero_carrinho INT,
    numero_vinho INT,
    quantidade INT DEFAULT 0,
    subtotal DECIMAL(10, 2) DEFAULT 0,
    CONSTRAINT ck_subtotal CHECK (subtotal >= 0),
    CONSTRAINT fk_numero_carrinho FOREIGN KEY (numero_carrinho)
        REFERENCES vinhos.carrinho(numero) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_numero_vinho FOREIGN KEY (numero_vinho)
        REFERENCES vinhos.vinhos(numero) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT pk_carrinho_produto PRIMARY KEY (numero_carrinho, numero_vinho)
);

CREATE TABLE vinhos.compra_carrinho_vinho (
    numero_compra INT,
    numero_carrinho INT,
    numero_vinho INT,
    quantidade INT DEFAULT 0,
    subtotal DECIMAL(10, 2) DEFAULT 0,
    CONSTRAINT ck_subtotal CHECK (subtotal >= 0),
    CONSTRAINT fk_numero_compra FOREIGN KEY (numero_compra)
        REFERENCES vinhos.compras(numero) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_carrinho_vinho FOREIGN KEY (numero_carrinho, numero_vinho)
        REFERENCES vinhos.carrinho_vinho(numero_carrinho, numero_vinho) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT pk_compra_carrinho_produto PRIMARY KEY (numero_compra, numero_carrinho, numero_vinho)
);

ALTER TABLE vinhos.vinhos
ALTER COLUMN nome SET NOT NULL;

ALTER TABLE vinhos.vinhos
ALTER COLUMN vinicula DROP NOT NULL;

ALTER TABLE vinhos.vinhos
ALTER COLUMN preco DROP NOT NULL;

ALTER TABLE vinhos.vinhos
ALTER COLUMN categoria DROP NOT NULL;

CREATE OR REPLACE FUNCTION calcular_subtotal()
RETURNS TRIGGER AS $$
BEGIN
    -- Recupera o preço do vinho
    SELECT COALESCE(v.preco, 0) INTO NEW.subtotal
    FROM vinhos.vinhos v
    WHERE v.numero = NEW.numero_vinho;

    -- Multiplica pelo quantidade
    NEW.subtotal := NEW.subtotal * NEW.quantidade;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_calcular_subtotal
BEFORE INSERT OR UPDATE ON vinhos.carrinho_vinho
FOR EACH ROW
EXECUTE FUNCTION calcular_subtotal();

CREATE OR REPLACE FUNCTION calcular_total_carrinho()
RETURNS TRIGGER AS $$
DECLARE
    total DECIMAL(10, 2);
BEGIN
    -- Verifica se é um DELETE
    IF TG_OP = 'DELETE' THEN
        -- Calcula o total para o carrinho relacionado
        SELECT SUM(subtotal) INTO total
        FROM vinhos.carrinho_vinho
        WHERE numero_carrinho = OLD.numero_carrinho;
    ELSE
        -- Para INSERT e UPDATE, usa NEW
        SELECT SUM(subtotal) INTO total
        FROM vinhos.carrinho_vinho
        WHERE numero_carrinho = NEW.numero_carrinho;
    END IF;

    -- Atualiza o valor total na tabela do carrinho
    UPDATE vinhos.carrinho
    SET valor_total = COALESCE(total, 0) -- Garante que o total não seja nulo
    WHERE numero = COALESCE(OLD.numero_carrinho, NEW.numero_carrinho);

    RETURN NULL; -- Não precisa retornar nada, pois é um AFTER
END;
$$ LANGUAGE plpgsql;


CREATE TRIGGER trigger_calcular_total
AFTER INSERT OR UPDATE ON vinhos.carrinho_vinho
FOR EACH ROW
EXECUTE FUNCTION calcular_total_carrinho();

-- Função que será chamada pela trigger
CREATE OR REPLACE FUNCTION atualizar_valor_total_compra()
RETURNS TRIGGER AS $$
BEGIN
    -- Caso seja uma inserção ou atualização
    IF (TG_OP = 'INSERT' OR TG_OP = 'UPDATE') THEN
        UPDATE vinhos.compras
        SET valor_total = (
            SELECT COALESCE(SUM(subtotal), 0)
            FROM vinhos.compra_carrinho_vinho
            WHERE numero_compra = NEW.numero_compra
        )
        WHERE numero = NEW.numero_compra;

    -- Caso seja uma exclusão
    ELSIF (TG_OP = 'DELETE') THEN
        UPDATE vinhos.compras
        SET valor_total = (
            SELECT COALESCE(SUM(subtotal), 0)
            FROM vinhos.compra_carrinho_vinho
            WHERE numero_compra = OLD.numero_compra
        )
        WHERE numero = OLD.numero_compra;
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Trigger que chama a função
DROP TRIGGER IF EXISTS tg_atualizar_valor_total ON vinhos.compra_carrinho_vinho;

CREATE TRIGGER tg_atualizar_valor_total
AFTER INSERT OR UPDATE OR DELETE
ON vinhos.compra_carrinho_vinho
FOR EACH ROW
EXECUTE FUNCTION atualizar_valor_total_compra();
