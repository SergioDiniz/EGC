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
import com.br.beans.EnderecoPrefeitura;
import com.br.beans.EstadoDeAcompanhamento;
import com.br.beans.Funcionario;
import com.br.beans.LideresPrefeitura;
import com.br.beans.MensagemPrefeitura;
import com.br.beans.Prefeitura;
import com.br.beans.Registro;
import com.br.beans.TipoDeRegistro;
import com.br.beans.Usuario;
import static com.br.controle.ControladorAdmin.info;
import static com.br.controle.ControladorAdmin.infoUsuarioInvalido;
import com.br.fachada.Fachada;
import com.br.service.PesquisarCep;
import com.sun.javafx.scene.control.skin.VirtualFlow;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.faces.bean.SessionScoped;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.faces.event.ComponentSystemEvent;
import javax.servlet.http.Part;
import org.apache.commons.mail.EmailException;
import org.primefaces.model.UploadedFile;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.CategoryAxis;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.LineChartModel;
import org.primefaces.model.chart.LineChartSeries;

/**
 *
 * @author SergioD
 */
@ManagedBean(name = "controladorPrefeitura")
@SessionScoped
public class ControladorPrefeitura implements Serializable {

    private UploadedFile file;
    private UploadedFile file_prefeito;
    private UploadedFile file_vice_prefeito;

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
    private Funcionario funcionarioPerfil;
    private String emailRecuperarSenha;
    private List<Denuncia> denunciaGerenciadas;
    private Denuncia denunciaGerenciada;
    private ConteudoInapropriado conteudoInapropriado;

    private String codigoDenuncia;
    private String filtroData;
    private String filtroAjuda;
    private String filtroQuery;
    private String filtroTipo;

    private LineChartModel chartDenunciasRealizadasEAtendidas;

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
        this.emailRecuperarSenha = "";
        this.denunciaGerenciadas = new ArrayList<>();
        this.filtroData = "DATA_DESC";
        this.filtroAjuda = "AJUDA_DESC";
        this.filtroQuery = "";
        this.filtroTipo = "";
        this.emailRecuperarSenha = "";
        this.codigoDenuncia = "";
        this.denunciaGerenciada = new Denuncia();
        this.conteudoInapropriado = new ConteudoInapropriado();
        this.chartDenunciasRealizadasEAtendidas = new LineChartModel();
        
        
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
                createLineModels();
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

    public String recuperarSenha() throws EmailException, MalformedURLException {
        Prefeitura p = new Prefeitura();
        p = fachada.prefeituraPorEmail(this.emailRecuperarSenha);
        if (p != null) {
            //Enviando Email
            fachada.emailRecuperarSenha(p.getEmail(), p.getNome(), p.getSenha());
            this.emailRecuperarSenha = "";
            ControladorAdmin.info("Verifique seu Email!");
            return null;
        }
        this.emailRecuperarSenha = "";
        ControladorAdmin.info("Email não Encontrado!");
        return null;
    }

    public String cadastro() throws IOException, EmailException {

        try {
            cidade.setCidadePK(cidadePK);
            fachada.cadastrar(cidade);
            cidade = fachada.pesquisarCidade(cidadePK.getNomeCidade(), cidadePK.getSiglaEstado());
        } catch (Exception e) {

        }

        prefeitura.setCodigo(String.valueOf(fachada.totalDePrefeitura() + 1));
        prefeitura.setCidade(cidade);
        prefeitura.getEnderecoPrefeitura().setCidade(cidade.getCidadePK().getNomeCidade());
        prefeitura.getEnderecoPrefeitura().setEstado(cidade.getCidadePK().getSiglaEstado());
        prefeitura.setDocumento(upload(this.file, UploadType.DOCUMENTO_SOLICITACAO));
//        LideresPrefeitura lideresPrefeitura = new LideresPrefeitura();
//        lideresPrefeitura.setPrefeito(prefeitura.getLideresPrefeitura().getPrefeito());
//        lideresPrefeitura.setVice_prefieto(prefeitura.getLideresPrefeitura().getVice_prefieto());
//        fachada.cadastrar(lideresPrefeitura);
//        prefeitura.setLideresPrefeitura(lideresPrefeitura);
        fachada.atualizar(prefeitura);

        //Enviando Email
        fachada.emailBemVindoPrefeitura(this.prefeitura.getEmail(), this.prefeitura.getNome(), (this.cidade.getCidadePK().getNomeCidade() + " - " + this.cidade.getCidadePK().getSiglaEstado()));

        cidade = new Cidade();
        cidadePK = new CidadePK();
        prefeitura = new Prefeitura(new EnderecoPrefeitura());

        return "/index.jsf?faces-redirect=true";

    }

