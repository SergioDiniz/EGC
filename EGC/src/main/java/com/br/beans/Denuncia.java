/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.br.beans;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Sergio Diniz
 */
@Entity
public class Denuncia implements Serializable{
    @Id @GeneratedValue
    private int id;
    @Column(nullable = false)
    private String descricao;
    @Column(nullable = false) @Enumerated(EnumType.STRING)
    private EstadoDeAcompanhamento estadoDeAcompanhamento;
    @Column(nullable = false) @Enumerated(EnumType.STRING)
    private TipoDeDenuncia tipoDeDenuncia;
    @Column(nullable = false) @Temporal(TemporalType.DATE)
    private Date data;
    @Column(nullable = false)
    private String foto;


    
    @OneToOne(cascade = CascadeType.ALL)
    private EnderecoDenuncia enderecoDenuncia;
    @ManyToOne
    private Cidade cidade;
    @OneToOne(cascade = CascadeType.ALL)
    private InformacaoDeAtendida informacaoDeAtendida;
    @OneToMany (cascade = CascadeType.ALL)
    private List<ConteudoInapropriado> conteudoInapropriados;
    
    
    public Denuncia() {
    }

    public Denuncia(String descricao, String foto, EnderecoDenuncia enderecoDenuncia) {
        this.descricao = descricao;
        this.foto = foto;
        this.enderecoDenuncia = enderecoDenuncia;
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

    public EstadoDeAcompanhamento getEstadoDeAcompanhamento() {
        return estadoDeAcompanhamento;
    }

    public void setEstadoDeAcompanhamento(EstadoDeAcompanhamento estadoDeAcompanhamento) {
        this.estadoDeAcompanhamento = estadoDeAcompanhamento;
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

    public EnderecoDenuncia getEnderecoDenuncia() {
        return enderecoDenuncia;
    }

    public void setEnderecoDenuncia(EnderecoDenuncia enderecoDenuncia) {
        this.enderecoDenuncia = enderecoDenuncia;
    }

    public Cidade getCidade() {
        return cidade;
    }

    public void setCidade(Cidade cidade) {
        this.cidade = cidade;
    }

    public InformacaoDeAtendida getInformacaoDeAtendida() {
        return informacaoDeAtendida;
    }

    public void setInformacaoDeAtendida(InformacaoDeAtendida informacaoDeAtendida) {
        this.informacaoDeAtendida = informacaoDeAtendida;
    }

    public TipoDeDenuncia getTipoDeDenuncia() {
        return tipoDeDenuncia;
    }

    public void setTipoDeDenuncia(TipoDeDenuncia tipoDeDenuncia) {
        this.tipoDeDenuncia = tipoDeDenuncia;
    }

    
    
    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + this.id;
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
        final Denuncia other = (Denuncia) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }
    
    
    
    
}
