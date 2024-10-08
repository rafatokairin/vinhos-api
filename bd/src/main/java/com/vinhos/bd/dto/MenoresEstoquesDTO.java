package com.vinhos.bd.dto;

public class MenoresEstoquesDTO {
    private String nome;
    private int quantidade_estoque;
    private int quantidade_vendida;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getQuantidade_estoque() {
        return quantidade_estoque;
    }

    public void setQuantidade_estoque(int quantidade_estoque) {
        this.quantidade_estoque = quantidade_estoque;
    }

    public int getQuantidade_vendida() {
        return quantidade_vendida;
    }

    public void setQuantidade_vendida(int quantidade_vendida) {
        this.quantidade_vendida = quantidade_vendida;
    }
}