    public String upload(UploadedFile fileUp, UploadType uploadType) {
        if (fileUp != null) {
            try {

                File targetFolder;
                if (uploadType == UploadType.BRASAO_PREFEITURA) {
                    targetFolder = new File("/Volumes/Arquivos/Sergio/Documentos/ADS/P6/TCC/Sistema/EGC/EGC/src/main/webapp/sis/prefeitura/brasao");
                } else if (uploadType == UploadType.PERFIL_PREFEITO || uploadType == UploadType.PERFIL_VICE_PREFEITO) {
                    targetFolder = new File("/Volumes/Arquivos/Sergio/Documentos/ADS/P6/TCC/Sistema/EGC/EGC/src/main/webapp/sis/prefeitura/lider");
                } else {
                    targetFolder = new File("/Volumes/Arquivos/Sergio/Documentos/ADS/P6/TCC/Sistema/EGC/EGC/src/main/webapp/sis/admin/documentos-de-solicitacao");
                }

                InputStream inputStream = fileUp.getInputstream();
                String tipoArquivo = fileUp.getFileName();
                tipoArquivo = tipoArquivo.substring(tipoArquivo.lastIndexOf("."), tipoArquivo.length());
                String nomeArquivo = prefeitura.getEmail() + tipoArquivo;
                if (uploadType == UploadType.PERFIL_PREFEITO) {
                    nomeArquivo = "prefeito_" + nomeArquivo;
                } else if (uploadType == UploadType.PERFIL_VICE_PREFEITO) {
                    nomeArquivo = "vice_prefeito_" + nomeArquivo;
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
            }
        }
        return null;

    }

    public String atualizarDados() {

        int sleep = 2000;

        if (file_prefeito.getSize() > 0 || file_vice_prefeito.getSize() > 0) {
            sleep = 0;
        }

        if (file.getSize() > 0) {

            try {

                this.prefeitura.setFoto(upload(this.file, UploadType.BRASAO_PREFEITURA));
                this.file = null;

                Thread.sleep(sleep);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (file_vice_prefeito.getSize() > 0) {
            sleep = 0;
        } else {
            sleep = 2000;
        }

        if (file_prefeito.getSize() > 0) {

            try {

                this.prefeitura.getLideresPrefeitura().setFoto_prefeito((upload(this.file_prefeito, UploadType.PERFIL_PREFEITO)));
                this.file_prefeito = null;

                Thread.sleep(sleep);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (file_vice_prefeito.getSize() > 0) {

            try {

                this.prefeitura.getLideresPrefeitura().setFoto_vice_prefeito((upload(this.file_vice_prefeito, UploadType.PERFIL_VICE_PREFEITO)));
                this.file_vice_prefeito = null;

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

    public String cadastrarNovoFuncionario() throws EmailException, MalformedURLException {
        funcionario.setCpf(CPFPesquisaF);
        if (this.funcionario.isSexo()) {
            this.funcionario.setFoto("h-funcionario.png");
        } else {
            this.funcionario.setFoto("m-funcionario.png");
        }
        this.funcionario.setCodigo(String.valueOf(fachada.numeroDeFuncionarios() + 1));
        fachada.cadastrar(funcionario);
        funcionario = fachada.buscarFuncionarioPorCPF(CPFPesquisaF);
        fachada.cadastrarNaPrefeitura(prefeitura, funcionario);
        this.prefeitura.setFuncionarios(funcionarios());

        //Enviando Email
        fachada.emailBemVindoFuncionario(funcionario.getEmail(), funcionario.getNome(), (this.prefeitura.getCidade().getCidadePK().getNomeCidade() + " - " + this.prefeitura.getCidade().getCidadePK().getSiglaEstado()));

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

    public String vincularFuncionario() throws EmailException, MalformedURLException {
        Funcionario f = fachada.buscarFuncionarioPorCPF(CPFPesquisaF);
        fachada.vincularFuncionarioPrefeitura(prefeitura, f);
        this.prefeitura.setFuncionarios(funcionarios());
        //Enviando Email
        fachada.emailBemVindoFuncionario(f.getEmail(), f.getNome(), (this.prefeitura.getCidade().getCidadePK().getNomeCidade() + " - " + this.prefeitura.getCidade().getCidadePK().getSiglaEstado()));
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

    public List<Denuncia> denunciasAtendidasEmCidade() {
        return fachada.denunciasAtendidasEmCidade(this.prefeitura.getCidade().getCidadePK().getNomeCidade(),
                this.prefeitura.getCidade().getCidadePK().getSiglaEstado());
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

    public String visualizarPerfilFuncionario(Funcionario funcionario) {
        this.funcionarioPerfil = new Funcionario();
        this.funcionarioPerfil = funcionario;
        return "perfil-funcionario?faces-redirect=true";
    }

    //
    //
    //
    public void encontraCEP() {

        PesquisarCep cepWebService = new PesquisarCep(this.prefeitura.getEnderecoPrefeitura().getCep());

        System.out.println("Resultado: " + cepWebService.getResultado());

        if (cepWebService.getResultado() >= 1) {

            this.cidadePK.setNomeCidade(cepWebService.getCidade());
            this.cidadePK.setSiglaEstado(cepWebService.getEstado());

        } else {
            this.cidadePK.setNomeCidade("");
            this.cidadePK.setSiglaEstado("");

            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Erro!",
                            "Cep Não encontrado."));
        }
    }

    public Long totalDePrefeituras() {
        return fachada.totalDePrefeitura();
    }

    public Long totalDePrefeiturasAtivas() {
        return fachada.totalDePrefeituraAtivas();
    }

    public void setTodasDenunciasEmGerenciamento() {
        this.denunciaGerenciadas = new ArrayList<>();
        this.denunciaGerenciadas = fachada.pesquisarTodasDenunciasPorCidade(this.prefeitura.getCidade().getCidadePK().getNomeCidade(),
                this.prefeitura.getCidade().getCidadePK().getSiglaEstado(), "data");
    }

    public String paginaGerenciarDenuncias() {
        setTodasDenunciasEmGerenciamento();
        this.filtroData = "DATA_DESC";
        this.filtroAjuda = "AJUDA_DESC";
        this.filtroQuery = "";
        this.filtroTipo = "";
        return "denuncias.jsf?faces-redirect=true";
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

        this.denunciaGerenciadas = new ArrayList<>();
        this.denunciaGerenciadas = fachada.gerenciarDenunciasFiltro(this.prefeitura.getCidade().getCidadePK().getNomeCidade(), this.prefeitura.getCidade().getCidadePK().getSiglaEstado(),
                ordem, this.filtroQuery, this.filtroTipo);

        return null;

    }

    public long andamentoDasDenuncias() {
        try {
            return (totalDeDenunciasAtendidasNaCidade() * 100) / totalDeDenunciasNaCidade();
        } catch (Exception e) {
        }

        return 0;
    }

    public void pesquisarDenuncia(ComponentSystemEvent event) {
//        System.out.println("Codigo Denuncia: " + this.codigoDenuncia);
        this.denunciaGerenciada = new Denuncia();
        this.denunciaGerenciada = fachada.pesquisarDenunicaCodigo(this.codigoDenuncia);
//        System.out.println("Descricao: " + this.denunciaGerenciada.getDescricao());

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

    public String excluirMensagemPrefeitura(MensagemPrefeitura mensagemPrefeitura) {
        fachada.excluirMensagemEmPrefeitura(mensagemPrefeitura, this.prefeitura);
        return null;
    }

    
    private void chart(){
        LineChartSeries series1 = new LineChartSeries();
        series1.setLabel("Serie 1");
        series1.set(1, 2);
        series1.set(2, 3);
        series1.set(3, 1);
        series1.set(4, 6);
        series1.set(5, 10);
        
        LineChartSeries series2 = new LineChartSeries();
        series2.setLabel("Serie 2");
        series2.set(1, 10);
        series2.set(2, 13);
        series2.set(3, 11);
        series2.set(4, 16);
        series1.set(5, 15);
        
        this.chartDenunciasRealizadasEAtendidas.addSeries(series1);
        this.chartDenunciasRealizadasEAtendidas.addSeries(series2);
        this.chartDenunciasRealizadasEAtendidas.setTitle("Teste de Grafico");
        this.chartDenunciasRealizadasEAtendidas.setLegendPosition("e");
        Axis yAxis = this.chartDenunciasRealizadasEAtendidas.getAxis(AxisType.Y);
        yAxis.setMax(0);
        yAxis.setMax(16);
        
    }
    
    // graficos
    private void createLineModels() {

        this.chartDenunciasRealizadasEAtendidas = initCategoryModel();
        this.chartDenunciasRealizadasEAtendidas.setTitle("Denúncias por Mês");
        this.chartDenunciasRealizadasEAtendidas.setLegendPosition("e");
        this.chartDenunciasRealizadasEAtendidas.setShowPointLabels(true);
        this.chartDenunciasRealizadasEAtendidas.getAxes().put(AxisType.X, new CategoryAxis("Dias"));
        Axis yAxis = this.chartDenunciasRealizadasEAtendidas.getAxis(AxisType.Y);
        yAxis.setLabel("Numero");
        yAxis.setMin(0);
        yAxis.setMax(10);
        
    }

    private LineChartModel initCategoryModel() {
        LineChartModel model = new LineChartModel();
 
        ChartSeries atendidas = new ChartSeries();
        atendidas.setLabel("Atendidas");
        
        for (List d : fachada.denunciasRealizadasPorMesChart(this.prefeitura.getCidade().getCidadePK().getNomeCidade(), this.prefeitura.getCidade().getCidadePK().getSiglaEstado())) {
            atendidas.set(d.get(0), Integer.parseInt(d.get(1).toString()));
        }
 
//        ChartSeries realizadas = new ChartSeries();
//        realizadas.setLabel("Realizadas");
//        realizadas.set("2004", 52);
//        realizadas.set("2005", 60);
//        realizadas.set("2006", 110);
//        realizadas.set("2007", 90);
//        realizadas.set("2008", 120);
 
        model.addSeries(atendidas);
//        model.addSeries(realizadas);
         
        return model;
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

    public Funcionario getFuncionarioPerfil() {
        return funcionarioPerfil;
    }

    public void setFuncionarioPerfil(Funcionario funcionarioPerfil) {
        this.funcionarioPerfil = funcionarioPerfil;
    }

    public UploadedFile getFile_prefeito() {
        return file_prefeito;
    }

    public void setFile_prefeito(UploadedFile file_prefeito) {
        this.file_prefeito = file_prefeito;
    }

    public UploadedFile getFile_vice_prefeito() {
        return file_vice_prefeito;
    }

    public void setFile_vice_prefeito(UploadedFile file_vice_prefeito) {
        this.file_vice_prefeito = file_vice_prefeito;
    }

    public String getEmailRecuperarSenha() {
        return emailRecuperarSenha;
    }

    public void setEmailRecuperarSenha(String emailRecuperarSenha) {
        this.emailRecuperarSenha = emailRecuperarSenha;
    }

    public List<Denuncia> getDenunciaGerenciadas() {
        return denunciaGerenciadas;
    }

    public void setDenunciaGerenciadas(List<Denuncia> denunciaGerenciadas) {
        this.denunciaGerenciadas = denunciaGerenciadas;
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

    public Denuncia getDenunciaGerenciada() {
        return denunciaGerenciada;
    }

    public void setDenunciaGerenciada(Denuncia denunciaGerenciada) {
        this.denunciaGerenciada = denunciaGerenciada;
    }

    public String getCodigoDenuncia() {
        return codigoDenuncia;
    }

    public void setCodigoDenuncia(String codigoDenuncia) {
        this.codigoDenuncia = codigoDenuncia;
    }

    public ConteudoInapropriado getConteudoInapropriado() {
        return conteudoInapropriado;
    }

    public void setConteudoInapropriado(ConteudoInapropriado conteudoInapropriado) {
        this.conteudoInapropriado = conteudoInapropriado;
    }

    public LineChartModel getChartDenunciasRealizadasEAtendidas() {
        return chartDenunciasRealizadasEAtendidas;
    }

    public void setChartDenunciasRealizadasEAtendidas(LineChartModel chartDenunciasRealizadasEAtendidas) {
        this.chartDenunciasRealizadasEAtendidas = chartDenunciasRealizadasEAtendidas;
    }
    
    

}
