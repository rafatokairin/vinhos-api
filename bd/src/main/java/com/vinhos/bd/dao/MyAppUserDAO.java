package com.vinhos.bd.dao;

import com.vinhos.bd.dto.ComprasPorSexoDTO;
import com.vinhos.bd.model.MyAppUser;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

public interface MyAppUserDAO extends DAO<MyAppUser, String>{

    /**
     * Autentica um usuário no sistema comparando seu email e senha com os dados armazenados no banco de dados.
     *
     * Se o usuário for autenticado com sucesso, seus dados (nome e email) serão preenchidos no objeto MyAppUser fornecido.
     * Caso contrário, uma exceção de segurança será lançada indicando falha na autenticação.
     *
     * @param usuario Objeto MyAppUser contendo as credenciais (email e senha) a serem verificadas.
     *
     * @throws SecurityException Se as credenciais fornecidas (email ou senha) forem inválidas.
     * @throws SQLException Se houver erro na execução da consulta ao banco de dados.
     */
    public void authenticate(MyAppUser usuario) throws SQLException, SecurityException;

    public List<ComprasPorSexoDTO> fetchVendasPorSexoPeriodo (String periodo) throws SQLException;

    public List<ComprasPorSexoDTO> fetchVendasPorSexoData (Date data1, Date data2) throws  SQLException;
}
