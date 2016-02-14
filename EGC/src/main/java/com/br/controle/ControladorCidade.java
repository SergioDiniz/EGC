/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.br.controle;

import com.br.beans.Registro;
import com.br.fachada.Fachada;
import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author sergiodiniz
 */
@ManagedBean(name = "controladorCidade")
@SessionScoped
public class ControladorCidade implements Serializable {

    @EJB
    private Fachada fachada;

    public ControladorCidade() {
    }

    public List<Registro> registrosDaCidade(String cidade, String estado) {
        return fachada.registrosDaCidade(cidade, estado);
    }
    
    public List<List> ruasDeUmaCidadeNumerosDeDenuncia(String cidade, String estado){
        return fachada.ruasDeUmaCidadeNumerosDeDenuncia(cidade, estado);
    }

    public List<List> cepDeUmaCidadeNumerosDeDenuncia(String cidade, String estado){
        return fachada.cepDeUmaCidadeNumerosDeDenuncia(cidade, estado);
    }
 
    public List<List> tiposDeDenunciasNumerosDeDenuncia(String cidade, String estado){
        return fachada.tiposDeDenunciasNumerosDeDenuncia(cidade, estado);
    }
    
    public List<List> estadoDeDenunciasNumerosDeDenuncia(String cidade, String estado){
        return fachada.estadoDeDenunciasNumerosDeDenuncia(cidade, estado);
    }
    
}
