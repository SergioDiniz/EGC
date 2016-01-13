/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.br.service;

import com.br.controle.UploadType;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author sergiodiniz
 */
@Stateless
@Remote(UploadServiceIT.class)
public class UploadService implements UploadServiceIT, Serializable{
    
    @Override
    public String upload(UploadType uploadType, String email, UploadedFile file) {
        if (file != null) {
            try {

                File targetFolder;

                if (uploadType == UploadType.PERFIL_FUNCIONARIO) {
                    targetFolder = new File("/Volumes/Arquivos/Sergio/Documentos/ADS/P6/TCC/Sistema/EGC/EGC/src/main/webapp/sis/funcionario/foto");
                } else if (uploadType == UploadType.DOCUMENTO_SOLICITACAO) {
                    targetFolder = new File("/Volumes/Arquivos/Sergio/Documentos/ADS/P6/TCC/Sistema/EGC/EGC/src/main/webapp/sis/admin/documentos-de-solicitacao");
                } else {
                    targetFolder = new File("/Volumes/Arquivos/Sergio/Documentos/ADS/P6/TCC/Sistema/EGC/EGC/src/main/webapp/sis/prefeitura/brasao");
                }

//                File targetFolder = new File("D:\\Sergio\\Documentos\\ADS\\P6\\TCC\\Sistema\\EGC\\EGC\\src\\main\\webapp\\sis\\admin\\documentos-de-solicitacao");
                InputStream inputStream = file.getInputstream();

                String tipoArquivo = file.getFileName();
                tipoArquivo = tipoArquivo.substring(tipoArquivo.lastIndexOf("."), tipoArquivo.length());
                String nomeArquivo = email + tipoArquivo;

                OutputStream out = new FileOutputStream(new File(targetFolder, nomeArquivo));

                int read = 0;
                byte[] bytes = new byte[1024];

                while ((read = inputStream.read(bytes)) != -1) {
                    out.write(bytes, 0, read);
                }
                inputStream.close();
                out.flush();
                out.close();
                return nomeArquivo;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;

    }
    
}
