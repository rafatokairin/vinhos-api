package com.vinhos.bd.model;

public class CarrinhoVinho {
    private int numero_carrinho;
    private int numero_vinho;
    private int quantidade;
    private double subtotal;

    public int getNumero_carrinho() { return numero_carrinho; }

    public void setNumero_carrinho(int numero_carrinho) { this.numero_carrinho = numero_carrinho; }

    public int getNumero_vinho() { return numero_vinho; }

    public void setNumero_vinho(int numero_vinho) { this.numero_vinho = numero_vinho; }

    public int getQuantidade() { return quantidade; }

    public void setQuantidade(int quantidade) { this.quantidade = quantidade; }

    public double getSubtotal() { return subtotal; }

    public void setSubtotal(double subtotal) { this.subtotal = subtotal; }
}
