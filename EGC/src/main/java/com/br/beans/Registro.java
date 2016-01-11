/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.br.beans;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author sergiodiniz
 */
@Entity
public class Registro implements Serializable {

    @Id
    @GeneratedValue
    private int id;
    @Temporal(TemporalType.TIMESTAMP)
    private Date data;
    @Enumerated(EnumType.STRING)
    private TipoDeRegistro tipoDeRegistro;

    @OneToOne
    private Denuncia denuncia;

    @OneToOne
    private Prefeitura prefeitura;

    public Registro() {
    }

    public Registro(Date data, TipoDeRegistro tipoDeRegistro, Denuncia denuncia, Prefeitura prefeitura) {
        this.data = data;
        this.tipoDeRegistro = tipoDeRegistro;
        this.denuncia = denuncia;
        this.prefeitura = prefeitura;
    }

    public String diaDaSemana() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(this.data);
        int day = cal.get(Calendar.DAY_OF_WEEK);

        switch (day) {
            case 1:
                return "Domingo";
            case 2:
                return "Segunda";
            case 3:
                return "Terça";
            case 4:
                return "Quarta";
            case 5:
                return "Quinta";
            case 6:
                return "Sexta";
            case 7:
                return "Sabado";
        }

        return "";
    }
    
    
    public String dataFormatada(){
        String d = new SimpleDateFormat("dd.MM.yyyy").format(this.data);
        return d;
    }
    
    public String horaFormatada(){
        String h = new SimpleDateFormat("mm:HH").format(this.data);
        return h;
    }
    
    

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public TipoDeRegistro getTipoDeRegistro() {
        return tipoDeRegistro;
    }

    public void setTipoDeRegistro(TipoDeRegistro tipoDeRegistro) {
        this.tipoDeRegistro = tipoDeRegistro;
    }

    public Denuncia getDenuncia() {
        return denuncia;
    }

    public void setDenuncia(Denuncia denuncia) {
        this.denuncia = denuncia;
    }

    public Prefeitura getPrefeitura() {
        return prefeitura;
    }

    public void setPrefeitura(Prefeitura prefeitura) {
        this.prefeitura = prefeitura;
    }

}
