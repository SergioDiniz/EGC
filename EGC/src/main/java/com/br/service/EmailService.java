/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.br.service;

import com.br.controle.EmailType;
import java.net.MalformedURLException;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;

/**
 *
 * @author sergiodiniz
 */
@Stateless
@Remote(EmailServiceIT.class)
public class EmailService implements EmailServiceIT{
    
    @Override
    public boolean emailBemVindoUsuario(String emailUsuario, String nomeUsuario){
        try {
            String layout = "<html><head><meta charset=\"UTF-8\"><style>*{font-family: sans-serif;}</style></head><body style=\"color: #666; font-size: 15px\"> <div align=\"center\" style=\"margin-top: 30px;\"> <img src=\"https://raw.githubusercontent.com/SergioDiniz/EGC/master/EGC/src/main/webapp/img/logo-azul.png\" alt=\"Logo EGC\"/> </div><div align=\"center\"> <h2 style=\"font-weight: 100\">Efici&ecirc;ncia em Gest&atilde;o de Cidades</h2> </div><div style=\"font-size: 20px; color: #fff; margin-top: 20px;padding: 15px 0 15px 0; background-color: #03a4f7; width: 100%; max-width: 600px; margin: 0 auto; text-align: center\"> BEM-VINDO! </div><div style=\"margin: 0 auto; margin-top: 50px; max-width: 500px\" align=\"center\"> Ol&aacute; <b style=\"color: #03a4f7\">" + nomeUsuario + "</b> <br/><br/><br/> Seu cadastrado foi realizado com sucesso e voc&ecirc; j&aacute; pode come&ccedil;ar a usar.<br/><br/><br/> <section style=\"margin-bottom: 8px\"><b>Parab&eacute;ns</b> por se inscrever no <b>EGC</b>! <br/></section> Juntos vamos redefinir o processo de reclama&ccedil;&otilde;es e criaremos cidades inteligentes! </div><div align=\"center\" style=\"margin-top: 40px\"> <img src=\"https://raw.githubusercontent.com/SergioDiniz/EGC/master/EGC/src/main/webapp/img/icon-parabens.png\" width=\"80\" alt=\"Logo EGC\"/> </div><div align=\"center\" style=\"margin-top: 80px; background-color: #f9f9f9; padding: 40px 0 40px 0\"> <div align=\"center\"> <img src=\"https://raw.githubusercontent.com/SergioDiniz/EGC/master/EGC/src/main/webapp/img/s-logo.png\" width=\"50\" alt=\"Logo EGC\"/> </div><br/> <br/> Em caso de <b>d&uacute;vidas</b>, entre em contato conosco atrav&eacute;s do email: <b style=\"color: #03a4f7\">contato@egc.com.br</b> <br/><br/> <i style=\"font-size: 12px\">Copyright &copy; 2016 - EGC, Todos os direitos reservados.</i> </div></body></html>";;
            enviarEmail(emailUsuario, layout);
            return true;
        } catch (Exception e) {
            System.out.println("ERRO AO ENVIAR EMAIL DE BEM VINDO PARA USUARIO: " + e.getMessage());
        }
        return false;
    }
    
    @Override
    public boolean emailBemVindoFuncionario(String emailUsuario, String nomeUsuario, String prefeitura){
        try {
            String layout = "<html><head><meta charset=\"UTF-8\"> <style>*{font-family: sans-serif;}</style></head><body style=\"color: #666; font-size: 15px\"> <div align=\"center\" style=\"margin-top: 30px;\"> <img src=\"https://raw.githubusercontent.com/SergioDiniz/EGC/master/EGC/src/main/webapp/img/logo-azul.png\" alt=\"Logo EGC\"/> </div><div align=\"center\"> <h2 style=\"font-weight: 100\">Efici&ecirc;ncia em Gest&atilde;o de Cidades</h2> </div><div style=\"font-size: 20px; color: #fff; margin-top: 20px;padding: 15px 0 15px 0; background-color: #03a4f7; width: 100%; max-width: 600px; margin: 0 auto; text-align: center\"> BEM-VINDO! </div><div style=\"margin: 0 auto; margin-top: 50px; max-width: 500px\" align=\"center\"> Ol&aacute; <b style=\"color: #03a4f7\">" + nomeUsuario + "</b> <br/><br/><br/> Seu cadastrado foi realizado com sucesso na <b>Prefeitura Municipal de " + prefeitura + "</b> e voc&ecirc; j&aacute; pode come&ccedil;ar a usar.<br/><br/><br/> <section style=\"margin-bottom: 8px\"><b>Parab&eacute;ns</b> por fazer parte do <b>EGC</b>! <br/></section> Juntos vamos redefinir o processo de reclama&ccedil;&otilde;es e criaremos cidades inteligentes! </div><div align=\"center\" style=\"margin-top: 40px\"> <img src=\"https://raw.githubusercontent.com/SergioDiniz/EGC/master/EGC/src/main/webapp/img/icon-parabens.png\" width=\"80\" alt=\"Logo EGC\"/> </div><div align=\"center\" style=\"margin-top: 80px; background-color: #f9f9f9; padding: 40px 0 40px 0\"> <div align=\"center\"> <img src=\"https://raw.githubusercontent.com/SergioDiniz/EGC/master/EGC/src/main/webapp/img/s-logo.png\" width=\"50\" alt=\"Logo EGC\"/> </div><br/> <br/> Em caso de <b>d&uacute;vidas</b>, entre em contato conosco atrav&eacute;s do email: <b style=\"color: #03a4f7\">contato@egc.com.br</b> <br/><br/> <i style=\"font-size: 12px\">Copyright &copy; 2016 - EGC, Todos os direitos reservados.</i> </div></body></html>";
            enviarEmail(emailUsuario, layout);
            return true;
        } catch (Exception e) {
            System.out.println("ERRO AO ENVIAR EMAIL DE BEM VINDO PARA FUNCIONARIO: " + e.getMessage());
        }
        return false;
    }
    
