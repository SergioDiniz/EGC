/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.br.fachada;

import com.br.beans.Administrador;
import com.br.beans.Cidade;
import com.br.beans.Funcionario;
import com.br.beans.Prefeitura;
import com.br.beans.Usuario;
import com.br.service.AdministradorService;
import com.br.service.AdministradorServiceIT;
import com.br.service.CidadeServiceIT;
import com.br.service.DaoIT;
import com.br.service.FuncionarioServiceIT;
import com.br.service.PrefeituraServiceIT;
import com.br.service.UsuarioServiceIT;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author Sergio Diniz
 */
@Stateless
public class Fachada{

    
    
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
    
    public boolean cadastrar(Object object) {
            return dao.salvar(object);
    }
    
    public boolean atualizar(Object object){
        return dao.atualizar(object);
    }
    
    
    
    
    
    
    
    
    
    // UsuarioServices
    
    public Usuario loginUsuario(Usuario usuario){
        return us.login(usuario.getEmail(), usuario.getSenha());
    }
    
    
    // FIM UsuarioServices
    //
    //
    //
    //
    
    
    
    
    
    // PrefeituraServices
    public Prefeitura loginPrefeitura(String email, String senha){
        return ps.login(email, senha);
    }
    
    public String cadastrarNaPrefeitura(Prefeitura prefeitura, Funcionario funcionario){
        return ps.cadastrarNaPrefeitura(prefeitura, funcionario);
    }
    
    public List<Prefeitura> prefeiturasPendentes(){
        return ps.prefeiturasPendentes();
    }
    
    
    public Funcionario pesquisarFuncionarioPorCPF(Prefeitura prefeitura, String cpf){
        return ps.pesquisarFuncionario(prefeitura, cpf);
    }
    
    public String vincularFuncionarioPrefeitura(Prefeitura prefeitura, Funcionario funcionario){
        return ps.vincular(prefeitura, funcionario);
    }
    
    public List<Funcionario> funcionariosPrefeitura(Prefeitura prefeitura) {
        return ps.funcionarios(prefeitura);
    }
    
    public String desvincularFuncionario(Prefeitura prefeitura, String cpf){
        return ps.desvincular(prefeitura, cpf);
    }
    
    
    // FIM PrefeituraServices
    //
    //
    //
    //
    
    
    
    // FuncionarioServices
    
    public Funcionario loginFuncionario(String email, String senha, String cidade, String estado){
        System.out.println("entrou na fachada");
        return fs.login(email, senha, cidade, estado);
    }
    
    public Funcionario buscarFuncionarioPorCPF(String cpf){
        return fs.buscarPorCPF(cpf);
    }
    
    public String excluirFuncionario(Funcionario funcionario){
        return fs.excluir(funcionario);
    }
    
    // FIM FuncionarioServices
    //
    //
    //
    //
    
    
    
    
    
    // CidadeServices
    
    public Cidade pesquisarCidade(String nome, String estado){
        return cs.pesquisarCidade(nome, estado);
    }
    
    // FIM CidadeServices
    //
    //
    //
    //
    
    
    
    // AdministradorServices
    
    
    
    // FIM AdministradorServices
    public Administrador loginAdmin(String email, String senha){
        return as.login(email, senha);
    }
    
    
    public String excluirPrefeitura(Prefeitura prefeitura){
        return as.excluir(prefeitura);
    }
    
    public String atualizarSituacaoPrefeitura(Prefeitura prefeitura, boolean situacao){
        return as.atualizarSituacao(prefeitura, situacao);
    }
    
    public List<Prefeitura> todasPrefeiturasAtivas(){
        return as.todasPrefeituras();
    }
    
    //
    //
    //
    //
    //
    
    
    
}
