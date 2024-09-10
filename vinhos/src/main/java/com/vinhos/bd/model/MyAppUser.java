package com.vinhos.bd.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Entity
@Setter
@Table(name = "usuarios", schema = "vinhos")
public class MyAppUser {
    @Id
    @Column(name = "email")
    @JsonProperty("email")
    private String username;
    @Column(name = "nome")
    @JsonProperty("nome")
    private String nome;
    @Column(name = "senha")
    @JsonProperty("senha")
    private String password;
}
