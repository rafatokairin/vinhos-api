package com.vinhos.bd.repository;

import com.vinhos.bd.model.Vinhos;
import com.vinhos.bd.model.VinhosId;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public class VinhosRepository {
    private final JdbcTemplate jdbcTemplate;

    public VinhosRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Vinhos> rowMapper = (rs, rowNum) -> {
        Vinhos vinho = new Vinhos();
        VinhosId vinhosId = new VinhosId(rs.getString("nome"), rs.getString("vinicula"));
        vinho.setId(vinhosId);
        vinho.setAno(rs.getInt("ano"));
        vinho.setDescricao(rs.getString("descricao"));
        vinho.setUva(rs.getString("uva"));
        vinho.setRegiao(rs.getString("regiao"));
        vinho.setPreco(rs.getDouble("preco"));
        vinho.setQuantidade_estoque(rs.getInt("quantidade_estoque"));
        vinho.setImg_path(rs.getString("img_path"));
        return vinho;
    };

    public void save(Vinhos vinho) {
        if (vinho.getId() == null) {
            throw new IllegalArgumentException("O objeto Vinhos deve ter um ID não nulo.");
        }
        String sqlVinhos = "INSERT INTO vinhos.vinhos (nome, ano, descricao, uva, vinicula, regiao, preco, quantidade_estoque, img_path) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sqlVinhos, vinho.getId().getNome(), vinho.getAno(), vinho.getDescricao(), vinho.getUva(), vinho.getId().getVinicula(), vinho.getRegiao(), vinho.getPreco(), vinho.getQuantidade_estoque(), vinho.getImg_path());

        String sqlVinhoTinto = "INSERT INTO vinhos.vinho_tinto (nome_vinho, vinicula, estilo) VALUES (?, ?, ?)";
        jdbcTemplate.update(sqlVinhoTinto, vinho.getId().getNome(), vinho.getId().getVinicula(), vinho.getUva());  // Usando 'uva' como estilo para simplificação
    }

    public Optional<Vinhos> findById(String nome, String vinicula) {
        try {
            String sql = "SELECT * FROM vinhos.vinhos WHERE nome = ? AND vinicula = ?";
            Vinhos vinho = jdbcTemplate.queryForObject(sql, rowMapper, nome, vinicula);
            return Optional.ofNullable(vinho);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public boolean existsById(String nome, String vinicula) {
        String sql = "SELECT COUNT(*) FROM vinhos.vinhos WHERE nome = ? AND vinicula = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, nome, vinicula);
        return count != null && count > 0;
    }

    public void deleteById(String nome, String vinicula) {
        // Remove vinhos e suas subclasses (ON CASCADE)
        String sqlVinhos = "DELETE FROM vinhos.vinhos WHERE nome = ? AND vinicula = ?";
        jdbcTemplate.update(sqlVinhos, nome, vinicula);
    }
}