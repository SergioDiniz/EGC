/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.br.service;

import com.br.beans.ConteudoInapropriado;
import com.br.beans.Denuncia;
import com.br.beans.EnderecoDenuncia;
import com.br.beans.TipoDeDenuncia;
import com.br.beans.Usuario;
import java.util.List;

/**
 *
 * @author SergioD
 */
public interface DenunciaServiceIT {
    
    public String novaDenuncia(Usuario usuario, EnderecoDenuncia enderecoDenuncia, String denucia, String foto, TipoDeDenuncia tipoDeDenuncia);
    
    public Long totalDeDenuncias();
    
    public List<Denuncia> pesquisarPorCidade(String cidade, String estado, String ordem);
    
    public List<Denuncia> pesquisarPorCidadeComFiltro(String cidade, String estado, TipoDeDenuncia tipoDeDenuncia, String ordem);
    
    public long totalDeDenunciasNaCidade(String cidade, String estado);
    
    public long totalDeDenunciasAtendidasNaCidade(String cidade, String estado);
    
    public boolean setAjudarDenuncia(Denuncia denuncia, Usuario usuario);
    
    public int getAjudarDenuncia(Denuncia denuncia);
    
    public boolean setReclamarDenuncia(Denuncia denuncia, ConteudoInapropriado conteudoInapropriado);
    
    public long getReclamarDenuncia(Denuncia denuncia);
    
    public List<Denuncia> denunciasComMaisAjudasPorCidade(String cidade, String estado);
    
    public List<Denuncia> denunciasMaisRecentesPorCidade(String cidade, String estado);
    
    public List<Denuncia> denunciasComReclamacoes();
    
    public List<ConteudoInapropriado> comentariosDeConteudoInapropriadoEmDenuncia(Denuncia denuncia);
    
}
