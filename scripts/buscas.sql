-- Retorna o nome e a quantidade vendida, dos vinhos, num certo período de tempo (dias, semanas, meses, anos)
SELECT nome, quantidade_vendida
FROM vinhos.vinhos v
JOIN (
    SELECT numero_vinho AS numero, SUM(quantidade) AS quantidade_vendida, data_registro
    FROM vinhos.compra_carrinho_vinho ccv
    JOIN vinhos.compras c ON ccv.numero_compra = c.numero
    GROUP BY numero_vinho, data_registro
) AS r ON v.numero = r.numero
WHERE r.data_registro >= CURRENT_DATE - INTERVAL '3 days'
ORDER BY quantidade_vendida DESC;

-- Retorna o nome e a quantidade vendida, dos vinhos, num período entre duas datas 
SELECT nome, quantidade_vendida
FROM vinhos.vinhos v
JOIN (
    SELECT numero_vinho AS numero, SUM(quantidade) AS quantidade_vendida, data_registro
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
