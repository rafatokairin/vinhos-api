package com.vinhos.bd.dao;

import com.vinhos.bd.model.Compras;

import java.sql.SQLException;
import java.util.List;

public interface ComprasDAO extends DAO<Compras, Integer> {
    public List<Compras> getComprasPorDias(int dias) throws SQLException;
}
