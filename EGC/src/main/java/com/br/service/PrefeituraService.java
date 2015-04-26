/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.br.service;

import com.br.beans.Funcionario;
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
@Remote(PrefeituraServiceIT.class)
public class PrefeituraService implements PrefeituraServiceIT {

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

    @Override
    public Prefeitura login(String email, String senha) {
        Query query = em.createQuery("SELECT p FROM Prefeitura p WHERE p.email = :email AND p.senha = :senha ");
        query.setParameter("email", email);
        query.setParameter("senha", senha);

        List<Prefeitura> p = query.getResultList();

        if (p.size() > 0) {
            p.get(0).getFuncionarios().size();
            return p.get(0);
        }

        return null;
    }

    @Override
    public List<Prefeitura> prefeiturasPendentes() {
        Query query = em.createQuery("SELECT p from Prefeitura p WHERE p.ativo = false");
        List<Prefeitura> prefeituras = query.getResultList();

        if (prefeituras.size() > 0) {
            return prefeituras;
        }

        return null;
    }

}
