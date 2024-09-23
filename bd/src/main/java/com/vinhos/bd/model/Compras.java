package com.vinhos.bd.model;

import java.util.Date;

public class Compras {
    private int numero;
    private double valor_total;
    private Date dataRegistro;

    public int getNumero() { return numero; }

    public void setNumero(int numero) { this.numero = numero; }

    public double getValor_total() { return valor_total; }

    public void setValor_total(double valor_total) { this.valor_total = valor_total; }

    public Date getDataRegistro () {
        return dataRegistro;
    }

    public void setDataRegistro (Date dataRegistro) {
        this.dataRegistro = dataRegistro;
    }
}
