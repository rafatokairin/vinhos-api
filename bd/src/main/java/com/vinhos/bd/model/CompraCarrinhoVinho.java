package com.vinhos.bd.model;

public class CompraCarrinhoVinho {
    private int numero_compra;
    private int numero_carrinho;
    private int numero_vinho;
    private int quantidade;
    private double subtotal;

    public int getNumero_compra() { return numero_compra; }

    public void setNumero_compra(int numero_compra) { this.numero_compra = numero_compra; }

    public int getNumero_carrinho() { return numero_carrinho; }

    public void setNumero_carrinho(int numero_carrinho) { this.numero_carrinho = numero_carrinho; }

    public int getNumero_vinho() { return numero_vinho; }

    public void setNumero_vinho(int numero_vinho) { this.numero_vinho = numero_vinho; }

    public int getQuantidade() { return quantidade; }

    public void setQuantidade(int quantidade) { this.quantidade = quantidade; }

    public double getSubtotal() { return subtotal; }

    public void setSubtotal(double subtotal) { this.subtotal = subtotal; }
}
