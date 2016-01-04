/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.br.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;

/**
 *
 * @author Sergio Diniz
 */
@Entity
public class Funcionario extends Pessoa implements Serializable{
    @Column (nullable = false)
    private String nome;
    @Column (nullable = false, unique = true)
    private String cpf;
    @Column (nullable = false, unique = true)
    private String telefone;
    @Column (nullable = false)
    private boolean sexo;
    // true = masculino, false = feminino
    
    @ManyToMany(mappedBy = "funcionarios", cascade = CascadeType.ALL)
    private List<Prefeitura> prefeituras;

    
    
    public Funcionario() {
    }

    public Funcionario(String nome, String cpf, String email, String senha) {
        super(email, senha);
        this.nome = nome;
        this.cpf = cpf;
        this.prefeituras = new ArrayList<>();
    }
    
    

    public Funcionario(String nome, String cpf, List<Prefeitura> prefeituras, String email, String senha) {
        super(email, senha);
        this.nome = nome;
        this.cpf = cpf;
        this.prefeituras = prefeituras;
    }
    
    

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public List<Prefeitura> getPrefeituras() {
        return prefeituras;
    }

    public void setPrefeituras(List<Prefeitura> prefeituras) {
        this.prefeituras = prefeituras;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public boolean isSexo() {
        return sexo;
    }

    public void setSexo(boolean sexo) {
        this.sexo = sexo;
    }
    
    
    
    
    
}
