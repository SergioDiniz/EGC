/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.br.fachada;

import com.br.beans.Administrador;
import com.br.beans.Cidade;
import com.br.beans.ConteudoInapropriado;
import com.br.beans.Denuncia;
import com.br.beans.EnderecoDenuncia;
import com.br.beans.Funcionario;
import com.br.beans.InformacaoDeAtendida;
import com.br.beans.MensagemPrefeitura;
import com.br.beans.Prefeitura;
import com.br.beans.Registro;
import com.br.beans.TipoDeDenuncia;
import com.br.beans.Usuario;
import com.br.controle.UploadType;
import com.br.service.AdministradorServiceIT;
import com.br.service.CidadeServiceIT;
import com.br.service.DaoIT;
import com.br.service.DenunciaServiceIT;
import com.br.service.EmailServiceIT;
import com.br.service.FuncionarioServiceIT;
import com.br.service.PrefeituraServiceIT;
import com.br.service.UploadServiceIT;
import com.br.service.UsuarioServiceIT;
import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author Sergio Diniz
 */
@Stateless
public class Fachada implements Serializable {

    @EJB
    private DaoIT dao;
    @EJB
    private UsuarioServiceIT us;
    @EJB
    private PrefeituraServiceIT ps;
    @EJB
    private CidadeServiceIT cs;
    @EJB
    private AdministradorServiceIT as;
    @EJB
    private FuncionarioServiceIT fs;
    @EJB
    private DenunciaServiceIT ds;
    @EJB
    private UploadServiceIT ups;
    @EJB
    private EmailServiceIT es;

    // DAO
    public boolean cadastrar(Object object) {
        return dao.salvar(object);
    }

    public boolean atualizar(Object object) {
        return dao.atualizar(object);
    }

    public Object pesquisar(Class classe, Object chave) {
        return dao.pesquisar(classe, chave);
    }

    //
    //
    //
    //
    //
    // UsuarioServices
    public Usuario loginUsuario(Usuario usuario) {
        return us.login(usuario.getEmail(), usuario.getSenha());
    }

    public Usuario usuarioPorEmail(String email) {
        return us.usuarioPorEmail(email);
    }

    public List<Denuncia> minhasDenuncias(String email) {
        return us.minhasDenuncias(email);
    }

    public List<Denuncia> denunciasQueAjudei(String email) {
        return us.denunciasQueAjudei(email);
    }
    
    public Long totalDeUsuarios(){
        return us.totalDeUsuarios();
    }

    // FIM UsuarioServices
    //
    //
    //
    //
    // PrefeituraServices
    public Prefeitura loginPrefeitura(String email, String senha) {
        return ps.login(email, senha);
    }

    public Prefeitura prefeituraPorEmail(String email) {
        return ps.prefeituraPorEmail(email);
    }

    public Prefeitura pesquisarPrefeituraPorCidade(String cidade, String estado) {
        return ps.pesquisarPrefeituraPorCidade(cidade, estado);
    }

    public Prefeitura pesquisarPrefeituraPorCodigo(String codigo) {
        return ps.pesquisarPrefeituraPorCodigo(codigo);
    }

    public Long totalDePrefeitura() {
        return ps.totalDePrefeitura();
    }
    
    public Long totalDePrefeituraAtivas() {
        return ps.totalDePrefeituraAtivas();
    }

    public String cadastrarNaPrefeitura(Prefeitura prefeitura, Funcionario funcionario) {
        return ps.cadastrarNaPrefeitura(prefeitura, funcionario);
    }

    public List<Prefeitura> prefeiturasPendentes() {
        return ps.prefeiturasPendentes();
    }

    public Funcionario pesquisarFuncionarioPorCPF(Prefeitura prefeitura, String cpf) {
        return ps.pesquisarFuncionario(prefeitura, cpf);
    }

    public String vincularFuncionarioPrefeitura(Prefeitura prefeitura, Funcionario funcionario) {
        return ps.vincular(prefeitura, funcionario);
    }

