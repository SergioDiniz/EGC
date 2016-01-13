/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.br.service;

import com.br.controle.UploadType;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author sergiodiniz
 */
public interface UploadServiceIT {
    
    public String upload(UploadType uploadType, String email, UploadedFile file);
    
}
