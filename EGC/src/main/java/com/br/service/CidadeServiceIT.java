/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.br.service;

import com.br.beans.Cidade;

/**
 *
 * @author SergioD
 */
public interface CidadeServiceIT {
 
    public Cidade pesquisarCidade(String nome, String estado);
    
    public long totalDeUsuariosNaCidade(String cidade, String estado);
    
}
