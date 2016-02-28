/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.br.controle;

import com.br.beans.Cidade;
import com.br.beans.CidadePK;
import com.br.beans.ConteudoInapropriado;
import com.br.beans.Denuncia;
import com.br.beans.EstadoDeAcompanhamento;
import com.br.beans.Funcionario;
import com.br.beans.InformacaoDeAtendida;
import com.br.beans.Prefeitura;
import com.br.beans.Registro;
import com.br.beans.TipoDeDenuncia;
import com.br.beans.TipoDeRegistro;
import com.br.beans.Usuario;
import com.br.fachada.Fachada;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ComponentSystemEvent;
import org.apache.commons.mail.EmailException;
import org.primefaces.event.FileUploadEvent;
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
    private InformacaoDeAtendida informacaoDeAtendida;
    private ConteudoInapropriado conteudoInapropriado;
    private String emailRecuperarSenha;
    
    private String filtroData;
    private String filtroAjuda;
    private String filtroQuery;
    private String filtroTipo;

    private Denuncia denunciaGerenciada;

    private String codigoDenuncia = "";

    public ControladorFuncionario() {
        this.funcionario = new Funcionario();
        this.cidade = new Cidade();
        this.cidadePK = new CidadePK();
        this.denunciaComMaisAjuda = new ArrayList<>();
        this.denunciaMaisRecentes = new ArrayList<>();
        this.informacaoDeAtendida = new InformacaoDeAtendida();
        this.conteudoInapropriado = new ConteudoInapropriado();
        this.filtroData = "DATA_DESC";
        this.filtroAjuda = "AJUDA_DESC";
        this.filtroQuery = "";
        this.filtroTipo = "";
        this.emailRecuperarSenha = "";
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

    public String recuperarSenha() throws EmailException, MalformedURLException {
        Funcionario f = new Funcionario();
        f = fachada.funcionarioPorEmail(this.emailRecuperarSenha);
        if (f != null) {
            //String emailUsuario, String nomeUsuario, String prefeitura, String senha, EmailType emailType
            ControladorAdmin.enviarEmail(f.getEmail(), f.getNome(), "", f.getSenha(), EmailType.RECUPERAR_SENHA);
            this.emailRecuperarSenha = "";
            ControladorAdmin.info("Verifique seu Email!");
            return null;
        }
        this.emailRecuperarSenha = "";
        ControladorAdmin.info("Email não Encontrado!");
        return null;
    }
    
    
    
    public String upload(UploadedFile fileUp, UploadType uploadType) {
        if (fileUp != null) {
            try {
                //Caminho
                File targetFolder;
                if (uploadType == UploadType.PERFIL_FUNCIONARIO) {
                    targetFolder = new File("/Volumes/Arquivos/Sergio/Documentos/ADS/P6/TCC/Sistema/EGC/EGC/src/main/webapp/sis/funcionario/foto");
                } else {
                    targetFolder = new File("/Volumes/Arquivos/Sergio/Documentos/ADS/P6/TCC/Sistema/EGC/EGC/src/main/webapp/sis/denuncias");
                }

                InputStream inputStream = fileUp.getInputstream();
                String tipoArquivo = fileUp.getFileName();
                tipoArquivo = tipoArquivo.substring(tipoArquivo.lastIndexOf("."), tipoArquivo.length());

                //Nome do Arquivo
                String nomeArquivo;
                if (uploadType == UploadType.PERFIL_FUNCIONARIO) {
                    nomeArquivo = this.funcionario.getEmail() + tipoArquivo;
                } else {
                    nomeArquivo = System.currentTimeMillis() + tipoArquivo;
                }

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
                System.out.println("EERO O FAZER UPLOAD DE ARQUIVO: " + e.getMessage());
            }
        }
        return null;

    }

    public String atualizarDados() {

        if (file.getSize() > 0) {

            try {

                this.funcionario.setFoto(upload(this.file, UploadType.PERFIL_FUNCIONARIO));
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
    
    public List<Denuncia> denunciasAtendidasEmCidade(){
        return fachada.denunciasAtendidasEmCidade(this.cidade.getCidadePK().getNomeCidade(),
                this.cidade.getCidadePK().getSiglaEstado());
    }

    public String paginaGerenciarDenuncias() {
        setTodasDenunciasEmGerenciamento();
        this.filtroData = "DATA_DESC";
        this.filtroAjuda = "AJUDA_DESC";
        this.filtroQuery = "";
        this.filtroTipo = "";
        return "denuncias.jsf?faces-redirect=true";
    }

    public void setTodasDenunciasEmGerenciamento() {
        this.denunciaGerenciadas = new ArrayList<>();
        this.denunciaGerenciadas = fachada.pesquisarTodasDenunciasPorCidade(this.cidade.getCidadePK().getNomeCidade(),
                this.cidade.getCidadePK().getSiglaEstado(), "data");
    }

    public void pesquisarDenuncia(ComponentSystemEvent event) {
//        System.out.println("Codigo Denuncia: " + this.codigoDenuncia);
        this.denunciaGerenciada = new Denuncia();
        this.denunciaGerenciada = fachada.pesquisarDenunicaCodigo(this.codigoDenuncia);
//        System.out.println("Descricao: " + this.denunciaGerenciada.getDescricao());

    }

    public void atualizarDenunciaGerenciada() {

        Registro registro = new Registro();
        registro.setData(new Date());
        registro.setDenuncia(this.denunciaGerenciada);
        registro.setFuncionario(this.funcionario);
        registro.setPrefeitura(fachada.pesquisarPrefeituraPorCidade(this.cidade.getCidadePK().getNomeCidade(), this.cidade.getCidadePK().getSiglaEstado()));
        if (this.denunciaGerenciada.getEstadoDeAcompanhamento() == EstadoDeAcompanhamento.AGUARDANDO) {
            registro.setTipoDeRegistro(TipoDeRegistro.AGUARDANDO);
        } else {
            registro.setTipoDeRegistro(TipoDeRegistro.DENUNCIA_EM_TRABALHO);
        }

        System.out.println("Registro: " + registro.toString());

        fachada.atualizarDenunciaGerenciada(registro);

    }

    public void paginaAtenderDenuncia() throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().redirect("/EGC/sis/funcionario/atenderdenuncia.jsf");
    }

    public String atenderDenuncia() {

        if (file.getSize() > 0) {
            try {
                // Upando e Setando informações sobre a foto do atendimento
                this.informacaoDeAtendida.setFoto((upload(this.file, UploadType.FOTO_DENUNCIA_ATENDIDA)));
                this.file = null;

                Thread.sleep(3000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // Setando informações sobre o atendimento
        this.informacaoDeAtendida.setData(new Date());
        this.informacaoDeAtendida.setFuncionario(this.funcionario);

        // Criando registro par agardar no historico
        Registro registro = new Registro();
        registro.setData(new Date());
        this.denunciaGerenciada.setEstadoDeAcompanhamento(EstadoDeAcompanhamento.ATENDIDA);
        registro.setDenuncia(this.denunciaGerenciada);
        registro.setFuncionario(this.funcionario);
        registro.setPrefeitura(fachada.pesquisarPrefeituraPorCidade(this.cidade.getCidadePK().getNomeCidade(), this.cidade.getCidadePK().getSiglaEstado()));
        registro.setTipoDeRegistro(TipoDeRegistro.DENUNCIA_ATENDIDA);

        fachada.atenderDenuncia(this.informacaoDeAtendida, registro);
        this.informacaoDeAtendida = new InformacaoDeAtendida();
        return null;
    }

    public String fazerReclamacaoEmDenuncia() {
        
        
        //Salvando reclamação
        fachada.setReclamarDenuncia(this.denunciaGerenciada, this.conteudoInapropriado);
        this.conteudoInapropriado = new ConteudoInapropriado();
            
        // Criando registro par agardar no historico
        Registro registro = new Registro();
        registro.setData(new Date());
        this.denunciaGerenciada.setEstadoDeAcompanhamento(EstadoDeAcompanhamento.ATENDIDA);
        registro.setDenuncia(this.denunciaGerenciada);
        registro.setFuncionario(this.funcionario);
        registro.setPrefeitura(fachada.pesquisarPrefeituraPorCidade(this.cidade.getCidadePK().getNomeCidade(), this.cidade.getCidadePK().getSiglaEstado()));
        registro.setTipoDeRegistro(TipoDeRegistro.RECLAMACAO);

        fachada.atualizar(registro);
        
        return null;
    }
    
    public String gerenciarDenunciasFiltro(String ordem, String filtroQuery, String filtro){
        
        // setando ordem
        if(ordem.compareToIgnoreCase("DATA") == 0){
            if(this.filtroData.compareToIgnoreCase("DATA_ASC") == 0){
                this.filtroData = "DATA_DESC";
            } else {
                this.filtroData = "DATA_ASC";
            }
            ordem = this.filtroData;
            
            
        } else if (ordem.compareToIgnoreCase("AJUDA") == 0) {
            if(this.filtroAjuda.compareToIgnoreCase("AJUDA_ASC") == 0){
                this.filtroAjuda = "AJUDA_DESC";
            } else{
                this.filtroAjuda = "AJUDA_ASC";
            }
            ordem = this.filtroAjuda;
            
        }
        
        this.filtroQuery = filtroQuery;
        this.filtroTipo = filtro;
        
        
        
        this.denunciaGerenciadas = new ArrayList<>();
        this.denunciaGerenciadas = fachada.gerenciarDenunciasFiltro(this.cidade.getCidadePK().getNomeCidade(), this.cidade.getCidadePK().getSiglaEstado(), 
                ordem, this.filtroQuery, this.filtroTipo);
        
        
        return null;
        
    }
    
    
    public long andamentoDasDenuncias(){
        return (totalDeDenunciasAtendidasNaCidade() * 100) / totalDeDenunciasNaCidade();
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
//        System.out.println("get: " + this.codigoDenuncia);
        return codigoDenuncia;
    }

    public void setCodigoDenuncia(String codigoDenuncia) {
//        System.out.println("set " + this.codigoDenuncia);
        this.codigoDenuncia = codigoDenuncia;
    }

    public Denuncia getDenunciaGerenciada() {
        return denunciaGerenciada;
    }

    public void setDenunciaGerenciada(Denuncia denunciaGerenciada) {
        this.denunciaGerenciada = denunciaGerenciada;
    }

    public InformacaoDeAtendida getInformacaoDeAtendida() {
        return informacaoDeAtendida;
    }

    public void setInformacaoDeAtendida(InformacaoDeAtendida informacaoDeAtendida) {
        this.informacaoDeAtendida = informacaoDeAtendida;
    }

    public ConteudoInapropriado getConteudoInapropriado() {
        return conteudoInapropriado;
    }

    public void setConteudoInapropriado(ConteudoInapropriado conteudoInapropriado) {
        this.conteudoInapropriado = conteudoInapropriado;
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

    public String getEmailRecuperarSenha() {
        return emailRecuperarSenha;
    }

    public void setEmailRecuperarSenha(String emailRecuperarSenha) {
        this.emailRecuperarSenha = emailRecuperarSenha;
    }
    
    
    

}
