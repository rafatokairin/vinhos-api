package com.vinhos.bd.dao;

import com.vinhos.bd.dto.VinhosMaisVendidosDTO;
import com.vinhos.bd.model.Vinho;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

public interface VinhoDAO extends DAO<Vinho, Integer>{

    /**
     * Funcao a qual vai realizar uma busca composta de vinhos. Podendo receber
     * varios parametros para a busca.
     * @param vinho Objeto vinho, em formato JSON
     * @return Lista dos vinhos, que possuem parametros em comum
     * @throws SQLException
     */
    public List<Vinho> findVinhos (Vinho vinho) throws SQLException;
    public List<VinhosMaisVendidosDTO> findVinhosByDataVendido(Date data_ini, Date data_fim) throws SQLException;

    public List<VinhosMaisVendidosDTO> fetchMostSoldWinesRecent(String period) throws SQLException;
}
