/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.br.controle;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

@ManagedBean
@SessionScoped
public class UploaderBB {

    private UploadedFile file;

    

    
    
    
    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

    //faz upload automatico
    public void handleFileUpload(FileUploadEvent event) {
        try {
            File targetFolder = new File("D:\\Sergio\\Documentos\\ADS\\P6\\TCC\\Sistema\\EGC\\EGC\\src\\main\\webapp");
            InputStream inputStream = event.getFile().getInputstream();
            OutputStream out = new FileOutputStream(new File(targetFolder,
                    event.getFile().getFileName()));
            int read = 0;
            byte[] bytes = new byte[1024];

            while ((read = inputStream.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }
            inputStream.close();
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //precisa aperta o botão
    public void upload() {
        if (file != null) {
            try {
                File targetFolder = new File("D:\\Sergio\\Documentos\\ADS\\P6\\TCC\\Sistema\\EGC\\EGC\\src\\main\\webapp");
                InputStream inputStream = file.getInputstream();
                
                String tipoArquivo = file.getFileName();
                tipoArquivo = tipoArquivo.substring(tipoArquivo.lastIndexOf("."), tipoArquivo.length());
//                System.out.println("tipo do arquivo: " + tipoArquivo);
                
                OutputStream out = new FileOutputStream(new File(targetFolder,
                        "sergiodiniz@gmail.com"+tipoArquivo));
                int read = 0;
                byte[] bytes = new byte[1024];

                while ((read = inputStream.read(bytes)) != -1) {
                    out.write(bytes, 0, read);
                }
                inputStream.close();
                out.flush();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

}
