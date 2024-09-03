package com.vinhos.bd.repository;

import com.vinhos.bd.model.MyAppUser;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class MyAppUserRepository {
    private final JdbcTemplate jdbcTemplate;

    public MyAppUserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<MyAppUser> rowMapper = (rs, rowNum) -> {
        MyAppUser user = new MyAppUser();
        user.setEmail(rs.getString("email"));
        user.setUsername(rs.getString("nome"));
        user.setPassword(rs.getString("senha"));
        return user;
    };

    public Optional<MyAppUser> findById(String email) {
        try {
            String sql = "SELECT * FROM vinhos.usuarios WHERE email = ?";
            MyAppUser user = jdbcTemplate.queryForObject(sql, rowMapper, email);
            return Optional.ofNullable(user);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public boolean existsById(String email) {
        String sql = "SELECT COUNT(*) FROM vinhos.usuarios WHERE email = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, email);
        return count != null && count > 0;
    }

    public void save(MyAppUser user) {
        String sql = "INSERT INTO vinhos.usuarios (email, nome, senha) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, user.getEmail(), user.getUsername(), user.getPassword());
    }

    public void update(MyAppUser user) {
        String sql = "UPDATE vinhos.usuarios SET nome = ?, senha = ? WHERE email = ?";
        jdbcTemplate.update(sql, user.getUsername(), user.getPassword(), user.getEmail());
    }

    public void deleteById(String email) {
        String sql = "DELETE FROM vinhos.usuarios WHERE email = ?";
        jdbcTemplate.update(sql, email);
    }
}
