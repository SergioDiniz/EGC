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
    String teste;
    String ordemDenuncia;
    TipoDeDenuncia tipoDenunciaFeed;
    TipoDeDenuncia tipoDenuncia;
    private UploadedFile ImgDenuncia;

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
        this.teste = "";
        this.tipoDenunciaFeed = TipoDeDenuncia.TODOS;
        this.ordemDenuncia = "data";
    }

    public String cadastro() {

        try {
            fachada.cadastrar(usuario);
            this.usuario = fachada.loginUsuario(usuario);
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

    public List<Denuncia> mostrarDenunicas() {

        switch (tipoDenunciaFeed) {
            case TODOS:
                return denunciasTodos(this.ordemDenuncia);
            case COLETA_DE_LIXO:
                return denunciasColetaDeLixo(this.ordemDenuncia);
        }

        return null;
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
    public String denunciasTodos() {
        this.tipoDenunciaFeed = TipoDeDenuncia.TODOS;
        return null;
    }

    public String denunciasColetaDeLixo() {
        this.tipoDenunciaFeed = TipoDeDenuncia.COLETA_DE_LIXO;
        return null;
    }

    //
    public List<Denuncia> denunciasTodos(String ordem) {
        return pesquisarTodasDenunciasPorCidade(TipoDeDenuncia.TODOS, ordem);
    }

    public List<Denuncia> denunciasColetaDeLixo(String ordem) {
        return pesquisarPorCidadeComFiltro(TipoDeDenuncia.COLETA_DE_LIXO, ordem);
    }

    

    //
    public List<Denuncia> pesquisarTodasDenunciasPorCidade(TipoDeDenuncia tipoDeDenuncia, String ordem) {
        return fachada.pesquisarTodasDenunciasPorCidade(this.cidadeDenuncia, this.ufDenuncia, ordem);
    }
    
    public List<Denuncia> pesquisarPorCidadeComFiltro(TipoDeDenuncia tipoDeDenuncia, String ordem) {
        return fachada.pesquisarDenunciaPorCidadeComFiltro(this.cidadeDenuncia, this.ufDenuncia, tipoDeDenuncia, ordem);
    }

    public void teste() {
        System.out.println("entrou: " + this.teste);
    }

    

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

    public String getTeste() {
        return teste;
    }

    public void setTeste(String teste) {
        this.teste = teste;
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

}
