package com.vinhos.bd.dto;

public class VinhosMaisVendidosDTO {

    private String nome;
    private int quantidade_vendida;

    private double total_vendido;

    public String getNome () {
        return nome;
    }

    public void setNome (String nome) {
        this.nome = nome;
    }

    public int getQuantidadeVendida () {
        return quantidade_vendida;
    }

    public void setQuantidadeVendida (int quantidade_vendida) {
        this.quantidade_vendida = quantidade_vendida;
    }

    public double getTotal_vendido () {
        return total_vendido;
    }

    public void setTotal_vendido (double total_vendido) {
        this.total_vendido = total_vendido;
    }
}
