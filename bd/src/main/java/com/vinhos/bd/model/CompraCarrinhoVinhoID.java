package com.vinhos.bd.model;

import java.io.Serializable;

public class CompraCarrinhoVinhoID implements Serializable {

    private int numero_compra;
    private CarrinhoVinhoID carrinhoVinhoID;

    public CompraCarrinhoVinhoID (int numero_compra, CarrinhoVinhoID carrinhoVinhoID) {
        this.numero_compra = numero_compra;
        this.carrinhoVinhoID = carrinhoVinhoID;
    }

    public int getNumeroCompra () {
        return numero_compra;
    }

    public void setNumeroCompra (int numero_compra) {
        this.numero_compra = numero_compra;
    }

    public CarrinhoVinhoID getCarrinhoVinhoID () {
        return carrinhoVinhoID;
    }

    public void setCarrinhoVinhoID (CarrinhoVinhoID carrinhoVinhoID) {
        this.carrinhoVinhoID = carrinhoVinhoID;
    }
}