    public List<Funcionario> funcionariosPrefeitura(Prefeitura prefeitura) {
        return ps.funcionarios(prefeitura);
    }

    public String desvincularFuncionario(Prefeitura prefeitura, String cpf) {
        return ps.desvincular(prefeitura, cpf);
    }

    public long totalDeFuncionariosNaPrefeitura(String email) {
        return ps.totalDeFuncionariosNaPrefeitura(email);
    }

    public List<Long> dadosGeraisPrefeitura(String emailPrefeitura, String cidade, String estado) {
        return ps.dadosGeraisPrefeitura(emailPrefeitura, cidade, estado);
    }

    public boolean prefeituraCadastrada(String cidade, String estado) {
        return ps.prefeituraCadastrada(cidade, estado);
    }

    public List<Registro> registroDaPrefeitura(String codigoPrefeitura) {
        return ps.registroDaPrefeitura(codigoPrefeitura);
    }

    public List<Denuncia> denunciaDaPrefeitura(String codigoPrefeitura) {
        return ps.denunciaDaPrefeitura(codigoPrefeitura);
    }

    public List<Funcionario> funcionarioDaPrefeitura(String codigoPrefeitura) {
        return ps.funcionarioDaPrefeitura(codigoPrefeitura);
    }

    public boolean novaMensagemEmPrefeitura(MensagemPrefeitura mensagemPrefeitura, Prefeitura prefeitura) {
        return ps.novaMensagemEmPrefeitura(mensagemPrefeitura, prefeitura);
    }

    public List<MensagemPrefeitura> mensagensDaPrefeitura(String codigoPrefeitura) {
        return ps.mensagensDaPrefeitura(codigoPrefeitura);
    }

    public boolean excluirMensagemEmPrefeitura(MensagemPrefeitura mensagemPrefeitura, Prefeitura prefeitura){
        return ps.excluirMensagemEmPrefeitura(mensagemPrefeitura, prefeitura);
    }
    
    // FIM PrefeituraServices
    //
    //
    //
    //
    // FuncionarioServices

    public Funcionario loginFuncionario(String email, String senha, String cidade, String estado) {
        return fs.login(email, senha, cidade, estado);
    }

    public Funcionario funcionarioPorEmail(String email) {
        return fs.funcionarioPorEmail(email);
    }

    public Funcionario buscarFuncionarioPorCPF(String cpf) {
        return fs.buscarPorCPF(cpf);
    }

    public String excluirFuncionario(Funcionario funcionario) {
        return fs.excluir(funcionario);
    }

    public List<Funcionario> todosFuncionarios() {
        return fs.todosFuncionarios();
    }

    public int numeroDeFuncionarios() {
        return fs.numeroDeFuncionarios();
    }

    public List<Registro> registrosDoFuncionario(String email) {
        return fs.registrosDoFuncionario(email);
    }

    public List<Prefeitura> prefeiturasDoFuncionario(String email) {
        return fs.prefeiturasDoFuncionario(email);
    }

    public List<Funcionario> funcionariosOnline(String email) {
        return fs.funcionariosOnline(email);
    }

    public boolean tornaFuncionarioOnline(String email) {
        return fs.tornaFuncionarioOnline(email);
    }

    public boolean tornaFuncionarioOffline(String email) {
        return fs.tornaFuncionarioOffline(email);
    }

    // FIM FuncionarioServices
    //
    //
    //
    //
    // CidadeServices
    public Cidade pesquisarCidade(String nome, String estado) {
        return cs.pesquisarCidade(nome, estado);
    }

    public long totalDeUsuariosNaCidade(String cidade, String estado) {
        return cs.totalDeUsuariosNaCidade(cidade, estado);
    }

    public List<Registro> registrosDaCidade(String cidade, String estado) {
        return cs.registrosDaCidade(cidade, estado);
    }

