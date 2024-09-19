package com.vinhos.bd.model;


import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Vinhos {
    @EmbeddedId
    private VinhosId id;  // Chave composta (nome + vinícula)
    private int ano;
    private String descricao;
    private String uva;
    private String regiao;
    private double preco;
    private int quantidade_estoque;
    private String img_path;
}
