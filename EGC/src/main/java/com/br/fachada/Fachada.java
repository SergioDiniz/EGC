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
import com.br.beans.Prefeitura;
import com.br.beans.Registro;
import com.br.beans.TipoDeDenuncia;
import com.br.beans.Usuario;
import com.br.controle.UploadType;
import com.br.service.AdministradorServiceIT;
import com.br.service.CidadeServiceIT;
import com.br.service.DaoIT;
import com.br.service.DenunciaServiceIT;
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
public class Fachada implements Serializable{

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

    public boolean cadastrar(Object object) {
        return dao.salvar(object);
    }

    public boolean atualizar(Object object) {
        return dao.atualizar(object);
    }

    // UsuarioServices
    public Usuario loginUsuario(Usuario usuario) {
        return us.login(usuario.getEmail(), usuario.getSenha());
    }

    public String novaDenuncia(Usuario usuario, EnderecoDenuncia enderecoDenuncia, String denucia, String foto, TipoDeDenuncia tipoDeDenuncia) {
        return us.novaDenuncia(usuario, enderecoDenuncia, denucia, foto, tipoDeDenuncia);
    }

    public List<Denuncia> minhasDenuncias(String email) {
        return us.minhasDenuncias(email);
    }

    public List<Denuncia> denunciasQueAjudei(String email) {
        return us.denunciasQueAjudei(email);
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

    public List<Long> dadosGeraisPrefeitura(String emailPrefeitura, String cidade, String estado){
        return ps.dadosGeraisPrefeitura(emailPrefeitura, cidade, estado);
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
    
    public List<Registro> registrosDoFuncionario(String email){
        return fs.registrosDoFuncionario(email);
    }
    
    public List<Prefeitura> prefeiturasDoFuncionario(String email){
        return  fs.prefeiturasDoFuncionario(email);
    }

    public List<Funcionario> funcionariosOnline(String email){
        return fs.funcionariosOnline(email);
    }
    
    public boolean tornaFuncionarioOnline(String email){
        return fs.tornaFuncionarioOnline(email);
    }
    
    public boolean tornaFuncionarioOffline(String email){
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

    public List<ConteudoInapropriado> comentariosDeConteudoInapropriadoEmDenuncia(Denuncia denuncia){
        return ds.comentariosDeConteudoInapropriadoEmDenuncia(denuncia);
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
}
