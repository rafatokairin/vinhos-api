package com.vinhos.bd.model;

import java.io.Serializable;

public class CarrinhoVinhoID implements Serializable {

    private int numero_carrinho;
    private int numero_vinho;

    public CarrinhoVinhoID (int numero_carrinho, int numero_vinho) {
        this.numero_carrinho = numero_carrinho;
        this.numero_vinho = numero_vinho;
    }

    public int getNumeroCarrinho () {
        return numero_carrinho;
    }

    public void setNumeroCarrinho (int numero_carrinho) {
        this.numero_carrinho = numero_carrinho;
    }

    public int getNumeroVinho () {
        return numero_vinho;
    }

    public void setNumeroVinho (int numero_vinho) {
        this.numero_vinho = numero_vinho;
    }
}
