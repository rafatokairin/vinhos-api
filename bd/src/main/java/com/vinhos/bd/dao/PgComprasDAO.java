package com.vinhos.bd.dao;

import com.vinhos.bd.dto.ComprasPorPeriodoDTO;
import com.vinhos.bd.model.Compras;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PgComprasDAO implements ComprasDAO {

    private final Connection connection;

    private static final String CREATE_QUERY =
            "INSERT INTO vinhos.compras(numero, email_usuario) " +
                    "VALUES(?, ?);";

    private static final String READ_QUERY =
            "SELECT * FROM vinhos.compras WHERE numero = ?;";

    private static final String UPDATE_QUERY =
            "UPDATE vinhos.compras SET valor_total = ? WHERE numero = ?;";

    private static final String DELETE_QUERY =
            "DELETE FROM vinhos.compras WHERE numero = ?;";

    private static final String ALL_QUERY =
            "SELECT * FROM vinhos.compras ORDER BY numero;";

    private static final String INTERVAL_QUERY =
            "SELECT DATE(c.data_registro) AS data, SUM(c.valor_total) AS valor_total_por_dia " +
                    "FROM vinhos.compras c " +
                    "WHERE c.data_registro >= NOW() - INTERVAL '?' " +
                    "GROUP BY DATE(c.data_registro) " +
                    "ORDER BY data ASC;";

    public PgComprasDAO(Connection connection) {
        this.connection = connection;
    }

    @Override
    public List<ComprasPorPeriodoDTO> fetchBuysPerDay(String periodo) throws SQLException {
        List<ComprasPorPeriodoDTO> comprasPorPeriodoDTOList = new ArrayList<>();

        String query = INTERVAL_QUERY.replace("?", periodo);

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    ComprasPorPeriodoDTO comprasPorPeriodoDTO = new ComprasPorPeriodoDTO();
                    comprasPorPeriodoDTO.setValor_total(resultSet.getDouble("valor_total_por_dia"));
                    comprasPorPeriodoDTO.setData(resultSet.getDate("data"));

                    comprasPorPeriodoDTOList.add(comprasPorPeriodoDTO);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(PgComprasDAO.class.getName()).log(Level.SEVERE, "DAO", ex);
            throw new SQLException("Erro ao listar compras por período.", ex);
        }

        return comprasPorPeriodoDTOList;
    }

    @Override
    public void create(Compras compras) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(CREATE_QUERY)) {
            statement.setInt(1, compras.getNumero()); // Definindo o número da compra
            statement.setString(2, compras.getEmail_usuario());
            statement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(PgComprasDAO.class.getName()).log(Level.SEVERE, "DAO", ex);
            throw new SQLException("Erro ao criar compra.");
        }
    }

    @Override
    public Compras read(Integer numero) throws SQLException {
        Compras compra = null;

        try (PreparedStatement statement = connection.prepareStatement(READ_QUERY)) {
            statement.setInt(1, numero); // Definindo o número da compra a ser lida

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    compra = new Compras();
                    compra.setNumero(resultSet.getInt("numero"));
                    compra.setValor_total(resultSet.getDouble("valor_total"));
                    compra.setDataRegistro(resultSet.getDate("data_registro"));
                    compra.setEmail_usuario(resultSet.getString("email_usuario"));
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(PgComprasDAO.class.getName()).log(Level.SEVERE, "DAO", ex);
            throw new SQLException("Erro ao buscar compra.");
        }
        return compra;
    }

    @Override
    public void update(Compras compras) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY)) {
            statement.setDouble(1, compras.getValor_total()); // Atualizando o valor total
            statement.setInt(2, compras.getNumero()); // Definindo o número da compra a ser atualizada

            if (statement.executeUpdate() < 1) {
                throw new SQLException("Erro ao atualizar: compra não encontrada.");
            }
        } catch (SQLException ex) {
            Logger.getLogger(PgComprasDAO.class.getName()).log(Level.SEVERE, "DAO", ex);
            throw new SQLException("Erro ao atualizar compra.");
        }
    }

    @Override
    public void delete(Integer numero) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(DELETE_QUERY)) {
            statement.setInt(1, numero); // Definindo o número da compra a ser deletada

            if (statement.executeUpdate() < 1) {
                throw new SQLException("Erro ao excluir: compra não encontrada.");
            }
        } catch (SQLException ex) {
            Logger.getLogger(PgComprasDAO.class.getName()).log(Level.SEVERE, "DAO", ex);
            throw new SQLException("Erro ao excluir compra.");
        }
    }

    @Override
    public List<Compras> all() throws SQLException {
        List<Compras> comprasList = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(ALL_QUERY)) {
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Compras compra = new Compras();
                    compra.setNumero(resultSet.getInt("numero"));
                    compra.setValor_total(resultSet.getDouble("valor_total"));
                    compra.setDataRegistro(resultSet.getDate("data_registro"));
                    compra.setEmail_usuario(resultSet.getString("email_usuario"));
                    comprasList.add(compra);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(PgComprasDAO.class.getName()).log(Level.SEVERE, "DAO", ex);
            throw new SQLException("Erro ao listar todas as compras.");
        }
        return comprasList;
    }
}
