/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.br.fachada;

import com.br.beans.Usuario;
import com.br.service.Dao;
import com.br.service.DaoIT;
import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.ejb.Stateless;

/**
 *
 * @author Sergio Diniz
 */
@Stateless
@Remote(FachadaIT.class)
public class Fachada implements FachadaIT{

    Dao dao = new Dao();
    
    @Override
    public String Cadastrar(Usuario object) {
        System.out.println("++++++++++++++++++" + object.getNickname());
        dao.salvar(object);
        return null;
    }

    
    
}
