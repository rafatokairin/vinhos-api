package com.vinhos.bd.dao;

import com.vinhos.bd.dto.CategoriaMaisVendidasFaixaIdadeDTO;
import com.vinhos.bd.dto.ComprasPorSexoDTO;
import com.vinhos.bd.dto.ComprasProFaixaEtariaDTO;
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

    /**
     * Retorna uma lista da quantidade e valor total, de vinhos comprados, por sexo, num perido de tempo
     * (dias, semanas, meses, anos)
     * @param periodo String que determina o periodo de tempo, ex:(3 days, 1 week, 4 months, 2 years)
     * @return
     * @throws SQLException
     */
    public List<ComprasPorSexoDTO> fetchVendasPorSexoPeriodo (String periodo) throws SQLException;

    /**
     * Encontra a relacao de quantidade e valor total, de vinhos vendidos, em relacao ao sexo do usuario,
     * em um certo periodo de tempo entre duas datas.
     * @param data1 Data de inicio do periodo
     * @param data2 Data de fim do periodo
     * @return Lista das relacoes encontradas, null caso vazia
     * @throws SQLException
     */
    public List<ComprasPorSexoDTO> fetchVendasPorSexoData (Date data1, Date data2) throws  SQLException;

    /**
     * Encontra a relacao de quantidade e valor total, de vinhos vendidos, em relacao a faixa etaria do usuario,
     * em um ultimo perido de tempo (dias, semanas, meses, anos)
     * @param periodo Representa o periodo, ex: (3 days, 1 week, 4 months, 2 years)
     * @return Lista das relacoes encontradas, null caso vazia
     * @throws SQLException
     */
    public List<ComprasProFaixaEtariaDTO> fetchVendasPorFaixaEtariaPeriodo (String periodo) throws SQLException;

    /**
     * Encontra a relacao de quantidade e valor total, de vinhos vendidos, em relacao a faixa etaria do usuario,
     * em um certo periodo de tempo entre duas datas.
     * @param data1 Data de inicio do periodo
     * @param data2 Data de fim do periodo
     * @return Lista das relacoes encontradas, null caso vazia
     * @throws SQLException
     */
    public List<ComprasProFaixaEtariaDTO> fetchVendasPorFaixaEtariaData (Date data1, Date data2) throws SQLException;

    /**
     * Encontra a relacao da quantidade e valor total, vendidos, das categorias dos vinhos, em relacao a faixa etaria do usuario,
     * em um ultimo periodo de tempo (dias, semanas, meses, anos)
     * @param periodo Representa o periodo, ex: (3 days, 1 week, 4 months, 2 years)
     * @return Lista das relacoes encontradas, null caso vazia
     * @throws SQLException
     */
    public List<CategoriaMaisVendidasFaixaIdadeDTO> fetchCategoriasMaisVendidasFaixaEtariaPeriodo (String periodo) throws SQLException;

    /**
     * Encontra a relacao da quantidade e valor total, vendidos, das categorias dos vinhos, em relacao a faixa etaria do usuario,
     * em um certo periodo de tempo entre duas datas
     * @param data1 Data de inicio do periodo
     * @param data2 Data de fim do periodo
     * @return Lista das relacoes encontradas, null caso vazia
     * @throws SQLException
     */
    public List<CategoriaMaisVendidasFaixaIdadeDTO> fetchCategoriasMaisVendidasFaixaEtariaData (Date data1, Date data2) throws SQLException;
}
