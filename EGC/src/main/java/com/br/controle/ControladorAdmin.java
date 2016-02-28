/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.br.controle;

import com.br.beans.Administrador;
import com.br.beans.ConteudoInapropriado;
import com.br.beans.Denuncia;
import com.br.beans.Funcionario;
import com.br.beans.Prefeitura;
import com.br.fachada.Fachada;
import java.io.IOException;
import javax.faces.bean.SessionScoped;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.apache.commons.mail.SimpleEmail;

/**
 *
 * @author SergioD
 */
@ManagedBean(name = "controladorAdmin")
@SessionScoped
public class ControladorAdmin implements Serializable {

    private Administrador administrador;
    private Prefeitura prefeituraAx;
    private Denuncia denunciaReclamacao;
    private Funcionario gerenciarFuncionario;

    private boolean mostrarModalRecusar;
    private boolean mostrarModalAceitar;

    @EJB
    private Fachada fachada;

    public ControladorAdmin() {
        this.administrador = new Administrador();
        this.prefeituraAx = new Prefeitura();
        this.mostrarModalRecusar = false;
        this.mostrarModalAceitar = false;
    }

    public void mostrapagina() throws IOException {
        if (this.administrador.getEmail() == null) {
            FacesContext.getCurrentInstance().getExternalContext().redirect("/EGC/index.jsf");
        }

    }

    public void mostrapaginaavaliacao() throws IOException {
        if (this.prefeituraAx.getEmail() == null) {
            FacesContext.getCurrentInstance().getExternalContext().redirect("index.jsf");
        }

    }

    public String login() {

        this.administrador = fachada.loginAdmin(administrador.getEmail(), administrador.getSenha());

        if (administrador != null) {
            return "/sis/admin/index.jsf?faces-redirect=true";
        }
        info("Usuário invalido!");
        this.administrador = new Administrador();
        return null;

    }

    public String logout() {
        this.administrador = new Administrador();
        return "/index.jsf?faces-redirect=true";
    }

    public List<Prefeitura> prefeiturasPendentes() {
        return fachada.prefeiturasPendentes();
    }

    public List<Prefeitura> prefeiturasAtivas() {
        return fachada.todasPrefeiturasAtivas();
    }

    public String avaliarSolicitacao(Prefeitura p) {
        this.prefeituraAx = p;
        return "avaliarsolicitacao.jsf?faces-redirect=true";
    }

    public String excluirPrefeitura() {
        fachada.excluirPrefeitura(prefeituraAx);
        this.prefeituraAx = new Prefeitura();
        return "solicitacoes.jsf?faces-redirect=true";
    }

    public String excluirPrefeituraAtiva() {
        fachada.excluirPrefeitura(prefeituraAx);
        this.prefeituraAx = new Prefeitura();
        return "prefeiturasativas.jsf?faces-redirect=true";
    }

    public String aceitarSolicitacao() {
        fachada.atualizarSituacaoPrefeitura(prefeituraAx, true);
        this.prefeituraAx = new Prefeitura();
        return "solicitacoes.jsf?faces-redirect=true";
    }

    public String bloquearPrefeitura() {
        fachada.atualizarSituacaoPrefeitura(prefeituraAx, false);
        this.prefeituraAx = new Prefeitura();
        return "prefeiturasativas.jsf?faces-redirect=true";
    }

