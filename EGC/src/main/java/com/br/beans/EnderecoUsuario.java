/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.br.beans;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author Sergio Diniz
 */
@Embeddable
public class EnderecoUsuario implements Serializable{
    @Column(nullable = false)
    private String estado;
    @Column(nullable = false)
    private String cidade;

    public EnderecoUsuario() {
    }

    public EnderecoUsuario(String estado, String cidade) {
        this.estado = estado;
        this.cidade = cidade;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }
    

    
}
