package com.vinhos.bd.dao;

import com.vinhos.bd.dto.VinhosMaisVendidosDTO;
import com.vinhos.bd.dto.VinhosVendidosPorPeriodoDTO;
import com.vinhos.bd.model.CarrinhoVinhoID;
import com.vinhos.bd.model.CompraCarrinhoVinho;
import com.vinhos.bd.model.CompraCarrinhoVinhoID;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PgCompraCarrinhoVinhoDAO implements CompraCarrinhoVinhoDAO {

    private final Connection connection;

    private static final String LISTA_COMPRA =
            "SELECT * FROM vinhos.compra_carrinho_vinho " +
                    "WHERE numero_compra = ?;";
    private static final String CREATE_QUERY =
            "INSERT INTO vinhos.compra_carrinho_vinho " +
                    "VALUES (?, ?, ?, ?, ?);";

    private static final String ALL_QUERY =
            "SELECT * FROM vinhos.compra_carrinho_vinho " +
                    "ORDER BY numero_compra;";

    private static final String READ_QUERY =
            "SELECT * FROM vinhos.compra_carrinho_vinho " +
                    "WHERE numero_compra = ? AND numero_carrinho = ? AND numero_vinho = ?;";

    private static final String UPDATE_QUERY =
            "UPDATE vinhos.compra_carrinho_vinho " +
                    "SET quantidade = ?, subtotal = ? " +
                    "WHERE numero_compra = ? AND numero_carrinho = ? AND numero_vinho = ?;";

    private static final String DELETE_QUERY =
            "DELETE FROM vinhos.compra_carrinho_vinho " +
                    "WHERE numero_compra = ? AND numero_carrinho = ? AND numero_vinho = ?;";

    private static final String VINHOS_VENDIDOS_PERIODO =
            "SELECT DISTINCT v.nome, v.vinicula, v.ano, v.categoria, v.estilo \n" +
                    "FROM vinhos.vinhos v \n" +
                    "JOIN vinhos.compra_carrinho_vinho ccv ON v.numero = ccv.numero_vinho \n" +
                    "JOIN vinhos.compras cp ON ccv.numero_compra = cp.numero \n" +
                    "WHERE cp.data_registro BETWEEN ? AND ?;";

    private static final String VINHOS_VENDIDOS_DATA =
            "SELECT DISTINCT v.nome, v.vinicula, v.ano, v.categoria, v.estilo \n" +
                    "FROM vinhos.vinhos v \n" +
                    "JOIN vinhos.compra_carrinho_vinho ccv ON v.numero = ccv.numero_vinho \n" +
                    "JOIN vinhos.compras cp ON ccv.numero_compra = cp.numero \n" +
                    "WHERE cp.data_registro >= CURRENT_DATE - INTERVAL '?'";

    public PgCompraCarrinhoVinhoDAO(Connection connection) {
        this.connection = connection;
    }

    @Override
    public List<VinhosVendidosPorPeriodoDTO> fetchWinesSoldByData(String periodo) throws SQLException {
        List<VinhosVendidosPorPeriodoDTO> vinhosVendidosPorPeriodoDTOList = new ArrayList<>();

        String query = VINHOS_VENDIDOS_DATA.replace("?", periodo); // Substitui '?' pela entrada

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    VinhosVendidosPorPeriodoDTO vinhosVendidosPorPeriodoDTO = new VinhosVendidosPorPeriodoDTO();
                    vinhosVendidosPorPeriodoDTO.setNome(resultSet.getString("nome"));
                    vinhosVendidosPorPeriodoDTO.setVinicula(resultSet.getString("vinicula"));
                    vinhosVendidosPorPeriodoDTO.setAno(resultSet.getInt("ano"));
                    vinhosVendidosPorPeriodoDTO.setCategoria(resultSet.getString("categoria"));
                    vinhosVendidosPorPeriodoDTO.setEstilo(resultSet.getString("estilo"));

                    vinhosVendidosPorPeriodoDTOList.add(vinhosVendidosPorPeriodoDTO);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(PgVinhoDAO.class.getName()).log(Level.SEVERE, "DAO", ex);
            throw new SQLException("Erro ao buscar vinhos por periodo de tempo.", ex);
        }

        return vinhosVendidosPorPeriodoDTOList;
    }

    @Override
    public List<VinhosVendidosPorPeriodoDTO> fetchWinesSoldByPeriod(Date data_ini, Date data_fim) throws SQLException {
        List<VinhosVendidosPorPeriodoDTO> vinhosVendidosPorPeriodoDTOList = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(VINHOS_VENDIDOS_PERIODO)) {
            statement.setDate(1, data_ini);
            statement.setDate(2, data_fim);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    VinhosVendidosPorPeriodoDTO vinhosVendidosPorPeriodoDTO = new VinhosVendidosPorPeriodoDTO();
                    vinhosVendidosPorPeriodoDTO.setNome(resultSet.getString("nome"));
                    vinhosVendidosPorPeriodoDTO.setVinicula(resultSet.getString("vinicula"));
                    vinhosVendidosPorPeriodoDTO.setAno(resultSet.getInt("ano"));
                    vinhosVendidosPorPeriodoDTO.setCategoria(resultSet.getString("categoria"));
                    vinhosVendidosPorPeriodoDTO.setEstilo(resultSet.getString("estilo"));

                    vinhosVendidosPorPeriodoDTOList.add(vinhosVendidosPorPeriodoDTO);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(PgVinhoDAO.class.getName()).log(Level.SEVERE, "DAO", ex);
            throw new SQLException("Erro ao buscar vinhos por data de registro.", ex);
        }

        return vinhosVendidosPorPeriodoDTOList;
    }

    @Override
    public List<CompraCarrinhoVinho> listaCompra (int numero_compra) throws SQLException {
        List<CompraCarrinhoVinho> compraCarrinhoVinhoList = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(LISTA_COMPRA)) {
            statement.setInt(1, numero_compra);

            ResultSet result = statement.executeQuery();

            while (result.next()) {
                CompraCarrinhoVinho compraCarrinhoVinho = new CompraCarrinhoVinho();
                CarrinhoVinhoID carrinhoVinhoID = new CarrinhoVinhoID(
                        result.getInt("numero_carrinho"),
                        result.getInt("numero_vinho")
                );
                CompraCarrinhoVinhoID compraCarrinhoVinhoID = new CompraCarrinhoVinhoID(
                        result.getInt("numero_compra"),
                        carrinhoVinhoID
                );
                compraCarrinhoVinho.setId(compraCarrinhoVinhoID);
                compraCarrinhoVinho.setQuantidade(result.getInt("quantidade"));
                compraCarrinhoVinho.setSubtotal(result.getDouble("subtotal"));

                compraCarrinhoVinhoList.add(compraCarrinhoVinho);
            }
        } catch (SQLException ex) {
            Logger.getLogger(PgMyAppUserDAO.class.getName()).log(Level.SEVERE, "DAO", ex);

            throw new SQLException("Erro ao listar itens da compra.");
        }

        return compraCarrinhoVinhoList;
    }

    @Override
    public void create (CompraCarrinhoVinho compraCarrinhoVinho) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(CREATE_QUERY)) {
            statement.setInt(1, compraCarrinhoVinho.getId().getNumeroCompra());
            statement.setInt(2, compraCarrinhoVinho.getId().getCarrinhoVinhoID().getNumeroCarrinho());
            statement.setInt(3, compraCarrinhoVinho.getId().getCarrinhoVinhoID().getNumeroVinho());
            statement.setInt(4, compraCarrinhoVinho.getQuantidade());
            statement.setDouble(5, compraCarrinhoVinho.getSubtotal());

            statement.executeUpdate();
        } catch (SQLException ex) {
            if (ex.getMessage().contains("pk_compra_carrinho_produto")) {
                throw new SQLException("Erro ao inserir item de compra: Compra já possui item.");
            } else if (ex.getMessage().contains("not-null")) {
                throw new SQLException("Erro ao inserir item de compra: Pelo menos um campo está vazio.");
            } else {
                throw new SQLException("Erro ao inserir item de compra.");
            }
        }
    }

    @Override
    public CompraCarrinhoVinho read (CompraCarrinhoVinhoID id) throws SQLException {
        CompraCarrinhoVinho compraCarrinhoVinho = new CompraCarrinhoVinho();

        try (PreparedStatement statement = connection.prepareStatement(READ_QUERY)) {
            statement.setInt(1, id.getNumeroCompra());
            statement.setInt(2, id.getCarrinhoVinhoID().getNumeroCarrinho());
            statement.setInt(3, id.getCarrinhoVinhoID().getNumeroVinho());

            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    CarrinhoVinhoID carrinhoVinhoID = new CarrinhoVinhoID(
                        result.getInt("numero_carrinho"),
                        result.getInt("numero_vinho")
                    );
                    CompraCarrinhoVinhoID compraCarrinhoVinhoID = new CompraCarrinhoVinhoID(
                      result.getInt("numero_compra"),
                      carrinhoVinhoID
                    );
                    compraCarrinhoVinho.setId(compraCarrinhoVinhoID);
                    compraCarrinhoVinho.setQuantidade(result.getInt("quantidade"));
                    compraCarrinhoVinho.setSubtotal(result.getDouble("subtotal"));
                } else {
                    throw new SQLException("Erro ao visualizar: Relacao item na compra não encontrada.");
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(PgMyAppUserDAO.class.getName()).log(Level.SEVERE, "DAO", ex);

            if (ex.getMessage().equals("Erro ao visualizar: Relacao item na compra não encontrada.")) {
                throw ex;
            } else {
                throw new SQLException("Erro ao visualizar relação item na compra.");
            }
        }

        return compraCarrinhoVinho;
    }

    @Override
    public void update (CompraCarrinhoVinho carrinhoVinho) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY)) {
            statement.setInt(1, carrinhoVinho.getQuantidade());
            statement.setDouble(2, carrinhoVinho.getSubtotal());
            statement.setInt(3, carrinhoVinho.getId().getNumeroCompra());
            statement.setInt(4, carrinhoVinho.getId().getCarrinhoVinhoID().getNumeroCarrinho());
            statement.setInt(5, carrinhoVinho.getId().getCarrinhoVinhoID().getNumeroVinho());

            if (statement.executeUpdate() < 1) {
                throw new SQLException("Erro ao editar: Relacao item na compra, não encontrada.");
            }
        } catch (SQLException ex) {
            Logger.getLogger(PgMyAppUserDAO.class.getName()).log(Level.SEVERE, "DAO", ex);

            if (ex.getMessage().equals("Erro ao editar: Relacao item na compra, não encontrada.")) {
                throw ex;
            } else if (ex.getMessage().contains("not-null")) {
                throw new SQLException("Erro ao editar: pelo menos um campo está em branco.");
            } else {
                throw new SQLException("Erro ao editar item da compra.");
            }
        }
    }

    @Override
    public void delete (CompraCarrinhoVinhoID id) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(DELETE_QUERY)) {
            statement.setInt(1, id.getNumeroCompra());
            statement.setInt(2, id.getCarrinhoVinhoID().getNumeroCarrinho());
            statement.setInt(3, id.getCarrinhoVinhoID().getNumeroVinho());

            if (statement.executeUpdate() < 1) {
                throw new SQLException("Erro ao excluir: Relação item e compra não encontrada.");
            }
        } catch (SQLException ex) {
            Logger.getLogger(PgMyAppUserDAO.class.getName()).log(Level.SEVERE, "DAO", ex);

            if (ex.getMessage().equals("Erro ao excluir: Relação item e compra não encontrada.")) {
                throw ex;
            } else {
                throw new SQLException("Erro ao exluir item da compra.");
            }
        }
    }

    @Override
    public List<CompraCarrinhoVinho> all () throws SQLException {
        List<CompraCarrinhoVinho> compraCarrinhoVinhoList = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(ALL_QUERY)) {
            ResultSet result = statement.executeQuery();

            while (result.next()) {
                CompraCarrinhoVinho compraCarrinhoVinho = new CompraCarrinhoVinho();
                CarrinhoVinhoID carrinhoVinhoID = new CarrinhoVinhoID(
                        result.getInt("numero_carrinho"),
                        result.getInt("numero_vinho")
                );
                CompraCarrinhoVinhoID compraCarrinhoVinhoID = new CompraCarrinhoVinhoID(
                        result.getInt("numero_compra"),
                        carrinhoVinhoID
                );
                compraCarrinhoVinho.setId(compraCarrinhoVinhoID);
                compraCarrinhoVinho.setQuantidade(result.getInt("quantidade"));
                compraCarrinhoVinho.setSubtotal(result.getDouble("subtotal"));

                compraCarrinhoVinhoList.add(compraCarrinhoVinho);
            }
        } catch (SQLException ex) {
            Logger.getLogger(PgMyAppUserDAO.class.getName()).log(Level.SEVERE, "DAO", ex);

            throw new SQLException("Erro ao listar itens das compras.");
        }

        return compraCarrinhoVinhoList;
    }
}
