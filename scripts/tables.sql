CREATE TABLE IF NOT EXISTS vinhos.usuarios
(
    nome character varying(255) COLLATE pg_catalog."default" NOT NULL,
    email character varying(255) COLLATE pg_catalog."default" NOT NULL,
    senha character varying(255) COLLATE pg_catalog."default" NOT NULL,
    data_registro timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    sexo character varying(1) COLLATE pg_catalog."default",
    data_nascimento date,
    CONSTRAINT pk_email PRIMARY KEY (email)
);

CREATE TABLE IF NOT EXISTS vinhos.vinhos
(
    nome character varying(255) COLLATE pg_catalog."default" NOT NULL,
    ano integer,
    descricao text COLLATE pg_catalog."default",
    uva character varying(50) COLLATE pg_catalog."default",
    vinicula character varying(50) COLLATE pg_catalog."default",
    regiao character varying(50) COLLATE pg_catalog."default",
    preco numeric(10,2),
    quantidade_estoque integer DEFAULT 0,
    img_path character varying(255) COLLATE pg_catalog."default",
    categoria character varying(50) COLLATE pg_catalog."default",
    estilo character varying(100) COLLATE pg_catalog."default",
    numero SERIAL,
    CONSTRAINT pk_vinho PRIMARY KEY (numero),
    CONSTRAINT ch_preco CHECK (preco >= 0::numeric),
    CONSTRAINT check_categoria_estilo CHECK (categoria::text = 'Tinto'::text AND (estilo::text = ANY (ARRAY['Cabernet Sauvignon'::character varying, 'Merlot'::character varying, 'Pinot Noir'::character varying, 'Syrah'::character varying]::text[])) OR categoria::text = 'Branco'::text AND (estilo::text = ANY (ARRAY['Chardonnay'::character varying, 'Sauvignon Blanc'::character varying, 'Riesling'::character varying, 'Pinot Grigio'::character varying]::text[])) OR categoria::text = 'Espumante'::text AND (estilo::text = ANY (ARRAY['Champagne'::character varying, 'Prosecco'::character varying, 'Cava'::character varying, 'Espumante Rosé'::character varying]::text[])) OR categoria::text = 'Rosé'::text AND (estilo::text = ANY (ARRAY['Provence Rosé'::character varying, 'Tempranillo Rosé'::character varying, 'Zinfandel Rosé'::character varying, 'Grenache Rosé'::character varying]::text[])) OR categoria::text = 'Sobremesa'::text AND (estilo::text = ANY (ARRAY['Sauternes'::character varying, 'Porto Branco'::character varying, 'Moscato d''Asti'::character varying, 'Tokaji'::character varying]::text[])) OR categoria::text = 'Fortificado'::text AND (estilo::text = ANY (ARRAY['Porto (Ruby)'::character varying, 'Porto (Tawny)'::character varying, 'Xerez (Sherry)'::character varying, 'Madeira'::character varying]::text[])))
);

CREATE TABLE IF NOT EXISTS vinhos.carrinho
(
    numero SERIAL,
    usuario_email character varying(255) COLLATE pg_catalog."default",
    valor_total numeric(10,2) DEFAULT 0,
    CONSTRAINT pk_carrinho PRIMARY KEY (numero),
    CONSTRAINT fk_usuario_email FOREIGN KEY (usuario_email)
        REFERENCES vinhos.usuarios (email) MATCH SIMPLE
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    CONSTRAINT ck_valor_total CHECK (valor_total >= 0::numeric)
);

CREATE TABLE IF NOT EXISTS vinhos.carrinho_vinho
(
    numero_carrinho integer NOT NULL,
    numero_vinho integer NOT NULL,
    quantidade integer DEFAULT 0,
    subtotal numeric(10,2) DEFAULT 0,
    CONSTRAINT pk_carrinho_produto PRIMARY KEY (numero_carrinho, numero_vinho),
    CONSTRAINT fk_numero_carrinho FOREIGN KEY (numero_carrinho)
        REFERENCES vinhos.carrinho (numero) MATCH SIMPLE
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    CONSTRAINT fk_numero_vinho FOREIGN KEY (numero_vinho)
        REFERENCES vinhos.vinhos (numero) MATCH SIMPLE
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    CONSTRAINT ck_subtotal CHECK (subtotal >= 0::numeric)
);

