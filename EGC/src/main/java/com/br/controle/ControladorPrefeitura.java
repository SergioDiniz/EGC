/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.br.controle;

import com.br.beans.Cidade;
import com.br.beans.CidadePK;
import com.br.beans.Denuncia;
import com.br.beans.EnderecoPrefeitura;
import com.br.beans.EstadoDeAcompanhamento;
import com.br.beans.Funcionario;
import com.br.beans.Prefeitura;
import com.br.beans.Registro;
import static com.br.controle.ControladorAdmin.info;
import static com.br.controle.ControladorAdmin.infoUsuarioInvalido;
import com.br.fachada.Fachada;
import com.sun.javafx.scene.control.skin.VirtualFlow;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.faces.bean.SessionScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
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

    private Prefeitura prefeitura;
    private Cidade cidade;
    private CidadePK cidadePK;
    private Funcionario funcionario;
    private Funcionario funcionarioAux;
    private boolean funcionarioCadastrado;
    private boolean funcionarioVinculado;
    private boolean funcionarioNovo;
    private boolean mostrarModalDesvincular;
    private String CPFPesquisaF;
    private List<Denuncia> denunciaComMaisAjuda;
    private List<Denuncia> denunciaMaisRecentes;
    private List<Long> dadosPrefeitura;
    private List<Funcionario> funcionariosOnline;

    @EJB
    private Fachada fachada;

    public ControladorPrefeitura() {
        this.prefeitura = new Prefeitura();
        this.prefeitura.setEnderecoPrefeitura(new EnderecoPrefeitura());
        this.cidade = new Cidade();
        this.cidadePK = new CidadePK();
        this.funcionarioCadastrado = false;
        this.funcionarioNovo = false;
        this.funcionarioVinculado = false;
        this.mostrarModalDesvincular = false;
        this.CPFPesquisaF = "";
        this.funcionario = new Funcionario();
        this.funcionarioAux = new Funcionario();
        this.denunciaComMaisAjuda = new ArrayList<>();
        this.denunciaMaisRecentes = new ArrayList<>();
        this.funcionariosOnline = new ArrayList<>();
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
                denunciasComMaisAjudas();
                denunciasMaisRecentes();
                return "/sis/prefeitura/index.jsf?faces-redirect=true";
            }
            info("Conta aguardando confirmação!");
            this.prefeitura = new Prefeitura();
            this.prefeitura.setEmail("");
            return null;
        } else {
            infoUsuarioInvalido();
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
        prefeitura.setDocumento(upload(UploadType.DOCUMENTO_SOLICITACAO));
        fachada.atualizar(prefeitura);

        cidade = new Cidade();
        cidadePK = new CidadePK();
        prefeitura = new Prefeitura(new EnderecoPrefeitura());

        return "/index.jsf?faces-redirect=true";

    }

    public String upload(UploadType uploadType) {
        if (file != null) {
            try {

                File targetFolder;
                if (uploadType == UploadType.BRASAO_PREFEITURA) {
                    targetFolder = new File("/Volumes/Arquivos/Sergio/Documentos/ADS/P6/TCC/Sistema/EGC/EGC/src/main/webapp/sis/prefeitura/brasao");
                } else {
                    targetFolder = new File("/Volumes/Arquivos/Sergio/Documentos/ADS/P6/TCC/Sistema/EGC/EGC/src/main/webapp/sis/admin/documentos-de-solicitacao");
                }

                InputStream inputStream = file.getInputstream();
                String tipoArquivo = file.getFileName();
                tipoArquivo = tipoArquivo.substring(tipoArquivo.lastIndexOf("."), tipoArquivo.length());
                String nomeArquivo = prefeitura.getEmail() + tipoArquivo;
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

                this.prefeitura.setFoto(upload(UploadType.BRASAO_PREFEITURA));
                this.file = null;

                Thread.sleep(2000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        fachada.atualizar(prefeitura);
        return null;
    }

    public void pesquisarCPFFuncionario() {

        Funcionario f = fachada.buscarFuncionarioPorCPF(CPFPesquisaF);

        if (f != null) {
            funcionarioNovo = false;
            funcionarioCadastrado = true;
        } else {
            funcionarioNovo = true;
            funcionarioCadastrado = false;
        }

    }

    public String cadastrarNovoFuncionario() {
        funcionario.setCpf(CPFPesquisaF);
        if (this.funcionario.isSexo()) {
            this.funcionario.setFoto("h-funcionario.png");
        } else {
            this.funcionario.setFoto("m-funcionario.png");
        }
        fachada.cadastrar(funcionario);
        funcionario = fachada.buscarFuncionarioPorCPF(CPFPesquisaF);
        fachada.cadastrarNaPrefeitura(prefeitura, funcionario);
        this.prefeitura.setFuncionarios(funcionarios());
        funcionario = new Funcionario();
        CPFPesquisaF = "";
        funcionarioNovo = false;
        return "listafuncionarios.jsf?faces-redirect=true";
    }

    public List<Funcionario> funcionarioPesquisaCPF() {
        List<Funcionario> fus = new ArrayList<>();
        fus.add(fachada.buscarFuncionarioPorCPF(CPFPesquisaF));
        return fus;
    }

    public String vincularFuncionario() {
        Funcionario f = fachada.buscarFuncionarioPorCPF(CPFPesquisaF);
        fachada.vincularFuncionarioPrefeitura(prefeitura, f);
        this.prefeitura.setFuncionarios(funcionarios());
        CPFPesquisaF = "";
        funcionarioCadastrado = false;
        return "listafuncionarios.jsf?faces-redirect=true";
    }

    public List<Funcionario> funcionarios() {
        return fachada.funcionariosPrefeitura(prefeitura);
    }

    public String desvincularFuncionario() {
//        System.out.println("desvinculando: " + funcionarioAux.getNome());
        fachada.desvincularFuncionario(this.prefeitura, funcionarioAux.getCpf());
        this.prefeitura.getFuncionarios().remove(funcionarioAux);
        this.funcionarioAux = new Funcionario();

        return null;
    }

    public String desvincularFuncionario(Funcionario funcionario) {
//        System.out.println("desvinculando: " + funcionarioAux.getNome());
        fachada.desvincularFuncionario(this.prefeitura, funcionario.getCpf());
        this.prefeitura.getFuncionarios().remove(funcionario);

        return null;
    }

    public void modalDesvincular(Funcionario f) {
        this.funcionarioAux = f;
        this.mostrarModalDesvincular = true;
//        System.out.println("funcionario: " + funcionarioAux.getNome());
    }

    public long totalDeDenunciasAtendidasNaCidade() {
        return fachada.totalDeDenunciasAtendidasNaCidade(this.prefeitura.getCidade().getCidadePK().getNomeCidade(),
                this.prefeitura.getCidade().getCidadePK().getSiglaEstado());
    }

    public long totalDeDenunciasNaCidade() {
        return fachada.totalDeDenunciasNaCidade(this.prefeitura.getCidade().getCidadePK().getNomeCidade(),
                this.prefeitura.getCidade().getCidadePK().getSiglaEstado());
    }

    public long totalDeFuncionariosNaPrefeitura() {
        return fachada.totalDeFuncionariosNaPrefeitura(this.prefeitura.getEmail());
    }

    public long totalDeUsuariosNaCidade() {
        return fachada.totalDeUsuariosNaCidade(this.prefeitura.getCidade().getCidadePK().getNomeCidade(),
                this.prefeitura.getCidade().getCidadePK().getSiglaEstado());
    }

    public void denunciasComMaisAjudas() {
        this.denunciaComMaisAjuda = new ArrayList<>();
        this.denunciaComMaisAjuda.addAll(fachada.denunciasComMaisAjudasPorCidade(this.prefeitura.getCidade().getCidadePK().getNomeCidade(),
                this.prefeitura.getCidade().getCidadePK().getSiglaEstado()));

    }

    public void denunciasMaisRecentes() {
        this.denunciaMaisRecentes = new ArrayList<>();
        this.denunciaMaisRecentes.addAll(fachada.denunciasMaisRecentesPorCidade(this.prefeitura.getCidade().getCidadePK().getNomeCidade(),
                this.prefeitura.getCidade().getCidadePK().getSiglaEstado()));
    }

    public void dadosGeraisPrefeitura(String emailPrefeitura, String cidade, String estado) {
        this.dadosPrefeitura = new ArrayList<>();

        long denuncias = fachada.totalDeDenunciasNaCidade(cidade, estado);
        long denunciasAtendidas = fachada.totalDeDenunciasAtendidasNaCidade(cidade, estado);
        long usuarios = fachada.totalDeUsuariosNaCidade(cidade, estado);
        long funcionarios = fachada.totalDeFuncionariosNaPrefeitura(emailPrefeitura);

        this.dadosPrefeitura.add(0, denuncias);
        this.dadosPrefeitura.add(1, denunciasAtendidas);
        this.dadosPrefeitura.add(2, usuarios);
        this.dadosPrefeitura.add(3, funcionarios);
    }

    public void funcionariosQueEstaoOnline() {
        this.funcionariosOnline = new ArrayList<>();
        this.funcionariosOnline.addAll(fachada.funcionariosOnline(this.prefeitura.getEmail()));
    }

    
    
    
    
    //
    //
    //
    //
    //
    //
    //
    //
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

    public Funcionario getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(Funcionario funcionario) {
        this.funcionario = funcionario;
    }

    public boolean isFuncionarioCadastrado() {
        return funcionarioCadastrado;
    }

    public void setFuncionarioCadastrado(boolean funcionarioCadastrado) {
        this.funcionarioCadastrado = funcionarioCadastrado;
    }

    public boolean isFuncionarioVinculado() {
        return funcionarioVinculado;
    }

    public void setFuncionarioVinculado(boolean funcionarioVinculado) {
        this.funcionarioVinculado = funcionarioVinculado;
    }

    public boolean isFuncionarioNovo() {
        return funcionarioNovo;
    }

    public void setFuncionarioNovo(boolean funcionarioNovo) {
        this.funcionarioNovo = funcionarioNovo;
    }

    public String getCPFPesquisaF() {
        return CPFPesquisaF;
    }

    public void setCPFPesquisaF(String CPFPesquisaF) {
        this.CPFPesquisaF = CPFPesquisaF;
    }

    public Funcionario getFuncionarioAux() {
        return funcionarioAux;
    }

    public void setFuncionarioAux(Funcionario funcionarioAux) {
        this.funcionarioAux = funcionarioAux;
    }

    public boolean isMostrarModalDesvincular() {
        return mostrarModalDesvincular;
    }

    public void setMostrarModalDesvincular(boolean mostrarModalDesvincular) {
        this.mostrarModalDesvincular = mostrarModalDesvincular;
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

    public List<Long> getDadosPrefeitura() {
        return dadosPrefeitura;
    }

    public void setDadosPrefeitura(List<Long> dadosPrefeitura) {
        this.dadosPrefeitura = dadosPrefeitura;
    }

    public List<Funcionario> getFuncionariosOnline() {
        return funcionariosOnline;
    }

    public void setFuncionariosOnline(List<Funcionario> funcionariosOnline) {
        this.funcionariosOnline = funcionariosOnline;
    }

}
