/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.br.service;

/**
 *
 * @author sergiodiniz
 */
public interface EmailServiceIT {
    
    public boolean emailBemVindoUsuario(String emailUsuario, String nomeUsuario);
    
    public boolean emailBemVindoFuncionario(String emailUsuario, String nomeUsuario, String prefeitura);
    
    public boolean emailBemVindoPrefeitura(String emailUsuario, String nomeUsuario, String prefeitura);
    
    public boolean emailAceitoPrefeitura(String emailUsuario, String nomeUsuario, String prefeitura);
    
    public boolean emailRecusoPrefeitura(String emailUsuario, String nomeUsuario, String prefeitura);
    
    public boolean emailRecuperarSenha(String emailUsuario, String nomeUsuario, String senha);
    
}
