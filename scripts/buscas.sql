	-- RRetorna o nome, quantidade vendida e valor total vendido dos vinhos, num certo período de tempo (dias, semanas, meses, anos)
SELECT nome, quantidade_vendida, total_vendido
FROM vinhos.vinhos v
JOIN (
    SELECT numero_vinho AS numero, SUM(quantidade) AS quantidade_vendida, data_registro, SUM(subtotal) AS total_vendido
    FROM vinhos.compra_carrinho_vinho ccv
    JOIN vinhos.compras c ON ccv.numero_compra = c.numero
    GROUP BY numero_vinho, data_registro
) AS r ON v.numero = r.numero
WHERE r.data_registro >= CURRENT_DATE - INTERVAL '3 days'
ORDER BY quantidade_vendida DESC;

-- Retorna o nome, quantidade vendida e valor total vendido dos vinhos, num período entre duas datas 
SELECT nome, quantidade_vendida, total_vendido
FROM vinhos.vinhos v
JOIN (
    SELECT numero_vinho AS numero, SUM(quantidade) AS quantidade_vendida, data_registro, SUM(subtotal) AS total_vendido
    FROM vinhos.compra_carrinho_vinho ccv
    JOIN vinhos.compras c ON ccv.numero_compra = c.numero
    GROUP BY numero_vinho, data_registro
) AS r ON v.numero = r.numero
WHERE r.data_registro BETWEEN '2024-10-01' AND '2024-10-31'
ORDER BY quantidade_vendida DESC;

-- Retorna a data e o valor total vendido do dia, para cada dia, num certo período (dias, semanas, meses, anos)
SELECT DATE(c.data_registro) AS data, SUM(c.valor_total) AS valor_total_por_dia
FROM vinhos.compras c
WHERE c.data_registro >= NOW() - INTERVAL '3 days'
GROUP BY DATE(c.data_registro)
ORDER BY data ASC;

-- Retorna a data e o valor total vendido do dia, para cada dia, num período entre duas datas
SELECT DATE(c.data_registro) AS data, SUM(c.valor_total) AS valor_total_por_dia
FROM vinhos.compras c
WHERE c.data_registro BETWEEN '2024-10-01' AND '2024-10-31'
GROUP BY DATE(c.data_registro)
ORDER BY data ASC;

-- Retorna a quantidade e valor total vendido, de vinhos, por sexo, num período de tempo (dias, semanas, meses, anos)
SELECT sexo, SUM(quantidade_vendida) AS quantidade_vendida, SUM(valor_total) AS valor_total
FROM vinhos.usuarios u
JOIN (
	SELECT quantidade_vendida, valor_total, email_usuario, data_registro
	FROM vinhos.compras c
	JOIN (
		SELECT numero_compra, SUM(quantidade) AS quantidade_vendida
		FROM vinhos.compra_carrinho_vinho
		GROUP BY numero_compra
	) AS ccv
	ON c.numero = ccv.numero_compra
) compras
ON u.email = compras.email_usuario AND compras.data_registro >= CURRENT_DATE - INTERVAL '3 days'
GROUP BY sexo
ORDER BY valor_total DESC;

-- Retorna a quantidade e valor total vendido, de vinhos, por sexo, num período entre duas datas
SELECT sexo, SUM(quantidade_vendida) AS quantidade_vendida, SUM(valor_total) AS valor_total
FROM vinhos.usuarios u
JOIN (
	SELECT quantidade_vendida, valor_total, email_usuario, data_registro
	FROM vinhos.compras c
	JOIN (
		SELECT numero_compra, SUM(quantidade) AS quantidade_vendida
		FROM vinhos.compra_carrinho_vinho
		GROUP BY numero_compra
	) AS ccv
	ON c.numero = ccv.numero_compra
) compras
ON u.email = compras.email_usuario AND compras.data_registro BETWEEN '2024-10-01' AND '2024-10-31'
GROUP BY sexo
ORDER BY valor_total DESC;

-- Retorna a quantidade vendida e valor total vendido de vinhos, por faixa etária, num perído de tempo (dias, semanas, meses, anos)
SELECT faixa_etaria(data_nascimento) AS faixa_etaria, SUM(quantidade_vendida) AS quantidade_vendida, SUM(valor_total) AS valor_total
FROM vinhos.usuarios u
JOIN (
	SELECT email_usuario, quantidade_vendida, valor_total
	FROM vinhos.compras c
	JOIN (
		SELECT numero_compra, SUM(quantidade) AS quantidade_vendida
		FROM vinhos.compra_carrinho_vinho
		GROUP BY numero_compra
	) ccv
	ON c.numero = ccv.numero_compra
) compras
ON u.email = compras.email_usuario AND compras.data_registro >= CURRENT_DATE - INTERVAL '3 days'
GROUP BY faixa_etaria
ORDER BY valor_total DESC;

