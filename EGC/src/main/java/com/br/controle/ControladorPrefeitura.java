/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.br.controle;

import com.br.beans.Cidade;
import com.br.beans.CidadePK;
import com.br.beans.EnderecoPrefeitura;
import com.br.beans.Funcionario;
import com.br.beans.Prefeitura;
import static com.br.controle.ControladorAdmin.info;
import com.br.fachada.Fachada;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.faces.bean.SessionScoped;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.servlet.http.Part;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author SergioD
 */
@ManagedBean(name = "controladorPrefeitura")
@SessionScoped
public class ControladorPrefeitura implements Serializable {

    private UploadedFile file;

    Prefeitura prefeitura;
    Cidade cidade;
    CidadePK cidadePK;
    Funcionario funcionario;
    boolean funcionarioCadastrado;
    boolean funcionarioVinculado;
    boolean funcionarioNovo;
    String CPFPesquisaF;

    @EJB
    private Fachada fachada;

    public ControladorPrefeitura() {
        this.prefeitura = new Prefeitura();
        this.prefeitura.setEnderecoPrefeitura(new EnderecoPrefeitura());
        this.cidade = new Cidade();
        this.cidadePK = new CidadePK();
    }

    public void mostrapagina() throws IOException {
        if (this.prefeitura.getEmail() == null) {
            FacesContext.getCurrentInstance().getExternalContext().redirect("/EGC/index.jsf");
        }

    }

    public String login() {
        this.prefeitura = fachada.loginPrefeitura(prefeitura.getEmail(), prefeitura.getSenha());

        if (this.prefeitura != null) {

            if (prefeitura.isAtivo() == true) {

                return "/sis/prefeitura/index.jsf?faces-redirect=true";
            }
            info("Conta aguardando confirmação!");
            this.prefeitura = new Prefeitura();
            this.prefeitura.setEmail("");
            return null;
        } else {
            info("Usuário invalido!");
            this.prefeitura = new Prefeitura();
            this.prefeitura.setEmail("");
            return null;
        }

    }

    
    
    public String logout() {
        this.cidade = new Cidade();
        this.cidadePK = new CidadePK();
        this.prefeitura = new Prefeitura();
        this.funcionarioCadastrado = false;
        this.funcionarioNovo = false;
        this.funcionarioVinculado = false;
        this.CPFPesquisaF = "";
        this.funcionario = new Funcionario();
        return "/index.jsf?faces-redirect=true";
    }

    public String cadastro() throws IOException {

        try {
            cidade.setCidadePK(cidadePK);
            fachada.cadastrar(cidade);
            cidade = fachada.pesquisarCidade(cidadePK.getNomeCidade(), cidadePK.getSiglaEstado());
        } catch (Exception e) {

        }

        prefeitura.setCidade(cidade);
        prefeitura.getEnderecoPrefeitura().setCidade(cidade.getCidadePK().getNomeCidade());
        prefeitura.getEnderecoPrefeitura().setEstado(cidade.getCidadePK().getSiglaEstado());
        prefeitura.setDocumento(upload());
        fachada.atualizar(prefeitura);

        cidade = new Cidade();
        cidadePK = new CidadePK();
        prefeitura = new Prefeitura(new EnderecoPrefeitura());

        return "/index.jsf?faces-redirect=true";

    }

    public String upload() {
        if (file != null) {
            try {
                File targetFolder = new File("D:\\Sergio\\Documentos\\ADS\\P6\\TCC\\Sistema\\EGC\\EGC\\src\\main\\webapp\\sis\\admin\\documentos-de-solicitacao");
                InputStream inputStream = file.getInputstream();

                String tipoArquivo = file.getFileName();
                tipoArquivo = tipoArquivo.substring(tipoArquivo.lastIndexOf("."), tipoArquivo.length());
                String nomeArquivo = prefeitura.getEmail() + tipoArquivo;
                System.out.println("tipo do arquivo: " + tipoArquivo);
                System.out.println("nome do arquivo: " + nomeArquivo);

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

    // gets and setes
    public Prefeitura getPrefeitura() {
        return prefeitura;
    }

    public void setPrefeitura(Prefeitura prefeitura) {
        this.prefeitura = prefeitura;
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

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

}
