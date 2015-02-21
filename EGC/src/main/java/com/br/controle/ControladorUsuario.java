/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.br.controle;

import com.br.beans.EnderecoUsuario;
import com.br.beans.Usuario;
import com.br.fachada.Fachada;
import com.br.fachada.FachadaIT;
import com.br.service.DaoIT;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author Sergio Diniz
 */
@ManagedBean(name = "controladorUsuario")
@SessionScoped
public class ControladorUsuario implements Serializable{
    
    Usuario usuario;

    @EJB
    private DaoIT dao;
    
    public ControladorUsuario() {
        this.usuario = new Usuario(new EnderecoUsuario());
    }

    public String cadastro(){
        dao.salvar(this.usuario);
        
        return null;
    }
    
    
    
    
    
    
    
    
//    
    
    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
    
    
    
    
    
}
