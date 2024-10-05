package com.vinhos.bd.dto;

public class VinhosMaisVendidosDTO {

    private String nome;
    private int quantidade_vendida;

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
}
