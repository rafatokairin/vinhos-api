package com.vinhos.bd.dao;

import com.vinhos.bd.dto.CategoriaMaisVendidasFaixaIdadeDTO;
import com.vinhos.bd.dto.ComprasPorPeriodoDTO;
import com.vinhos.bd.dto.ComprasPorSexoDTO;
import com.vinhos.bd.dto.ComprasProFaixaEtariaDTO;
import com.vinhos.bd.model.MyAppUser;

import javax.swing.undo.UndoableEditSupport;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PgMyAppUserDAO implements MyAppUserDAO {

    private final Connection connection;

    private static final String AUTHENTICATE_QUERY =
            "SELECT email, nome " +
                    "FROM vinhos.usuarios " +
                    "WHERE email = ? AND senha = md5(?);";

    private static final String CREATE_QUERY =
            "INSERT INTO vinhos.usuarios(nome, email, senha, sexo, data_nascimento) " +
                    "VALUES(?, ?, md5(?), ?, ?);";

    private static final String ALL_QUERY =
            "SELECT nome, email, data_registro " +
                    "FROM vinhos.usuarios " +
                    "ORDER BY nome;";

    private static final String READ_QUERY =
            "SELECT nome, email, data_registro  " +
                    "FROM vinhos.usuarios " +
                    "WHERE email = ?;";

    private static final String UPDATE_QUERY =
            "UPDATE vinhos.usuarios " +
                    "SET nome = ? " +
                    "WHERE email = ?;";

    private static final String UPDATE_WITH_PASSWORD_QUERY =
            "UPDATE vinhos.usuarios " +
                    "SET nome = ?, senha = md5(?) " +
                    "WHERE email = ?;";

    private static final String DELETE_QUERY =
            "DELETE FROM vinhos.usuarios " +
                    "WHERE email = ?;";

    private static final String VENDAS_VINHOS_POR_SEXO_PERIODO =
            "SELECT sexo, SUM(quantidade_vendida) AS quantidade_vendida, SUM(valor_total) AS valor_total\n" +
                    "FROM vinhos.usuarios u\n" +
                    "JOIN (\n" +
                    "\tSELECT quantidade_vendida, valor_total, email_usuario, data_registro\n" +
                    "\tFROM vinhos.compras c\n" +
                    "\tJOIN (\n" +
                    "\t\tSELECT numero_compra, SUM(quantidade) AS quantidade_vendida\n" +
                    "\t\tFROM vinhos.compra_carrinho_vinho\n" +
                    "\t\tGROUP BY numero_compra\n" +
                    "\t) AS ccv\n" +
                    "\tON c.numero = ccv.numero_compra\n" +
                    ") compras\n" +
                    "ON u.email = compras.email_usuario AND compras.data_registro >= CURRENT_DATE - INTERVAL '?'\n" +
                    "GROUP BY sexo\n" +
                    "ORDER BY valor_total DESC;\n";

    private static final String VENDAS_VINHOS_POR_SEXO_DATA =
            "SELECT sexo, SUM(quantidade_vendida) AS quantidade_vendida, SUM(valor_total) AS valor_total\n" +
                    "FROM vinhos.usuarios u\n" +
                    "JOIN (\n" +
                    "\tSELECT quantidade_vendida, valor_total, email_usuario, data_registro\n" +
                    "\tFROM vinhos.compras c\n" +
                    "\tJOIN (\n" +
                    "\t\tSELECT numero_compra, SUM(quantidade) AS quantidade_vendida\n" +
                    "\t\tFROM vinhos.compra_carrinho_vinho\n" +
                    "\t\tGROUP BY numero_compra\n" +
                    "\t) AS ccv\n" +
                    "\tON c.numero = ccv.numero_compra\n" +
                    ") compras\n" +
                    "ON u.email = compras.email_usuario AND compras.data_registro BETWEEN ? AND ?\n" +
                    "GROUP BY sexo\n" +
                    "ORDER BY valor_total DESC;";

    private static final String VENDAS_VINHOS_POR_FAIXA_ETARIA_PERIODO =
            "SELECT faixa_etaria(data_nascimento) AS faixa_etaria, SUM(quantidade_vendida) AS quantidade_vendida, SUM(valor_total) AS valor_total\n" +
                    "FROM vinhos.usuarios u\n" +
                    "JOIN (\n" +
                    "\tSELECT email_usuario, quantidade_vendida, valor_total, c.data_registro\n" +  // Adicione c.data_registro aqui
                    "\tFROM vinhos.compras c\n" +
                    "\tJOIN (\n" +
                    "\t\tSELECT numero_compra, SUM(quantidade) AS quantidade_vendida\n" +
                    "\t\tFROM vinhos.compra_carrinho_vinho\n" +
                    "\t\tGROUP BY numero_compra\n" +
                    "\t) ccv\n" +
                    "\tON c.numero = ccv.numero_compra\n" +
                    ") compras\n" +
                    "ON u.email = compras.email_usuario AND compras.data_registro >= CURRENT_DATE - INTERVAL '?'\n" +
                    "GROUP BY faixa_etaria\n" +
                    "ORDER BY valor_total DESC;";


    private static final String VENDAS_VINHOS_POR_FAIXA_ETARIA_DATA =
            "SELECT faixa_etaria(data_nascimento) AS faixa_etaria, SUM(quantidade_vendida) AS quantidade_vendida, SUM(valor_total) AS valor_total\n" +
                    "FROM vinhos.usuarios u\n" +
                    "JOIN (\n" +
                    "\tSELECT email_usuario, quantidade_vendida, valor_total, c.data_registro\n" +
                    "\tFROM vinhos.compras c\n" +
                    "\tJOIN (\n" +
                    "\t\tSELECT numero_compra, SUM(quantidade) AS quantidade_vendida\n" +
                    "\t\tFROM vinhos.compra_carrinho_vinho\n" +
                    "\t\tGROUP BY numero_compra\n" +
                    "\t) ccv\n" +
                    "\tON c.numero = ccv.numero_compra\n" +
                    ") compras\n" +
                    "ON u.email = compras.email_usuario AND compras.data_registro BETWEEN ? AND ?\n" +
                    "GROUP BY faixa_etaria\n" +
                    "ORDER BY valor_total DESC;";

    private static final String CATEGORIAS_MAIS_VENDIDAS_FAIXA_ETARIA_PERIODO =
            "SELECT faixa_etaria(data_nascimento) AS faixa_etaria, categoria, \n" +
                    "\t\tSUM(quantidade_vendida) AS quantidade_vendida, SUM(valor_total) AS valor_total\n" +
                    "FROM vinhos.usuarios u\n" +
                    "JOIN (\n" +
                    "\tSELECT email_usuario, categoria, MIN(c.data_registro) AS data_registro, SUM(quantidade_vendida) AS quantidade_vendida, SUM(itens.valor_total) AS valor_total\n" +
                    "\tFROM vinhos.compras c\n" +
                    "\tJOIN (\n" +
                    "\t\tSELECT ccv.numero_compra, v.categoria, SUM(ccv.quantidade) AS quantidade_vendida, SUM(ccv.subtotal) AS valor_total\n" +
                    "\t\tFROM vinhos.compra_carrinho_vinho ccv\n" +
                    "\t\tJOIN vinhos.vinhos v\n" +
                    "\t\tON ccv.numero_vinho = v.numero\n" +
                    "\t\tGROUP BY numero_compra, categoria\n" +
                    "\t) itens\n" +
                    "\tON c.numero = itens.numero_compra\n" +
                    "\tGROUP BY email_usuario, categoria\n" +
                    ") compras\n" +
                    "ON u.email = compras.email_usuario AND compras.data_registro >= CURRENT_DATE - INTERVAL '?'\n" +
                    "GROUP BY faixa_etaria, categoria\n" +
                    "ORDER BY faixa_etaria, quantidade_vendida DESC, valor_total DESC;";

    private static final String CATEGORIAS_MAIS_VENDIDAS_FAIXA_ETARIA_DATA =
            "SELECT faixa_etaria(data_nascimento) AS faixa_etaria, categoria, \n" +
                    "\t\tSUM(quantidade_vendida) AS quantidade_vendida, SUM(valor_total) AS valor_total\n" +
                    "FROM vinhos.usuarios u\n" +
                    "JOIN (\n" +
                    "\tSELECT email_usuario, categoria, MIN(c.data_registro) AS data_registro, SUM(quantidade_vendida) AS quantidade_vendida, SUM(itens.valor_total) AS valor_total\n" +
                    "\tFROM vinhos.compras c\n" +
                    "\tJOIN (\n" +
                    "\t\tSELECT ccv.numero_compra, v.categoria, SUM(ccv.quantidade) AS quantidade_vendida, SUM(ccv.subtotal) AS valor_total\n" +
                    "\t\tFROM vinhos.compra_carrinho_vinho ccv\n" +
                    "\t\tJOIN vinhos.vinhos v\n" +
                    "\t\tON ccv.numero_vinho = v.numero\n" +
                    "\t\tGROUP BY numero_compra, categoria\n" +
                    "\t) itens\n" +
                    "\tON c.numero = itens.numero_compra\n" +
                    "\tGROUP BY email_usuario, categoria\n" +
                    ") compras\n" +
                    "ON u.email = compras.email_usuario AND compras.data_registro BETWEEN ? AND ?\n" +
                    "GROUP BY faixa_etaria, categoria\n" +
                    "ORDER BY faixa_etaria, quantidade_vendida DESC, valor_total DESC;\n";

    public PgMyAppUserDAO (Connection connection) {
        this.connection = connection;
    }
    @Override
    public void authenticate (MyAppUser usuario) throws SQLException, SecurityException {
        try (PreparedStatement statement = connection.prepareStatement(AUTHENTICATE_QUERY)) {
            statement.setString(1, usuario.getEmail());
            statement.setString(2, usuario.getSenha());

            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    usuario.setNome(result.getString("nome"));
                    usuario.setEmail(result.getString("email"));
                } else {
                    throw new SecurityException("Email ou senha incorretos.");
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(PgMyAppUserDAO.class.getName()).log(Level.SEVERE, "DAO", ex);

            throw new SQLException("Erro ao autenticar usuário.");
        }
    }

    @Override
    public void create (MyAppUser usuario) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(CREATE_QUERY)) {
            statement.setString(1, usuario.getNome());
            statement.setString(2, usuario.getEmail());
            statement.setString(3, usuario.getSenha());
            statement.setString(4, usuario.getSexo());
            // Adicionando a data de nascimento
            if (usuario.getDataNascimento() != null) {
                statement.setDate(5, usuario.getDataNascimento());
            } else {
                // Se a dataNascimento for nula, você pode definir um valor padrão ou lançar uma exceção
                statement.setNull(5, java.sql.Types.DATE);
            }

            statement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(PgMyAppUserDAO.class.getName()).log(Level.SEVERE, "DAO", ex);

            if (ex.getMessage().contains("pk_email")) {
                throw new SQLException("Erro ao inserir usuário: Email já existente");
            } else if (ex.getMessage().contains("not-null")) {
                throw new SQLException("Erro ao inserir usuário: pelo menos um campo está em branco.");
            } else {
                throw new SQLException("Erro ao inserir usuário.");
            }
        }
    }

    @Override
    public MyAppUser read (String email) throws SQLException {
        MyAppUser user = new MyAppUser();

        try (PreparedStatement statement = connection.prepareStatement(READ_QUERY)) {
            statement.setString(1, email);

            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    user.setNome(result.getString("nome"));
                    user.setEmail(result.getString("email"));
                    user.setDataRegistro(result.getDate("data_registro"));
                    user.setSexo(result.getString("sexo"));
                    user.setDataNascimento(result.getDate("data_nascimento"));
                } else {
                    throw new SQLException("Erro ao visualizar: usuário não encontrado.");
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(PgMyAppUserDAO.class.getName()).log(Level.SEVERE, "DAO", ex);

            if (ex.getMessage().equals("Erro ao visualizar: usuário não encontrado.")) {
                throw ex;
            } else {
                throw new SQLException("Erro ao visualizar usuário.");
            }
        }

        return user;
    }

    @Override
    public void update (MyAppUser usuario) throws SQLException {
        String query;

        if (usuario.getSenha() == null || usuario.getSenha().isBlank()) {
            query = UPDATE_QUERY;
        } else {
            query = UPDATE_WITH_PASSWORD_QUERY;
        }

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, usuario.getNome());

            if (usuario.getSenha() == null || usuario.getSenha().isBlank()) {
                statement.setString(2, usuario.getEmail());
            } else {
                statement.setString(2, usuario.getSenha());
                statement.setString(3, usuario.getEmail());
            }

            if (statement.executeUpdate() < 1) {
                throw new SQLException("Erro ao editar: usuário não encontrado.");
            }
        } catch (SQLException ex) {
            Logger.getLogger(PgMyAppUserDAO.class.getName()).log(Level.SEVERE, "DAO", ex);

            if (ex.getMessage().equals("Erro ao editar: usuário não encontrado.")) {
                throw ex;
            } else if (ex.getMessage().contains("pk_email")) {
                throw new SQLException("Erro ao editar usuário: email já existente.");
            } else if (ex.getMessage().contains("not-null")) {
                throw new SQLException("Erro ao editar usuário: pelo menos um campo está em branco.");
            } else {
                throw new SQLException("Erro ao editar usuário");
            }
        }
    }

    @Override
    public void delete (String email) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(DELETE_QUERY)) {
            statement.setString(1, email);

            if (statement.executeUpdate() < 1) {
                throw new SQLException("Erro ao excluir: usuário não encontrado.");
            }
        } catch (SQLException ex) {
            Logger.getLogger(PgMyAppUserDAO.class.getName()).log(Level.SEVERE, "DAO", ex);

            if (ex.getMessage().equals("Erro ao excluir: usuário não encontrado.")) {
                throw ex;
            } else {
                throw new SQLException("Erro ao exluir usuário.");
            }
        }
    }

    @Override
    public List<MyAppUser> all () throws SQLException {
        List<MyAppUser> userList = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(ALL_QUERY)) {
            ResultSet result = statement.executeQuery();

            while (result.next()) {
                MyAppUser user = new MyAppUser();
                user.setNome(result.getString("nome"));
                user.setEmail(result.getString("email"));
                user.setDataRegistro(result.getDate("data_registro"));
                user.setSexo(result.getString("sexo"));
                user.setDataNascimento(result.getDate("data_nascimento"));

                userList.add(user);
            }
        } catch (SQLException ex) {
            Logger.getLogger(PgMyAppUserDAO.class.getName()).log(Level.SEVERE, "DAO", ex);

            throw new SQLException("Erro ao listar usuários.");
        }

        return userList;
    }

    @Override
    public List<ComprasPorSexoDTO> fetchVendasPorSexoPeriodo (String periodo) throws SQLException {
        List<ComprasPorSexoDTO> comprasPorSexoDTOList = new ArrayList<>();

        String query = VENDAS_VINHOS_POR_SEXO_PERIODO.replace("?", periodo);

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    ComprasPorSexoDTO comprasPorSexoDTO = new ComprasPorSexoDTO();
                    comprasPorSexoDTO.setSexo(resultSet.getString("sexo"));
                    comprasPorSexoDTO.setQuantidade_vendida(resultSet.getInt("quantidade_vendida"));
                    comprasPorSexoDTO.setValor_total(resultSet.getDouble("valor_total"));

                    comprasPorSexoDTOList.add(comprasPorSexoDTO);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(PgComprasDAO.class.getName()).log(Level.SEVERE, "DAO", ex);
            throw new SQLException("Erro ao listar compras por período.", ex);
        }

        return comprasPorSexoDTOList;
    }

    @Override
    public List<ComprasPorSexoDTO> fetchVendasPorSexoData (Date data1, Date data2) throws  SQLException {
        List<ComprasPorSexoDTO> comprasPorSexoDTOList = new ArrayList<>();


        try (PreparedStatement statement = connection.prepareStatement(VENDAS_VINHOS_POR_SEXO_DATA)) {
            statement.setDate(1, data1);
            statement.setDate(2, data2);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    ComprasPorSexoDTO comprasPorSexoDTO = new ComprasPorSexoDTO();
                    comprasPorSexoDTO.setSexo(resultSet.getString("sexo"));
                    comprasPorSexoDTO.setQuantidade_vendida(resultSet.getInt("quantidade_vendida"));
                    comprasPorSexoDTO.setValor_total(resultSet.getDouble("valor_total"));

                    comprasPorSexoDTOList.add(comprasPorSexoDTO);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(PgComprasDAO.class.getName()).log(Level.SEVERE, "DAO", ex);
            throw new SQLException("Erro ao listar compras por período.", ex);
        }

        return comprasPorSexoDTOList;
    }

    @Override
    public List<ComprasProFaixaEtariaDTO> fetchVendasPorFaixaEtariaPeriodo (String periodo) throws SQLException {
        List<ComprasProFaixaEtariaDTO> comprasProFaixaEtariaDTOS = new ArrayList<>();

        String query = VENDAS_VINHOS_POR_FAIXA_ETARIA_PERIODO.replace("?", periodo);

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    ComprasProFaixaEtariaDTO comprasProFaixaEtariaDTO = new ComprasProFaixaEtariaDTO();
                    comprasProFaixaEtariaDTO.setFaixa_etaria(resultSet.getString("faixa_etaria"));
                    comprasProFaixaEtariaDTO.setQuantidade_vendida(resultSet.getInt("quantidade_vendida"));
                    comprasProFaixaEtariaDTO.setValor_total(resultSet.getDouble("valor_total"));

                    comprasProFaixaEtariaDTOS.add(comprasProFaixaEtariaDTO);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(PgComprasDAO.class.getName()).log(Level.SEVERE, "DAO", ex);
            throw new SQLException("Erro ao listar compras por período.", ex);
        }

        return comprasProFaixaEtariaDTOS;
    }

    @Override
    public List<ComprasProFaixaEtariaDTO> fetchVendasPorFaixaEtariaData (Date data1, Date data2) throws SQLException {
        List<ComprasProFaixaEtariaDTO> comprasProFaixaEtariaDTOS = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(VENDAS_VINHOS_POR_FAIXA_ETARIA_DATA)) {
            statement.setDate(1, data1);
            statement.setDate(2, data2);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    ComprasProFaixaEtariaDTO comprasProFaixaEtariaDTO = new ComprasProFaixaEtariaDTO();
                    comprasProFaixaEtariaDTO.setFaixa_etaria(resultSet.getString("faixa_etaria"));
                    comprasProFaixaEtariaDTO.setQuantidade_vendida(resultSet.getInt("quantidade_vendida"));
                    comprasProFaixaEtariaDTO.setValor_total(resultSet.getDouble("valor_total"));

                    comprasProFaixaEtariaDTOS.add(comprasProFaixaEtariaDTO);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(PgComprasDAO.class.getName()).log(Level.SEVERE, "DAO", ex);
            throw new SQLException("Erro ao listar compras por período.", ex);
        }

        return comprasProFaixaEtariaDTOS;
    }

    @Override
    public List<CategoriaMaisVendidasFaixaIdadeDTO> fetchCategoriasMaisVendidasFaixaEtariaPeriodo (String periodo) throws SQLException {
        List<CategoriaMaisVendidasFaixaIdadeDTO> vendidasFaixaIdadeDTOArrayList = new ArrayList<>();

        String query = CATEGORIAS_MAIS_VENDIDAS_FAIXA_ETARIA_PERIODO.replace("?", periodo);

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    CategoriaMaisVendidasFaixaIdadeDTO categoriaMaisVendidasFaixaIdadeDTO = new CategoriaMaisVendidasFaixaIdadeDTO();
                    categoriaMaisVendidasFaixaIdadeDTO.setFaixa_etaria(resultSet.getString("faixa_etaria"));
                    categoriaMaisVendidasFaixaIdadeDTO.setCategoria(resultSet.getString("categoria"));
                    categoriaMaisVendidasFaixaIdadeDTO.setQuantidade_vendida(resultSet.getInt("quantidade_vendida"));
                    categoriaMaisVendidasFaixaIdadeDTO.setValor_total(resultSet.getDouble("valor_total"));

                    vendidasFaixaIdadeDTOArrayList.add(categoriaMaisVendidasFaixaIdadeDTO);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(PgComprasDAO.class.getName()).log(Level.SEVERE, "DAO", ex);
            throw new SQLException("Erro ao listar compras por período.", ex);
        }

        return vendidasFaixaIdadeDTOArrayList;
    }

    @Override
    public List<CategoriaMaisVendidasFaixaIdadeDTO> fetchCategoriasMaisVendidasFaixaEtariaData (Date data1, Date data2) throws SQLException {
        List<CategoriaMaisVendidasFaixaIdadeDTO> categoriaMaisVendidasFaixaIdadeDTOList = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(CATEGORIAS_MAIS_VENDIDAS_FAIXA_ETARIA_DATA)) {
            statement.setDate(1, data1);
            statement.setDate(2, data2);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    CategoriaMaisVendidasFaixaIdadeDTO categoriaMaisVendidasFaixaIdadeDTO = new CategoriaMaisVendidasFaixaIdadeDTO();
                    categoriaMaisVendidasFaixaIdadeDTO.setFaixa_etaria(resultSet.getString("faixa_etaria"));
                    categoriaMaisVendidasFaixaIdadeDTO.setCategoria(resultSet.getString("categoria"));
                    categoriaMaisVendidasFaixaIdadeDTO.setQuantidade_vendida(resultSet.getInt("quantidade_vendida"));
                    categoriaMaisVendidasFaixaIdadeDTO.setValor_total(resultSet.getDouble("valor_total"));

                    categoriaMaisVendidasFaixaIdadeDTOList.add(categoriaMaisVendidasFaixaIdadeDTO);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(PgComprasDAO.class.getName()).log(Level.SEVERE, "DAO", ex);
            throw new SQLException("Erro ao listar compras por período.", ex);
        }

        return categoriaMaisVendidasFaixaIdadeDTOList;
    }
}
