/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.br.beans;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;

/**
 *
 * @author Sergio Diniz
 */
@Entity
public class Prefeitura implements Serializable{
    @Id @GeneratedValue
    private int id;
    @Column(nullable = false)
    private String nome;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false)
    private String senha;
    @Column(nullable = false)
    private String documento;
    @Column (nullable = false, unique = true)
    private String telefone;
    @Column(nullable = false)
    private String foto = "brasao.jpg";
    private String capa;
    @Column(length = 1024)
    private String slogan;
    @Column(length = 2048)
    private String textoSobre;
    @Column(length = 1024)
    private String website;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date aniversario;
    
    
    
    @Embedded @Column(nullable = false)
    private EnderecoPrefeitura enderecoPrefeitura;
    
    @OneToOne(cascade = CascadeType.ALL)
    private LideresPrefeitura lideresPrefeitura;
    
    @OneToMany
    private List<MensagemPrefeitura> mensagensPrefeitura;
    
    private boolean ativo;
    
    @Column(nullable = false, unique = true)
    private String codigo;
    
    @OneToOne(cascade = CascadeType.ALL)
    private Cidade cidade;
    @ManyToMany
    private List<Funcionario> funcionarios;
    

    public Prefeitura() {
    }

    public Prefeitura(String nome, String email, String senha, String documento, EnderecoPrefeitura enderecoPrefeitura, boolean ativo, Cidade cidade) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.documento = documento;
        this.enderecoPrefeitura = enderecoPrefeitura;
        this.ativo = ativo;
        this.cidade = cidade;
    }

    public Prefeitura(String nome, String email, String senha, boolean ativo, Cidade cidade) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.ativo = ativo;
        this.cidade = cidade;
    }

    public Prefeitura(EnderecoPrefeitura enderecoPrefeitura) {
        this.enderecoPrefeitura = enderecoPrefeitura;
    }

    


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public EnderecoPrefeitura getEnderecoPrefeitura() {
        return enderecoPrefeitura;
    }

    public void setEnderecoPrefeitura(EnderecoPrefeitura enderecoPrefeitura) {
        this.enderecoPrefeitura = enderecoPrefeitura;
    }

    public Cidade getCidade() {
        return cidade;
    }

    public void setCidade(Cidade cidade) {
        this.cidade = cidade;
    }

    public List<Funcionario> getFuncionarios() {
        return funcionarios;
    }

    public void setFuncionarios(List<Funcionario> funcionarios) {
        this.funcionarios = funcionarios;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }
    
    
    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + this.id;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Prefeitura other = (Prefeitura) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }
    
    
    
    
    
}
