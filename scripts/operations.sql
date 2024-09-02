-- INSERIR NOVO VINHO
INSERT INTO vinhos.vinhos (nome, ano, descricao, uva, vinicula, regiao, preco, quantidade_estoque, img_path) 
VALUES ('Château Margaux', 2015, 'Um vinho elegante e refinado com notas de frutas vermelhas.', 'Cabernet Sauvignon', 'Château Margaux', 'Bordeaux', 1200.00, 50, '\\imagens\\Chateau_Margaux.jpg');
INSERT INTO vinhos.vinhos (nome, ano, descricao, uva, vinicula, regiao, preco, quantidade_estoque, img_path) 
VALUES ('Santa Rita 120', 2018, 'Vinho chileno com sabores frutados e um toque de carvalho.', 'Merlot', 'Santa Rita', 'Vale Central', 85.00, 200, '\\imagens\\Santa_Rita_120.jpg');

-- ASSOCIAR VINHOS ÀS CATEGORIAS RESPECTIVAS
INSERT INTO vinhos.vinho_tinto (nome_vinho, vinicula, estilo) VALUES ('Château Margaux', 'Château Margaux', 'Cabernet Sauvignon');
INSERT INTO vinhos.vinho_tinto (nome_vinho, vinicula, estilo) VALUES ('Santa Rita 120', 'Santa Rita', 'Merlot');

-- CADASTRAR NOVO USUÁRIO
INSERT INTO vinhos.usuarios (nome, email, senha) 
VALUES ('João Silva', 'joao.silva@example.com', 'senha123');
INSERT INTO vinhos.usuarios (nome, email, senha) 
VALUES ('Ana Pereira', 'ana.pereira@example.com', 'senha456');

-- ADICIONAR ITENS AO CARRINHO
INSERT INTO vinhos.carrinho (usuario_email, valor_total) VALUES ('joao.silva@example.com', 2400.00);
-- assumindo que o carrinho criado tem o número 1 (chave estrangeira do carrinho do usuário)
INSERT INTO vinhos.carrinho_produto (numero_carrinho, nome_vinho, vinicula, quantidade, subtotal) 
VALUES (1, 'Château Margaux', 'Château Margaux', 2, 2400.00);
INSERT INTO vinhos.carrinho_produto (numero_carrinho, nome_vinho, vinicula, quantidade, subtotal) 
VALUES (1, 'Santa Rita 120', 'Santa Rita', 3, 255.00);

-- ATUALIZAR QUANTIDADE NO CARRINHO
UPDATE vinhos.carrinho_produto
-- subtotal = 340.00 (85.00 x 4)
SET quantidade = 4, subtotal = 340.00 
WHERE numero_carrinho = 1 AND nome_vinho = 'Santa Rita 120' AND vinicula = 'Santa Rita';

-- REMOVER ITEM DO CARRINHO
DELETE FROM vinhos.carrinho_produto 
WHERE numero_carrinho = 1 AND nome_vinho = 'Santa Rita 120' AND vinicula = 'Santa Rita';

-- FINALIZAR COMPRA
-- valor total (soma dos preços em carrinho_produto)
-- numero (número nota fiscal)
INSERT INTO vinhos.compras (numero, valor_total) 
VALUES (123, 2400.00);
-- assumindo que carrinho 1 está sendo finalizado
INSERT INTO vinhos.compra_carrinho_produto (numero_compra, numero_carrinho, nome_vinho, vinicula, quantidade, subtotal) 
VALUES (123, 1, 'Château Margaux', 'Château Margaux', 2, 2400.00);

-- CONSULTAR VINHOS POR CATEGORIA
SELECT v.nome, v.descricao, v.preco, v.ano, v.uva, v.vinicula, v.regiao, v.img_path, t.estilo
FROM vinhos.vinho_tinto t
JOIN vinhos.vinhos v ON t.nome_vinho = v.nome AND t.vinicula = v.vinicula
WHERE t.estilo = 'Cabernet Sauvignon';

-- CONSULTAR VINHOS NO CARRINHO
SELECT u.nome, u.email, c.numero, cp.nome_vinho, cp.vinicula, cp.quantidade, cp.subtotal
FROM vinhos.usuarios u
JOIN vinhos.carrinho c ON u.email = c.usuario_email
JOIN vinhos.carrinho_produto cp ON c.numero = cp.numero_carrinho
WHERE u.email = 'joao.silva@example.com';

-- ATUALIZAR INFORMAÇÕES DO USUÁRIO
UPDATE vinhos.usuarios 
SET nome = 'Maria Oliveira', 
email = 'maria.oliveira@example.com', 
senha = 'novaSenha123' 
WHERE email = 'joao.silva@example.com';

-- REMOVER VINHO
DELETE FROM vinhos.vinhos WHERE nome = 'Château Margaux' AND vinicula = 'Château Margaux';

-- REMOVER USUÁRIO
DELETE FROM vinhos.usuarios WHERE email = 'maria.oliveira@example.com';

-- REMOVER COMPRA
DELETE FROM vinhos.compras WHERE numero = 123;
