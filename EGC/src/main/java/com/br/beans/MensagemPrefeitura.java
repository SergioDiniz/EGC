/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.br.beans;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author sergiodiniz
 */
@Entity
public class MensagemPrefeitura implements Serializable {
    
    @Id @GeneratedValue
    private int id;
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataMensagem;
    @Column(length = 1024)
    private String mensagem;

    public MensagemPrefeitura() {
    }

    public MensagemPrefeitura(Date dataMensagem, String mensagem) {
        this.dataMensagem = dataMensagem;
        this.mensagem = mensagem;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDataMensagem() {
        return dataMensagem;
    }

    public void setDataMensagem(Date dataMensagem) {
        this.dataMensagem = dataMensagem;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public String getDataMensagemFormatado() {
        SimpleDateFormat formato = new SimpleDateFormat("HH:mm - dd/MM/yyyy");
        return formato.format(this.dataMensagem);
    }
    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + this.id;
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
        final MensagemPrefeitura other = (MensagemPrefeitura) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "MensagemPrefeitura{" + "id=" + id + ", dataMensagem=" + dataMensagem + ", mensagem=" + mensagem + '}';
    }
    
    
    
    
    
}
