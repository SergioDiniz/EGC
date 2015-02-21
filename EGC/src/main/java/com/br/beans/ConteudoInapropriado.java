/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.br.beans;

import java.io.Serializable;
import javax.persistence.*;

/**
 *
 * @author Sergio Diniz
 */
@Entity
public class ConteudoInapropriado implements Serializable{
    
    @Id @GeneratedValue
    private int id;
    @Column (nullable = false)
    private String descricao;
    @Column (nullable = false) @Enumerated(EnumType.STRING)
    private TipoDeConteudo tipoDeConteudo;

    public ConteudoInapropriado() {
    }

    public ConteudoInapropriado(String descricao, TipoDeConteudo tipoDeConteudo) {
        this.descricao = descricao;
        this.tipoDeConteudo = tipoDeConteudo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public TipoDeConteudo getTipoDeConteudo() {
        return tipoDeConteudo;
    }

    public void setTipoDeConteudo(TipoDeConteudo tipoDeConteudo) {
        this.tipoDeConteudo = tipoDeConteudo;
    }
    
    
    
    
}