    @Override
    public boolean emailBemVindoPrefeitura(String emailUsuario, String nomeUsuario, String prefeitura){
        try {
            String layout = "<html><head><meta charset=\"UTF-8\"> <style>*{font-family: sans-serif;}</style></head><body style=\"color: #666; font-size: 15px\"> <div align=\"center\" style=\"margin-top: 30px;\"> <img src=\"https://raw.githubusercontent.com/SergioDiniz/EGC/master/EGC/src/main/webapp/img/logo-azul.png\" alt=\"Logo EGC\"/> </div><div align=\"center\"> <h2 style=\"font-weight: 100\">Efici&ecirc;ncia em Gest&atilde;o de Cidades</h2> </div><div style=\"font-size: 20px; color: #fff; margin-top: 20px;padding: 15px 0 15px 0; background-color: #03a4f7; width: 100%; max-width: 600px; margin: 0 auto; text-align: center\"> BEM-VINDO! </div><div style=\"margin: 0 auto; margin-top: 50px; max-width: 500px\" align=\"center\"> Ol&aacute; <b style=\"color: #03a4f7\">" + nomeUsuario + "</b> <br/><br/><br/> Seu cadastrado foi realizado com sucesso para a <b>Cidade de " + prefeitura + "</b>. Agora voc&ecirc; precisa aguarda um pouco enquanto verificamos os seus dados. Fique atento, vamos te informar quando estiver tudo pronto.<br/><br/><br/> <section style=\"margin-bottom: 8px\"><b>Parab&eacute;ns</b> por fazer parte do <b>EGC</b>! <br/></section> Juntos vamos redefinir o processo de reclama&ccedil;&otilde;es e criaremos cidades inteligentes! </div><div align=\"center\" style=\"margin-top: 40px\"> <img src=\"https://raw.githubusercontent.com/SergioDiniz/EGC/master/EGC/src/main/webapp/img/icon-parabens.png\" width=\"80\" alt=\"Logo EGC\"/> </div><div align=\"center\" style=\"margin-top: 80px; background-color: #f9f9f9; padding: 40px 0 40px 0\"> <div align=\"center\"> <img src=\"https://raw.githubusercontent.com/SergioDiniz/EGC/master/EGC/src/main/webapp/img/s-logo.png\" width=\"50\" alt=\"Logo EGC\"/> </div><br/> <br/> Em caso de <b>d&uacute;vidas</b>, entre em contato conosco atrav&eacute;s do email: <b style=\"color: #03a4f7\">contato@egc.com.br</b> <br/><br/> <i style=\"font-size: 12px\">Copyright &copy; 2016 - EGC, Todos os direitos reservados.</i> </div></body></html>";
            enviarEmail(emailUsuario, layout);
            return true;
        } catch (Exception e) {
            System.out.println("ERRO AO ENVIAR EMAIL DE BEM VINDO PARA PREFEITURA: " + e.getMessage());
        }
        return false;
    }
    
