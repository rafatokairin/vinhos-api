package com.vinhos.bd.dto;

import java.sql.Date;

public class ValorCompradoPorDiaDTO {
    private Date data;
    private int valor_total_por_dia;

    public int getValor_total_por_dia() {
        return valor_total_por_dia;
    }

    public void setValor_total_por_dia(int valor_total_por_dia) {
        this.valor_total_por_dia = valor_total_por_dia;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }
}
