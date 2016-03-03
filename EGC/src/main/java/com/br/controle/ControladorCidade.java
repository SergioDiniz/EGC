/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.br.controle;

import com.br.beans.CidadePK;
import com.br.beans.Denuncia;
import com.br.beans.Funcionario;
import com.br.beans.MensagemPrefeitura;
import com.br.beans.Prefeitura;
import com.br.beans.Registro;
import com.br.fachada.Fachada;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ComponentSystemEvent;
import org.primefaces.event.map.OverlaySelectEvent;
import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;

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
    private boolean mostrarInformacoesPrefeitura;
    private Prefeitura prefeitura;
    private List<Long> informacoesMunicipio;
    private Denuncia visualizarDenuncia;
    private String codigoDenuncia;
    private String codigoPrefeitura;
    private MensagemPrefeitura mensagemPrefeitura;

    //mapa pagina de pesquisa
    private MapModel advancedModel;
    private Marker marker;
    private String endLatitude;
    private String endLogitude;
    private String endZoom;

    public ControladorCidade() {
        this.cidadePK = new CidadePK();
        this.denunciasPesquisaVisitante = new ArrayList<>();
        this.filtroData = "DATA_DESC";
        this.filtroAjuda = "AJUDA_DESC";
        this.filtroQuery = "";
        this.filtroTipo = "";
        this.enderecoPesquisa = "";
        this.mostrarInformacoesPrefeitura = false;
        this.prefeitura = new Prefeitura();
        this.visualizarDenuncia = new Denuncia();
        this.codigoDenuncia = "";
        this.codigoPrefeitura = "";
        this.mensagemPrefeitura = new MensagemPrefeitura();
        this.advancedModel = new DefaultMapModel();
        this.endLatitude = "0";
        this.endLogitude = "0";
        this.endZoom = "2";
//        setarMarkerNoMapa();

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

    public boolean mostrarInformacoesDaPrefeitura() {
        return fachada.prefeituraCadastrada(this.cidadePK.getNomeCidade(), this.cidadePK.getSiglaEstado());
    }

    public void informacoesGeraisMunicipio(String emailPrefeitura, String cidade, String estado) {
        this.informacoesMunicipio = new ArrayList<>();

        long denuncias = fachada.totalDeDenunciasNaCidade(cidade, estado);
        long denunciasAtendidas = fachada.totalDeDenunciasAtendidasNaCidade(cidade, estado);
        long usuarios = fachada.totalDeUsuariosNaCidade(cidade, estado);
        long funcionarios = fachada.totalDeFuncionariosNaPrefeitura(emailPrefeitura);

        this.informacoesMunicipio.add(0, denuncias);
        this.informacoesMunicipio.add(1, denunciasAtendidas);
        this.informacoesMunicipio.add(2, usuarios);
        this.informacoesMunicipio.add(3, funcionarios);
    }

    public String pesquisarCidade() throws IOException {

        // pegando informações de denuncias
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

        // pegando informações da prefeitura
        this.prefeitura = new Prefeitura();
        this.prefeitura = fachada.pesquisarPrefeituraPorCidade(this.cidadePK.getNomeCidade(), this.cidadePK.getSiglaEstado());

        // vefiricando se a prefeitura existe
        if (this.prefeitura != null) {
            this.mostrarInformacoesPrefeitura = true;
        } else {
            this.mostrarInformacoesPrefeitura = false;
        }

        // pegando informações gerais do municipio
        String emailPrefeitura = "";
        if (this.prefeitura != null) {
            emailPrefeitura = this.prefeitura.getEmail();
        }
        informacoesGeraisMunicipio(emailPrefeitura, this.cidadePK.getNomeCidade(), this.cidadePK.getSiglaEstado());

        setarMarkerNoMapa();

//        FacesContext.getCurrentInstance().getExternalContext().redirect("/EGC/pesquisar");
        return "/sis/visitante/pesquisar.jsf?faces-redirect=true";
    }

    public void setarMarkerNoMapa() {

        // mudando o zoom do mapa
        this.endZoom = "15";

        List<Denuncia> denuncias = new ArrayList<>();
        denuncias = fachada.denunciasNaoAtendidasEmCidade(this.cidadePK.getNomeCidade(), this.cidadePK.getSiglaEstado());
        
        for (Denuncia denuncia : denuncias) {
            LatLng coord = new LatLng(Double.valueOf(denuncia.getEnderecoDenuncia().getLatitude()), Double.valueOf(denuncia.getEnderecoDenuncia().getLongitude()));
            Marker m = new Marker(coord, denuncia.getCodigo(), denuncia.getFoto(), ("/EGC/img/marker-icon/" + denuncia.getIconeDenunica()));
            
            if(!advancedModel.getMarkers().contains(m)){
                advancedModel.addOverlay(m);
            }
            
            
            
        }

    }

    public long andamentoDasDenuncias() {
        try {
            return ((informacoesMunicipio.get(1) * 100) / informacoesMunicipio.get(0));
        } catch (Exception e) {
        }
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

    public void pesquisarDenuncia(ComponentSystemEvent event) {

        try {
            this.visualizarDenuncia = new Denuncia();
            this.visualizarDenuncia = fachada.pesquisarDenunicaCodigo(this.codigoDenuncia);

            // pegando informações da prefeitura
            String cidade = this.visualizarDenuncia.getCidade().getCidadePK().getNomeCidade();
            String estado = this.visualizarDenuncia.getCidade().getCidadePK().getSiglaEstado();
            this.prefeitura = new Prefeitura();
            this.prefeitura = fachada.pesquisarPrefeituraPorCidade(cidade, estado);

            // vefiricando se a prefeitura existe
            if (this.prefeitura != null) {
                this.mostrarInformacoesPrefeitura = true;
            } else {
                this.mostrarInformacoesPrefeitura = false;
            }

            // pegando informações gerais do municipio
            String emailPrefeitura = "";
            if (this.prefeitura != null) {
                emailPrefeitura = this.prefeitura.getEmail();
            }
            informacoesGeraisMunicipio(emailPrefeitura, cidade, estado);
        } catch (Exception e) {
            System.out.println("ERRO AO TENTAR PESQUISAR DENUNCIA: " + e.getMessage());
        }

    }

    public void pesquisarPrefeitura(ComponentSystemEvent event) throws IOException {

        try {
            this.prefeitura = new Prefeitura();
            this.prefeitura = fachada.pesquisarPrefeituraPorCodigo(this.codigoPrefeitura);

            // vefiricando se a prefeitura existe
            if (this.prefeitura != null) {
                this.mostrarInformacoesPrefeitura = true;

                // pegando informações gerais do municipio
                informacoesGeraisMunicipio(this.prefeitura.getEmail(), this.prefeitura.getCidade().getCidadePK().getNomeCidade(), this.prefeitura.getCidade().getCidadePK().getSiglaEstado());

            } else {
                this.mostrarInformacoesPrefeitura = false;
//                FacesContext.getCurrentInstance().getExternalContext().redirect("/EGC/erro-404.jsf");
            }
        } catch (Exception e) {
            System.out.println("ERRO AO PESQUISAR PREFEITURA: " + e.getMessage());
        }

    }

    public List<Registro> registrosDaPrefeitura(String codigoPrefeitura) {
        return fachada.registroDaPrefeitura(codigoPrefeitura);
    }

    public List<Denuncia> denunciasPorCidade(String codigoPrefeitura) {
        return fachada.denunciaDaPrefeitura(codigoPrefeitura);
    }

    public List<Funcionario> funcionariosDaPrefeitura(String codigoPrefeitura) {
        return fachada.funcionarioDaPrefeitura(codigoPrefeitura);
    }

    public String novoComentario() {
        this.mensagemPrefeitura.setDataMensagem(new Date());
        System.out.println("mensagem: " + this.mensagemPrefeitura.toString());
        fachada.novaMensagemEmPrefeitura(this.mensagemPrefeitura, this.prefeitura);
        this.mensagemPrefeitura = new MensagemPrefeitura();
        return null;
    }

    public List<MensagemPrefeitura> mensagensDaPrefeitura(String codigoPrefeitura) {
        return fachada.mensagensDaPrefeitura(codigoPrefeitura);
    }

    //mapa pagina de pesquisa
    public void onMarkerSelect(OverlaySelectEvent event) {
        marker = (Marker) event.getOverlay();
    }

    public long totalDeDenunciasNaCidade(String cidade, String estado){
        return fachada.totalDeDenunciasNaCidade(cidade, estado);
    }
    
    public long totalDeDenunciasAtendidasNaCidade(String cidade, String estado){
        return fachada.totalDeDenunciasAtendidasNaCidade(cidade, estado);
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

    public boolean isMostrarInformacoesPrefeitura() {
        return mostrarInformacoesPrefeitura;
    }

    public void setMostrarInformacoesPrefeitura(boolean mostrarInformacoesPrefeitura) {
        this.mostrarInformacoesPrefeitura = mostrarInformacoesPrefeitura;
    }

    public Prefeitura getPrefeitura() {
        return prefeitura;
    }

    public void setPrefeitura(Prefeitura prefeitura) {
        this.prefeitura = prefeitura;
    }

    public List<Long> getInformacoesMunicipio() {
        return informacoesMunicipio;
    }

    public void setInformacoesMunicipio(List<Long> informacoesMunicipio) {
        this.informacoesMunicipio = informacoesMunicipio;
    }

    public Denuncia getVisualizarDenuncia() {
        return visualizarDenuncia;
    }

    public void setVisualizarDenuncia(Denuncia visualizarDenuncia) {
        this.visualizarDenuncia = visualizarDenuncia;
    }

    public String getCodigoDenuncia() {
        return codigoDenuncia;
    }

    public void setCodigoDenuncia(String codigoDenuncia) {
        this.codigoDenuncia = codigoDenuncia;
    }

    public String getCodigoPrefeitura() {
        return codigoPrefeitura;
    }

    public void setCodigoPrefeitura(String codigoPrefeitura) {
        this.codigoPrefeitura = codigoPrefeitura;
    }

    public MensagemPrefeitura getMensagemPrefeitura() {
        return mensagemPrefeitura;
    }

    public void setMensagemPrefeitura(MensagemPrefeitura mensagemPrefeitura) {
        this.mensagemPrefeitura = mensagemPrefeitura;
    }

    public MapModel getAdvancedModel() {
        return advancedModel;
    }

    public void setAdvancedModel(MapModel advancedModel) {
        this.advancedModel = advancedModel;
    }

    public Marker getMarker() {
        return marker;
    }

    public void setMarker(Marker marker) {
        this.marker = marker;
    }

    public String getEndLatitude() {
        return endLatitude;
    }

    public void setEndLatitude(String endLatitude) {
        this.endLatitude = endLatitude;
    }

    public String getEndLogitude() {
        return endLogitude;
    }

    public void setEndLogitude(String endLogitude) {
        this.endLogitude = endLogitude;
    }

    public String getEndZoom() {
        return endZoom;
    }

    public void setEndZoom(String endZoom) {
        this.endZoom = endZoom;
    }

}
