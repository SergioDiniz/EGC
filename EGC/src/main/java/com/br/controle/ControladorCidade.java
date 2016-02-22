/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.br.controle;

import com.br.beans.CidadePK;
import com.br.beans.Denuncia;
import com.br.beans.Registro;
import com.br.fachada.Fachada;
import java.io.Serializable;
import java.util.ArrayList;
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

    private CidadePK cidadePK;
    private List<Denuncia> denunciasPesquisaVisitante;
    private String filtroData;
    private String filtroAjuda;
    private String filtroQuery;
    private String filtroTipo;
    private String enderecoPesquisa;

    public ControladorCidade() {
        this.cidadePK = new CidadePK();
        this.denunciasPesquisaVisitante = new ArrayList<>();
        this.filtroData = "DATA_DESC";
        this.filtroAjuda = "AJUDA_DESC";
        this.filtroQuery = "";
        this.filtroTipo = "";
        this.enderecoPesquisa = "";
    }

    public List<Registro> registrosDaCidade(String cidade, String estado) {
        return fachada.registrosDaCidade(cidade, estado);
    }

    public List<List> ruasDeUmaCidadeNumerosDeDenuncia(String cidade, String estado) {
        return fachada.ruasDeUmaCidadeNumerosDeDenuncia(cidade, estado);
    }

    public List<List> cepDeUmaCidadeNumerosDeDenuncia(String cidade, String estado) {
        return fachada.cepDeUmaCidadeNumerosDeDenuncia(cidade, estado);
    }

    public List<List> tiposDeDenunciasNumerosDeDenuncia(String cidade, String estado) {
        return fachada.tiposDeDenunciasNumerosDeDenuncia(cidade, estado);
    }

    public List<List> estadoDeDenunciasNumerosDeDenuncia(String cidade, String estado) {
        return fachada.estadoDeDenunciasNumerosDeDenuncia(cidade, estado);
    }

    public String pesquisarCidade() {
        
        String end[] = this.enderecoPesquisa.split(", ");


        if (end.length == 3) {
            String cs[] = end[1].split(" - ");
            this.cidadePK.setNomeCidade(cs[0]);
            this.cidadePK.setSiglaEstado(cs[1]);
        } else if (end.length == 1) {
            String cs[] = end[0].split(" - ");
            if (cs.length == 2) {
                this.cidadePK.setNomeCidade(cs[0]);
                this.cidadePK.setSiglaEstado(cs[1]);
            } else {
                this.cidadePK.setNomeCidade("");
                this.cidadePK.setSiglaEstado("");
            }
        }
        
        this.denunciasPesquisaVisitante = new ArrayList<>();
        this.denunciasPesquisaVisitante = fachada.pesquisarTodasDenunciasPorCidade(this.cidadePK.getNomeCidade(), this.cidadePK.getSiglaEstado(), "DATA");
        
        
        
        
        return null;
    }
    
    public long andamentoDasDenuncias(){
        return 0;
    }

    public String gerenciarDenunciasFiltro(String ordem, String filtroQuery, String filtro) {

        // setando ordem
        if (ordem.compareToIgnoreCase("DATA") == 0) {
            if (this.filtroData.compareToIgnoreCase("DATA_ASC") == 0) {
                this.filtroData = "DATA_DESC";
            } else {
                this.filtroData = "DATA_ASC";
            }
            ordem = this.filtroData;

        } else if (ordem.compareToIgnoreCase("AJUDA") == 0) {
            if (this.filtroAjuda.compareToIgnoreCase("AJUDA_ASC") == 0) {
                this.filtroAjuda = "AJUDA_DESC";
            } else {
                this.filtroAjuda = "AJUDA_ASC";
            }
            ordem = this.filtroAjuda;

        }

        this.filtroQuery = filtroQuery;
        this.filtroTipo = filtro;

        this.denunciasPesquisaVisitante = new ArrayList<>();
        this.denunciasPesquisaVisitante = fachada.gerenciarDenunciasFiltro(this.cidadePK.getNomeCidade(), this.cidadePK.getSiglaEstado(),
                ordem, this.filtroQuery, this.filtroTipo);

        return null;

    }

    //
    //
    // GETs and SETs
    //
    public List<Denuncia> getDenunciasPesquisaVisitante() {
        return denunciasPesquisaVisitante;
    }

    public void setDenunciasPesquisaVisitante(List<Denuncia> denunciasPesquisaVisitante) {
        this.denunciasPesquisaVisitante = denunciasPesquisaVisitante;
    }

    public String getEnderecoPesquisa() {
        return enderecoPesquisa;
    }

    public void setEnderecoPesquisa(String enderecoPesquisa) {
        this.enderecoPesquisa = enderecoPesquisa;
    }

    public String getFiltroData() {
        return filtroData;
    }

    public void setFiltroData(String filtroData) {
        this.filtroData = filtroData;
    }

    public String getFiltroAjuda() {
        return filtroAjuda;
    }

    public void setFiltroAjuda(String filtroAjuda) {
        this.filtroAjuda = filtroAjuda;
    }

    public String getFiltroQuery() {
        return filtroQuery;
    }

    public void setFiltroQuery(String filtroQuery) {
        this.filtroQuery = filtroQuery;
    }

    public String getFiltroTipo() {
        return filtroTipo;
    }

    public void setFiltroTipo(String filtroTipo) {
        this.filtroTipo = filtroTipo;
    }

    public CidadePK getCidadePK() {
        return cidadePK;
    }

    public void setCidadePK(CidadePK cidadePK) {
        this.cidadePK = cidadePK;
    }

}
