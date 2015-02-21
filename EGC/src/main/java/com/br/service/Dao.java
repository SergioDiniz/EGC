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
    public void salvar(Object object) {
        em.persist(object);
    }

    @Override
    public void atualizar(Object object) {
        em.merge(object);
    }

    @Override
    public void excluir(Object object) {
        em.remove(object);
    }
    
}
