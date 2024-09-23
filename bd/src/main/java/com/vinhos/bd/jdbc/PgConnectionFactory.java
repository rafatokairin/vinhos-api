package com.vinhos.bd.jdbc;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PgConnectionFactory extends ConnectionFactory {

    @Value("${spring.datasource.url}")
    private String dbUrl;

    @Value("${spring.datasource.username}")
    private String dbUser;

    @Value("${spring.datasource.password}")
    private String dbPassword;

    public PgConnectionFactory() {
        // Chama o método readProperties para garantir que as propriedades sejam carregadas corretamente.
        try {
            readProperties();
        } catch (IOException e) {
            System.err.println("Erro ao carregar propriedades: " + e.getMessage());
        }
    }

    @Override
    public Connection getConnection() throws SQLException {
        Connection connection = null;

        try {
            // Verifica se as propriedades foram carregadas
            if (dbUrl == null || dbUser == null || dbPassword == null) {
                throw new SQLException("URL do banco de dados, usuário ou senha não podem ser nulos.");
            }

            connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
            throw new SQLException("Erro de conexão ao banco de dados.");
        }

        return connection;
    }

    private void readProperties() throws IOException {
        Properties properties = new Properties();

        try (InputStream input = this.getClass().getClassLoader().getResourceAsStream("application.properties")) {
            if (input == null) {
                throw new IOException("Arquivo de propriedades não encontrado.");
            }
            properties.load(input);

            // Atualize as propriedades apenas se forem nulas
            if (dbUrl == null) dbUrl = properties.getProperty("spring.datasource.url");
            if (dbUser == null) dbUser = properties.getProperty("spring.datasource.username");
            if (dbPassword == null) dbPassword = properties.getProperty("spring.datasource.password");
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
            throw new IOException("Erro ao obter informações do banco de dados.");
        }
    }
}