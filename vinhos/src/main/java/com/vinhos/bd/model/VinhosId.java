package com.vinhos.bd.model;

import java.io.Serializable;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Embeddable
public class VinhosId implements Serializable {
    private String nome;
    private String vinicula;

    public VinhosId() {}

    public VinhosId(String nome, String vinicula) {
        this.nome = nome;
        this.vinicula = vinicula;
    }
}
