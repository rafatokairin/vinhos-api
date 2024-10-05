package com.vinhos.bd.model;

public class Vinho {

    private int numero;     // primary key
    private String nome;    // not null
    private int ano;
    private String descricao;
    private String uva;
    private String vinicula;
    private String regiao;
    private double preco;
    private int quantidade_estoque;
    private String img_path;
    private String categoria;
    private String estilo;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getAno() { return ano; }

    public void setAno(int ano) {
        this.ano = ano;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getUva() {
        return uva;
    }

    public void setUva(String uva) {
        this.uva = uva;
    }

    public String getVinicula() {
        return vinicula;
    }

    public void setVinicula(String vinicula) {
        this.vinicula = vinicula;
    }

    public String getRegiao() {
        return regiao;
    }

    public void setRegiao(String regiao) {
        this.regiao = regiao;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    public int getQuantidadeEstoque() {
        return quantidade_estoque;
    }

    public void setQuantidadeEstoque(int quantidade_estoque) {
        this.quantidade_estoque = quantidade_estoque;
    }

    public String getImgPath() {
        return img_path;
    }

    public void setImgPath(String img_path) {
        this.img_path = img_path;
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

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }
}
