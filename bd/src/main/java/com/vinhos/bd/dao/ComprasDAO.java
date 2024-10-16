package com.vinhos.bd.dao;

import com.vinhos.bd.dto.ComprasPorDiaDaSemanaDTO;
import com.vinhos.bd.dto.ComprasPorPeriodoDTO;
import com.vinhos.bd.dto.ComprasPorUsuarioDTO;
import com.vinhos.bd.dto.ValorCompradoPorDiaDTO;
import com.vinhos.bd.model.Compras;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

public interface ComprasDAO extends DAO<Compras, Integer> {

    /**
     * Encontra a relacao do valor total vendido, por dia, de vinhos em um ultimo periodo de tempo
     * (dias, semanas, meses, anos)
     * @param periodo Representa o periodo de tempo
     * @return Lista com as relacoes encontradas, null caso vazia
     * @throws SQLException
     */
    public List<ComprasPorPeriodoDTO> fetchBuysPerDay(String periodo) throws SQLException;

    /**
     * Encontra a relacao do valor total vendido, por dia, de vinhos em um certo periodo de tempo entre duas datas
     * @param data1 Data de inicio do periodo
     * @param data2 Date de fim do periodo
     * @return Lista com as relacoes encontradas, null caso vazia
     * @throws SQLException
     */
    public List<ComprasPorPeriodoDTO> fetchBuysByDate (Date data1, Date data2) throws SQLException;

    public List<ComprasPorDiaDaSemanaDTO> fetchBuysByWeekdayPeriodo (String periodo) throws SQLException;

    public List<ComprasPorDiaDaSemanaDTO> fetchBuysByWeekdayData (Date data1, Date data2) throws SQLException;

    public List<ComprasPorUsuarioDTO> fetchBuysByUserData (Date data1, Date data2) throws SQLException;

    public List<ComprasPorUsuarioDTO> fetchBuysByUserPeriodo (String periodo) throws SQLException;

    public List<ValorCompradoPorDiaDTO> fetchBuysPerDayByPeriodo (String periodo) throws SQLException;

    public List<ValorCompradoPorDiaDTO> fetchBuysPerDayByData (Date data1, Date data2) throws SQLException;
}
