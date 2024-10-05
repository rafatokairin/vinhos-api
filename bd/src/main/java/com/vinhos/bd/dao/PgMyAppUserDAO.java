package com.vinhos.bd.dao;

import com.vinhos.bd.model.MyAppUser;

import javax.swing.undo.UndoableEditSupport;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
}
