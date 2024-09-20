package com.vinhos.bd.dao;

import com.vinhos.bd.model.Vinho;

import java.sql.SQLException;
import java.util.List;

public interface VinhoDAO extends DAO<Vinho, Integer>{

    public List<Vinho> findVinhoByNome (String nome) throws SQLException;
    public List<Vinho> findVinhoByAno (int ano) throws SQLException;
    public List<Vinho> findVinhoByUva (String uva) throws SQLException;
    public List<Vinho> findVinhoByVinicula (String vinicula) throws SQLException;
    public List<Vinho> findVinhoByRegiao (String regiao) throws SQLException;

    // Procura vinhos, que tenham um preco <= precoMax.
    public List<Vinho> findVinhoByMaxPreco (double precoMax) throws SQLException;
    public List<Vinho> findVinhoByCategoria (String categoria) throws SQLException;
    public List<Vinho> findVinhoByEstilo (String estilo) throws SQLException;
}
