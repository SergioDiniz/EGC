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
public class CidadePK implements Serializable{

    @Column(nullable = false)
    private String nomeCidade;
    @Column(nullable = false)
    private String siglaEstado;

    public CidadePK() {
    }

    public CidadePK(String nomeCidade, String siglaEstado) {
        this.nomeCidade = nomeCidade;
        this.siglaEstado = siglaEstado;
    }

    public String getNomeCidade() {
        return nomeCidade;
    }

    public void setNomeCidade(String nomeCidade) {
        this.nomeCidade = nomeCidade;
    }

    public String getSiglaEstado() {
        return siglaEstado;
    }

    public void setSiglaEstado(String siglaEstado) {
        this.siglaEstado = siglaEstado;
    }


    
}
