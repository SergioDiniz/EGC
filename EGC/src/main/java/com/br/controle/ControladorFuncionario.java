/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.br.controle;

import com.br.beans.Cidade;
import com.br.beans.CidadePK;
import com.br.beans.Funcionario;
import com.br.fachada.Fachada;
import java.io.IOException;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author SergioD
 */
@ManagedBean(name = "controladorFuncionario")
@SessionScoped
public class ControladorFuncionario implements Serializable {

    @EJB
    private Fachada fachada;

    Funcionario funcionario;
    Cidade cidade;
    CidadePK cidadePK;

    public ControladorFuncionario() {
        this.funcionario = new Funcionario();
        this.cidade = new Cidade();
        this.cidadePK = new CidadePK();
    }

    public void mostrapagina() throws IOException {
        if (this.funcionario.getEmail() == null) {
            FacesContext.getCurrentInstance().getExternalContext().redirect("/EGC/index.jsf");
        }

    }

    public String login() {
        System.out.println("entrou no login");
        this.funcionario = fachada.loginFuncionario(funcionario.getEmail(), funcionario.getSenha(),
                cidadePK.getNomeCidade(), cidadePK.getSiglaEstado());
        System.out.println("pegou funcionario");
        if (funcionario != null) {

            this.cidade = fachada.pesquisarCidade(cidadePK.getNomeCidade(), cidadePK.getSiglaEstado());
            return "/sis/funcionario/index.jsf?faces-redirect=true";

        } else {
            ControladorAdmin.info();
            funcionario = new Funcionario();
            cidadePK = new CidadePK();
            return null;
        }

    }

    public String logout() {
        this.funcionario = new Funcionario();
        this.cidadePK = new CidadePK();
        return "/index.jsf?faces-redirect=true";
    }

    // geters and seters
    public Funcionario getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(Funcionario funcionario) {
        this.funcionario = funcionario;
    }

    public Cidade getCidade() {
        return cidade;
    }

    public void setCidade(Cidade cidade) {
        this.cidade = cidade;
    }

    public CidadePK getCidadePK() {
        return cidadePK;
    }

    public void setCidadePK(CidadePK cidadePK) {
        this.cidadePK = cidadePK;
    }

}
