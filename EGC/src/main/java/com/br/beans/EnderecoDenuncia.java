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
public class EnderecoDenuncia implements Serializable{
    
    @Id @GeneratedValue
    private int id;
    @Column(nullable = false)
    private String rua;
    @Column(nullable = false)
    private int numero;
    @Column(nullable = false)
    private String cidade;
    @Column(nullable = false)
    private String estado;
    private String cep;
    private String pais;
    private String longitude;
    private String latitude;

    public EnderecoDenuncia() {
    }

    public EnderecoDenuncia(String rua, int numero, String cidade, String estado, String cep, String pais, String longitude, String latitude) {
        this.rua = rua;
        this.numero = numero;
        this.cidade = cidade;
        this.estado = estado;
        this.cep = cep;
        this.pais = pais;
        this.longitude = longitude;
        this.latitude = latitude;
    }


    public String getRua() {
        return rua;
    }

    public void setRua(String rua) {
        this.rua = rua;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    
    
}
