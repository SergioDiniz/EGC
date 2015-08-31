/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.br.controle;

import com.br.beans.*;
import com.br.fachada.Fachada;
import com.sun.webkit.Timer;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author Sergio Diniz
 */
@ManagedBean(name = "controladorUsuario")
@SessionScoped
public class ControladorUsuario implements Serializable {

    Usuario usuario;
    String denuncia;
    String endMapa;
    String endLatitude;
    String endLogitude;
    String cidadeDenuncia;
    String ufDenuncia;
    String ordemDenuncia;
    String strPesquisarCidadeFiltro;
    TipoDeDenuncia tipoDenunciaFeed;
    TipoDeDenuncia tipoDenuncia;
    ConteudoInapropriado conteudoInapropriado;
    private UploadedFile ImgDenuncia;
    int totalDenuncias;
    int denunicasAtendidas;

    @EJB
    private Fachada fachada;

    public ControladorUsuario() {
        this.usuario = new Usuario(new EnderecoUsuario());
        this.endMapa = "";
        this.endLatitude = "";
        this.endLogitude = "";
        this.denuncia = "";
        this.cidadeDenuncia = "";
        this.ufDenuncia = "";
        this.tipoDenunciaFeed = TipoDeDenuncia.TODOS;
        this.ordemDenuncia = "data";
        this.strPesquisarCidadeFiltro = "";
        this.totalDenuncias = 0;
        this.denunicasAtendidas = 0;
        this.conteudoInapropriado = new ConteudoInapropriado();
    }

    public String cadastro() {

        try {
            fachada.cadastrar(usuario);
            this.usuario = fachada.loginUsuario(usuario);
            this.cidadeDenuncia = this.usuario.getEndereco().getCidade();
            this.ufDenuncia = this.usuario.getEndereco().getEstado();
            return "/sis/usuario/index.jsf?faces-redirect=true";
        } catch (Exception e) {

        }

        this.usuario = new Usuario(new EnderecoUsuario());
        return null;
    }

    public String atualizar() {

        fachada.atualizar(this.usuario);
        return null;
    }

    public void mostrapagina() throws IOException {
        if (usuario.getEmail() == null) {
            FacesContext.getCurrentInstance().getExternalContext().redirect("/EGC/index.jsf");
        }

    }

    public String login() {
        this.usuario = fachada.loginUsuario(this.usuario);
        if (usuario != null) {
            this.cidadeDenuncia = this.usuario.getEndereco().getCidade();
            this.ufDenuncia = this.usuario.getEndereco().getEstado();
            this.tipoDenunciaFeed = TipoDeDenuncia.TODOS;
            this.ordemDenuncia = "data";
            return "/sis/usuario/index.jsf?faces-redirect=true";
//            FacesContext.getCurrentInstance().getExternalContext().redirect(null);
        } else {
            ControladorAdmin.infoUsuarioInvalido();
            this.usuario = new Usuario(new EnderecoUsuario());
            return null;
        }

    }

    public String logout() {
        this.usuario = new Usuario(new EnderecoUsuario());
        return "/index.jsf?faces-redirect=true";
    }

    public String fazerDenuncia() {

        try {
            String end[] = this.endMapa.split(", ");
            String numeroBairro[] = end[1].split(" - ");
            String cidadeEstado[] = end[2].split(" - ");

            EnderecoDenuncia enderecoDenuncia = new EnderecoDenuncia();

            enderecoDenuncia.setRua(end[0]); // rua
            enderecoDenuncia.setNumero(numeroBairro[0]); //numero
            if (numeroBairro.length > 1) { //bairro
                enderecoDenuncia.setBairro(numeroBairro[1]);
            } else {
                enderecoDenuncia.setBairro("S/B");
            }
            enderecoDenuncia.setCidade(cidadeEstado[0]); // Cidade
            enderecoDenuncia.setEstado(cidadeEstado[1]); // UF
            enderecoDenuncia.setCep(end[3]); //CEP
            enderecoDenuncia.setPais(end[4]); //Pais
            enderecoDenuncia.setLatitude(this.endLatitude); //Latitude
            enderecoDenuncia.setLongitude(this.endLogitude); //Longitude

            String nomeFoto = upload();

            fachada.novaDenuncia(this.usuario, enderecoDenuncia, this.denuncia, nomeFoto, tipoDenuncia);

            this.endLatitude = "";
            this.endLogitude = "";
            this.endMapa = "";
            this.denuncia = "";
        } catch (Exception e) {
        }

        return "/sis/usuario/index.jsf?faces-redirect=true";

    }

