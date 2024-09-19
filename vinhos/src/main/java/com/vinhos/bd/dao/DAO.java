package com.vinhos.bd.dao;

import java.sql.SQLException;
import java.util.List;

public interface DAO<T, ID> {

    public void create(T t) throws SQLException;
    public T read(ID id) throws  SQLException;
    public void update(T t) throws  SQLException;
    public void delete(ID id) throws SQLException;
    public List<T> all() throws SQLException;
}
