/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.br.service;

import com.br.beans.Cidade;
import com.br.beans.CidadePK;
import com.br.beans.Denuncia;
import com.br.beans.EnderecoDenuncia;
import com.br.beans.TipoDeDenuncia;
import com.br.beans.Usuario;
import java.util.List;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Sergio Diniz
 */
@Stateless
@Remote(UsuarioServiceIT.class)
public class UsuarioService implements UsuarioServiceIT {

    @PersistenceContext(name = "jdbc/EGC")
    private EntityManager em;

    @Override
    public Usuario login(String email, String senha) {
        Query query = em.createQuery("SELECT u FROM Usuario u WHERE u.email = :email AND u.senha = :senha ");
        query.setParameter("email", email);
        query.setParameter("senha", senha);

        List<Usuario> u = query.getResultList();

        if (u.size() > 0) {
            u.get(0).getDenuncias().size();
            u.get(0).getDenunciasAjudadas().size();
            return u.get(0);
        }

        return null;
    }

    @Override
    public Usuario usuarioPorEmail(String email) {
        Query query = em.createQuery("SELECT u FROM Usuario u WHERE u.email = :email");
        query.setParameter("email", email);

        System.out.println("email: " + email);
        List<Usuario> u = query.getResultList();

        if (u.size() > 0) {
            System.out.println("usuario: " + u.get(0).getNickname());
            return u.get(0);
        }

        return null;
    }

    @Override
    public boolean excluir(Usuario usuario) {
        try {
            usuario.setDenuncias(null);

            em.remove(em.merge(usuario));
            return true;
        } catch (Exception e) {
        }
        return false;
    }

    @Override
    public List<Denuncia> minhasDenuncias(String email) {

        Query query = em.createQuery("SELECT d from Usuario u JOIN u.denuncias d WHERE u.email = :email ORDER BY d.data DESC");
        query.setParameter("email", email);

        List<Denuncia> d = query.getResultList();

        if (d.size() > 0) {
            return d;
        }

        return null;

    }

    @Override
    public List<Denuncia> denunciasQueAjudei(String email) {
        Query query = em.createQuery("SELECT d from Usuario u JOIN u.denunciasAjudadas d WHERE d.ativo = true AND u.email = :email ORDER BY d.data DESC");
        query.setParameter("email", email);

        List<Denuncia> d = query.getResultList();

        if (d.size() > 0) {
            return d;
        }

        return null;
    }

    @Override
    public Long totalDeUsuarios(){
        Query query = em.createQuery("SELECT COUNT(u) FROM Usuario u");
        List<Long> d = query.getResultList();

        return d.get(0);
        
    }
    
}
