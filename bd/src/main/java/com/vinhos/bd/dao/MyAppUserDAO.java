package com.vinhos.bd.dao;

import com.vinhos.bd.model.MyAppUser;

import java.sql.SQLException;

public interface MyAppUserDAO extends DAO<MyAppUser, String>{

    public void authenticate(MyAppUser usuario) throws SQLException, SecurityException;
}
