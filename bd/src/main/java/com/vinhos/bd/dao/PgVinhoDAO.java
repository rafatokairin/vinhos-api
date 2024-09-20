package com.vinhos.bd.dao;

import java.sql.Connection;

public class PgVinhoDAO /*implements VinhoDAO*/{

    private final Connection connection;

    /*
        Criar as QUERYS, para operacoes no banco de dados.
     */

    public PgVinhoDAO (Connection connection) {
        this.connection = connection;
    }

    /*
        Implementacao das funcoes, as quais foram declaradas na interfaces:
        DAO e VinhoDao.
     */
}