    public List<List> ruasDeUmaCidadeNumerosDeDenuncia(String cidade, String estado) {
        return cs.ruasDeUmaCidadeNumerosDeDenuncia(cidade, estado);
    }

    public List<List> cepDeUmaCidadeNumerosDeDenuncia(String cidade, String estado) {
        return cs.cepDeUmaCidadeNumerosDeDenuncia(cidade, estado);
    }

    public List<List> tiposDeDenunciasNumerosDeDenuncia(String cidade, String estado) {
        return cs.tiposDeDenunciasNumerosDeDenuncia(cidade, estado);
    }

    public List<List> estadoDeDenunciasNumerosDeDenuncia(String cidade, String estado) {
        return cs.estadoDeDenunciasNumerosDeDenuncia(cidade, estado);
    }

    // FIM CidadeServices
    //
    //
    //
    //
    // AdministradorServices
    // FIM AdministradorServices

    public Administrador loginAdmin(String email, String senha) {
        return as.login(email, senha);
    }

    public String excluirPrefeitura(Prefeitura prefeitura) {
        return as.excluir(prefeitura);
    }

    public String atualizarSituacaoPrefeitura(Prefeitura prefeitura, boolean situacao) {
        return as.atualizarSituacao(prefeitura, situacao);
    }

    public List<Prefeitura> todasPrefeiturasAtivas() {
        return as.todasPrefeituras();
    }

    public boolean bloquearDenuncia(Denuncia denuncia) {
        return as.bloquearDenuncia(denuncia);
    }

    public boolean desbloquearDenuncia(Denuncia denuncia) {
        return as.desbloquearDenuncia(denuncia);
    }

    //
    //
    //
    //
    //
    // DenunciaServices
    public String novaDenuncia(Usuario usuario, EnderecoDenuncia enderecoDenuncia, String denucia, String foto, TipoDeDenuncia tipoDeDenuncia) {
        return ds.novaDenuncia(usuario, enderecoDenuncia, denucia, foto, tipoDeDenuncia);
    }

    public Long totalDeDenuncias() {
        return ds.totalDeDenuncias();
    }

    public List<Denuncia> pesquisarTodasDenunciasPorCidade(String cidade, String estado, String ordem) {
        return ds.pesquisarPorCidade(cidade, estado, ordem);
    }

    public List<Denuncia> pesquisarDenunciaPorCidadeComFiltro(String cidade, String estado, TipoDeDenuncia tipoDeDenuncia, String ordem) {
        return ds.pesquisarPorCidadeComFiltro(cidade, estado, tipoDeDenuncia, ordem);
    }

    public long totalDeDenunciasNaCidade(String cidade, String estado) {
        return ds.totalDeDenunciasNaCidade(cidade, estado);
    }

    public long totalDeDenunciasAtendidasNaCidade(String cidade, String estado) {
        return ds.totalDeDenunciasAtendidasNaCidade(cidade, estado);
    }

    public boolean setAjudarDenuncia(Denuncia denuncia, Usuario usuario) {
        return ds.setAjudarDenuncia(denuncia, usuario);
    }

    public int getAjudarDenuncia(Denuncia denuncia) {
        return ds.getAjudarDenuncia(denuncia);
    }

    public boolean setReclamarDenuncia(Denuncia denuncia, ConteudoInapropriado conteudoInapropriado) {
        return ds.setReclamarDenuncia(denuncia, conteudoInapropriado);
    }

    public long getReclamarDenuncia(Denuncia denuncia) {
        return ds.getReclamarDenuncia(denuncia);
    }

    public List<Denuncia> denunciasComMaisAjudasPorCidade(String cidade, String estado) {
        return ds.denunciasComMaisAjudasPorCidade(cidade, estado);
    }

    public List<Denuncia> denunciasMaisRecentesPorCidade(String cidade, String estado) {
        return ds.denunciasMaisRecentesPorCidade(cidade, estado);
    }

    public List<Denuncia> denunciasComReclamacoes() {
        return ds.denunciasComReclamacoes();
    }