    public static void info(String s) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", s));
    }

    public static void infoUsuarioInvalido() {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "Usuário inválido!"));
    }

    public static void infoDadosAtualizados() {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "Dados atualizado com sucesso!"));
    }

    public void saveMessage() {
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage("Info", "Dados atualizados com sucesso!"));
    }

    public String atualizar() {
        fachada.atualizar(this.administrador);
        infoDadosAtualizados();
        return null;
    }

    public void mostraModalRecusar(Prefeitura p) throws IOException {
        this.prefeituraAx = p;
        this.mostrarModalRecusar = true;

//        System.out.println("nome: " + p.getNome());
//        return "/sis/admin/solicitacoes.jsf?faces-redirect=true#modal";
//        FacesContext.getCurrentInstance().getExternalContext().redirect("/EGC/sis/admin/solicitacoes.jsf#modal");
    }

    public void mostraModalAceitar(Prefeitura p) throws IOException {
        this.prefeituraAx = p;
        this.mostrarModalAceitar = true;

//        System.out.println("nome: " + p.getNome());
//        return "/sis/admin/solicitacoes.jsf?faces-redirect=true#modal";
//        FacesContext.getCurrentInstance().getExternalContext().redirect("/EGC/sis/admin/solicitacoes.jsf#modal");
    }

    public int numeroDeFuncionarios() {
        return fachada.numeroDeFuncionarios();
    }

    public List<Funcionario> todosFuncionarios() {
        return fachada.todosFuncionarios();
    }

    public List<Denuncia> denunciasComReclamacoes() {
        return fachada.denunciasComReclamacoes();
    }

    public String selecionarDenunciaReclamacao(Denuncia denuncia) {
        this.denunciaReclamacao = new Denuncia();
        this.denunciaReclamacao = denuncia;

        return "gerenciar-reclamacao.jsf?faces-redirect=true";
    }

    public void bloquearDenuncia(Denuncia denuncia) {
        fachada.bloquearDenuncia(denuncia);
        this.denunciaReclamacao.setAtivo(false);
    }

    public void desbloquearDenuncia(Denuncia denuncia) {
        fachada.desbloquearDenuncia(denuncia);
        this.denunciaReclamacao.setAtivo(true);
    }

    public List<ConteudoInapropriado> comentariosDeConteudoInapropriadoEmDenuncia() {
        return fachada.comentariosDeConteudoInapropriadoEmDenuncia(this.denunciaReclamacao);
    }

    public String selecionarFuncionarioGerenciar(Funcionario funcionario) {
        this.gerenciarFuncionario = new Funcionario();
        this.gerenciarFuncionario = funcionario;
        return "perfil-funcionario.jsf?faces-redirect=true";
    }

    public String atualizarDadosFuncionario() {
        infoDadosAtualizados();
        fachada.atualizar(this.gerenciarFuncionario);
        return "perfil-funcionario.jsf?faces-redirect=true";
    }

    public List<Prefeitura> prefeiturasDoFuncionario(String email) {
        return fachada.prefeiturasDoFuncionario(email);
    }

    public void estarEmail() throws EmailException, MalformedURLException{
        enviarEmail("sergiodinizsh@gmail.com", "SergioDC", "Santa Helena - B", "", EmailType.BEM_VINDO_FUNCIONARIO);
    }
    
    public static String enviarEmail(String emailUsuario, String nomeUsuario, String prefeitura, String senha, EmailType emailType) throws EmailException, MalformedURLException {
        System.out.println("Preparando email: ");
        String eDestino = "sergiodinizsh@gmail.com";
        String eRemetente = "sergiodinizsh@gmail.com";
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

        
        //layout emails
        String layoutEmail = "";
        String bemVindoUsuario = "<html> <head> <meta charset=\"UTF-8\"> <style>*{font-family: sans-serif;}</style> </head> <body style=\"color: #666; font-size: 15px\"> <div align=\"center\" style=\"margin-top: 30px;\"> <img src=\"https://raw.githubusercontent.com/SergioDiniz/EGC/master/EGC/src/main/webapp/img/logo-azul.png\" alt=\"Logo EGC\"/> </div><div align=\"center\"> <h2 style=\"font-weight: 100\">Eficiência em Gestão de Cidades</h2> </div><div style=\"font-size: 20px; color: #fff; margin-top: 20px;padding: 15px 0 15px 0; background-color: #03a4f7; width: 100%; max-width: 600px; margin: 0 auto; text-align: center\"> BEM-VINDO! </div><div style=\"margin: 0 auto; margin-top: 50px; max-width: 500px\" align=\"center\"> Olá <b style=\"color: #03a4f7\">" + nomeUsuario + "</b> <br/><br/><br/> Seu cadastrado foi realizado com sucesso e você já pode começar a usar.<br/><br/><br/> <section style=\"margin-bottom: 8px\"><b>Parabéns</b> por se inscrever no <b>EGC</b>! <br/></section> Juntos vamos redefinir o processo de reclamações e criaremos cidades inteligentes! </div><div align=\"center\" style=\"margin-top: 40px\"> <img src=\"https://raw.githubusercontent.com/SergioDiniz/EGC/master/EGC/src/main/webapp/img/icon-parabens.png\" width=\"80\" alt=\"Logo EGC\"/> </div><div align=\"center\" style=\"margin-top: 80px; background-color: #f9f9f9; padding: 40px 0 40px 0\"> <div align=\"center\"> <img src=\"https://raw.githubusercontent.com/SergioDiniz/EGC/master/EGC/src/main/webapp/img/s-logo.png\" width=\"50\" alt=\"Logo EGC\"/> </div><br/> <br/> Em caso de <b>dúvidas</b>, entre em contato conosco através do email: <b style=\"color: #03a4f7\">contato@egc.com.br</b> <br/><br/> <i style=\"font-size: 12px\">Copyright © 2016 - EGC, Todos os direitos reservados.</i> </div></body></html>";
        String bemVindoFuncionario = "<html><body style=\"color: #666; font-size: 15px\"> <div align=\"center\" style=\"margin-top: 30px;\"> <img src=\"https://raw.githubusercontent.com/SergioDiniz/EGC/master/EGC/src/main/webapp/img/logo-azul.png\" alt=\"Logo EGC\"/> </div><div align=\"center\"> <h2 style=\"font-weight: 100\">Eficiência em Gestão de Cidades</h2> </div><div style=\"font-size: 20px; color: #fff; margin-top: 20px;padding: 15px 0 15px 0; background-color: #03a4f7; width: 100%; max-width: 600px; margin: 0 auto; text-align: center\"> BEM-VINDO! </div><div style=\"margin: 0 auto; margin-top: 50px; max-width: 500px\" align=\"center\"> Olá <b style=\"color: #03a4f7\">" + nomeUsuario + "</b> <br/><br/><br/> Seu cadastrado foi realizado com sucesso na <b>Prefeitura Municipal de " + prefeitura + "</b> e você já pode começar a usar.<br/><br/><br/> <section style=\"margin-bottom: 8px\"><b>Parabéns</b> por fazer parte do <b>EGC</b>! <br/></section> Juntos vamos redefinir o processo de reclamações e criaremos cidades inteligentes! </div><div align=\"center\" style=\"margin-top: 40px\"> <img src=\"https://raw.githubusercontent.com/SergioDiniz/EGC/master/EGC/src/main/webapp/img/icon-parabens.png\" width=\"80\" alt=\"Logo EGC\"/> </div><div align=\"center\" style=\"margin-top: 80px; background-color: #f9f9f9; padding: 40px 0 40px 0\"> <div align=\"center\"> <img src=\"https://raw.githubusercontent.com/SergioDiniz/EGC/master/EGC/src/main/webapp/img/s-logo.png\" width=\"50\" alt=\"Logo EGC\"/> </div><br/> <br/> Em caso de <b>dúvidas</b>, entre em contato conosco através do email: <b style=\"color: #03a4f7\">contato@egc.com.br</b> <br/><br/> <i style=\"font-size: 12px\">Copyright © 2016 - EGC, Todos os direitos reservados.</i> </div></body></html>";
        String bemVindoPrefeitura = "<html><body style=\"color: #666; font-size: 15px\"> <div align=\"center\" style=\"margin-top: 30px;\"> <img src=\"https://raw.githubusercontent.com/SergioDiniz/EGC/master/EGC/src/main/webapp/img/logo-azul.png\" alt=\"Logo EGC\"/> </div><div align=\"center\"> <h2 style=\"font-weight: 100\">Eficiência em Gestão de Cidades</h2> </div><div style=\"font-size: 20px; color: #fff; margin-top: 20px;padding: 15px 0 15px 0; background-color: #03a4f7; width: 100%; max-width: 600px; margin: 0 auto; text-align: center\"> BEM-VINDO! </div><div style=\"margin: 0 auto; margin-top: 50px; max-width: 500px\" align=\"center\"> Olá <b style=\"color: #03a4f7\">" + nomeUsuario + "</b> <br/><br/><br/> Seu cadastrado foi realizado com sucesso para a <b>Cidade de " + prefeitura + "</b>. Agora você precisa aguarda um pouco enquanto verificamos os seus dados. Fique atento, vamos te informar quando estiver tudo pronto.<br/><br/><br/> <section style=\"margin-bottom: 8px\"><b>Parabéns</b> por fazer parte do <b>EGC</b>! <br/></section> Juntos vamos redefinir o processo de reclamações e criaremos cidades inteligentes! </div><div align=\"center\" style=\"margin-top: 40px\"> <img src=\"https://raw.githubusercontent.com/SergioDiniz/EGC/master/EGC/src/main/webapp/img/icon-parabens.png\" width=\"80\" alt=\"Logo EGC\"/> </div><div align=\"center\" style=\"margin-top: 80px; background-color: #f9f9f9; padding: 40px 0 40px 0\"> <div align=\"center\"> <img src=\"https://raw.githubusercontent.com/SergioDiniz/EGC/master/EGC/src/main/webapp/img/s-logo.png\" width=\"50\" alt=\"Logo EGC\"/> </div><br/> <br/> Em caso de <b>dúvidas</b>, entre em contato conosco através do email: <b style=\"color: #03a4f7\">contato@egc.com.br</b> <br/><br/> <i style=\"font-size: 12px\">Copyright © 2016 - EGC, Todos os direitos reservados.</i> </div></body></html>";
        String aceitoPrefeitura = "<html> <body style=\"color: #666; font-size: 15px\"> <div align=\"center\" style=\"margin-top: 30px;\"> <img src=\"https://raw.githubusercontent.com/SergioDiniz/EGC/master/EGC/src/main/webapp/img/logo-azul.png\" alt=\"Logo EGC\"/> </div><div align=\"center\"> <h2 style=\"font-weight: 100\">Eficiência em Gestão de Cidades</h2> </div><div style=\"font-size: 20px; color: #fff; margin-top: 20px;padding: 15px 0 15px 0; background-color: #03a4f7; width: 100%; max-width: 600px; margin: 0 auto; text-align: center\"> BEM-VINDO! </div><div style=\"margin: 0 auto; margin-top: 50px; max-width: 500px\" align=\"center\"> Olá <b style=\"color: #03a4f7\">" + nomeUsuario + "</b> <br/><br/><br/> Após analise, o seu cadastrado foi confirmado com sucesso para a <b>Cidade de " + prefeitura + "</b>. e você já pode começar a usar.<br/><br/><br/> <section style=\"margin-bottom: 8px\"><b>Parabéns</b> por fazer parte do <b>EGC</b>! <br/></section> Juntos vamos redefinir o processo de reclamações e criaremos cidades inteligentes! </div><div align=\"center\" style=\"margin-top: 40px\"> <img src=\"https://raw.githubusercontent.com/SergioDiniz/EGC/master/EGC/src/main/webapp/img/icon-parabens.png\" width=\"80\" alt=\"Logo EGC\"/> </div><div align=\"center\" style=\"margin-top: 80px; background-color: #f9f9f9; padding: 40px 0 40px 0\"> <div align=\"center\"> <img src=\"https://raw.githubusercontent.com/SergioDiniz/EGC/master/EGC/src/main/webapp/img/s-logo.png\" width=\"50\" alt=\"Logo EGC\"/> </div><br/> <br/> Em caso de <b>dúvidas</b>, entre em contato conosco através do email: <b style=\"color: #03a4f7\">contato@egc.com.br</b> <br/><br/> <i style=\"font-size: 12px\">Copyright © 2016 - EGC, Todos os direitos reservados.</i> </div></body></html>";
        String recusoPrefeitura = "<html> <body style=\"color: #666; font-size: 15px\"> <div align=\"center\" style=\"margin-top: 30px;\"> <img src=\"https://raw.githubusercontent.com/SergioDiniz/EGC/master/EGC/src/main/webapp/img/logo-azul.png\" alt=\"Logo EGC\"/> </div><div align=\"center\"> <h2 style=\"font-weight: 100\">Eficiência em Gestão de Cidades</h2> </div><div style=\"font-size: 20px; color: #fff; margin-top: 20px;padding: 15px 0 15px 0; background-color: #03a4f7; width: 100%; max-width: 600px; margin: 0 auto; text-align: center\"> Ops! </div><div style=\"margin: 0 auto; margin-top: 50px; max-width: 500px\" align=\"center\"> Olá <b style=\"color: #03a4f7\">" + nomeUsuario + "</b> <br/><br/><br/> Após analise, não conseguimos confirmar seu dados para a <b>Cidade de " + prefeitura + "</b>. Entre em contato para mais informações.<br/><br/><br/> <section style=\"margin-bottom: 8px\">Faça parte do <b>EGC</b>! <br/></section> Juntos vamos redefinir o processo de reclamações e criaremos cidades inteligentes! </div><div align=\"center\" style=\"margin-top: 40px\"> <img src=\"https://raw.githubusercontent.com/SergioDiniz/EGC/master/EGC/src/main/webapp/img/icon-parabens.png\" width=\"80\" alt=\"Logo EGC\"/> </div><div align=\"center\" style=\"margin-top: 80px; background-color: #f9f9f9; padding: 40px 0 40px 0\"> <div align=\"center\"> <img src=\"https://raw.githubusercontent.com/SergioDiniz/EGC/master/EGC/src/main/webapp/img/s-logo.png\" width=\"50\" alt=\"Logo EGC\"/> </div><br/> <br/> Em caso de <b>dúvidas</b>, entre em contato conosco através do email: <b style=\"color: #03a4f7\">contato@egc.com.br</b> <br/><br/> <i style=\"font-size: 12px\">Copyright © 2016 - EGC, Todos os direitos reservados.</i> </div></body></html>";
        String recuperarSenha = "<html> <body style=\"color: #666; font-size: 15px\"> <div align=\"center\" style=\"margin-top: 30px;\"> <img src=\"https://raw.githubusercontent.com/SergioDiniz/EGC/master/EGC/src/main/webapp/img/logo-azul.png\" alt=\"Logo EGC\"/> </div><div align=\"center\"> <h2 style=\"font-weight: 100\">Eficiência em Gestão de Cidades</h2> </div><div style=\"font-size: 20px; color: #fff; margin-top: 20px;padding: 15px 0 15px 0; background-color: #03a4f7; width: 100%; max-width: 600px; margin: 0 auto; text-align: center\"> ALGUM PROBLEMA? </div><div style=\"margin: 0 auto; margin-top: 50px; max-width: 500px\" align=\"center\"> Olá <b style=\"color: #03a4f7\">" + nomeUsuario + "</b> <br/><br/><br/> Nós recebemos uma solicitação e parece que você <b>esqueceu sua senha</b>. Então estamos te enviando os seus dados. Caso não tenha sido você, aconselhamos mudar sua senha. <br/><br/> <b>email:</b> "+ emailUsuario +" <br/> <b>senha:</b> " + senha + " <br/><br/><br/> <section style=\"margin-bottom: 8px\"><b>Obrigado</b> por fazer parte do <b>EGC</b>! <br/></section> Juntos vamos redefinir o processo de reclamações e criaremos cidades inteligentes! </div><div align=\"center\" style=\"margin-top: 40px\"> <img src=\"https://raw.githubusercontent.com/SergioDiniz/EGC/master/EGC/src/main/webapp/img/icon-parabens.png\" width=\"80\" alt=\"Logo EGC\"/> </div><div align=\"center\" style=\"margin-top: 80px; background-color: #f9f9f9; padding: 40px 0 40px 0\"> <div align=\"center\"> <img src=\"https://raw.githubusercontent.com/SergioDiniz/EGC/master/EGC/src/main/webapp/img/s-logo.png\" width=\"50\" alt=\"Logo EGC\"/> </div><br/> <br/> Em caso de <b>dúvidas</b>, entre em contato conosco através do email: <b style=\"color: #03a4f7\">contato@egc.com.br</b> <br/><br/> <i style=\"font-size: 12px\">Copyright © 2016 - EGC, Todos os direitos reservados.</i> </div></body></html>";
        switch(emailType){
            case BEM_VINDO_USUARIO: layoutEmail = bemVindoUsuario; break;
            case BEM_VINDO_FUNCIONARIO: layoutEmail = bemVindoFuncionario; break;
            case BEM_VINDO_PREFEITURA: layoutEmail = bemVindoPrefeitura; break;
            case ACEITO_PREFEITURA: layoutEmail = aceitoPrefeitura; break;
            case RECUSO_PREFEITURA: layoutEmail = recusoPrefeitura; break;
            case RECUPERAR_SENHA: layoutEmail = recuperarSenha; break;
        }
        

        // configura a mensagem para o formato HTML
        email.setHtmlMsg(layoutEmail);

        // configure uma mensagem alternativa caso o servidor não suporte HTML
        email.setTextMsg("Seu servidor de e-mail não suporta mensagem HTML");

        System.out.println("Tentando enviar: ");
        email.send();

        System.out.println("Email enviado. ");
        return null;
    }

    //getes and seters
    public Administrador getAdministrador() {
        return administrador;
    }

    public void setAdministrador(Administrador administrador) {
        this.administrador = administrador;
    }

    public Prefeitura getPrefeituraAx() {
        return prefeituraAx;
    }

    public void setPrefeituraAx(Prefeitura prefeituraAx) {
        this.prefeituraAx = prefeituraAx;
    }

    public boolean isMostrarModalRecusar() {
        return mostrarModalRecusar;
    }

    public void setMostrarModalRecusar(boolean mostrarModalRecusar) {
        this.mostrarModalRecusar = mostrarModalRecusar;
    }

    public boolean isMostrarModalAceitar() {
        return mostrarModalAceitar;
    }

    public void setMostrarModalAceitar(boolean mostrarModalAceitar) {
        this.mostrarModalAceitar = mostrarModalAceitar;
    }

    public Denuncia getDenunciaReclamacao() {
        return denunciaReclamacao;
    }

    public void setDenunciaReclamacao(Denuncia denunciaReclamacao) {
        this.denunciaReclamacao = denunciaReclamacao;
    }

    public Funcionario getGerenciarFuncionario() {
        return gerenciarFuncionario;
    }

    public void setGerenciarFuncionario(Funcionario gerenciarFuncionario) {
        this.gerenciarFuncionario = gerenciarFuncionario;
    }

}
