/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.br.service;

import com.br.beans.Denuncia;
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
@Remote(DenunciaServiceIT.class)
public class DenunciaService implements DenunciaServiceIT{

    @PersistenceContext(unitName = "jdbc/EGC")
    private EntityManager em;
    
    @Override
    public List<Denuncia> pesquisarPorCidade(String cidade, String estado) {
        Query query =  em.createQuery("SELECT d FROM Denuncia d WHERE d.cidade.CidadePK.nomeCidade = :cidade AND d.cidade.CidadePK.siglaEstado = :estado ORDER BY d.data DESC");
              query.setParameter("cidade", cidade);
              query.setParameter("estado", estado);
        
        List d = query.getResultList();
        
        if(d.size() > 0){
           return d;
        }         
        
        return null;
    }
    
}
