/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.br.service;

import com.br.beans.Usuario;
import java.util.List;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Sergio Diniz
 */
@Stateless
@Remote(UsuarioServiceIT.class)
public class UsuarioService implements UsuarioServiceIT{

    @PersistenceContext(name = "jdbc/EGC")
    private EntityManager em;      
    
    @Override
    public Usuario login(String email, String senha) {
              Query query =  em.createQuery("SELECT u FROM Usuario u WHERE u.email = :email AND u.senha = :senha ");
              query.setParameter("email", email);
              query.setParameter("senha", senha);
        
        List<Usuario> u = query.getResultList();
        
        if(u.size() > 0){
            u.get(0).getDenuncias().size();
           return u.get(0);
        } 
        
        return null;
    }

    @Override
    public boolean excluir(Usuario usuario) {
        try {
            usuario.setDenuncias(null);
        
            em.remove(em.merge(usuario));
            return true;
        } catch (Exception e) {
        }
        return false;
    }

    
    
    

    
}
