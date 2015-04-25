/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.br.fachada;

import com.br.beans.Administrador;
import com.br.beans.Cidade;
import com.br.beans.Prefeitura;
import com.br.beans.Usuario;
import com.br.service.AdministradorService;
import com.br.service.AdministradorServiceIT;
import com.br.service.CidadeServiceIT;
import com.br.service.DaoIT;
import com.br.service.PrefeituraServiceIT;
import com.br.service.UsuarioServiceIT;
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
    
    
    
    // FIM PrefeituraServices
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
    
    //
    //
    //
    //
    //
    
    
    
}
