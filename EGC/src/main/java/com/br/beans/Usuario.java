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
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

/**
 *
 * @author Sergio Diniz
 */
@Entity
public class Usuario extends Pessoa implements Serializable{
    
    @Embedded @Column(nullable = false)
    private EnderecoUsuario endereco;
    @Column(nullable = false, unique = true)
    private String nickname;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Denuncia> denuncias;
    
    public Usuario() {
    }

    public Usuario(EnderecoUsuario endereco, String nickname, String email, String senha) {
        super(email, senha);
        this.endereco = endereco;
        this.nickname = nickname;
        this.denuncias = new ArrayList<>();
    }

    public Usuario(EnderecoUsuario endereco) {
        this.endereco = endereco;
    }

    
    
    public EnderecoUsuario getEndereco() {
        return endereco;
    }

    public void setEndereco(EnderecoUsuario endereco) {
        this.endereco = endereco;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public List<Denuncia> getDenuncias() {
        return denuncias;
    }

    public void setDenuncias(List<Denuncia> denuncias) {
        this.denuncias = denuncias;
    }
    
    
    
    
}