    @Override
    public boolean emailAceitoPrefeitura(String emailUsuario, String nomeUsuario, String prefeitura){
        try {
            String layout = "<html><head><meta charset=\"UTF-8\"> <style>*{font-family: sans-serif;}</style></head><body style=\"color: #666; font-size: 15px\"> <div align=\"center\" style=\"margin-top: 30px;\"> <img src=\"https://raw.githubusercontent.com/SergioDiniz/EGC/master/EGC/src/main/webapp/img/logo-azul.png\" alt=\"Logo EGC\"/> </div><div align=\"center\"> <h2 style=\"font-weight: 100\">Efici&ecirc;ncia em Gest&atilde;o de Cidades</h2> </div><div style=\"font-size: 20px; color: #fff; margin-top: 20px;padding: 15px 0 15px 0; background-color: #03a4f7; width: 100%; max-width: 600px; margin: 0 auto; text-align: center\"> BEM-VINDO! </div><div style=\"margin: 0 auto; margin-top: 50px; max-width: 500px\" align=\"center\"> Ol&aacute; <b style=\"color: #03a4f7\">" + nomeUsuario + "</b> <br/><br/><br/> Ap&oacute;s analise, o seu cadastrado foi confirmado com sucesso para a <b>Cidade de " + prefeitura + "</b>. e voc&ecirc; j&aacute; pode come&ccedil;ar a usar.<br/><br/><br/> <section style=\"margin-bottom: 8px\"><b>Parab&eacute;ns</b> por fazer parte do <b>EGC</b>! <br/></section> Juntos vamos redefinir o processo de reclama&ccedil;&otilde;es e criaremos cidades inteligentes! </div><div align=\"center\" style=\"margin-top: 40px\"> <img src=\"https://raw.githubusercontent.com/SergioDiniz/EGC/master/EGC/src/main/webapp/img/icon-parabens.png\" width=\"80\" alt=\"Logo EGC\"/> </div><div align=\"center\" style=\"margin-top: 80px; background-color: #f9f9f9; padding: 40px 0 40px 0\"> <div align=\"center\"> <img src=\"https://raw.githubusercontent.com/SergioDiniz/EGC/master/EGC/src/main/webapp/img/s-logo.png\" width=\"50\" alt=\"Logo EGC\"/> </div><br/> <br/> Em caso de <b>d&uacute;vidas</b>, entre em contato conosco atrav&eacute;s do email: <b style=\"color: #03a4f7\">contato@egc.com.br</b> <br/><br/> <i style=\"font-size: 12px\">Copyright &copy; 2016 - EGC, Todos os direitos reservados.</i> </div></body></html>";
            enviarEmail(emailUsuario, layout);
            return true;
        } catch (Exception e) {
            System.out.println("ERRO AO ENVIAR EMAIL DE ACEITO PREFEITURA: " + e.getMessage());
        }
        return false;
    }
    
    @Override
    public boolean emailRecusoPrefeitura(String emailUsuario, String nomeUsuario, String prefeitura){
        try {
            String layout = "<html><head><meta charset=\"UTF-8\"> <style>*{font-family: sans-serif;}</style></head><body style=\"color: #666; font-size: 15px\"> <div align=\"center\" style=\"margin-top: 30px;\"> <img src=\"https://raw.githubusercontent.com/SergioDiniz/EGC/master/EGC/src/main/webapp/img/logo-azul.png\" alt=\"Logo EGC\"/> </div><div align=\"center\"> <h2 style=\"font-weight: 100\">Efici&ecirc;ncia em Gest&atilde;o de Cidades</h2> </div><div style=\"font-size: 20px; color: #fff; margin-top: 20px;padding: 15px 0 15px 0; background-color: #03a4f7; width: 100%; max-width: 600px; margin: 0 auto; text-align: center\"> Ops! </div><div style=\"margin: 0 auto; margin-top: 50px; max-width: 500px\" align=\"center\"> Ol&aacute; <b style=\"color: #03a4f7\">" + nomeUsuario + "</b> <br/><br/><br/> Ap&oacute;s analise, não conseguimos confirmar seu dados para a <b>Cidade de " + prefeitura + "</b>. Entre em contato para mais informa&ccedil;&otilde;es.<br/><br/><br/> <section style=\"margin-bottom: 8px\">Fa&ccedil;a parte do <b>EGC</b>! <br/></section> Juntos vamos redefinir o processo de reclama&ccedil;&otilde;es e criaremos cidades inteligentes! </div><div align=\"center\" style=\"margin-top: 40px\"> <img src=\"https://raw.githubusercontent.com/SergioDiniz/EGC/master/EGC/src/main/webapp/img/icon-parabens.png\" width=\"80\" alt=\"Logo EGC\"/> </div><div align=\"center\" style=\"margin-top: 80px; background-color: #f9f9f9; padding: 40px 0 40px 0\"> <div align=\"center\"> <img src=\"https://raw.githubusercontent.com/SergioDiniz/EGC/master/EGC/src/main/webapp/img/s-logo.png\" width=\"50\" alt=\"Logo EGC\"/> </div><br/> <br/> Em caso de <b>d&uacute;vidas</b>, entre em contato conosco atrav&eacute;s do email: <b style=\"color: #03a4f7\">contato@egc.com.br</b> <br/><br/> <i style=\"font-size: 12px\">Copyright &copy; 2016 - EGC, Todos os direitos reservados.</i> </div></body></html>";
            enviarEmail(emailUsuario, layout);
            return true;
        } catch (Exception e) {
            System.out.println("ERRO AO ENVIAR EMAIL DE RECUSO PREFEITURA: " + e.getMessage());
        }
        return false;
    }
    
