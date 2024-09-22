package com.vinhos.bd.model;

public class CompraCarrinhoVinho {
    private CompraCarrinhoVinhoID id;
    private int quantidade;
    private double subtotal;

   public CompraCarrinhoVinhoID getId () {
       return id;
   }

   public void setId (CompraCarrinhoVinhoID id) {
       this.id = id;
   }

   public int getQuantidade () {
       return quantidade;
   }

   public void setQuantidade (int quantidade) {
       this.quantidade = quantidade;
   }

    public double getSubtotal() { return subtotal; }

    public void setSubtotal(double subtotal) { this.subtotal = subtotal; }
}
