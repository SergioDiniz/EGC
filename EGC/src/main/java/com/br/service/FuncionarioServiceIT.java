/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.br.service;

import com.br.beans.Funcionario;
import com.br.beans.Prefeitura;
import com.br.beans.Registro;
import java.util.List;

/**
 *
 * @author SergioD
 */
public interface FuncionarioServiceIT {

    public Funcionario login(String email, String senha, String cidade, String estado);
    
    public Funcionario funcionarioPorEmail(String email);

    public Funcionario buscarPorCPF(String cpf);

    public String excluir(Funcionario funcionario);
    
    public List<Funcionario> todosFuncionarios();
    
    public int numeroDeFuncionarios();
    
    public List<Registro> registrosDoFuncionario(String email);
    
    public List<Prefeitura> prefeiturasDoFuncionario(String email);
    
    public List<Funcionario> funcionariosOnline(String email);
    
    public boolean tornaFuncionarioOnline(String email);
    
    public boolean tornaFuncionarioOffline(String email);

}
