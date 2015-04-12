/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.br.controle;

import java.io.IOException;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.servlet.http.Part;

/**
 *
 * @author SergioD
 */
@ManagedBean(name = "controladorPrefeitura")
@SessionScoped
public class ControladorPrefeitura implements Serializable {

    private Part termo;
    
    public ControladorPrefeitura() {
    }
    
    public String uploadTermo() throws IOException{
        termo.write("\\"+getFilename(termo));
        return "ok";
    }

    private static String getFilename(Part part){
        for(String cd : part.getHeader("content-disposition").split(";")){
            if(cd.trim().startsWith("filename")){
                String filename = cd.substring(cd.indexOf('=') + 1).trim().replace("\"", "");
                return filename.substring(filename.lastIndexOf('/') + 1).substring(filename.lastIndexOf('\\') + 1);
            }
        }
        return null;
    }
    
    public Part getTermo() {
        return termo;
    }

    public void setTermo(Part termo) {
        this.termo = termo;
    }
    
    
    
    
    
}
