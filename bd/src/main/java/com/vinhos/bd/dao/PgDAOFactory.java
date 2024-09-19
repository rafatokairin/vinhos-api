package com.vinhos.bd.dao;

import java.sql.Connection;

public class PgDAOFactory extends DAOFactory{

    public PgDAOFactory (Connection connection) {
        this.connection = connection;
    }

    public MyAppUserDAO getMyAppUserDAO () {
        return new PgMyAppUserDAO(this.connection);
    }
}
