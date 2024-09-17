package com.vinhos.bd.model;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class CarrinhoProduto {

    @EmbeddedId
    private CarrinhoProdutoId id;
    private int quantidade;
    private double subtotal;
}
