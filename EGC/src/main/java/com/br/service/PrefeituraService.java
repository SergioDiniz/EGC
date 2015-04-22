/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.br.service;

import com.br.beans.Funcionario;
import com.br.beans.Prefeitura;

import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author SergioD
 */
@Stateless
@Remote(PrefeituraServiceIT.class)
public class PrefeituraService implements PrefeituraServiceIT{
    
    @PersistenceContext(unitName = "jdbc/EGC")
    private EntityManager em;
    
    
    
    @Override
    public String cadastrarNaPrefeitura(Prefeitura prefeitura, Funcionario funcionario) {
        try {
            prefeitura.getFuncionarios().add(funcionario);
            
            em.merge(prefeitura);
            
            return "Cadastrado com Sucesso";

        } catch (Exception e) {
        }

        return "ERRO!";
    }
    
}
