/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.br.beans;

import java.io.Serializable;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

/**
 *
 * @author sergiodiniz
 */
@Entity
public class LideresPrefeitura implements Serializable {
    
    @Id @GeneratedValue
    private int id;
    
    private String prefeito;
    private String foto_prefeito = "lider.png";
    private String vice_prefieto;
    private String foto_vice_prefeito = "lider.png";
    
    @OneToOne(mappedBy = "lideresPrefeitura")
    private Prefeitura prefeitura;


    public LideresPrefeitura() {
    }

    public LideresPrefeitura(String prefeito, String vice_prefieto) {
        this.prefeito = prefeito;
        this.vice_prefieto = vice_prefieto;
    }

    public String getPrefeito() {
        return prefeito;
    }

    public void setPrefeito(String prefeito) {
        this.prefeito = prefeito;
    }

    public String getFoto_prefeito() {
        return foto_prefeito;
    }

    public void setFoto_prefeito(String foto_prefeito) {
        this.foto_prefeito = foto_prefeito;
    }

    public String getVice_prefieto() {
        return vice_prefieto;
    }

    public void setVice_prefieto(String vice_prefieto) {
        this.vice_prefieto = vice_prefieto;
    }

    public String getFoto_vice_prefeito() {
        return foto_vice_prefeito;
    }

    public void setFoto_vice_prefeito(String foto_vice_prefeito) {
        this.foto_vice_prefeito = foto_vice_prefeito;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Prefeitura getPrefeitura() {
        return prefeitura;
    }

    public void setPrefeitura(Prefeitura prefeitura) {
        this.prefeitura = prefeitura;
    }

    
    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + this.id;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final LideresPrefeitura other = (LideresPrefeitura) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

    
    
    
    
    
}
