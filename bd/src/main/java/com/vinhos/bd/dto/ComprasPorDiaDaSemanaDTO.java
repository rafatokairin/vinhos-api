package com.vinhos.bd.dto;

public class ComprasPorDiaDaSemanaDTO {

    private String dia_da_semana;

    private int quantidade_vendida;

    private double valor_total;

    public String getDia_da_semana() {
        return dia_da_semana;
    }

    public void setDia_da_semana(String dia_da_semana) {
        this.dia_da_semana = dia_da_semana;
    }

    public int getQuantidade_vendida() {
        return quantidade_vendida;
    }

    public void setQuantidade_vendida(int quantidade_vendida) {
        this.quantidade_vendida = quantidade_vendida;
    }

    public double getValor_total() {
        return valor_total;
    }

    public void setValor_total(double valor_total) {
        this.valor_total = valor_total;
    }
}
