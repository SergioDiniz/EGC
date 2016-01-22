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
public class EnderecoPrefeitura implements Serializable{
    
    @Column(nullable = false)
    private String cidade;
    @Column(nullable = false)
    private String estado;
    @Column(nullable = false)
    private String rua;
    @Column(nullable = false)
    private String cep;
    @Column(nullable = false)
    private int numero;
    
    private String longitude;
    private String latitude;

    public EnderecoPrefeitura() {
    }

    public EnderecoPrefeitura(String cidade, String estado, String rua, String cep, int numero, String longitude, String latitude) {
        this.cidade = cidade;
        this.estado = estado;
        this.rua = rua;
        this.cep = cep;
        this.numero = numero;
        this.longitude = longitude;
        this.latitude = latitude;
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

    public String getRua() {
        return rua;
    }

    public void setRua(String rua) {
        this.rua = rua;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
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
    
    
    
    
}
