/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.br.service;

import com.br.beans.Cidade;
import com.br.beans.Registro;
import java.util.List;

/**
 *
 * @author SergioD
 */
public interface CidadeServiceIT {
 
    public Cidade pesquisarCidade(String nome, String estado);
    
    public long totalDeUsuariosNaCidade(String cidade, String estado);
    
    public List<Registro> registrosDaCidade(String cidade, String estado);
    
    public List<List> ruasDeUmaCidadeNumerosDeDenuncia(String cidade, String estado);
    
    public List<List> cepDeUmaCidadeNumerosDeDenuncia(String cidade, String estado);
 
    public List<List> tiposDeDenunciasNumerosDeDenuncia(String cidade, String estado);
    
    public List<List> estadoDeDenunciasNumerosDeDenuncia(String cidade, String estado);
    
}
