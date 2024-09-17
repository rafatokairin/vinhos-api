package com.vinhos.bd.model;

import jakarta.persistence.*;

@Entity
public class VinhoEspumante {
    @EmbeddedId
    private VinhosId id; // Chave primária @EmbeddedId .JSON
    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "nome", referencedColumnName = "nome", insertable = false, updatable = false),
            @JoinColumn(name = "vinicula", referencedColumnName = "vinicula", insertable = false, updatable = false)
    })
    private Vinhos vinhos; // Chave estrangeira Vinhos
    private String estilo;
}