    public List<ConteudoInapropriado> comentariosDeConteudoInapropriadoEmDenuncia(Denuncia denuncia) {
        return ds.comentariosDeConteudoInapropriadoEmDenuncia(denuncia);
    }

    public Denuncia pesquisarDenunicaCodigo(String codigo) {
        return ds.pesquisarDenunicaCodigo(codigo);
    }

    public boolean atualizarDenunciaGerenciada(Registro registro) {
        return ds.atualizarDenunciaGerenciada(registro);
    }

    public List<Registro> registroDeUmaDenuncia(String codigoDenuncia) {
        return ds.registroDeUmaDenuncia(codigoDenuncia);
    }

    public boolean atenderDenuncia(InformacaoDeAtendida informacaoDeAtendida, Registro registro) {
        return ds.atenderDenuncia(informacaoDeAtendida, registro);
    }

    public List<Denuncia> gerenciarDenunciasFiltro(String cidade, String estado, String ordem, String filtroQuery, String filtro) {
        return ds.gerenciarDenunciasFiltro(cidade, estado, ordem, filtroQuery, filtro);
    }

    public List<Denuncia> denunciasAtendidasEmCidade(String cidade, String estado) {
        return ds.denunciasAtendidasEmCidade(cidade, estado);
    }
    
    public List<Denuncia> denunciasNaoAtendidasEmCidade(String cidade, String estado){
        return ds.denunciasNaoAtendidasEmCidade(cidade, estado);
    }
    
    public List<List> ruasDeUmaCidadeTipoDenunciaNumerosDeDenuncia(String cidade, String estado){
        return ds.ruasDeUmaCidadeTipoDenunciaNumerosDeDenuncia(cidade, estado);
    }
    
    public Long totalDeDenunciasAtendidasPorTipoERua(String cidade, String estado, String rua, String tipoDeDenuncia) {
        return ds.totalDeDenunciasAtendidasPorTipoERua(cidade, estado, rua, tipoDeDenuncia);
    }
    
    public List<MensagemPrefeitura> denunciasPorPrefeituraLimitado(String codigo, int limite){
        return ds.denunciasPorPrefeituraLimitado(codigo, limite);
    }
    
    public List<List> denunciasRealizadasPorMesChart(String cidade, String estado) {
        return ds.denunciasRealizadasPorMesChart(cidade, estado);
    }

    // FIM DenunciaServices
    //
    //
    //
    //
    // UploadService
    public String upload(UploadType uploadType, String email, UploadedFile file) {
        return ups.upload(uploadType, email, file);
    }

    //
    //
    //
    //
    //
    //
    // Email Service
    public boolean emailBemVindoUsuario(String emailUsuario, String nomeUsuario) {
        return es.emailBemVindoUsuario(emailUsuario, nomeUsuario);
    }

    public boolean emailBemVindoFuncionario(String emailUsuario, String nomeUsuario, String prefeitura) {
        return es.emailBemVindoFuncionario(emailUsuario, nomeUsuario, prefeitura);
    }

    public boolean emailBemVindoPrefeitura(String emailUsuario, String nomeUsuario, String prefeitura) {
        return es.emailBemVindoPrefeitura(emailUsuario, nomeUsuario, prefeitura);
    }

    public boolean emailAceitoPrefeitura(String emailUsuario, String nomeUsuario, String prefeitura) {
        return es.emailAceitoPrefeitura(emailUsuario, nomeUsuario, prefeitura);
    }

    public boolean emailRecusoPrefeitura(String emailUsuario, String nomeUsuario, String prefeitura) {
        return es.emailRecusoPrefeitura(emailUsuario, nomeUsuario, prefeitura);
    }

    public boolean emailRecuperarSenha(String emailUsuario, String nomeUsuario, String senha) {
        return es.emailRecuperarSenha(emailUsuario, nomeUsuario, senha);
    }
    // FIM Email Service

}
