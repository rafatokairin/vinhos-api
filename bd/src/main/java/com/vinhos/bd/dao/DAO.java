package com.vinhos.bd.dao;

import java.sql.SQLException;
import java.util.List;

public interface DAO<T, ID> {

    /**
     * Cria um novo registro no banco de dados.
     *
     * @param t O objeto a ser criado.
     * @throws
     */
    public void create(T t) throws SQLException;

    /**
     * Lê um registro do banco de dados com base no ID fornecido.
     * @param id O identificador único do registro a ser lido.
     * @return O objeto correspondente ao ID ou null se não for encontrado.
     * @throws SQLException
     */
    public T read(ID id) throws  SQLException;

    /**
     * Atualiza um registro existente no banco de dados.
     * @param t O objeto contendo os dados atualizados.
     * @throws SQLException
     */
    public void update(T t) throws  SQLException;

    /**
     * Exclui um registro do banco de dados com base no ID fornecido.
     * @param id O identificador único do registro a ser excluído.
     * @throws SQLException
     */
    public void delete(ID id) throws SQLException;

    /**
     * Retorna todos os registros do banco de dados.
     * @return Uma lista com todos os registros do banco.
     * @throws SQLException
     */
    public List<T> all() throws SQLException;
}
