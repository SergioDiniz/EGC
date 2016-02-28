/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.br.service;

import com.br.beans.Denuncia;
import com.br.beans.EnderecoDenuncia;
import com.br.beans.TipoDeDenuncia;
import com.br.beans.Usuario;
import java.util.List;

/**
 *
 * @author Sergio Diniz
 */

public interface UsuarioServiceIT{
    
    public Usuario login(String email, String senha);
    
    public Usuario usuarioPorEmail(String email);
    
    public boolean excluir(Usuario usuario);
    
    public List<Denuncia> minhasDenuncias(String email);
    
    public List<Denuncia> denunciasQueAjudei(String email);
   
    
}