    public String upload() {
        if (ImgDenuncia != null) {

            try {

                File targetFolder = new File("D:\\Sergio\\Documentos\\ADS\\P6\\TCC\\Sistema\\EGC\\EGC\\src\\main\\webapp\\sis\\denuncias");
                InputStream inputStream = ImgDenuncia.getInputstream();
                String tipoArquivo = ImgDenuncia.getFileName();
                tipoArquivo = tipoArquivo.substring(tipoArquivo.lastIndexOf("."), tipoArquivo.length());
                String nomeArquivo = System.currentTimeMillis() + tipoArquivo;
                OutputStream out = new FileOutputStream(new File(targetFolder,
                        nomeArquivo));

                int read = 0;

                byte[] bytes = new byte[1024];

                while ((read = inputStream.read(bytes)) != -1) {
                    out.write(bytes, 0, read);
                }

                inputStream.close();
                out.flush();
                out.close();
                return nomeArquivo;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;

    }

    public String pesquisarCidadeFiltro() {
        String cidadeEstado[] = this.strPesquisarCidadeFiltro.split(" - ");

        this.cidadeDenuncia = cidadeEstado[0];
        this.ufDenuncia = cidadeEstado[1];

        this.strPesquisarCidadeFiltro = "";
        this.ordemDenuncia = "data";
        this.tipoDenunciaFeed = TipoDeDenuncia.TODOS;
        return null;
    }

    public List<Denuncia> mostrarDenunicas() {

        switch (tipoDenunciaFeed) {
            case TODOS:
                return denunciasTodos(this.ordemDenuncia);
            case COLETA_DE_LIXO:
                return denunciasColetaDeLixo(this.ordemDenuncia);
            case DISTRIBUICAO_E_QUALIDADE_DA_AGUA:
                return denunciasQualidadeAgua(this.ordemDenuncia);
            case ILUMINACAO_PUBLICA:
                return denunciasIluminacaoPublica(this.ordemDenuncia);
            case OBRAS_PUBLICAS:
                return denunciasObrasPublicas(this.ordemDenuncia);
            case MANUTENCAO_DE_CANAIS_E_REDES_DE_ESGOTOS:
                return denunciasManutencaoEsgoto(this.ordemDenuncia);
            case MANUTENCAO_E_CONSERVACAO_DE_VIAS_PUBLICAS:
                return denunciasManutencaoViasPublicas(this.ordemDenuncia);
            case PODA_E_MANUTENCAO_DAS_ARVORES:
                return denunciasPodaDeArvores(this.ordemDenuncia);
            case POLUICAO_VISUAL:
                return denunciasPoluicaoVisual(this.ordemDenuncia);
            case TRANSITO_SINALIZACAO_E_PLACAS:
                return denunciasTransitoPlacas(this.ordemDenuncia);
            case ACESSIBILIDADE:
                return denunciasAcessibilidade(this.ordemDenuncia);
            case SAUDE:
                return denunciasSaude(this.ordemDenuncia);
            case EDUCACAO:
                return denunciasEducacao(this.ordemDenuncia);
            case BRASIL:
                return denunciasBrasil(this.ordemDenuncia);

        }

        return null;
    }

    public long totalDeDenunciasNaCidade() {
        return fachada.totalDeDenunciasNaCidade(this.cidadeDenuncia, this.ufDenuncia);
    }

    public long totalDeDenunciasAtendidasNaCidade() {
        return fachada.totalDeDenunciasAtendidasNaCidade(this.cidadeDenuncia, this.ufDenuncia);
    }

    //
    public String denunciasOrdemData() {
        this.ordemDenuncia = "data";
        return null;
    }

    public String denunciasOrdemReclamacao() {
        this.ordemDenuncia = "reclamacao";
        return null;
    }

    //
    public String denunciasBrasil() {
        this.tipoDenunciaFeed = TipoDeDenuncia.BRASIL;
        this.cidadeDenuncia = "Brasil";
        this.ufDenuncia = "Brasil";
        return null;
    }

    public String denunciasTodos() {
        this.tipoDenunciaFeed = TipoDeDenuncia.TODOS;

        if (this.cidadeDenuncia.equals("Brasil")) {
            this.cidadeDenuncia = this.usuario.getEndereco().getCidade();
            this.ufDenuncia = this.usuario.getEndereco().getEstado();
        }

        return null;
    }

    public String denunciasColetaDeLixo() {
        this.tipoDenunciaFeed = TipoDeDenuncia.COLETA_DE_LIXO;
        return null;
    }

    public String denunciasQualidadeAgua() {
        this.tipoDenunciaFeed = TipoDeDenuncia.DISTRIBUICAO_E_QUALIDADE_DA_AGUA;
        return null;
    }

    public String denunciasIluminacaoPublica() {
        this.tipoDenunciaFeed = TipoDeDenuncia.ILUMINACAO_PUBLICA;
        return null;
    }

    public String denunciasObrasPublicas() {
        this.tipoDenunciaFeed = TipoDeDenuncia.OBRAS_PUBLICAS;
        return null;
    }

    public String denunciasManutencaoEsgoto() {
        this.tipoDenunciaFeed = TipoDeDenuncia.MANUTENCAO_DE_CANAIS_E_REDES_DE_ESGOTOS;
        return null;
    }

    public String denunciasManutencaoViasPublicas() {
        this.tipoDenunciaFeed = TipoDeDenuncia.MANUTENCAO_E_CONSERVACAO_DE_VIAS_PUBLICAS;
        return null;
    }

    public String denunciasPodaDeArvores() {
        this.tipoDenunciaFeed = TipoDeDenuncia.PODA_E_MANUTENCAO_DAS_ARVORES;
        return null;
    }

    public String denunciasPoluicaoVisual() {
        this.tipoDenunciaFeed = TipoDeDenuncia.POLUICAO_VISUAL;
        return null;
    }

    public String denunciasTransitoPlacas() {
        this.tipoDenunciaFeed = TipoDeDenuncia.TRANSITO_SINALIZACAO_E_PLACAS;
        return null;
    }

    public String denunciasAcessibilidade() {
        this.tipoDenunciaFeed = TipoDeDenuncia.ACESSIBILIDADE;
        return null;
    }

    public String denunciasSaude() {
        this.tipoDenunciaFeed = TipoDeDenuncia.SAUDE;
        return null;
    }

    public String denunciasEducacao() {
        this.tipoDenunciaFeed = TipoDeDenuncia.EDUCACAO;
        return null;
    }

    //
    public List<Denuncia> denunciasBrasil(String ordem) {
        return pesquisarPorCidadeComFiltro(TipoDeDenuncia.BRASIL, ordem);
    }

    public List<Denuncia> denunciasTodos(String ordem) {
        return pesquisarTodasDenunciasPorCidade(TipoDeDenuncia.TODOS, ordem);
    }

    public List<Denuncia> denunciasColetaDeLixo(String ordem) {
        return pesquisarPorCidadeComFiltro(TipoDeDenuncia.COLETA_DE_LIXO, ordem);
    }

    public List<Denuncia> denunciasQualidadeAgua(String ordem) {
        return pesquisarPorCidadeComFiltro(TipoDeDenuncia.DISTRIBUICAO_E_QUALIDADE_DA_AGUA, ordem);
    }

    public List<Denuncia> denunciasIluminacaoPublica(String ordem) {
        return pesquisarPorCidadeComFiltro(TipoDeDenuncia.ILUMINACAO_PUBLICA, ordem);
    }

    public List<Denuncia> denunciasObrasPublicas(String ordem) {
        return pesquisarPorCidadeComFiltro(TipoDeDenuncia.OBRAS_PUBLICAS, ordem);
    }

    public List<Denuncia> denunciasManutencaoEsgoto(String ordem) {
        return pesquisarPorCidadeComFiltro(TipoDeDenuncia.MANUTENCAO_DE_CANAIS_E_REDES_DE_ESGOTOS, ordem);
    }

    public List<Denuncia> denunciasManutencaoViasPublicas(String ordem) {
        return pesquisarPorCidadeComFiltro(TipoDeDenuncia.MANUTENCAO_E_CONSERVACAO_DE_VIAS_PUBLICAS, ordem);
    }

    public List<Denuncia> denunciasPodaDeArvores(String ordem) {
        return pesquisarPorCidadeComFiltro(TipoDeDenuncia.PODA_E_MANUTENCAO_DAS_ARVORES, ordem);
    }

    public List<Denuncia> denunciasPoluicaoVisual(String ordem) {
        return pesquisarPorCidadeComFiltro(TipoDeDenuncia.POLUICAO_VISUAL, ordem);
    }

    public List<Denuncia> denunciasTransitoPlacas(String ordem) {
        return pesquisarPorCidadeComFiltro(TipoDeDenuncia.TRANSITO_SINALIZACAO_E_PLACAS, ordem);
    }

    public List<Denuncia> denunciasAcessibilidade(String ordem) {
        return pesquisarPorCidadeComFiltro(TipoDeDenuncia.ACESSIBILIDADE, ordem);
    }

    public List<Denuncia> denunciasSaude(String ordem) {
        return pesquisarPorCidadeComFiltro(TipoDeDenuncia.SAUDE, ordem);
    }

    public List<Denuncia> denunciasEducacao(String ordem) {
        return pesquisarPorCidadeComFiltro(TipoDeDenuncia.EDUCACAO, ordem);
    }

    //
    public List<Denuncia> pesquisarTodasDenunciasPorCidade(TipoDeDenuncia tipoDeDenuncia, String ordem) {
        return fachada.pesquisarTodasDenunciasPorCidade(this.cidadeDenuncia, this.ufDenuncia, ordem);
    }

    public List<Denuncia> pesquisarPorCidadeComFiltro(TipoDeDenuncia tipoDeDenuncia, String ordem) {
        return fachada.pesquisarDenunciaPorCidadeComFiltro(this.cidadeDenuncia, this.ufDenuncia, tipoDeDenuncia, ordem);
    }

    public List<Denuncia> minhasDenunicas() {
        return fachada.minhasDenuncias(this.usuario.getEmail());
    }

    public void setAjudarDenuncia(Denuncia denuncia) {
        fachada.setAjudarDenuncia(denuncia, this.usuario);
    }

    public int getAjudarDenuncia(Denuncia denuncia) {
        return fachada.getAjudarDenuncia(denuncia);
    }
    
    public List<Denuncia> denunciasQueAjudei(){
        return fachada.denunciasQueAjudei(this.usuario.getEmail());
    }
    
    public String reclamarDenuncia(Denuncia denuncia){
        
        
        this.conteudoInapropriado = new ConteudoInapropriado();
        return "";
        
    }
    

//    
//
//
//
//    
//    
//
//
//    
    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getEndMapa() {
        return endMapa;
    }

    public void setEndMapa(String endMapa) {
        this.endMapa = endMapa;
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

    public String getDenuncia() {
        return denuncia;
    }

    public void setDenuncia(String denuncia) {
        this.denuncia = denuncia;
    }

    public UploadedFile getImgDenuncia() {
        return ImgDenuncia;
    }

    public void setImgDenuncia(UploadedFile ImgDenuncia) {
        this.ImgDenuncia = ImgDenuncia;
    }

    public TipoDeDenuncia getTipoDenuncia() {
        return tipoDenuncia;
    }

    public void setTipoDenuncia(TipoDeDenuncia tipoDenuncia) {
        this.tipoDenuncia = tipoDenuncia;
    }

    public String getCidadeDenuncia() {
        return cidadeDenuncia;
    }

    public void setCidadeDenuncia(String cidadeDenuncia) {
        this.cidadeDenuncia = cidadeDenuncia;
    }

    public String getUfDenuncia() {
        return ufDenuncia;
    }

    public void setUfDenuncia(String ufDenuncia) {
        this.ufDenuncia = ufDenuncia;
    }

    public String getOrdemDenuncia() {
        return ordemDenuncia;
    }

    public void setOrdemDenuncia(String ordemDenuncia) {
        this.ordemDenuncia = ordemDenuncia;
    }

    public TipoDeDenuncia getTipoDenunciaFeed() {
        return tipoDenunciaFeed;
    }

    public void setTipoDenunciaFeed(TipoDeDenuncia tipoDenunciaFeed) {
        this.tipoDenunciaFeed = tipoDenunciaFeed;
    }

    public String getStrPesquisarCidadeFiltro() {
        return strPesquisarCidadeFiltro;
    }

    public void setStrPesquisarCidadeFiltro(String strPesquisarCidadeFiltro) {
        this.strPesquisarCidadeFiltro = strPesquisarCidadeFiltro;
    }

    public int getTotalDenuncias() {
        return totalDenuncias;
    }

    public void setTotalDenuncias(int totalDenuncias) {
        this.totalDenuncias = totalDenuncias;
    }

    public int getDenunicasAtendidas() {
        return denunicasAtendidas;
    }

    public void setDenunicasAtendidas(int denunicasAtendidas) {
        this.denunicasAtendidas = denunicasAtendidas;
    }

    public ConteudoInapropriado getConteudoInapropriado() {
        return conteudoInapropriado;
    }

    public void setConteudoInapropriado(ConteudoInapropriado conteudoInapropriado) {
        this.conteudoInapropriado = conteudoInapropriado;
    }

    
}
