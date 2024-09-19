package com.vinhos.bd.model;
import java.sql.Date;

public class MyAppUser {
    private String email;
    private String nome;
    private String senha;
    private Date dataRegistro;

    public String getEmail () { return email; }

    public void setEmail (String email) {
        this.email = email;
    }

    public String getNome () {
        return nome;
    }

    public void setNome (String nome) {
        this.nome = nome;
    }

    public String getSenha () {
        return senha;
    }

    public void setSenha (String senha) {
        this.senha = senha;
    }

    public Date getDataRegistro () {
        return dataRegistro;
    }

    public void setDataRegistro (Date dataRegistro) {
        this.dataRegistro = dataRegistro;
    }
}
