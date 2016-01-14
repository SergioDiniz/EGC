/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.br.beans;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Sergio Diniz
 */
@Entity
public class Funcionario extends Pessoa implements Serializable {

    @Column(nullable = false)
    private String nome;
    @Column(nullable = false, unique = true)
    private String cpf;
    @Column(nullable = false, unique = true)
    private String telefone;
    @Column(nullable = false)
    private boolean sexo;
    //true = masculino , false = feminino
    @Column(nullable = false)
    private boolean ativo;
    @Column(nullable = false)
    private String foto;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date ultimoDiaAtivo;

    @ManyToMany(mappedBy = "funcionarios", cascade = CascadeType.ALL)
    private List<Prefeitura> prefeituras;

    @OneToMany(mappedBy = "funcionario", cascade = CascadeType.ALL)
    private List<InformacaoDeAtendida> informacaoDeAtendidas;

    
    @OneToMany(mappedBy = "funcionario")
    private List<Registro> registros;

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

    public String dataUltimoDiaAtivoFormatada(){
        String d = new SimpleDateFormat("dd.MM.yyyy").format(this.ultimoDiaAtivo);
        return d;
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
    
    public boolean getSexo() {
        return sexo;
    }

    public void setSexo(boolean sexo) {
        this.sexo = sexo;
    }

    public List<InformacaoDeAtendida> getInformacaoDeAtendidas() {
        return informacaoDeAtendidas;
    }

    public void setInformacaoDeAtendidas(List<InformacaoDeAtendida> informacaoDeAtendidas) {
        this.informacaoDeAtendidas = informacaoDeAtendidas;
    }

    public List<Registro> getRegistros() {
        return registros;
    }

    public void setRegistros(List<Registro> registros) {
        this.registros = registros;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public Date getUltimoDiaAtivo() {
        return ultimoDiaAtivo;
    }

    public void setUltimoDiaAtivo(Date ultimoDiaAtivo) {
        this.ultimoDiaAtivo = ultimoDiaAtivo;
    }
    
    

}