CREATE TABLE IF NOT EXISTS vinhos.compras
(
    numero integer NOT NULL,
    valor_total numeric(10,2) DEFAULT 0,
    data_registro timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    email_usuario character varying(255) COLLATE pg_catalog."default",
    CONSTRAINT pk_compra PRIMARY KEY (numero),
    CONSTRAINT ck_valor_total CHECK (valor_total >= 0::numeric)
);

CREATE TABLE IF NOT EXISTS vinhos.compra_carrinho_vinho
(
    numero_compra integer NOT NULL,
    numero_carrinho integer NOT NULL,
    numero_vinho integer NOT NULL,
    quantidade integer DEFAULT 0,
    subtotal numeric(10,2) DEFAULT 0,
    CONSTRAINT pk_compra_carrinho_produto PRIMARY KEY (numero_compra, numero_carrinho, numero_vinho),
    CONSTRAINT fk_numero_compra FOREIGN KEY (numero_compra)
        REFERENCES vinhos.compras (numero) MATCH SIMPLE
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    CONSTRAINT ck_subtotal CHECK (subtotal >= 0::numeric)
);

-- Trigger Functions
CREATE OR REPLACE FUNCTION vinhos.calcular_subtotal()
    RETURNS trigger
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE NOT LEAKPROOF
AS $BODY$
BEGIN
    -- Recupera o preço do vinho
    SELECT COALESCE(v.preco, 0) INTO NEW.subtotal
    FROM vinhos.vinhos v
    WHERE v.numero = NEW.numero_vinho;

    -- Multiplica pelo quantidade
    NEW.subtotal := NEW.subtotal * NEW.quantidade;

    RETURN NEW;
END;
$BODY$;

CREATE OR REPLACE FUNCTION vinhos.calcular_total_carrinho()
    RETURNS trigger
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE NOT LEAKPROOF
AS $BODY$
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
$BODY$;

CREATE OR REPLACE FUNCTION vinhos.atualizar_valor_total_compra()
    RETURNS trigger
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE NOT LEAKPROOF
AS $BODY$
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
$BODY$;

CREATE OR REPLACE FUNCTION vinhos.faixa_etaria(
    data_nascimento date)
    RETURNS character varying
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE PARALLEL UNSAFE
AS $BODY$
DECLARE
    idade INT;
    faixa VARCHAR;
BEGIN
    -- Calcula a idade da pessoa
    idade := EXTRACT(YEAR FROM AGE(CURRENT_DATE, data_nascimento));

    -- Define a faixa etária com base na idade
    IF idade < 13 THEN
        faixa := 'Criança';
    ELSIF idade BETWEEN 13 AND 17 THEN
        faixa := 'Adolescente';
    ELSIF idade BETWEEN 18 AND 24 THEN
        faixa := 'Jovem Adulto';
    ELSIF idade BETWEEN 25 AND 64 THEN
        faixa := 'Adulto';
    ELSE
        faixa := 'Idoso';
    END IF;

    -- Retorna a faixa etária
    RETURN faixa;
END;
$BODY$;

CREATE OR REPLACE FUNCTION vinhos.dia_da_semana(
	data_input timestamp without time zone)
    RETURNS character varying
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE PARALLEL UNSAFE
AS $BODY$
BEGIN
    -- Retorna o nome do dia da semana, com a primeira letra em maiúscula
    RETURN INITCAP(TO_CHAR(data_input, 'TMDay'));
END;
$BODY$;

SET lc_time = 'pt_BR';

CREATE TRIGGER trigger_calcular_subtotal
    BEFORE INSERT OR UPDATE 
    ON vinhos.carrinho_vinho
    FOR EACH ROW
    EXECUTE FUNCTION vinhos.calcular_subtotal();

CREATE TRIGGER trigger_calcular_total
    AFTER INSERT OR DELETE OR UPDATE 
    ON vinhos.carrinho_vinho
    FOR EACH ROW
    EXECUTE FUNCTION vinhos.calcular_total_carrinho();

CREATE TRIGGER tg_atualizar_valor_total
    AFTER INSERT OR DELETE OR UPDATE 
    ON vinhos.compra_carrinho_vinho
    FOR EACH ROW
    EXECUTE FUNCTION vinhos.atualizar_valor_total_compra();
