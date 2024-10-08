package com.vinhos.bd.dao;

import com.vinhos.bd.dto.ComprasPorDiaDaSemanaDTO;
import com.vinhos.bd.dto.ComprasPorPeriodoDTO;
import com.vinhos.bd.dto.ComprasPorUsuarioDTO;
import com.vinhos.bd.dto.ValorCompradoPorDiaDTO;
import com.vinhos.bd.model.Compras;

import java.sql.*;
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

    private static final String RETRIEVE_BUYS_PER_DAY_BY_DATE_RANGE =
            "SELECT DATE(c.data_registro) AS data, SUM(c.valor_total) AS valor_total_por_dia\n" +
                    "FROM vinhos.compras c\n" +
                    "WHERE c.data_registro BETWEEN ? AND ?\n" +
                    "GROUP BY DATE(c.data_registro)\n" +
                    "ORDER BY data ASC;";

    private static final String COMPRAS_DIA_DA_SEMANA_PERIODO =
            "WITH \n" +
                    "dias AS (\n" +
                    "    SELECT unnest(ARRAY['Domingo', 'Segunda', 'Terça', 'Quarta', 'Quinta', 'Sexta', 'Sábado']) AS dia_da_semana,\n" +
                    "           generate_series(1, 7) AS dia_ordenacao\n" +
                    "),\n" +
                    "vendas AS (\n" +
                    "    SELECT vinhos.dia_da_semana(c.data_registro) AS dia_da_semana, \n" +
                    "           SUM(quantidade) AS quantidade_vendida,\n" +
                    "           SUM(valor_total) AS valor_total\n" +
                    "    FROM vinhos.compras c\n" +
                    "    JOIN vinhos.compra_carrinho_vinho ccv\n" +
                    "    ON c.numero = ccv.numero_compra\n" +
                    "    WHERE c.data_registro >= CURRENT_DATE - INTERVAL '?'\n" +
                    "    GROUP BY dia_da_semana\n" +
                    ")\n" +
                    "\n" +
                    "SELECT \n" +
                    "       d.dia_da_semana::VARCHAR AS dia_da_semana,\n" +
                    "       COALESCE(v.quantidade_vendida, 0)::NUMERIC AS quantidade_vendida,\n" +
                    "       COALESCE(v.valor_total, 0)::NUMERIC AS valor_total\n" +
                    "FROM dias d\n" +
                    "LEFT JOIN vendas v ON d.dia_da_semana = v.dia_da_semana\n" +
                    "ORDER BY d.dia_ordenacao;\n";

    private static final String COMPRAS_DIA_DA_SEMANA_DATA =
            "WITH \n" +
                    "dias AS (\n" +
                    "    SELECT unnest(ARRAY['Domingo', 'Segunda', 'Terça', 'Quarta', 'Quinta', 'Sexta', 'Sábado']) AS dia_da_semana,\n" +
                    "           generate_series(1, 7) AS dia_ordenacao\n" +
                    "),\n" +
                    "vendas AS (\n" +
                    "    SELECT vinhos.dia_da_semana(c.data_registro) AS dia_da_semana, \n" +
                    "           SUM(quantidade) AS quantidade_vendida,\n" +
                    "           SUM(valor_total) AS valor_total\n" +
                    "    FROM vinhos.compras c\n" +
                    "    JOIN vinhos.compra_carrinho_vinho ccv\n" +
                    "    ON c.numero = ccv.numero_compra\n" +
                    "    WHERE c.data_registro BETWEEN ? AND ?\n" +
                    "    GROUP BY dia_da_semana\n" +
                    ")\n" +
                    "\n" +
                    "SELECT \n" +
                    "       d.dia_da_semana::VARCHAR AS dia_da_semana,\n" +
                    "       COALESCE(v.quantidade_vendida, 0)::NUMERIC AS quantidade_vendida,\n" +
                    "       COALESCE(v.valor_total, 0)::NUMERIC AS valor_total\n" +
                    "FROM dias d\n" +
                    "LEFT JOIN vendas v ON d.dia_da_semana = v.dia_da_semana\n" +
                    "ORDER BY d.dia_ordenacao;";

    private static final String COMPRAS_POR_USUARIO_DATA =
            "SELECT u.nome AS nome_usuario, COUNT(c.numero) AS total_compras, SUM(c.valor_total) AS total_gasto\n" +
                    "FROM vinhos.usuarios u\n" +
                    "JOIN vinhos.compras c ON u.email = c.email_usuario\n" +
                    "WHERE c.data_registro BETWEEN ? AND ?\n" +
                    "GROUP BY u.nome\n" +
                    "ORDER BY total_gasto DESC;";

    private static final String COMPRAS_POR_USUARIO_PERIODO =
            "SELECT u.nome AS nome_usuario, COUNT(c.numero) AS total_compras, SUM(c.valor_total) AS total_gasto\n" +
                    "FROM vinhos.usuarios u\n" +
                    "JOIN vinhos.compras c ON u.email = c.email_usuario\n" +
                    "WHERE c.data_registro >= CURRENT_DATE - INTERVAL '?'\n" +
                    "GROUP BY u.nome\n" +
                    "ORDER BY total_gasto DESC;";

    private static final String VALOR_COMPRADO_DIA_POR_DATA =
            "SELECT DATE(c.data_registro) AS data, SUM(c.valor_total) AS valor_total_por_dia\n" +
                    "FROM vinhos.compras c\n" +
                    "WHERE c.data_registro BETWEEN ? AND ?\n" +
                    "GROUP BY DATE(c.data_registro)\n" +
                    "ORDER BY data DESC;";

    private static final String VALOR_COMPRADO_DIA_POR_PERIODO =
            "SELECT DATE(c.data_registro) AS data, SUM(c.valor_total) AS valor_total_por_dia\n" +
                    "FROM vinhos.compras c\n" +
                    "WHERE c.data_registro >= CURRENT_DATE - INTERVAL '?'\n" +
                    "GROUP BY DATE(c.data_registro)\n" +
                    "ORDER BY data DESC;";

    public PgComprasDAO(Connection connection) {
        this.connection = connection;
    }

    @Override
    public List<ValorCompradoPorDiaDTO> fetchBuysPerDayByPeriodo (String periodo) throws SQLException {
        List<ValorCompradoPorDiaDTO> valorCompradoPorDiaDTOList = new ArrayList<>();

        String query = VALOR_COMPRADO_DIA_POR_PERIODO.replace("?", periodo);

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    ValorCompradoPorDiaDTO valorCompradoPorDiaDTO = new ValorCompradoPorDiaDTO();
                    valorCompradoPorDiaDTO.setData(resultSet.getDate("data"));
                    valorCompradoPorDiaDTO.setValor_total_por_dia(resultSet.getInt("valor_total_por_dia"));

                    valorCompradoPorDiaDTOList.add(valorCompradoPorDiaDTO);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(PgComprasDAO.class.getName()).log(Level.SEVERE, "DAO", ex);
            throw new SQLException("Erro ao listar compras por período.", ex);
        }
        return valorCompradoPorDiaDTOList;
    }

    @Override
    public List<ValorCompradoPorDiaDTO> fetchBuysPerDayByData (Date data1, Date data2) throws SQLException {
        List<ValorCompradoPorDiaDTO> valorCompradoPorDiaDTOList = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(VALOR_COMPRADO_DIA_POR_DATA)) {
            statement.setDate(1, data1);
            statement.setDate(2, data2);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    ValorCompradoPorDiaDTO valorCompradoPorDiaDTO = new ValorCompradoPorDiaDTO();
                    valorCompradoPorDiaDTO.setData(resultSet.getDate("data"));
                    valorCompradoPorDiaDTO.setValor_total_por_dia(resultSet.getInt("valor_total_por_dia"));

                    valorCompradoPorDiaDTOList.add(valorCompradoPorDiaDTO);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(PgComprasDAO.class.getName()).log(Level.SEVERE, "DAO", ex);
            throw new SQLException("Erro ao listar compras por período.", ex);
        }
        return valorCompradoPorDiaDTOList;
    }

    @Override
    public List<ComprasPorUsuarioDTO> fetchBuysByUserPeriodo (String periodo) throws SQLException {
        List<ComprasPorUsuarioDTO> comprasPorUsuarioDTOList = new ArrayList<>();

        String query = COMPRAS_POR_USUARIO_PERIODO.replace("?", periodo);

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    ComprasPorUsuarioDTO comprasPorUsuarioDTO = new ComprasPorUsuarioDTO();
                    comprasPorUsuarioDTO.setNome_usuario(resultSet.getString("nome_usuario"));
                    comprasPorUsuarioDTO.setTotal_compras(resultSet.getInt("total_compras"));
                    comprasPorUsuarioDTO.setTotal_gasto(resultSet.getDouble("total_gasto"));

                    comprasPorUsuarioDTOList.add(comprasPorUsuarioDTO);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(PgComprasDAO.class.getName()).log(Level.SEVERE, "DAO", ex);
            throw new SQLException("Erro ao listar compras por período.", ex);
        }
        return comprasPorUsuarioDTOList;
    }

    @Override
    public List<ComprasPorUsuarioDTO> fetchBuysByUserData (Date data1, Date data2) throws SQLException {
        List<ComprasPorUsuarioDTO> comprasPorUsuarioDTOList = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(COMPRAS_POR_USUARIO_DATA)) {
            statement.setDate(1, data1);
            statement.setDate(2, data2);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    ComprasPorUsuarioDTO comprasPorUsuarioDTO = new ComprasPorUsuarioDTO();
                    comprasPorUsuarioDTO.setNome_usuario(resultSet.getString("nome_usuario"));
                    comprasPorUsuarioDTO.setTotal_compras(resultSet.getInt("total_compras"));
                    comprasPorUsuarioDTO.setTotal_gasto(resultSet.getDouble("total_gasto"));

                    comprasPorUsuarioDTOList.add(comprasPorUsuarioDTO);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(PgComprasDAO.class.getName()).log(Level.SEVERE, "DAO", ex);
            throw new SQLException("Erro ao listar compras por período.", ex);
        }
        return comprasPorUsuarioDTOList;
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
    public List<ComprasPorPeriodoDTO> fetchBuysByDate (Date data1, Date data2) throws SQLException {
        List<ComprasPorPeriodoDTO> comprasPorPeriodoDTOList = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(RETRIEVE_BUYS_PER_DAY_BY_DATE_RANGE)) {
            statement.setDate(1, data1);
            statement.setDate(2, data2);

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

    @Override
    public List<ComprasPorDiaDaSemanaDTO> fetchBuysByWeekdayPeriodo (String periodo) throws SQLException {
        List<ComprasPorDiaDaSemanaDTO> comprasPorDiaDaSemanaDTOList = new ArrayList<>();

        String query = COMPRAS_DIA_DA_SEMANA_PERIODO.replace("?", periodo);

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    ComprasPorDiaDaSemanaDTO comprasPorDiaDaSemanaDTO = new ComprasPorDiaDaSemanaDTO();
                    comprasPorDiaDaSemanaDTO.setDia_da_semana(resultSet.getString("dia_da_semana"));
                    comprasPorDiaDaSemanaDTO.setQuantidade_vendida(resultSet.getInt("quantidade_vendida"));
                    comprasPorDiaDaSemanaDTO.setValor_total(resultSet.getDouble("valor_total"));

                    comprasPorDiaDaSemanaDTOList.add(comprasPorDiaDaSemanaDTO);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(PgComprasDAO.class.getName()).log(Level.SEVERE, "DAO", ex);
            throw new SQLException("Erro ao listar compras por período.", ex);
        }

        return comprasPorDiaDaSemanaDTOList;
    }

    @Override
    public List<ComprasPorDiaDaSemanaDTO> fetchBuysByWeekdayData (Date data1, Date data2) throws SQLException {
        List<ComprasPorDiaDaSemanaDTO> comprasPorDiaDaSemanaDTOList = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(COMPRAS_DIA_DA_SEMANA_DATA)) {
            statement.setDate(1, data1);
            statement.setDate(2, data2);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    ComprasPorDiaDaSemanaDTO comprasPorDiaDaSemanaDTO = new ComprasPorDiaDaSemanaDTO();
                    comprasPorDiaDaSemanaDTO.setDia_da_semana(resultSet.getString("dia_da_semana"));
                    comprasPorDiaDaSemanaDTO.setQuantidade_vendida(resultSet.getInt("quantidade_vendida"));
                    comprasPorDiaDaSemanaDTO.setValor_total(resultSet.getDouble("valor_total"));

                    comprasPorDiaDaSemanaDTOList.add(comprasPorDiaDaSemanaDTO);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(PgComprasDAO.class.getName()).log(Level.SEVERE, "DAO", ex);
            throw new SQLException("Erro ao listar compras por período.", ex);
        }

        return comprasPorDiaDaSemanaDTOList;
    }
}
