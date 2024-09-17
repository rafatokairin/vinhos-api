package com.vinhos.bd.model;

import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Embedded;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Embeddable
public class CarrinhoProdutoId implements Serializable {

    @ManyToOne
    @JoinColumn(name = "numero_carrinho")
    private Carrinho carrinho;  // Referência à chave do carrinho

    @Embedded
    private VinhosId vinho;  // Referência à chave composta do vinho

    public CarrinhoProdutoId() {
    }

    // Construtor com argumentos
    public CarrinhoProdutoId(Carrinho carrinho, VinhosId vinho) {
        this.carrinho = carrinho;
        this.vinho = vinho;
    }
}
