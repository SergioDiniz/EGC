/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.br.service;


/**
 *
 * @author Sergio Diniz
 */
public interface DaoIT {
    
    public boolean salvar(Object object);
    
    public boolean atualizar(Object object);
    
    public boolean excluir(Object object);
    
    public Object pesquisar(Class classe, Object chave);
    
}
