/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.br.service;

import com.br.beans.Denuncia;
import com.br.beans.TipoDeDenuncia;
import java.util.List;

/**
 *
 * @author SergioD
 */
public interface DenunciaServiceIT {
    
    public List<Denuncia> pesquisarPorCidade(String cidade, String estado, String ordem);
    
    public List<Denuncia> pesquisarPorCidadeComFiltro(String cidade, String estado, TipoDeDenuncia tipoDeDenuncia, String ordem);
    
    public long totalDeDenunciasNaCidade(String cidade, String estado);
    
    public long totalDeDenunciasAtendidasNaCidade(String cidade, String estado);
    
}
