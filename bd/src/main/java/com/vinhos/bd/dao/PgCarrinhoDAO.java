package com.vinhos.bd.dao;

import com.vinhos.bd.model.Carrinho;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class PgCarrinhoDAO implements CarrinhoDAO {

    private final Connection connection;

    private static final String CREATE_QUERY =
            "INSERT INTO vinhos.carrinho(usuario_email, valor_total) " +
                    "VALUES (?, ?);";

    private static final String ALL_QUERY =
            "SELECT * FROM vinhos.carrinho " +
                    "ORDER BY numero;";

    private static final String READ_QUERY =
            "SELECT * FROM vinhos.carrinho " +
                    "WHERE numero = ?;";

    private static final String UPDATE_QUERY =
            "UPDATE vinhos.carrinho " +
                    "SET usuario_email = ? " +
                    "WHERE valor_total = ?;";

    private static final String DELETE_QUERY =
            "DELETE FROM vinhos.carrinho " +
                    "WHERE numero = ?;";

    public PgCarrinhoDAO (Connection connection) {
        this.connection = connection;
    }

    @Override
    public List<Carrinho> findCarrinhoByUsuario (Carrinho carrinho) throws SQLException, SecurityException {

    }

    @Override
    public void create (Carrinho carrinho) throws SQLException {

    }

    @Override
    public Carrinho read (Integer numero) throws SQLException {

    }

    @Override
    public void update (Carrinho carrinho) throws SQLException {

    }

    @Override
    public void delete (Integer numero) throws SQLException {

    }

    @Override
    public List<Carrinho> all () throws SQLException {

    }
}
