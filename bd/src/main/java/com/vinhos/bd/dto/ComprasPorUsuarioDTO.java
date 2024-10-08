package com.vinhos.bd.dto;

public class ComprasPorUsuarioDTO {
    private String nome_usuario;
    private int total_compras;
    private double total_gasto;

    public String getNome_usuario() {
        return nome_usuario;
    }

    public void setNome_usuario(String nome_usuario) {
        this.nome_usuario = nome_usuario;
    }

    public int getTotal_compras() {
        return total_compras;
    }

    public void setTotal_compras(int total_compras) {
        this.total_compras = total_compras;
    }

    public double getTotal_gasto() {
        return total_gasto;
    }

    public void setTotal_gasto(double total_gasto) {
        this.total_gasto = total_gasto;
    }
}
