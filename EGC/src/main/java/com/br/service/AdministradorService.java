/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.br.service;

import com.br.beans.Administrador;
import com.br.beans.Denuncia;
import com.br.beans.Prefeitura;
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
@Remote(AdministradorServiceIT.class)
public class AdministradorService implements AdministradorServiceIT {

    @PersistenceContext(unitName = "jdbc/EGC")
    private EntityManager em;

    @Override
    public Administrador login(String email, String senha) {

        Query query = em.createQuery("SELECT a FROM Administrador a WHERE a.email = :email AND a.senha = :senha ");
        query.setParameter("email", email);
        query.setParameter("senha", senha);

        List<Administrador> a = query.getResultList();

        if (a.size() > 0) {
            return a.get(0);
        }

        return null;
    }

    @Override
    public String excluir(Prefeitura prefeitura) {
        try {
            prefeitura.setFuncionarios(null);
            prefeitura.setCidade(null);

            em.remove(em.merge(prefeitura));
            return "Sucesso!";

        } catch (Exception e) {
        }

        return "ERRO!";
    }

    @Override
    public String atualizarSituacao(Prefeitura prefeitura, boolean situacao) {

        try {
            prefeitura.setAtivo(situacao);
            em.merge(prefeitura);
            return "Sucesso";

        } catch (Exception e) {
        }

        return "ERRO!";
    }

    @Override
    public List<Prefeitura> todasPrefeituras() {
        Query query = em.createQuery("SELECT p FROM Prefeitura p WHERE p.ativo = true");

        List<Prefeitura> prefeituras = query.getResultList();

        if (prefeituras.size() > 0) {
            return prefeituras;
        }

        return null;
    }

    @Override
    public boolean bloquearDenuncia(Denuncia denuncia) {
        
        try {
            denuncia = em.find(Denuncia.class, denuncia.getId());
            denuncia.setAtivo(false);
            em.merge(denuncia);
            return true;
        }catch (Exception e) {
            
        }

        return false;
    }

    @Override
    public boolean desbloquearDenuncia(Denuncia denuncia) {

        try {
            denuncia = em.find(Denuncia.class, denuncia.getId());
            denuncia.setAtivo(true);
            em.merge(denuncia);
            return true;
        }catch (Exception e) {
            
        }

        return false;
    }

}
