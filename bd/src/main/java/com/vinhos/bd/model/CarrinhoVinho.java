package com.vinhos.bd.model;

public class CarrinhoVinho {
    private CarrinhoVinhoID id;
    private int quantidade;
    private double subtotal;

    public CarrinhoVinhoID getId () {
        return this.id;
    }

    public void setId (CarrinhoVinhoID id) {
        this.id = id;
    }

    public int getQuantidade() { return quantidade; }

    public void setQuantidade(int quantidade) { this.quantidade = quantidade; }

    public double getSubtotal() { return subtotal; }

    public void setSubtotal(double subtotal) { this.subtotal = subtotal; }
}
