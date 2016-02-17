/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.br.controle;

/**
 *
 * @author sergiodiniz
 */
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
 

@ManagedBean(name = "user")
@RequestScoped
public class User {
 
    private String email;
    private String name;
 
    public String getEmail() {
        return email;
    }
 
    public void setEmail(String email) {
        this.email = email;
    }
 
    public String getName() {
        return name;
    }
 
    public void setName(String name) {
        this.name = name;
    }
 
    public String getNameAndEmail(){
        return email + " " + name;
    }
}