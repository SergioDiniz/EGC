/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.br.service;

import com.br.beans.Cidade;
import com.br.beans.CidadePK;
import com.br.beans.Registro;
import java.util.List;

import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author SergioD
 */
@Stateless
@Remote(CidadeServiceIT.class)
public class CidadeService implements CidadeServiceIT{
    
    @PersistenceContext(unitName = "jdbc/EGC")
    private EntityManager em;

    
    
    @Override
    public Cidade pesquisarCidade(String nome, String estado) {
        
        try {
            CidadePK cidadePK = new CidadePK(nome, estado);
            Cidade cidade = new Cidade();
            cidade = em.find(Cidade.class, cidadePK);
            
            cidade.getDenuncias().size();

            return cidade;
        } catch (Exception e) {
            System.out.println("erro ao cadastrar cidade");
        }

        return null;
    }

    @Override
    public long totalDeUsuariosNaCidade(String cidade, String estado) {
        Query query = em.createQuery("SELECT COUNT(u) FROM Usuario u WHERE u.endereco.cidade = :cidade AND u.endereco.estado = :estado");
            query.setParameter("cidade", cidade);
            query.setParameter("estado", estado);
            
        List u = query.getResultList();
        
        if (u.size() > 0){
            return (long) u.get(0);
        }
        
        return 0;
    }
    
    
    @Override
    public List<Registro> registrosDaCidade(String cidade, String estado){
        
        Query query = em.createQuery("SELECT r FROM Registro r WHERE r.prefeitura.enderecoPrefeitura.cidade = :cidade and r.prefeitura.enderecoPrefeitura.estado = :estado ORDER BY r.data DESC");
        query.setParameter("cidade", cidade);
        query.setParameter("estado", estado);
        
        query.setMaxResults(6);
        
        List<Registro> r = query.getResultList();
        
        if(r.size() > 0){
            return r;
        }
        
        return null;
    }
    
}
