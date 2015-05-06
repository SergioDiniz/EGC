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
        
        System.out.println(tipoDenuncia);
        
        String end[] = this.endMapa.split(",");
        
        for (String e : end) {
            System.out.println(e);
        }
        
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


    
    

}
