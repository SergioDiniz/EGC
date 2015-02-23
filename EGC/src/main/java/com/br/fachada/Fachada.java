/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.br.fachada;

import com.br.beans.Usuario;
import com.br.service.DaoIT;
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
    
    public boolean cadastrar(Object object) {
            return dao.salvar(object);
    }
    
    public boolean atualizar(Object object){
        return dao.atualizar(object);
    }
    
    
    
    
    
    
    
    
    
    
    
    public Usuario loginUsuario(Usuario usuario){
        return us.login(usuario.getEmail(), usuario.getSenha());
    }
    
    

    
    
}
