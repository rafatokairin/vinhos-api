package com.vinhos.bd.dao;

import com.vinhos.bd.model.Carrinho;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PgCarrinhoDAO implements CarrinhoDAO {

    private final Connection connection;

    // Consultas SQL
    private static final String CREATE_QUERY =
            "INSERT INTO vinhos.carrinho(usuario_email) VALUES (?);";

    private static final String FIND_BY_USUARIO_QUERY =
            "SELECT * FROM vinhos.carrinho WHERE usuario_email = ?;";

    private static final String ALL_QUERY =
            "SELECT * FROM vinhos.carrinho ORDER BY numero;";

    private static final String READ_QUERY =
            "SELECT * FROM vinhos.carrinho WHERE numero = ?;";

    private static final String UPDATE_QUERY =
            "UPDATE vinhos.carrinho SET valor_total = ? WHERE numero = ?;";

    private static final String DELETE_QUERY =
            "DELETE FROM vinhos.carrinho WHERE numero = ?;";

    public PgCarrinhoDAO(Connection connection) {
        this.connection = connection;
    }

    @Override
    public List<Carrinho> findCarrinhoByUsuario(Carrinho carrinho) throws SQLException {
        List<Carrinho> carrinhosList = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(FIND_BY_USUARIO_QUERY)) {
            statement.setString(1, carrinho.getUsuario_email());

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Carrinho c = new Carrinho();
                    c.setNumero(resultSet.getInt("numero"));
                    c.setUsuario_email(resultSet.getString("usuario_email"));
                    c.setValor_total(resultSet.getDouble("valor_total"));
                    carrinhosList.add(c);
                }
            }

        } catch (SQLException ex) {
            Logger.getLogger(PgCarrinhoDAO.class.getName()).log(Level.SEVERE, "DAO", ex);
            throw new SQLException("Erro ao listar carrinhos do usuário.");
        }

        return carrinhosList;
    }

    @Override
    public void create(Carrinho carrinho) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(CREATE_QUERY)) {
            statement.setString(1, carrinho.getUsuario_email()); // Associar o email do usuário
            statement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(PgCarrinhoDAO.class.getName()).log(Level.SEVERE, "DAO", ex);
            throw new SQLException("Erro ao criar carrinho.");
        }
    }

    @Override
    public Carrinho read(Integer numero) throws SQLException {
        Carrinho carrinho = null;

        try (PreparedStatement statement = connection.prepareStatement(READ_QUERY)) {
            statement.setInt(1, numero);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    carrinho = new Carrinho();
                    carrinho.setNumero(resultSet.getInt("numero"));
                    carrinho.setUsuario_email(resultSet.getString("usuario_email"));
                    carrinho.setValor_total(resultSet.getDouble("valor_total"));
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(PgCarrinhoDAO.class.getName()).log(Level.SEVERE, "DAO", ex);
            throw new SQLException("Erro ao buscar carrinho.");
        }

        return carrinho;
    }

    @Override
    public void update(Carrinho carrinho) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY)) {
            statement.setDouble(1, carrinho.getValor_total());
            statement.setInt(2, carrinho.getNumero());

            if (statement.executeUpdate() < 1) {
                throw new SQLException("Erro ao atualizar: carrinho não encontrado.");
            }
        } catch (SQLException ex) {
            Logger.getLogger(PgCarrinhoDAO.class.getName()).log(Level.SEVERE, "DAO", ex);
            throw new SQLException("Erro ao atualizar carrinho.");
        }
    }

    @Override
    public void delete(Integer numero) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(DELETE_QUERY)) {
            statement.setInt(1, numero);

            if (statement.executeUpdate() < 1) {
                throw new SQLException("Erro ao excluir: carrinho não encontrado.");
            }
        } catch (SQLException ex) {
            Logger.getLogger(PgCarrinhoDAO.class.getName()).log(Level.SEVERE, "DAO", ex);
            throw new SQLException("Erro ao excluir carrinho.");
        }
    }

    @Override
    public List<Carrinho> all() throws SQLException {
        List<Carrinho> carrinhosList = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(ALL_QUERY)) {
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Carrinho carrinho = new Carrinho();
                    carrinho.setNumero(resultSet.getInt("numero"));
                    carrinho.setUsuario_email(resultSet.getString("usuario_email"));
                    carrinho.setValor_total(resultSet.getDouble("valor_total"));
                    carrinhosList.add(carrinho);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(PgCarrinhoDAO.class.getName()).log(Level.SEVERE, "DAO", ex);
            throw new SQLException("Erro ao listar todos os carrinhos.");
        }

        return carrinhosList;
    }
}