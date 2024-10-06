package com.vinhos.bd.dto;

import java.sql.Date;

public class ComprasPorPeriodoDTO {

    private double valor_total;

    private Date data;

    public double getValor_total () {
        return valor_total;
    }

    public void setValor_total (double valor_total) {
        this.valor_total = valor_total;
    }

    public Date getData () {
        return data;
    }

    public void setData (Date data) {
        this.data = data;
    }
}