-- Retorna a quantidade vendida e valor total vendido de vinhos, por faixa etária, num perído entre duas datas
SELECT faixa_etaria(data_nascimento) AS faixa_etaria, SUM(quantidade_vendida) AS quantidade_vendida, SUM(valor_total) AS valor_total
FROM vinhos.usuarios u
JOIN (
	SELECT email_usuario, quantidade_vendida, valor_total
	FROM vinhos.compras c
	JOIN (
		SELECT numero_compra, SUM(quantidade) AS quantidade_vendida
		FROM vinhos.compra_carrinho_vinho
		GROUP BY numero_compra
	) ccv
	ON c.numero = ccv.numero_compra
) compras
ON u.email = compras.email_usuario AND compras.data_registro BETWEEN '2024-10-01' AND '2024-10-31'
GROUP BY faixa_etaria
ORDER BY valor_total DESC;

-- Retorna a quantidade vendida e valor total vendido, de cada categoria de vinho, por faixa etaria, em um ultimo período de tempo (dias, semanas, meses, anos)
WITH
faixas AS (
    SELECT unnest(ARRAY['Criança', 'Adolescente', 'Jovem Adulto', 'Adulto', 'Idoso']) AS faixa_etaria,
           generate_series(1, 5) AS faixa_ordenacao
),
cat AS (
    SELECT unnest(ARRAY['Tinto', 'Branco', 'Espumante', 'Rosé', 'Sobremesa', 'Fortificado']) AS categorias,
           generate_series(1, 6) AS categoria_ordenacao
),
vendas AS (
    SELECT faixa_etaria(data_nascimento) AS faixa_etaria, categoria, 
           SUM(quantidade_vendida) AS quantidade_vendida, 
           SUM(valor_total) AS valor_total
    FROM vinhos.usuarios u
    JOIN (
        SELECT email_usuario, categoria, MIN(c.data_registro) AS data_registro, 
               SUM(quantidade_vendida) AS quantidade_vendida, 
               SUM(itens.valor_total) AS valor_total
        FROM vinhos.compras c
        JOIN (
            SELECT ccv.numero_compra, v.categoria, SUM(ccv.quantidade) AS quantidade_vendida, 
                   SUM(ccv.subtotal) AS valor_total
            FROM vinhos.compra_carrinho_vinho ccv
            JOIN vinhos.vinhos v
            ON ccv.numero_vinho = v.numero
            GROUP BY numero_compra, categoria
        ) itens
        ON c.numero = itens.numero_compra
        GROUP BY email_usuario, categoria
    ) compras
    ON u.email = compras.email_usuario AND compras.data_registro >= CURRENT_DATE - INTERVAL '1 day'
    GROUP BY faixa_etaria, categoria
),
fai_cat AS (
    SELECT f.faixa_etaria::VARCHAR AS faixa_etaria, f.faixa_ordenacao,
           c.categorias::VARCHAR AS categoria, c.categoria_ordenacao
    FROM faixas f
    CROSS JOIN cat c
)

SELECT fai_cat.faixa_etaria::VARCHAR AS faixa_etaria,
       fai_cat.categoria::VARCHAR AS categoria,
       COALESCE(v.quantidade_vendida, 0)::NUMERIC AS quantidade_vendida,
       COALESCE(v.valor_total, 0)::NUMERIC AS valor_total
FROM fai_cat
LEFT JOIN vendas v
ON fai_cat.faixa_etaria = v.faixa_etaria AND fai_cat.categoria = v.categoria
ORDER BY fai_cat.faixa_ordenacao, fai_cat.categoria_ordenacao;


