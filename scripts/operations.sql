-- Criar usuário
INSERT INTO vinhos.usuarios (nome, email, senha, sexo, data_nascimento) 
VALUES ('Nome do Usuário', 'email@exemplo.com', 'senha123', 'M', '1990-05-12');

-- Adicionar vinho
INSERT INTO vinhos.vinhos (nome, ano, descricao, uva, vinicula, regiao, preco, quantidade_estoque, img_path, categoria, estilo) 
VALUES ('Vinho Exemplo', 2018, 'Vinho encorpado e saboroso', 'Cabernet Sauvignon', 'Vinícola Exemplo', 'Mendoza', 150.50, 100, 'img/caminho.jpg', 'Tinto', 'Cabernet Sauvignon');

-- Criar carrinho para um usuário
INSERT INTO vinhos.carrinho (usuario_email) 
VALUES ('email@exemplo.com');

-- Adicionar vinho ao carrinho
INSERT INTO vinhos.carrinho_vinho (numero_carrinho, numero_vinho, quantidade) 
VALUES (1, 1, 3);

-- Finalizar compra
INSERT INTO vinhos.compras (valor_total, email_usuario)
VALUES (
    (SELECT valor_total FROM vinhos.carrinho WHERE numero = 1),  -- Valor total do carrinho
    'email@exemplo.com'  -- Email do usuário que está finalizando a compra
);
INSERT INTO vinhos.compra_carrinho_vinho (numero_compra, numero_carrinho, numero_vinho, quantidade, subtotal)
SELECT 1, numero_carrinho, numero_vinho, quantidade, subtotal
FROM vinhos.carrinho_vinho
WHERE numero_carrinho = 1;  -- Número do carrinho que está sendo finalizado
DELETE FROM vinhos.carrinho_vinho WHERE numero_carrinho = 1;
