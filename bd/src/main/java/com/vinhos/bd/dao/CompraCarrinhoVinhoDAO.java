package com.vinhos.bd.dao;

import com.vinhos.bd.model.CompraCarrinhoVinho;
import com.vinhos.bd.model.CompraCarrinhoVinhoID;

import java.sql.SQLException;
import java.util.List;

public interface CompraCarrinhoVinhoDAO extends DAO<CompraCarrinhoVinho, CompraCarrinhoVinhoID> {

    /**
     * Lista todos os itens de uma compra.
     * @param numero_compra Identificador da compra.
     * @return Lista com todos os itens de uma compra.
     * @throws SQLException
     */
    public List<CompraCarrinhoVinho> listaCompra (int numero_compra) throws SQLException;
}
