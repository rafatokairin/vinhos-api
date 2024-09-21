package com.vinhos.bd.dao;

import com.vinhos.bd.model.Carrinho;

import java.sql.SQLException;
import java.util.List;

public interface CarrinhoDAO extends DAO<Carrinho, Integer> {
    /**
     * Funcao a qual vai realizar uma busca de carrinhos associado
     * ao usuario
     * @param carrinho
     * @return
     * @throws SQLException
     */
    public List<Carrinho> findCarrinhoByUsuario(Carrinho carrinho) throws SQLException;
}
