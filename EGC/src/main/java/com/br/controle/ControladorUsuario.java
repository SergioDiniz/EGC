/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.br.controle;

import com.br.beans.EnderecoUsuario;
import com.br.beans.Usuario;
import com.br.fachada.Fachada;
import java.io.IOException;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author Sergio Diniz
 */
@ManagedBean(name = "controladorUsuario")
@SessionScoped
public class ControladorUsuario implements Serializable{
    
    Usuario usuario;

    
    @EJB
    private Fachada fachada;
    
    public ControladorUsuario() {
        this.usuario = new Usuario(new EnderecoUsuario());
    }

    public String cadastro(){
        try{
            fachada.cadastrar(usuario);
            return "/sis/usuario/index.jsf?faces-redirect=true";
        } catch(Exception e){
            
        }
        this.usuario = new Usuario(new EnderecoUsuario());
        return null;
    }
    
    public void mostrapagina() throws IOException{
        if(usuario.getEmail() == null){
            FacesContext.getCurrentInstance().getExternalContext().redirect("/EGC/index.jsf");
        }
        
    }
    
    public String login(){
        this.usuario = fachada.loginUsuario(this.usuario);
        if (usuario != null){
            return "/sis/usuario/index.jsf?faces-redirect=true";
//            FacesContext.getCurrentInstance().getExternalContext().redirect(null);
        } else {
            System.out.println("//////////////////////////////////////// erro");
            this.usuario = new Usuario(new EnderecoUsuario());
            return null;
        }
        
    }
    
    public String logout(){
        this.usuario = new Usuario(new EnderecoUsuario());
        return "/index.jsf?faces-redirect=true";
    }    
    
    
    
    
    
    
    
    
//    
    
    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
    
    
    
    
    
}