-- Retorna a quantidade vendida e valor total vendido, de cada categoria de vinho, por faixa etaria, em um período entre duas datas
WITH
faixas AS (
    SELECT unnest(ARRAY['Criança', 'Adolescente', 'Jovem Adulto', 'Adulto', 'Idoso']) AS faixa_etaria,
           generate_series(1, 5) AS faixa_ordenacao
),
cat AS (
    SELECT unnest(ARRAY['Tinto', 'Branco', 'Espumante', 'Rosé', 'Sobremesa', 'Fortificado']) AS categorias,
           generate_series(1, 6) AS categoria_ordenacao
),
vendas AS (
    SELECT faixa_etaria(data_nascimento) AS faixa_etaria, categoria, 
           SUM(quantidade_vendida) AS quantidade_vendida, 
           SUM(valor_total) AS valor_total
    FROM vinhos.usuarios u
    JOIN (
        SELECT email_usuario, categoria, MIN(c.data_registro) AS data_registro, 
               SUM(quantidade_vendida) AS quantidade_vendida, 
               SUM(itens.valor_total) AS valor_total
        FROM vinhos.compras c
        JOIN (
            SELECT ccv.numero_compra, v.categoria, SUM(ccv.quantidade) AS quantidade_vendida, 
                   SUM(ccv.subtotal) AS valor_total
            FROM vinhos.compra_carrinho_vinho ccv
            JOIN vinhos.vinhos v
            ON ccv.numero_vinho = v.numero
            GROUP BY numero_compra, categoria
        ) itens
        ON c.numero = itens.numero_compra
        GROUP BY email_usuario, categoria
    ) compras
    ON u.email = compras.email_usuario AND compras.data_registro BETWEEN '2024-10-01' AND '2024-10-31'
    GROUP BY faixa_etaria, categoria
),
fai_cat AS (
    SELECT f.faixa_etaria::VARCHAR AS faixa_etaria, f.faixa_ordenacao,
           c.categorias::VARCHAR AS categoria, c.categoria_ordenacao
    FROM faixas f
    CROSS JOIN cat c
)

SELECT fai_cat.faixa_etaria::VARCHAR AS faixa_etaria,
       fai_cat.categoria::VARCHAR AS categoria,
       COALESCE(v.quantidade_vendida, 0)::NUMERIC AS quantidade_vendida,
       COALESCE(v.valor_total, 0)::NUMERIC AS valor_total
FROM fai_cat
LEFT JOIN vendas v
ON fai_cat.faixa_etaria = v.faixa_etaria AND fai_cat.categoria = v.categoria
ORDER BY fai_cat.faixa_ordenacao, fai_cat.categoria_ordenacao;


-- Retorna a quantidade e valor total vendido, de vinhos, por dia da semana, em um ultimo periodo de tempo
WITH 
dias AS (
    SELECT unnest(ARRAY['Domingo', 'Segunda', 'Terça', 'Quarta', 'Quinta', 'Sexta', 'Sábado']) AS dia_da_semana,
           generate_series(1, 7) AS dia_ordenacao
),
vendas AS (
    SELECT dia_da_semana(c.data_registro) AS dia_da_semana, 
           SUM(quantidade) AS quantidade_vendida,
           SUM(valor_total) AS valor_total
    FROM vinhos.compras c
    JOIN vinhos.compra_carrinho_vinho ccv
    ON c.numero = ccv.numero_compra
    WHERE c.data_registro >= CURRENT_DATE - INTERVAL '1 week'
    GROUP BY dia_da_semana
)

SELECT 
       d.dia_da_semana::VARCHAR AS dia_da_semana,
       COALESCE(v.quantidade_vendida, 0)::NUMERIC AS quantidade_vendida,
       COALESCE(v.valor_total, 0)::NUMERIC AS valor_total
FROM dias d
LEFT JOIN vendas v ON d.dia_da_semana = v.dia_da_semana
ORDER BY d.dia_ordenacao;

-- Retorna a quantidade e valor total vendido, de vinhos, por dia da semana, em um certo periodo entre duas datas
WITH 
dias AS (
    SELECT unnest(ARRAY['Domingo', 'Segunda', 'Terça', 'Quarta', 'Quinta', 'Sexta', 'Sábado']) AS dia_da_semana,
           generate_series(1, 7) AS dia_ordenacao
),
vendas AS (
    SELECT dia_da_semana(c.data_registro) AS dia_da_semana, 
           SUM(quantidade) AS quantidade_vendida,
           SUM(valor_total) AS valor_total
    FROM vinhos.compras c
    JOIN vinhos.compra_carrinho_vinho ccv
    ON c.numero = ccv.numero_compra
    WHERE c.data_registro BETWEEN '2024-10-06' AND '2024-10-12'
    GROUP BY dia_da_semana
)

SELECT 
       d.dia_da_semana::VARCHAR AS dia_da_semana,
       COALESCE(v.quantidade_vendida, 0)::NUMERIC AS quantidade_vendida,
       COALESCE(v.valor_total, 0)::NUMERIC AS valor_total
FROM dias d
LEFT JOIN vendas v ON d.dia_da_semana = v.dia_da_semana
ORDER BY d.dia_ordenacao;
