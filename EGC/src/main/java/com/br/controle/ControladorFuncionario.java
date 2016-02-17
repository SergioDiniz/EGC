/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.br.controle;

import com.br.beans.Cidade;
import com.br.beans.CidadePK;
import com.br.beans.Denuncia;
import com.br.beans.Funcionario;
import com.br.beans.Registro;
import com.br.beans.TipoDeDenuncia;
import com.br.fachada.Fachada;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ComponentSystemEvent;
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
    private List<Denuncia> denunciaComMaisAjuda;
    private List<Denuncia> denunciaMaisRecentes;
    private List<Denuncia> denunciaGerenciadas;
    private TipoDeDenuncia tipoDeDenunciaGerenciando;

    private Denuncia denunciaGerenciada;

    private String codigoDenuncia = "";
    private List<String> listaDeCodigo;

    public ControladorFuncionario() {
        this.funcionario = new Funcionario();
        this.cidade = new Cidade();
        this.cidadePK = new CidadePK();
        this.denunciaComMaisAjuda = new ArrayList<>();
        this.denunciaMaisRecentes = new ArrayList<>();
        this.listaDeCodigo = new ArrayList<>();
    }

    public void mostrapagina() throws IOException {
        if (this.funcionario.getEmail() == null) {
            FacesContext.getCurrentInstance().getExternalContext().redirect("/EGC/index.jsf");
        }

    }

    public void login() throws IOException {
        this.funcionario = fachada.loginFuncionario(funcionario.getEmail(), funcionario.getSenha(),
                cidadePK.getNomeCidade(), cidadePK.getSiglaEstado());
        if (funcionario != null) {

            this.cidade = fachada.pesquisarCidade(cidadePK.getNomeCidade(), cidadePK.getSiglaEstado());

            fachada.tornaFuncionarioOnline(this.funcionario.getEmail());

            denunciasComMaisAjudas();
            denunciasMaisRecentes();

            this.tipoDeDenunciaGerenciando = TipoDeDenuncia.TODOS;
            FacesContext.getCurrentInstance().getExternalContext().redirect("/EGC/funcionario");
//            return "/EGC/funcionario";

        } else {
            ControladorAdmin.infoUsuarioInvalido();
            funcionario = new Funcionario();
            cidadePK = new CidadePK();
//            return null;
        }

    }

    public String logout() {

        fachada.tornaFuncionarioOffline(this.funcionario.getEmail());

//        this.funcionario = new Funcionario();
//        this.cidadePK = new CidadePK();
        this.funcionario = new Funcionario();
        this.cidade = new Cidade();
        this.cidadePK = new CidadePK();
        this.denunciaComMaisAjuda = new ArrayList<>();
        this.denunciaMaisRecentes = new ArrayList<>();

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

        if (file.getSize() > 0) {

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

    public long totalDeDenunciasNaCidade() {
        return fachada.totalDeDenunciasNaCidade(this.cidade.getCidadePK().getNomeCidade(), this.cidade.getCidadePK().getSiglaEstado());
    }

    public long totalDeDenunciasAtendidasNaCidade() {
        return fachada.totalDeDenunciasAtendidasNaCidade(this.cidade.getCidadePK().getNomeCidade(), this.cidade.getCidadePK().getSiglaEstado());
    }

    public void denunciasComMaisAjudas() {
        this.denunciaComMaisAjuda = new ArrayList<>();
        this.denunciaComMaisAjuda.addAll(fachada.denunciasComMaisAjudasPorCidade(this.cidade.getCidadePK().getNomeCidade(),
                this.cidade.getCidadePK().getSiglaEstado()));

    }

    public void denunciasMaisRecentes() {
        this.denunciaMaisRecentes = new ArrayList<>();
        this.denunciaMaisRecentes.addAll(fachada.denunciasMaisRecentesPorCidade(this.cidade.getCidadePK().getNomeCidade(),
                this.cidade.getCidadePK().getSiglaEstado()));
    }

    public String paginaGerenciarDenuncias() {
        setTodasDenunciasEmGerenciamento();
        return "denuncias.jsf?faces-redirect=true";
    }

    public void setTodasDenunciasEmGerenciamento() {
        this.denunciaGerenciadas = new ArrayList<>();
        this.denunciaGerenciadas = fachada.pesquisarTodasDenunciasPorCidade(this.cidade.getCidadePK().getNomeCidade(),
                this.cidade.getCidadePK().getSiglaEstado(), "data");
    }

    public void addCodigoDenuncia(ComponentSystemEvent event) {
        System.out.println("Codigo: " + this.codigoDenuncia);
        this.listaDeCodigo.add(this.codigoDenuncia);
        System.out.println("Tamanho da Lista: " + this.listaDeCodigo.size());
    }

    public void pesquisarDenuncia(ComponentSystemEvent event) {
        System.out.println("Codigo Denuncia: " + this.codigoDenuncia);
        this.denunciaGerenciada = new Denuncia();
        this.denunciaGerenciada = fachada.pesquisarDenunicaCodigo(this.codigoDenuncia);
        System.out.println("Descricao: " + this.denunciaGerenciada.getDescricao());

    }

    //
    //
    //
    //
    //
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

    public List<Denuncia> getDenunciaComMaisAjuda() {
        return denunciaComMaisAjuda;
    }

    public void setDenunciaComMaisAjuda(List<Denuncia> denunciaComMaisAjuda) {
        this.denunciaComMaisAjuda = denunciaComMaisAjuda;
    }

    public List<Denuncia> getDenunciaMaisRecentes() {
        return denunciaMaisRecentes;
    }

    public void setDenunciaMaisRecentes(List<Denuncia> denunciaMaisRecentes) {
        this.denunciaMaisRecentes = denunciaMaisRecentes;
    }

    public List<Denuncia> getDenunciaGerenciadas() {
        return denunciaGerenciadas;
    }

    public void setDenunciaGerenciadas(List<Denuncia> denunciaGerenciadas) {
        this.denunciaGerenciadas = denunciaGerenciadas;
    }

    public TipoDeDenuncia getTipoDeDenunciaGerenciando() {
        return tipoDeDenunciaGerenciando;
    }

    public void setTipoDeDenunciaGerenciando(TipoDeDenuncia tipoDeDenunciaGerenciando) {
        this.tipoDeDenunciaGerenciando = tipoDeDenunciaGerenciando;
    }

    public String getCodigoDenuncia() {
        System.out.println("get: " + this.codigoDenuncia);
        return codigoDenuncia;
    }

    public void setCodigoDenuncia(String codigoDenuncia) {
        System.out.println("set " + this.codigoDenuncia);
        this.codigoDenuncia = codigoDenuncia;
    }

    public List<String> getListaDeCodigo() {
        return listaDeCodigo;
    }

    public void setListaDeCodigo(List<String> listaDeCodigo) {
        this.listaDeCodigo = listaDeCodigo;
    }

    public Denuncia getDenunciaGerenciada() {
        return denunciaGerenciada;
    }

    public void setDenunciaGerenciada(Denuncia denunciaGerenciada) {
        this.denunciaGerenciada = denunciaGerenciada;
    }

}
