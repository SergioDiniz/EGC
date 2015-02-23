/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.br.service;

import com.br.beans.Usuario;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Sergio Diniz
 */
@Stateless
@Remote(DaoIT.class)
public class Dao implements DaoIT{

    @PersistenceContext(name = "jdbc/EGC")
    private EntityManager em;

    public Dao() {
    }
    
    @Override
    public boolean salvar(Object object) {
        try{
            em.persist(object);
            return true;
        } catch (Exception e){
            
        }
        return false;
    }

    @Override
    public boolean atualizar(Object object) {
        try{
            em.merge(object);
            return true;
        } catch (Exception e){
            
        }
        return false;
    }

    @Override
    public boolean excluir(Object object) {
        try{
            em.remove(object);
            return true;
        } catch (Exception e){
            
        }
        return false;
    }
    
}
