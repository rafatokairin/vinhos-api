package com.vinhos.bd.dao;

import com.vinhos.bd.model.CarrinhoVinho;
import com.vinhos.bd.model.CarrinhoVinhoID;

import java.sql.SQLException;
import java.util.List;

public interface CarrinhoVinhoDAO extends DAO<CarrinhoVinho, CarrinhoVinhoID> {

    /**
     * Procura por todos os itens de um mesmo carrinho.
     * @param numero_carrinho Identificador do carrinho
     * @return  Lista com todos os itens de um certo carrihno, com sua quantidade e subtotal
     * @throws SQLException
     */
    public List<CarrinhoVinho> findAllVinhosOfCarrinho (int numero_carrinho) throws SQLException;
}
