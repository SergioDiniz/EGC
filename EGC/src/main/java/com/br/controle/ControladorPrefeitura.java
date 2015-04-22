/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.br.controle;

import com.br.beans.Cidade;
import com.br.beans.CidadePK;
import com.br.beans.EnderecoPrefeitura;
import com.br.beans.Prefeitura;
import com.br.fachada.Fachada;
import java.io.IOException;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import javax.ejb.EJB;
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
    
    Prefeitura prefeitura;
    Cidade cidade;
    CidadePK  cidadePK;
    
    @EJB
    private Fachada fachada;
    
    public ControladorPrefeitura() {
        this.prefeitura = new Prefeitura();
        this.prefeitura.setEnderecoPrefeitura(new EnderecoPrefeitura());
        this.cidade = new Cidade();
        this.cidadePK = new CidadePK();
    }
    
    public String cadastro() throws IOException{
        
        try{
            cidade.setCidadePK(cidadePK);
            fachada.cadastrar(cidade);
            cidade = fachada.pesquisarCidade(cidadePK.getNomeCidade(), cidadePK.getSiglaEstado());
        }catch(Exception e){
            
        }
        
        
        
        prefeitura.setCidade(cidade);
        prefeitura.getEnderecoPrefeitura().setCidade(cidade.getCidadePK().getNomeCidade());
        prefeitura.getEnderecoPrefeitura().setEstado(cidade.getCidadePK().getSiglaEstado());
        prefeitura.setDocumento(uploadTermo());
        fachada.atualizar(prefeitura);
        
        
        
        cidade = new Cidade();
        cidadePK = new CidadePK();
        prefeitura = new Prefeitura(new EnderecoPrefeitura());       
        
        return "/index.jsf?faces-redirect=true";
        
    }
    
    // Local de Salvamento
    //C:\Glassfish\glassfish\domains\domain1\generated\jsp\EGC
    public String uploadTermo() throws IOException{
        String nomeArquivo = getFilename(termo);
        String extencaoArquivo = nomeArquivo.substring(nomeArquivo.lastIndexOf("."), nomeArquivo.length());
        String nome = prefeitura.getEmail()+extencaoArquivo;
        
        termo.write("\\"+nome);
        return nome;
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
    
    
    
    
    
    // gets and setes
    
    public Part getTermo() {
        return termo;
    }

    public void setTermo(Part termo) {
        this.termo = termo;
    }

    public Prefeitura getPrefeitura() {
        return prefeitura;
    }

    public void setPrefeitura(Prefeitura prefeitura) {
        this.prefeitura = prefeitura;
    }

    public Cidade getCidade() {
        return cidade;
    }

    public void setCidade(Cidade cidade) {
        this.cidade = cidade;
    }

    public CidadePK getCidadePK() {
        return cidadePK;
    }

    public void setCidadePK(CidadePK cidadePK) {
        this.cidadePK = cidadePK;
    }
    
    
    
    
    
    
}
