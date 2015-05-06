/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import com.br.beans.Denuncia;
import com.br.beans.EnderecoDenuncia;
import com.br.beans.TipoDeDenuncia;

/**
 *
 * @author SergioD
 */
public class main {
    public static void main(String[] args) {
        
        System.out.println("teste");
        EnderecoDenuncia ed = new EnderecoDenuncia();
        ed.setCidade("Santa helena");
        ed.setEstado("PB");
        
        Denuncia denuncia = new Denuncia("asdas", "asdasd", ed, TipoDeDenuncia.COLETA_DE_LIXO);
        
        
    }
}
