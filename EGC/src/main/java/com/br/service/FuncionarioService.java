/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.br.service;

import com.br.beans.Funcionario;
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
@Remote(FuncionarioServiceIT.class)
public class FuncionarioService implements FuncionarioServiceIT {

    @PersistenceContext(unitName = "jdbc/EGC")
    private EntityManager em;

    @Override
    public Funcionario login(String email, String senha, String cidade, String estado) {
        
        System.out.println("entrou no service 1");
        
        Query query = em.createQuery("SELECT f FROM Funcionario f JOIN f.prefeituras fp WHERE f.email = :email "
                + "AND f.senha = :senha AND fp.cidade.CidadePK.nomeCidade = :cidade "
                + "AND fp.cidade.CidadePK.siglaEstado = :estado");

        query.setParameter("email", email);
        query.setParameter("senha", senha);
        query.setParameter("cidade", cidade);
        query.setParameter("estado", estado);

        List<Funcionario> f = query.getResultList();

        if (f.size() > 0) {
            f.get(0).getPrefeituras().size();
            return f.get(0);
        }

        return null;
    }

    @Override
    public Funcionario buscarPorCPF(String cpf) {
        Query query = em.createQuery("SELECT f FROM Funcionario f WHERE f.cpf = :cpf");
        query.setParameter("cpf", cpf);

        List<Funcionario> f = query.getResultList();

        if (f.size() > 0) {
            f.get(0).getPrefeituras().size();
            return f.get(0);
        }

        return null;
    }

    @Override
    public String excluir(Funcionario funcionario) {
        try {
            funcionario.setPrefeituras(null);
            em.remove(em.merge(funcionario));

            return "Excluido com Sucesso.";
        } catch (Exception e) {
        }

        return "ERRO!";
    }

}
