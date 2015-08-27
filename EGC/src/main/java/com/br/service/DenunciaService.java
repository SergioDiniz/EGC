/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.br.service;

import com.br.beans.Denuncia;
import com.br.beans.TipoDeDenuncia;
import java.text.Normalizer;
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
public class DenunciaService implements DenunciaServiceIT {

    @PersistenceContext(unitName = "jdbc/EGC")
    private EntityManager em;

    @Override
    public List<Denuncia> pesquisarPorCidade(String cidade, String estado, String ordem) {

        Query query;

//        String sql = "SELECT d.* FROM denuncia d LEFT OUTER JOIN enderecodenuncia ed on d.enderecodenuncia_id = ed.id WHERE upper(TRANSLATE(d.nomecidade,'ÀÁáàÉÈéèÍíÓóÒòÚú','AAaaEEeeIiOoOoUu')) = ?1 AND upper(TRANSLATE(d.siglaestado,'ÀÁáàÉÈéèÍíÓóÒòÚú','AAaaEEeeIiOoOoUu')) = ?2 ORDER BY d.data DESC";
        if (ordem.equals("data")) {
//            query = em.createNativeQuery(sql);
            query = em.createQuery("SELECT d FROM Denuncia d WHERE d.cidade.CidadePK.nomeCidade = :cidade AND d.cidade.CidadePK.siglaEstado = :estado ORDER BY d.data DESC");
//            query = em.createNativeQuery(sql, Denuncia.class);
        } else {
            query = em.createQuery("SELECT d FROM Denuncia d WHERE d.cidade.CidadePK.nomeCidade = :cidade AND d.cidade.CidadePK.siglaEstado = :estado ORDER BY d.data ASC");
        }

        query.setParameter("cidade", cidade);
        query.setParameter("estado", estado);

        List d = query.getResultList();

        if (d.size() > 0) {
            return d;
        }

        return null;
    }

    @Override
    public List<Denuncia> pesquisarPorCidadeComFiltro(String cidade, String estado, TipoDeDenuncia tipoDeDenuncia, String ordem) {

        if (cidade.equals("Brasil")) {
            return pesquisarNoBrasil(cidade, tipoDeDenuncia, ordem);
        }

        Query query;

        if (ordem.equals("data")) {
            query = em.createQuery("SELECT d FROM Denuncia d WHERE d.cidade.CidadePK.nomeCidade = :cidade AND d.cidade.CidadePK.siglaEstado = :estado AND d.tipoDeDenuncia = :tipo ORDER BY d.data DESC");
        } else {
            query = em.createQuery("SELECT d FROM Denuncia d WHERE d.cidade.CidadePK.nomeCidade = :cidade AND d.cidade.CidadePK.siglaEstado = :estado AND d.tipoDeDenuncia = :tipo ORDER BY d.data ASC");
        }

        query.setParameter("cidade", cidade);
        query.setParameter("estado", estado);
        query.setParameter("tipo", tipoDeDenuncia);

        List d = query.getResultList();

        if (d.size() > 0) {
            return d;
        }

        return null;

    }

    public List<Denuncia> pesquisarNoBrasil(String cidade, TipoDeDenuncia tipoDeDenuncia, String ordem) {
        
//        if (tipoDeDenuncia == TipoDeDenuncia.BRASIL){
//            tipoDeDenuncia = TipoDeDenuncia.TODOS;
//        }
        
        Query query;
        if (ordem.equals("data")) {
            if (tipoDeDenuncia == TipoDeDenuncia.BRASIL) {
                query = em.createQuery("SELECT d FROM Denuncia d JOIN d.enderecoDenuncia ed WHERE ed.pais = 'Brasil' ORDER BY d.data DESC");
            } else {
                query = em.createQuery("SELECT d FROM Denuncia d JOIN d.enderecoDenuncia ed WHERE ed.pais = 'Brasil' AND d.tipoDeDenuncia = :tipo ORDER BY d.data DESC");
                query.setParameter("tipo", tipoDeDenuncia);
            }
        } else {
            if (tipoDeDenuncia == TipoDeDenuncia.BRASIL) {
                query = em.createQuery("SELECT d FROM Denuncia d JOIN d.enderecoDenuncia ed WHERE ed.pais = 'Brasil' ORDER BY d.data ASC");
            } else {
                query = em.createQuery("SELECT d FROM Denuncia d JOIN d.enderecoDenuncia ed WHERE ed.pais = 'Brasil' AND d.tipoDeDenuncia = :tipo ORDER BY d.data ASC");
                query.setParameter("tipo", tipoDeDenuncia);
            }
        }

        

        List d = query.getResultList();

        if (d.size() > 0) {
            return d;
        }

        return null;

    }

    @Override
    public long totalDeDenunciasNaCidade(String cidade, String estado) {
        Query query;

        if (cidade.equals("Brasil")) {
            query = em.createQuery("SELECT COUNT(d) FROM Denuncia d JOIN d.enderecoDenuncia ed WHERE ed.pais = 'Brasil'");
        } else {
            query = em.createQuery("SELECT COUNT(d) FROM Denuncia d WHERE d.cidade.CidadePK.nomeCidade = :cidade AND d.cidade.CidadePK.siglaEstado = :estado");
            query.setParameter("cidade", cidade);
            query.setParameter("estado", estado);
        }

        List d = query.getResultList();

        if (d.size() > 0) {
            return (long) d.get(0);
        }

        return 0;

    }

    @Override
    public long totalDeDenunciasAtendidasNaCidade(String cidade, String estado) {
        Query query;

        if (cidade.equals("Brasil")) {
            query = em.createQuery("SELECT COUNT(d) FROM Denuncia d JOIN d.enderecoDenuncia ed JOIN d.informacaoDeAtendida ia WHERE ed.pais = 'Brasil' AND ia.id > -1 ");
        } else {
            query = em.createQuery("SELECT COUNT(d) FROM Denuncia d JOIN d.informacaoDeAtendida ia WHERE d.cidade.CidadePK.nomeCidade = :cidade AND d.cidade.CidadePK.siglaEstado = :estado AND ia.id > -1");
            query.setParameter("cidade", cidade);
            query.setParameter("estado", estado);
        }

        List d = query.getResultList();

        if (d.size() > 0) {
            return (long) d.get(0);
        }

        return 0;

    }

    public static String removerAcento(String str) {
        str = Normalizer.normalize(str, Normalizer.Form.NFD);
        str = str.replaceAll("[^\\p{ASCII}]", "");
        return str.toUpperCase();
    }

    @Override
    public boolean setAjudarDenuncia(Denuncia denuncia) {
        int valor = denuncia.getNumeroAjuda();
        denuncia.setNumeroAjuda(++valor); 
        
        em.merge(denuncia);
        
        return true;
    }

    @Override
    public int getAjudarDenuncia(Denuncia denuncia) {
        Query query = em.createQuery("SELECT d.numeroAjuda FROM Denuncia d WHERE d.id = :id");
        query.setParameter("id", denuncia.getId());
        
        int valor = (int) query.getSingleResult();
        
        return valor;
    }

    
    
}