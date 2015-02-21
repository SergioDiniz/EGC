/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.br.beans;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

/**
 *
 * @author Sergio Diniz
 */
@Entity
public class InformacaoDeAtendida implements Serializable{
    @Id @GeneratedValue
    private int id;
    @Column (nullable = false)
    private String descricao;
    @Column (nullable = false) @Temporal(TemporalType.DATE)
    private Date data;
    @Column (nullable = false)
    private String foto;

    public InformacaoDeAtendida() {
        this.data = new Date();
    }

    public InformacaoDeAtendida(String descricao, Date data, String foto) {
        this.descricao = descricao;
        this.data = data;
        this.foto = foto;
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

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }
    
    
    
}
