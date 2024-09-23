package com.vinhos.bd.dao;

import com.vinhos.bd.model.Vinho;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PgVinhoDAO implements VinhoDAO {

    private final Connection connection;

    private static final String FIND_VINHOS_QUERY =
            "SELECT * FROM vinhos.vinhos " +
                    "WHERE (nome = ? OR ? IS NULL) " +
                    "AND (ano = ? OR ? = 0) " +
                    "AND (uva = ? OR ? IS NULL) " +
                    "AND (vinicula = ? OR ? IS NULL) " +
                    "AND (regiao = ? OR ? IS NULL) " +
                    "AND (categoria = ? OR ? IS NULL) " +
                    "AND (estilo = ? OR ? IS NULL) " +
                    "AND (preco <= ? OR ? = 0);";

    private static final String CREATE_QUERY =
            "INSERT INTO vinhos.vinhos(nome, ano, descricao, uva, vinicula, regiao, preco," +
                    " quantidade_estoque, img_path, categoria, estilo) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

    private static final String ALL_QUERY =
            "SELECT * FROM vinhos.vinhos " +
                    "ORDER BY numero;";

    private static final String READ_QUERY =
            "SELECT * FROM vinhos.vinhos " +
                    "WHERE numero = ?;";

    private static final String UPDATE_QUERY =
            "UPDATE vinhos.vinhos " +
                    "SET nome = CASE WHEN ? IS NOT NULL THEN ? ELSE nome END, " +
                    "ano = CASE WHEN ? != 0 THEN ? ELSE ano END, " +
                    "descricao = CASE WHEN ? IS NOT NULL THEN ? ELSE descricao END, " +
                    "uva = CASE WHEN ? IS NOT NULL THEN ? ELSE uva END, " +
                    "vinicula = CASE WHEN ? IS NOT NULL THEN ? ELSE vinicula END, " +
                    "regiao = CASE WHEN ? IS NOT NULL THEN ? ELSE regiao END, " +
                    "preco = CASE WHEN ? != 0 THEN ? ELSE preco END, " +
                    "quantidade_estoque = CASE WHEN ? != 0 THEN ? ELSE quantidade_estoque END, " +
                    "img_path = CASE WHEN ? IS NOT NULL THEN ? ELSE img_path END, " +
                    "categoria = CASE WHEN ? IS NOT NULL THEN ? ELSE categoria END, " +
                    "estilo = CASE WHEN ? IS NOT NULL THEN ? ELSE estilo END " +
                    "WHERE numero = ?;";

    private static final String DELETE_QUERY =
            "DELETE FROM vinhos.vinhos " +
                    "WHERE numero = ?;";

    public PgVinhoDAO(Connection connection) {
        this.connection = connection;
    }

    @Override
    public List<Vinho> findVinhos(Vinho vinho) throws SQLException {

        List<Vinho> vinhosList = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(FIND_VINHOS_QUERY)) {
            // Configura os parâmetros da query
            statement.setString(1, vinho.getNome());
            statement.setString(2, vinho.getNome());

            statement.setInt(3, vinho.getAno());
            statement.setInt(4, vinho.getAno());

            statement.setString(5, vinho.getUva());
            statement.setString(6, vinho.getUva());

            statement.setString(7, vinho.getVinicula());
            statement.setString(8, vinho.getVinicula());

            statement.setString(9, vinho.getRegiao());
            statement.setString(10, vinho.getRegiao());

            statement.setString(11, vinho.getCategoria());
            statement.setString(12, vinho.getCategoria());

            statement.setString(13, vinho.getEstilo());
            statement.setString(14, vinho.getEstilo());

            statement.setDouble(15, vinho.getPreco());
            statement.setDouble(16, vinho.getPreco());

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Vinho vinhoResult = new Vinho();
                    vinhoResult.setNumero(resultSet.getInt("numero"));
                    vinhoResult.setNome(resultSet.getString("nome"));
                    vinhoResult.setAno(resultSet.getInt("ano"));
                    vinhoResult.setDescricao(resultSet.getString("descricao"));
                    vinhoResult.setUva(resultSet.getString("uva"));
                    vinhoResult.setVinicula(resultSet.getString("vinicula"));
                    vinhoResult.setRegiao(resultSet.getString("regiao"));
                    vinhoResult.setCategoria(resultSet.getString("categoria"));
                    vinhoResult.setEstilo(resultSet.getString("estilo"));
                    vinhoResult.setPreco(resultSet.getDouble("preco"));
                    vinhoResult.setQuantidadeEstoque(resultSet.getInt("quantidade_estoque"));
                    vinhoResult.setImgPath(resultSet.getString("img_path"));

                    vinhosList.add(vinhoResult);
                }
            }

        } catch (SQLException ex) {
            Logger.getLogger(PgVinhoDAO.class.getName()).log(Level.SEVERE, "DAO", ex);
            throw new SQLException("Erro ao listar vinhos.");
        }

        return vinhosList;
    }

    @Override
    public void create(Vinho vinho) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(CREATE_QUERY)) {
            statement.setString(1, vinho.getNome());
            statement.setInt(2, vinho.getAno());
            statement.setString(3, vinho.getDescricao());
            statement.setString(4, vinho.getUva());
            statement.setString(5, vinho.getVinicula());
            statement.setString(6, vinho.getRegiao());
            statement.setDouble(7, vinho.getPreco());
            statement.setInt(8, vinho.getQuantidadeEstoque());
            statement.setString(9, vinho.getImgPath());
            statement.setString(10, vinho.getCategoria());
            statement.setString(11, vinho.getEstilo());

            System.out.println(vinho.getQuantidadeEstoque());
            System.out.println(vinho.getImgPath());

            statement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(PgVinhoDAO.class.getName()).log(Level.SEVERE, "DAO", ex);
            throw new SQLException("Erro ao criar vinho.");
        }
    }

    @Override
    public Vinho read(Integer numero) throws SQLException {
        Vinho vinho = null;

        try (PreparedStatement statement = connection.prepareStatement(READ_QUERY)) {
            statement.setInt(1, numero);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    vinho = new Vinho();
                    vinho.setNumero(resultSet.getInt("numero"));
                    vinho.setNome(resultSet.getString("nome"));
                    vinho.setAno(resultSet.getInt("ano"));
                    vinho.setDescricao(resultSet.getString("descricao"));
                    vinho.setUva(resultSet.getString("uva"));
                    vinho.setVinicula(resultSet.getString("vinicula"));
                    vinho.setRegiao(resultSet.getString("regiao"));
                    vinho.setCategoria(resultSet.getString("categoria"));
                    vinho.setEstilo(resultSet.getString("estilo"));
                    vinho.setPreco(resultSet.getDouble("preco"));
                    vinho.setQuantidadeEstoque(resultSet.getInt("quantidade_estoque"));
                    vinho.setImgPath(resultSet.getString("img_path"));
                } else {
                    throw new SQLException("Erro ao visualizar: vinho não encontrado.");
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(PgVinhoDAO.class.getName()).log(Level.SEVERE, "DAO", ex);
            throw new SQLException("Erro ao visualizar vinho.");
        }

        return vinho;
    }

    @Override
    public void update(Vinho vinho) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY)) {
            statement.setString(1, vinho.getNome());
            statement.setString(2, vinho.getNome());

            statement.setInt(3, vinho.getAno());
            statement.setInt(4, vinho.getAno());

            statement.setString(5, vinho.getDescricao());
            statement.setString(6, vinho.getDescricao());

            statement.setString(7, vinho.getUva());
            statement.setString(8, vinho.getUva());

            statement.setString(9, vinho.getVinicula());
            statement.setString(10, vinho.getVinicula());

            statement.setString(11, vinho.getRegiao());
            statement.setString(12, vinho.getRegiao());

            statement.setDouble(13, vinho.getPreco());
            statement.setDouble(14, vinho.getPreco());

            statement.setInt(15, vinho.getQuantidadeEstoque());
            statement.setInt(16, vinho.getQuantidadeEstoque());

            statement.setString(17, vinho.getImgPath());
            statement.setString(18, vinho.getImgPath());

            statement.setString(19, vinho.getCategoria());
            statement.setString(20, vinho.getCategoria());

            statement.setString(21, vinho.getEstilo());
            statement.setString(22, vinho.getEstilo());

            statement.setInt(23, vinho.getNumero());

            if (statement.executeUpdate() < 1) {
                throw new SQLException("Erro ao editar: vinho não encontrado.");
            }
        } catch (SQLException ex) {
            Logger.getLogger(PgVinhoDAO.class.getName()).log(Level.SEVERE, "DAO", ex);
            throw new SQLException("Erro ao editar vinho.");
        }
    }

    @Override
    public void delete(Integer numero) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(DELETE_QUERY)) {
            statement.setInt(1, numero);

            if (statement.executeUpdate() < 1) {
                throw new SQLException("Erro ao excluir: vinho não encontrado.");
            }
        } catch (SQLException ex) {
            Logger.getLogger(PgVinhoDAO.class.getName()).log(Level.SEVERE, "DAO", ex);
            throw new SQLException("Erro ao excluir vinho.");
        }
    }

    @Override
    public List<Vinho> all() throws SQLException {
        List<Vinho> vinhosList = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(ALL_QUERY)) {
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Vinho vinho = new Vinho();
                    vinho.setNumero(resultSet.getInt("numero"));
                    vinho.setNome(resultSet.getString("nome"));
                    vinho.setAno(resultSet.getInt("ano"));
                    vinho.setDescricao(resultSet.getString("descricao"));
                    vinho.setUva(resultSet.getString("uva"));
                    vinho.setVinicula(resultSet.getString("vinicula"));
                    vinho.setRegiao(resultSet.getString("regiao"));
                    vinho.setCategoria(resultSet.getString("categoria"));
                    vinho.setEstilo(resultSet.getString("estilo"));
                    vinho.setPreco(resultSet.getDouble("preco"));
                    vinho.setQuantidadeEstoque(resultSet.getInt("quantidade_estoque"));
                    vinho.setImgPath(resultSet.getString("img_path"));

                    vinhosList.add(vinho);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(PgVinhoDAO.class.getName()).log(Level.SEVERE, "DAO", ex);
            throw new SQLException("Erro ao listar todos os vinhos.");
        }

        return vinhosList;
    }
}
