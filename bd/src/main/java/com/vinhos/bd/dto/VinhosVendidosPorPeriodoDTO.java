package com.vinhos.bd.dto;

public class VinhosVendidosPorPeriodoDTO {
    private String nome;
    private String vinicula;
    private int ano;
    private String categoria;
    private String estilo;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getVinicula() {
        return vinicula;
    }

    public void setVinicula(String vinicula) {
        this.vinicula = vinicula;
    }

    public int getAno() {
        return ano;
    }

    public void setAno(int ano) {
        this.ano = ano;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getEstilo() {
        return estilo;
    }

    public void setEstilo(String estilo) {
        this.estilo = estilo;
    }
}
