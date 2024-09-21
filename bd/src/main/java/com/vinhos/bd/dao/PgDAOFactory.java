package com.vinhos.bd.dao;

import java.awt.font.TextHitInfo;
import java.sql.Connection;

public class PgDAOFactory extends DAOFactory{

    public PgDAOFactory (Connection connection) {
        this.connection = connection;
    }

    public MyAppUserDAO getMyAppUserDAO () {
        return new PgMyAppUserDAO(this.connection);
    }

    public VinhoDAO getVinhoDAO () {
        return new PgVinhoDAO(this.connection);
    }

    public CarrinhoDAO getCarrinhoDAO() {return new PgCarrinhoDAO(this.connection);}

    public ComprasDAO getComprasDAO() {return new PgComprasDAO(this.connection);}
}
