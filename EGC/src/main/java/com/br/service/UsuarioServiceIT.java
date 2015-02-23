/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.br.service;

import com.br.beans.Usuario;

/**
 *
 * @author Sergio Diniz
 */

public interface UsuarioServiceIT{
    
    public Usuario login(String email, String senha);
    
    public boolean excluir(Usuario usuario);
    
}
