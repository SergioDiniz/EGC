/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.br.controle;

import com.br.beans.Cidade;
import com.br.beans.CidadePK;
import com.br.beans.Funcionario;
import com.br.beans.Registro;
import com.br.fachada.Fachada;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author SergioD
 */
@ManagedBean(name = "controladorFuncionario")
@SessionScoped
public class ControladorFuncionario implements Serializable {

    private UploadedFile file;

    @EJB
    private Fachada fachada;

    private Funcionario funcionario;
    private Cidade cidade;
    private CidadePK cidadePK;

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
        this.funcionario = fachada.loginFuncionario(funcionario.getEmail(), funcionario.getSenha(),
                cidadePK.getNomeCidade(), cidadePK.getSiglaEstado());
        if (funcionario != null) {

            this.cidade = fachada.pesquisarCidade(cidadePK.getNomeCidade(), cidadePK.getSiglaEstado());

            fachada.tornaFuncionarioOnline(this.funcionario.getEmail());

            return "/sis/funcionario/index.jsf?faces-redirect=true";

        } else {
            ControladorAdmin.infoUsuarioInvalido();
            funcionario = new Funcionario();
            cidadePK = new CidadePK();
            return null;
        }

    }

    public String logout() {

        fachada.tornaFuncionarioOffline(this.funcionario.getEmail());

        this.funcionario = new Funcionario();
        this.cidadePK = new CidadePK();
        return "/index.jsf?faces-redirect=true";
    }

    public String upload() {
        if (file != null) {
            try {
                File targetFolder = new File("/Volumes/Arquivos/Sergio/Documentos/ADS/P6/TCC/Sistema/EGC/EGC/src/main/webapp/sis/funcionario/foto");
                InputStream inputStream = file.getInputstream();
                String tipoArquivo = file.getFileName();
                tipoArquivo = tipoArquivo.substring(tipoArquivo.lastIndexOf("."), tipoArquivo.length());
                String nomeArquivo = this.funcionario.getEmail() + tipoArquivo; //Nome do Arquivo
                OutputStream out = new FileOutputStream(new File(targetFolder, nomeArquivo));
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

    public String atualizarDados() {

        if (file != null) {

            try {

                this.funcionario.setFoto(upload());
                this.file = null;

                Thread.sleep(3000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        fachada.atualizar(this.funcionario);
        return null;
    }

    public List<Registro> registrosDoFuncionario(String email) {
        return fachada.registrosDoFuncionario(email);
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

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

}
