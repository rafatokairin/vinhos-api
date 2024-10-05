package com.vinhos.bd.model;
import java.sql.Date;

public class MyAppUser {
    private String nome;
    private String email;
    private String senha;
    private Date dataRegistro;

    private String sexo;

    private Date dataNascimento;

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

    public String getSexo () {
        return sexo;
    }

    public void setSexo (String sexo) {
        this.sexo = sexo;
    }

    public Date getDataNascimento () {
        return dataNascimento;
    }

    public void setDataNascimento (Date dataNascimento) {
        this.dataNascimento = dataNascimento;
    }
}
