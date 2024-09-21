package com.vinhos.bd.model;

public class Carrinho {
    private int numero;
    private String usuario_email;
    private double valor_total;

    public int getNumero() { return numero; }

    public void setNumero(int numero) { this.numero = numero; }

    public String getUsuario_email() { return usuario_email; }

    public void setUsuario_email(String usuario_email) { this.usuario_email = usuario_email; }

    public double getValor_total() { return valor_total; }

    public void setValor_total(double valor_total) { this.valor_total = valor_total; }
}
