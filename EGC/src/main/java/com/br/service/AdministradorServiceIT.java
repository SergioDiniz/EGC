/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.br.service;

import com.br.beans.Administrador;
import com.br.beans.Denuncia;
import com.br.beans.Prefeitura;
import java.util.List;

/**
 *
 * @author SergioD
 */
public interface AdministradorServiceIT {
    
    public Administrador login(String email, String senha);
    
    public String  excluir(Prefeitura prefeitura);
    
    public String atualizarSituacao(Prefeitura prefeitura, boolean situacao);
    
    public List<Prefeitura> todasPrefeituras();
    
    public boolean bloquearDenuncia(Denuncia denuncia);
    
    public boolean desbloquearDenuncia(Denuncia denuncia);
    
}
