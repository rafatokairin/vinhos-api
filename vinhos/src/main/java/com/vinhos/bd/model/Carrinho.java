package com.vinhos.bd.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Carrinho {
        @Id
        private int numero;
        @ManyToOne
        @JoinColumn(name = "usuario_email", referencedColumnName = "email")
        private MyAppUser usuario;
        private double valor_total;
}
