package com.vinhos.bd.dao;

import com.vinhos.bd.model.CarrinhoVinho;
import com.vinhos.bd.model.CarrinhoVinhoID;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PgCarrinhoVinhoDAO implements CarrinhoVinhoDAO {

    private final Connection connection;

    private static final String ALL_VINHOS_CARRINHO =
            "SELECT * FROM vinhos.carrinho_vinho " +
                    "WHERE numero_carrinho = ?;";

    private static final String ESVAZIA_CARRINHO_QUERY =
            "DELETE FROM vinhos.carrinho_vinho " +
                    "WHERE numero_carrinho = ?;";

    private static final String CREATE_QUERY =
            "INSERT INTO vinhos.carrinho_vinho(numero_carrinho, " +
                    "numero_vinho, quantidade) " +
                    "VALUES (?, ?, ?);";

    private static final String ALL_QUERY =
            "SELECT * FROM vinhos.carrinho_vinho " +
                    "ORDER BY numero_carrinho;";

    private static final String READ_QUERY =
            "SELECT * FROM vinhos.carrinho_vinho " +
                    "WHERE numero_carrinho = ? AND numero_vinho = ?;";

    private static final String UPDATE_QUERY =
            "UPDATE vinhos.carrinho_vinho " +
                    "SET quantidade = ? " +
                    "WHERE numero_carrinho = ? AND numero_vinho = ?;";

    private static final String DELETE_QUERY =
            "DELETE FROM vinhos.carrinho_vinho " +
                    "WHERE numero_carrinho = ? AND numero_vinho = ?;";

    public PgCarrinhoVinhoDAO(Connection connection) {
        this.connection = connection;
    }

    @Override
    public List<CarrinhoVinho> findAllVinhosOfCarrinho (int numero_carrinho) throws SQLException {
        List<CarrinhoVinho> carrinhoVinhoList = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(ALL_VINHOS_CARRINHO)) {
            statement.setInt(1, numero_carrinho);

            try (ResultSet result = statement.executeQuery()) {
                while (result.next()) {
                    CarrinhoVinho carrinhoVinho = new CarrinhoVinho();
                    CarrinhoVinhoID carrinhoVinhoID = new CarrinhoVinhoID(
                            result.getInt("numero_carrinho"),
                            result.getInt("numero_vinho")
                    );
                    carrinhoVinho.setId(carrinhoVinhoID);
                    carrinhoVinho.setQuantidade(result.getInt("quantidade"));
                    carrinhoVinho.setSubtotal(result.getDouble("subtotal"));

                    carrinhoVinhoList.add(carrinhoVinho);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(PgMyAppUserDAO.class.getName()).log(Level.SEVERE, "DAO", ex);

            throw new SQLException("Erro ao listar itens do carrinho.");
        }
        return carrinhoVinhoList;
    }

    @Override
    public void esvaziaCarrinho (int numero_carrinho) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(ESVAZIA_CARRINHO_QUERY)) {
            statement.setInt(1, numero_carrinho);

            if (statement.executeUpdate() < 1) {
                throw new SQLException("Erro ao excluir: Carrinho não encontrado.");
            }
        } catch (SQLException ex) {
            Logger.getLogger(PgMyAppUserDAO.class.getName()).log(Level.SEVERE, "DAO", ex);

            if (ex.getMessage().equals("Erro ao excluir: Carrinho não encontrado.")) {
                throw ex;
            } else {
                throw new SQLException("Erro ao esvaziar carrinho.");
            }
        }
    }

    @Override
    public void create (CarrinhoVinho carrinhoVinho) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(CREATE_QUERY)) {
            statement.setInt(1, carrinhoVinho.getId().getNumeroCarrinho());
            statement.setInt(2, carrinhoVinho.getId().getNumeroVinho());
            statement.setInt(3, carrinhoVinho.getQuantidade());

            statement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(PgMyAppUserDAO.class.getName()).log(Level.SEVERE, "DAO", ex);

            if (ex.getMessage().contains("pk_carrinho_produto")) {
                throw new SQLException("Erro ao inserir vinho no carrinho: Carrinho já possui vinho.");
            } else if (ex.getMessage().contains("not-null")) {
                throw new SQLException("Erro ao inserir vinho no carrinho: Pelo menos um campo está em branco.");
            } else {
                throw new SQLException("Erro ao inserir vinho no carrinho.");
            }
        }
    }

    @Override
    public CarrinhoVinho read (CarrinhoVinhoID id) throws SQLException {
        CarrinhoVinho carrinhoVinho = new CarrinhoVinho();

        try (PreparedStatement statement = connection.prepareStatement(READ_QUERY)) {
            statement.setInt(1, id.getNumeroCarrinho());
            statement.setInt(2, id.getNumeroVinho());

            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    CarrinhoVinhoID carrinhoVinhoID = new CarrinhoVinhoID(
                            result.getInt("numero_carrinho"),
                            result.getInt("numero_vinho")
                    );

                    carrinhoVinho.setId(carrinhoVinhoID);
                    carrinhoVinho.setQuantidade(result.getInt("quantidade"));
                    carrinhoVinho.setSubtotal(result.getDouble("subtotal"));
                } else {
                    throw new SQLException("Erro ao visualizar: Relação vinho e carrinho não encontrada");
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(PgMyAppUserDAO.class.getName()).log(Level.SEVERE, "DAO", ex);

            if (ex.getMessage().equals("Erro ao visualizar: Relação vinho e carrinho não encontrada")) {
                throw ex;
            } else {
                throw new SQLException("Erro ao visualizar relação vinho e carrinho.");
            }
        }

        return carrinhoVinho;
    }

    @Override
    public void update (CarrinhoVinho carrinhoVinho) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY)) {
            statement.setInt(1, carrinhoVinho.getQuantidade());
            statement.setInt(2, carrinhoVinho.getId().getNumeroCarrinho());
            statement.setInt(3, carrinhoVinho.getId().getNumeroVinho());

            if (statement.executeUpdate() < 1) {
                throw new SQLException("Erro ao editar: Relação vinho e carrinho não encontrada.");
            }
        } catch (SQLException ex) {
            Logger.getLogger(PgMyAppUserDAO.class.getName()).log(Level.SEVERE, "DAO", ex);

            if (ex.getMessage().equals("Erro ao editar: Relação vinho e carrinho não encontrada.")) {
                throw ex;
            } else if (ex.getMessage().contains("not-null")) {
                throw new SQLException("Erro ao editar: pelo menos um campo está em branco.");
            } else {
                throw new SQLException("Erro ao editar vinho no carrinho.");
            }
        }
    }

    @Override
    public void delete (CarrinhoVinhoID id) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(DELETE_QUERY)) {
            statement.setInt(1, id.getNumeroCarrinho());
            statement.setInt(2, id.getNumeroVinho());

            if (statement.executeUpdate() < 1) {
                throw new SQLException("Erro ao excluir: Relação vinho e carrinho não encontrada.");
            }
        } catch (SQLException ex) {
            Logger.getLogger(PgMyAppUserDAO.class.getName()).log(Level.SEVERE, "DAO", ex);

            if (ex.getMessage().equals("Erro ao excluir: Relação vinho e carrinho não encontrada.")) {
                throw ex;
            } else {
                throw new SQLException("Erro ao exluir item do carrinho.");
            }
        }
    }

    @Override
    public List<CarrinhoVinho> all() throws SQLException {

        List<CarrinhoVinho> carrinhoVinhoList = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(ALL_QUERY)) {
            ResultSet result = statement.executeQuery();

            while (result.next()) {
                CarrinhoVinho carrinhoVinho = new CarrinhoVinho();
                CarrinhoVinhoID carrinhoVinhoID = new CarrinhoVinhoID(
                        result.getInt("numero_carrinho"),
                        result.getInt("numero_vinho")
                );
                carrinhoVinho.setId(carrinhoVinhoID);
                carrinhoVinho.setQuantidade(result.getInt("quantidade"));
                carrinhoVinho.setSubtotal(result.getDouble("subtotal"));

                carrinhoVinhoList.add(carrinhoVinho);
            }
        } catch (SQLException ex) {
            Logger.getLogger(PgMyAppUserDAO.class.getName()).log(Level.SEVERE, "DAO", ex);

            throw new SQLException("Erro ao listar itens dos carrinhos.");
        }
        return carrinhoVinhoList;
    }
}
