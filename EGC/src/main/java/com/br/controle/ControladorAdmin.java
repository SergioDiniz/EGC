/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.br.controle;


import com.br.beans.Administrador;
import com.br.fachada.Fachada;
import java.io.IOException;
import javax.faces.bean.SessionScoped;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

/**
 *
 * @author SergioD
 */
@ManagedBean(name = "controladorAdmin")
@SessionScoped
public class ControladorAdmin implements Serializable {

    Administrador administrador;
    
    @EJB
    private Fachada fachada;
    
    
    public ControladorAdmin() {
        this.administrador = new Administrador();
    }
    
    
    public void mostrapagina() throws IOException{
        if(this.administrador.getEmail() == null){
            FacesContext.getCurrentInstance().getExternalContext().redirect("/EGC/index.jsf");
        }
        
    }
    
    public String login(){
        
        this.administrador = fachada.loginAdmin(administrador.getEmail(), administrador.getSenha());
        
        if(administrador != null){
            return "/sis/admin/index.jsf?faces-redirect=true";
        } 
            info("Usuário invalido!");
            this.administrador = new Administrador();    
            return null;
        
    }
    
    public String logout(){
        this.administrador = new Administrador();
        return "/index.jsf?faces-redirect=true";
    }
    
    public static void info(String s) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", s));
    }
    
    
    public static void infoUsuarioInvalido() {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "Usuário inválido!"));
    }
    
    public static void infoDadosAtualizados() {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "Dados atualizado com sucesso!"));
    }
    
    public void saveMessage() {
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage("Info", "Dados atualizados com sucesso!"));
    }
    
    public String atualizar(){
        fachada.atualizar(this.administrador);
        infoDadosAtualizados();
        return null;
    }
    
    
    
    
    
    
    
    //getes and seters

    public Administrador getAdministrador() {
        return administrador;
    }

    public void setAdministrador(Administrador administrador) {
        this.administrador = administrador;
    }
    
    
}
