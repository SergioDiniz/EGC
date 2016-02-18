/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.br.service;

import com.br.beans.Cidade;
import com.br.beans.CidadePK;
import com.br.beans.Registro;
import java.util.ArrayList;
import java.util.Iterator;
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
        
        Query query = em.createQuery("SELECT r FROM Registro r JOIN r.denuncia d WHERE r.prefeitura.enderecoPrefeitura.cidade = :cidade and r.prefeitura.enderecoPrefeitura.estado = :estado AND d.ativo = TRUE ORDER BY r.data DESC");
        query.setParameter("cidade", cidade);
        query.setParameter("estado", estado);
        
        query.setMaxResults(6);
        
        List<Registro> r = query.getResultList();
        
        if(r.size() > 0){
            return r;
        }
        
        return null;
    }
    
    
    @Override
    public List<List> ruasDeUmaCidadeNumerosDeDenuncia(String cidade, String estado){
        
        Query query = em.createQuery("SELECT d.enderecoDenuncia.rua, COUNT(d.enderecoDenuncia.rua) FROM Cidade c JOIN c.denuncias d WHERE c.CidadePK.nomeCidade = :cidade and c.CidadePK.siglaEstado = :estado GROUP BY d.enderecoDenuncia.rua ORDER BY d.enderecoDenuncia.rua ASC");
        query.setParameter("cidade", cidade);
        query.setParameter("estado", estado);
        
        List resultado = query.getResultList();
        
        List<List> ruas = new ArrayList<>();
        Iterator i = resultado.iterator();
        
        while(i.hasNext()){
            Object[] r = (Object[]) i.next();
            
            List l = new ArrayList();
            l.add(r[0]);
            l.add(r[1]);
            
            
            ruas.add(l);
            
        }
        
        
        if (ruas.size() > 0){
            return ruas;
        }
        
        return new ArrayList<>();
        
    }
    
    
    @Override
    public List<List> cepDeUmaCidadeNumerosDeDenuncia(String cidade, String estado){
        Query query = em.createQuery("SELECT d.enderecoDenuncia.cep, COUNT(d.enderecoDenuncia.cep) FROM Cidade c JOIN c.denuncias d WHERE c.CidadePK.nomeCidade = :cidade and c.CidadePK.siglaEstado = :estado GROUP BY d.enderecoDenuncia.cep ORDER BY d.enderecoDenuncia.cep ASC");
        query.setParameter("cidade", cidade);
        query.setParameter("estado", estado);
        
        List resultado = query.getResultList();
        
        List<List> ruas = new ArrayList<>();
        Iterator i = resultado.iterator();
        
        while(i.hasNext()){
            Object[] r = (Object[]) i.next();
            
            List l = new ArrayList();
            l.add(r[0]);
            l.add(r[1]);
            
            
            ruas.add(l);
            
        }
        
        
        if (ruas.size() > 0){
            return ruas;
        }
        
        return new ArrayList<>();
    }
 
    @Override
    public List<List> tiposDeDenunciasNumerosDeDenuncia(String cidade, String estado){
        Query query = em.createQuery("SELECT d.tipoDeDenuncia, COUNT(d.tipoDeDenuncia) FROM Cidade c JOIN c.denuncias d WHERE c.CidadePK.nomeCidade = :cidade and c.CidadePK.siglaEstado = :estado GROUP BY d.tipoDeDenuncia ORDER BY d.tipoDeDenuncia ASC");
        query.setParameter("cidade", cidade);
        query.setParameter("estado", estado);
        
        List resultado = query.getResultList();
        
        List<List> ruas = new ArrayList<>();
        Iterator i = resultado.iterator();
        
        while(i.hasNext()){
            Object[] r = (Object[]) i.next();
            
            List l = new ArrayList();
            String tipo = (String) r[0].toString();
            l.add(tipo.replaceAll("_", " "));
            l.add(r[1]);
            
            
            ruas.add(l);
            
        }
        
        
        if (ruas.size() > 0){
            return ruas;
        }
        
        return new ArrayList<>();
    }
    
    @Override
    public List<List> estadoDeDenunciasNumerosDeDenuncia(String cidade, String estado){
        Query query = em.createQuery("SELECT d.estadoDeAcompanhamento, COUNT(d.estadoDeAcompanhamento) FROM Cidade c JOIN c.denuncias d WHERE c.CidadePK.nomeCidade = :cidade and c.CidadePK.siglaEstado = :estado GROUP BY d.estadoDeAcompanhamento ORDER BY d.estadoDeAcompanhamento ASC");
        query.setParameter("cidade", cidade);
        query.setParameter("estado", estado);
        
        List resultado = query.getResultList();
        
        List<List> ruas = new ArrayList<>();
        Iterator i = resultado.iterator();
        
        while(i.hasNext()){
            Object[] r = (Object[]) i.next();
            
            List l = new ArrayList();
            String tipo = (String) r[0].toString();
            l.add(tipo.replaceAll("_", " "));
            l.add(r[1]);
            
            
            ruas.add(l);
            
        }
        
        
        if (ruas.size() > 0){
            return ruas;
        }
        
        return new ArrayList<>();
        
    }
    
}