    @Override
    public boolean emailRecuperarSenha(String emailUsuario, String nomeUsuario, String senha){
        try {
            String layout = "<html><head><meta charset=\"UTF-8\"> <style>*{font-family: sans-serif;}</style></head><body style=\"color: #666; font-size: 15px\"> <div align=\"center\" style=\"margin-top: 30px;\"> <img src=\"https://raw.githubusercontent.com/SergioDiniz/EGC/master/EGC/src/main/webapp/img/logo-azul.png\" alt=\"Logo EGC\"/> </div><div align=\"center\"> <h2 style=\"font-weight: 100\">Efici&ecirc;ncia em Gest&atilde;o de Cidades</h2> </div><div style=\"font-size: 20px; color: #fff; margin-top: 20px;padding: 15px 0 15px 0; background-color: #03a4f7; width: 100%; max-width: 600px; margin: 0 auto; text-align: center\"> ALGUM PROBLEMA? </div><div style=\"margin: 0 auto; margin-top: 50px; max-width: 500px\" align=\"center\"> Ol&aacute; <b style=\"color: #03a4f7\">" + nomeUsuario + "</b> <br/><br/><br/> N&oacute;s recebemos uma solicita&ccedil;ão e parece que voc&ecirc; <b>esqueceu sua senha</b>. Então estamos te enviando os seus dados. Caso não tenha sido voc&ecirc;, aconselhamos mudar sua senha. <br/><br/> <b>email:</b> " + emailUsuario + " <br/> <b>senha:</b> " + senha + " <br/><br/><br/> <section style=\"margin-bottom: 8px\"><b>Obrigado</b> por fazer parte do <b>EGC</b>! <br/></section> Juntos vamos redefinir o processo de reclama&ccedil;&otilde;es e criaremos cidades inteligentes! </div><div align=\"center\" style=\"margin-top: 40px\"> <img src=\"https://raw.githubusercontent.com/SergioDiniz/EGC/master/EGC/src/main/webapp/img/icon-parabens.png\" width=\"80\" alt=\"Logo EGC\"/> </div><div align=\"center\" style=\"margin-top: 80px; background-color: #f9f9f9; padding: 40px 0 40px 0\"> <div align=\"center\"> <img src=\"https://raw.githubusercontent.com/SergioDiniz/EGC/master/EGC/src/main/webapp/img/s-logo.png\" width=\"50\" alt=\"Logo EGC\"/> </div><br/> <br/> Em caso de <b>d&uacute;vidas</b>, entre em contato conosco atrav&eacute;s do email: <b style=\"color: #03a4f7\">contato@egc.com.br</b> <br/><br/> <i style=\"font-size: 12px\">Copyright &copy; 2016 - EGC, Todos os direitos reservados.</i> </div></body></html>";
            enviarEmail(emailUsuario, layout);
            return true;
        } catch (Exception e) {
            System.out.println("ERRO AO ENVIAR EMAIL DE RECUPERAR SENHA: " + e.getMessage());
        }
        return false;
    }
    
    public boolean enviarEmail(String emailUsuario, String layoutEmail) throws EmailException, MalformedURLException {

        System.out.println("Preparando email: ");

        String eDestino = emailUsuario;
        String eRemetente = "seu@email.com";
        String eTitulo = "EGC - Eficiência em Gestão de Cidades ";

        HtmlEmail email = new HtmlEmail();
        email.setHostName("smtp.gmail.com");
        email.setSmtpPort(587);
        email.setSubject(eTitulo);
        email.setAuthenticator(new DefaultAuthenticator(eRemetente, "senha"));
        email.setSSLOnConnect(true);
        email.setFrom(eRemetente);
        email.addTo(eDestino);
        email.setTLS(true);
        email.setSSL(true);

        // configura a mensagem para o formato HTML
        email.setHtmlMsg(layoutEmail);

        // configure uma mensagem alternativa caso o servidor não suporte HTML
        email.setTextMsg("Seu servidor de e-mail não suporta mensagem HTML");

        System.out.println("Tentando enviar: ");
        email.send();

        System.out.println("Email enviado. ");
        
        return true;
        
    }
    
    
    
    
}
