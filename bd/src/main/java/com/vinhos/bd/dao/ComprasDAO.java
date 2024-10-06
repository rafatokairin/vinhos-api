package com.vinhos.bd.dao;

import com.vinhos.bd.dto.ComprasPorPeriodoDTO;
import com.vinhos.bd.model.Compras;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

public interface ComprasDAO extends DAO<Compras, Integer> {
    public List<ComprasPorPeriodoDTO> fetchBuysPerDay(String periodo) throws SQLException;

    public List<ComprasPorPeriodoDTO> fetchBuysByDate (Date data1, Date data2) throws SQLException;
}
