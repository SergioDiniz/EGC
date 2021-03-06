/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.br.service;

import com.br.beans.Denuncia;
import com.br.beans.Funcionario;
import com.br.beans.MensagemPrefeitura;
import com.br.beans.Prefeitura;
import com.br.beans.Registro;
import java.util.List;

/**
 *
 * @author SergioD
 */
public interface PrefeituraServiceIT {

    public Prefeitura login(String email, String senha);
    
    public Prefeitura pesquisarPrefeituraPorCidade(String cidade, String estado);
    
    public Prefeitura prefeituraPorEmail(String email);
    
    public Prefeitura pesquisarPrefeituraPorCodigo(String codigo);
    
    public Long totalDePrefeitura();
    
    public Long totalDePrefeituraAtivas();
    
    public String cadastrarNaPrefeitura(Prefeitura prefeitura, Funcionario funcionario);
    
    public List<Prefeitura> prefeiturasPendentes();
    
    public Funcionario pesquisarFuncionario(Prefeitura prefeitura, String cpf);
    
    public String vincular(Prefeitura prefeitura, Funcionario funcionario);
    
    public String desvincular(Prefeitura prefeitura, String cpf);
    
    public List<Funcionario> funcionarios(Prefeitura prefeitura);
    
    public long totalDeFuncionariosNaPrefeitura(String email);
    
    public List<Long> dadosGeraisPrefeitura(String emailPrefeitura, String cidade, String estado);
    
    public boolean prefeituraCadastrada(String cidade, String estado);

    public List<Registro> registroDaPrefeitura(String codigoPrefeitura);
    
    public List<Denuncia> denunciaDaPrefeitura(String codigoPrefeitura);
    
    public List<Funcionario> funcionarioDaPrefeitura(String codigoPrefeitura);
    
    public boolean novaMensagemEmPrefeitura(MensagemPrefeitura mensagemPrefeitura, Prefeitura prefeitura);
    
    public List<MensagemPrefeitura> mensagensDaPrefeitura(String codigoPrefeitura);
    
    public boolean excluirMensagemEmPrefeitura(MensagemPrefeitura mensagemPrefeitura, Prefeitura prefeitura);
    
}
