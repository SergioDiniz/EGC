/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.br.service;

import com.br.beans.Funcionario;
import com.br.beans.Prefeitura;
import java.util.List;

/**
 *
 * @author SergioD
 */
public interface PrefeituraServiceIT {

    public Prefeitura login(String email, String senha);
    
    public Long totalDePrefeitura();
    
    public String cadastrarNaPrefeitura(Prefeitura prefeitura, Funcionario funcionario);
    
    public List<Prefeitura> prefeiturasPendentes();
    
    public Funcionario pesquisarFuncionario(Prefeitura prefeitura, String cpf);
    
    public String vincular(Prefeitura prefeitura, Funcionario funcionario);
    
    public String desvincular(Prefeitura prefeitura, String cpf);
    
    public List<Funcionario> funcionarios(Prefeitura prefeitura);
    
    public long totalDeFuncionariosNaPrefeitura(String email);
    
    public List<Long> dadosGeraisPrefeitura(String emailPrefeitura, String cidade, String estado);
    
    

    
    
}
