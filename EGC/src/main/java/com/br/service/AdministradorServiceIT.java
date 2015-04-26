/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.br.service;

import com.br.beans.Administrador;
import com.br.beans.Prefeitura;

/**
 *
 * @author SergioD
 */
public interface AdministradorServiceIT {
    
    public Administrador login(String email, String senha);
    
    public String  excluir(Prefeitura prefeitura);
    
}
