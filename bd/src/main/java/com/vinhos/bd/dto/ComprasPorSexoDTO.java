package com.vinhos.bd.dto;

public class ComprasPorSexoDTO {

    private String sexo;

    private int quantidade_vendida;

    private double valor_total;

    public String getSexo () {
        return sexo;
    }

    public void setSexo (String sexo) {
        this.sexo = sexo;
    }

    public int getQuantidade_vendida () {
        return quantidade_vendida;
    }

    public void setQuantidade_vendida (int quantidade_vendida) {
        this.quantidade_vendida = quantidade_vendida;
    }

    public double getValor_total () {
        return valor_total;
    }

    public void setValor_total (double valor_total) {
        this.valor_total = valor_total;
    }
}
