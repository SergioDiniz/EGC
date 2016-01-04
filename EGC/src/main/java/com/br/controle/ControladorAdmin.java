/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.br.controle;

import com.br.beans.Administrador;
import com.br.beans.Funcionario;
import com.br.beans.Prefeitura;
import com.br.fachada.Fachada;
import java.io.IOException;
import javax.faces.bean.SessionScoped;
import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

/**
 *
 * @author SergioD
 */
@ManagedBean(name = "controladorAdmin")
@SessionScoped
public class ControladorAdmin implements Serializable {

    Administrador administrador;
    Prefeitura prefeituraAx;

    boolean mostrarModalRecusar;
    boolean mostrarModalAceitar;

    @EJB
    private Fachada fachada;

    public ControladorAdmin() {
        this.administrador = new Administrador();
        this.prefeituraAx = new Prefeitura();
        this.mostrarModalRecusar = false;
        this.mostrarModalAceitar = false;
    }

    public void mostrapagina() throws IOException {
        if (this.administrador.getEmail() == null) {
            FacesContext.getCurrentInstance().getExternalContext().redirect("/EGC/index.jsf");
        }

    }

    public void mostrapaginaavaliacao() throws IOException {
        if (this.prefeituraAx.getEmail() == null) {
            FacesContext.getCurrentInstance().getExternalContext().redirect("index.jsf");
        }

    }

    public String login() {

        this.administrador = fachada.loginAdmin(administrador.getEmail(), administrador.getSenha());

        if (administrador != null) {
            return "/sis/admin/index.jsf?faces-redirect=true";
        }
        info("Usuário invalido!");
        this.administrador = new Administrador();
        return null;

    }

    public String logout() {
        this.administrador = new Administrador();
        return "/index.jsf?faces-redirect=true";
    }

    public List<Prefeitura> prefeiturasPendentes() {
        return fachada.prefeiturasPendentes();
    }

    public List<Prefeitura> prefeiturasAtivas() {
        return fachada.todasPrefeiturasAtivas();
    }

    public String avaliarSolicitacao(Prefeitura p) {
        this.prefeituraAx = p;
        return "avaliarsolicitacao.jsf?faces-redirect=true";
    }

    public String excluirPrefeitura() {
        fachada.excluirPrefeitura(prefeituraAx);
        this.prefeituraAx = new Prefeitura();
        return "solicitacoes.jsf?faces-redirect=true";
    }

    public String excluirPrefeituraAtiva() {
        fachada.excluirPrefeitura(prefeituraAx);
        this.prefeituraAx = new Prefeitura();
        return "prefeiturasativas.jsf?faces-redirect=true";
    }

    public String aceitarSolicitacao() {
        fachada.atualizarSituacaoPrefeitura(prefeituraAx, true);
        this.prefeituraAx = new Prefeitura();
        return "solicitacoes.jsf?faces-redirect=true";
    }

    public String bloquearPrefeitura() {
        fachada.atualizarSituacaoPrefeitura(prefeituraAx, false);
        this.prefeituraAx = new Prefeitura();
        return "prefeiturasativas.jsf?faces-redirect=true";
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

    public String atualizar() {
        fachada.atualizar(this.administrador);
        infoDadosAtualizados();
        return null;
    }

    public void mostraModalRecusar(Prefeitura p) throws IOException {
        this.prefeituraAx = p;
        this.mostrarModalRecusar = true;

//        System.out.println("nome: " + p.getNome());
//        return "/sis/admin/solicitacoes.jsf?faces-redirect=true#modal";
//        FacesContext.getCurrentInstance().getExternalContext().redirect("/EGC/sis/admin/solicitacoes.jsf#modal");
    }

    public void mostraModalAceitar(Prefeitura p) throws IOException {
        this.prefeituraAx = p;
        this.mostrarModalAceitar = true;

//        System.out.println("nome: " + p.getNome());
//        return "/sis/admin/solicitacoes.jsf?faces-redirect=true#modal";
//        FacesContext.getCurrentInstance().getExternalContext().redirect("/EGC/sis/admin/solicitacoes.jsf#modal");
    }

    
    public int numeroDeFuncionarios(){
        return fachada.numeroDeFuncionarios();
    }
    
    public List<Funcionario> todosFuncionarios(){
        return fachada.todosFuncionarios();
    }
    
    
    
    //getes and seters
    public Administrador getAdministrador() {
        return administrador;
    }

    public void setAdministrador(Administrador administrador) {
        this.administrador = administrador;
    }

    public Prefeitura getPrefeituraAx() {
        return prefeituraAx;
    }

    public void setPrefeituraAx(Prefeitura prefeituraAx) {
        this.prefeituraAx = prefeituraAx;
    }

    public boolean isMostrarModalRecusar() {
        return mostrarModalRecusar;
    }

    public void setMostrarModalRecusar(boolean mostrarModalRecusar) {
        this.mostrarModalRecusar = mostrarModalRecusar;
    }

    public boolean isMostrarModalAceitar() {
        return mostrarModalAceitar;
    }

    public void setMostrarModalAceitar(boolean mostrarModalAceitar) {
        this.mostrarModalAceitar = mostrarModalAceitar;
    }

}
