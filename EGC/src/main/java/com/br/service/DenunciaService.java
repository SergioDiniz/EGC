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

        System.out.println(removerAcento(cidade));

        Query query;

//        String sql = "SELECT d.* FROM denuncia d LEFT OUTER JOIN enderecodenuncia ed on d.enderecodenuncia_id = ed.id WHERE upper(TRANSLATE(d.nomecidade,'ÀÁáàÉÈéèÍíÓóÒòÚú','AAaaEEeeIiOoOoUu')) LIKE '%?1%' AND upper(TRANSLATE(d.siglaestado,'ÀÁáàÉÈéèÍíÓóÒòÚú','AAaaEEeeIiOoOoUu')) LIKE '%?2%' ORDER BY d.data DESC";
        if (ordem.equals("data")) {
//            query = em.createNativeQuery(sql);
            query = em.createQuery("SELECT d FROM Denuncia d WHERE d.cidade.CidadePK.nomeCidade = :cidade AND d.cidade.CidadePK.siglaEstado = :estado ORDER BY d.data DESC");
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

    @Override
    public long totalDeDenunciasNaCidade(String cidade, String estado) {
        Query query = em.createQuery("SELECT COUNT(d) FROM Denuncia d WHERE d.cidade.CidadePK.nomeCidade = :cidade AND d.cidade.CidadePK.siglaEstado = :estado");

        query.setParameter("cidade", cidade);
        query.setParameter("estado", estado);

        List d = query.getResultList();

        if (d.size() > 0) {
            return (long) d.get(0);
        }

        return 0;

    }

    @Override
    public long totalDeDenunciasAtendidasNaCidade(String cidade, String estado) {
        Query query = em.createQuery("SELECT COUNT(d) FROM Denuncia d JOIN d.informacaoDeAtendida ia WHERE d.cidade.CidadePK.nomeCidade = :cidade AND d.cidade.CidadePK.siglaEstado = :estado AND ia.id > -1");

        query.setParameter("cidade", cidade);
        query.setParameter("estado", estado);

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

}
