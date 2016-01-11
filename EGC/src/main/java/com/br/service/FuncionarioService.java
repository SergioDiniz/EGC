/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.br.service;

import com.br.beans.Funcionario;
import com.br.beans.Prefeitura;
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
@Remote(FuncionarioServiceIT.class)
public class FuncionarioService implements FuncionarioServiceIT {

    @PersistenceContext(unitName = "jdbc/EGC")
    private EntityManager em;

    @Override
    public Funcionario login(String email, String senha, String cidade, String estado) {
        
        
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
    
    @Override
    public List<Funcionario> todosFuncionarios(){
        Query query = em.createQuery("SELECT f FROM Funcionario f ORDER BY f.nome ASC");

        List<Funcionario> f = query.getResultList();

        if (f.size() > 0) {
            return f;
        }

        return null;
    }
    
    @Override
    public int numeroDeFuncionarios(){
        Query query = em.createQuery("SELECT f FROM Funcionario f");

        List<Funcionario> f = query.getResultList();

        if (f.size() > 0) {
            return f.size();
        }

        return 0;
    }
    
    @Override
    public List<Registro> registrosDoFuncionario(String email){
        
        Query query = em.createQuery("SELECT r FROM Funcionario f JOIN f.registros r WHERE f.email = :email");
        query.setParameter("email", email);
        
        List<Registro> r = query.getResultList();
        
        if(r.size() > 0){
            return r;
        }
        
        return null;
    }
    
    @Override
    public List<Prefeitura> prefeiturasDoFuncionario(String email){
        
        Query query = em.createQuery("SELECT p FROM Funcionario f JOIN f.prefeituras p WHERE f.email = :email");
        query.setParameter("email", email);
        
        List<Prefeitura> p = query.getResultList();
        
        if(p.size() > 0){
            return p;
        }
        
        return null;
    }
    
    

}
