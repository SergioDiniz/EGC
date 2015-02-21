/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.br.beans;

import java.io.Serializable;
import javax.persistence.Entity;

/**
 *
 * @author Sergio Diniz
 */
@Entity
public class Administrador extends Pessoa implements Serializable {
    
    public String nome;

    public Administrador() {
    }

    public Administrador(String nome) {
        this.nome = nome;
    }

    public Administrador(String nome, String email, String senha) {
        super(email, senha);
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
    
    
    
}
