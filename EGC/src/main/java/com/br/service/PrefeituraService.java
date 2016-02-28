/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.br.service;

import com.br.beans.Denuncia;
import com.br.beans.Funcionario;
import com.br.beans.MensagemPrefeitura;
import com.br.beans.Prefeitura;
import com.br.beans.Registro;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author SergioD
 */
@Stateless
@Remote(PrefeituraServiceIT.class)
public class PrefeituraService implements PrefeituraServiceIT {

    @PersistenceContext(unitName = "jdbc/EGC")
    private EntityManager em;

    @Override
    public String cadastrarNaPrefeitura(Prefeitura prefeitura, Funcionario funcionario) {
        try {
            prefeitura.getFuncionarios().add(funcionario);
            em.merge(prefeitura);
            return "Cadastrado com Sucesso";

        } catch (Exception e) {
        }

        return "ERRO!";
    }

    @Override
    public Prefeitura pesquisarPrefeituraPorCidade(String cidade, String estado) {

        try {

            Query query = em.createQuery("SELECT p FROM Prefeitura p WHERE p.cidade.CidadePK.nomeCidade = :cidade AND p.cidade.CidadePK.siglaEstado = :estado");
            query.setParameter("cidade", cidade);
            query.setParameter("estado", estado);

            List<Prefeitura> p = query.getResultList();

            if (p.size() > 0) {
                return p.get(0);
            }

        } catch (Exception e) {
            System.out.println("ERRO EM PESQUISAR PREFEITURA POR CIDADE: " + e.getMessage());
        }

        return null;

    }
    
    @Override
    public Prefeitura prefeituraPorEmail(String email) {

        try {

            Query query = em.createQuery("SELECT p FROM Prefeitura p WHERE p.email = :email");
            query.setParameter("email", email);

            List<Prefeitura> p = query.getResultList();

            if (p.size() > 0) {
                return p.get(0);
            }

        } catch (Exception e) {
            System.out.println("ERRO EM PESQUISAR PREFEITURA POR EMAIL: " + e.getMessage());
        }

        return null;

    }

    @Override
    public Prefeitura pesquisarPrefeituraPorCodigo(String codigo) {
        try {

            Query query = em.createQuery("SELECT p FROM Prefeitura p WHERE p.codigo = :codigo");
            query.setParameter("codigo", codigo);

            List<Prefeitura> p = query.getResultList();

            if (p.size() > 0) {
                return p.get(0);
            }

        } catch (Exception e) {
            System.out.println("ERRO EM PESQUISAR PREFEITURA POR CODIGO: " + e.getMessage());
        }

        return null;
    }

    @Override
    public Long totalDePrefeitura() {
        Query query = em.createQuery("SELECT COUNT(p) FROM Prefeitura P");
        List<Long> d = query.getResultList();

        return d.get(0);

    }

    @Override
    public Prefeitura login(String email, String senha) {
        Query query = em.createQuery("SELECT p FROM Prefeitura p WHERE p.email = :email AND p.senha = :senha ");
        query.setParameter("email", email);
        query.setParameter("senha", senha);

        List<Prefeitura> p = query.getResultList();

        if (p.size() > 0) {
            p.get(0).getFuncionarios().size();
            return p.get(0);
        }

        return null;
    }

    @Override
    public List<Prefeitura> prefeiturasPendentes() {
        Query query = em.createQuery("SELECT p from Prefeitura p WHERE p.ativo = false");
        List<Prefeitura> prefeituras = query.getResultList();

        if (prefeituras.size() > 0) {
            return prefeituras;
        }

        return null;
    }

    @Override
    public Funcionario pesquisarFuncionario(Prefeitura prefeitura, String cpf) {
        Query query = em.createQuery("SELECT f FROM Prefeitura p JOIN p.funcionarios f WHERE p.email = :pEmail AND f.cpf = :fCpf");
        query.setParameter("pEmail", prefeitura.getEmail());
        query.setParameter("fCpf", cpf);

        List<Funcionario> pf = query.getResultList();

        if (pf.size() > 0) {
            pf.get(0).getPrefeituras().size();
            return pf.get(0);
        }

        return null;
    }

    @Override
    public String vincular(Prefeitura prefeitura, Funcionario funcionario) {

        Query query = em.createQuery("SELECT f FROM Prefeitura p JOIN p.funcionarios f WHERE p.email = :pEmail AND f.cpf = :fCpf");
        query.setParameter("pEmail", prefeitura.getEmail());
        query.setParameter("fCpf", funcionario.getCpf());

        List fs = query.getResultList();

        if (fs.size() > 0) {
            return "Funcionario ja esta vinculado na prefeitura!";
        } else {
            cadastrarNaPrefeitura(prefeitura, funcionario);
            return "Vinculado com Sucesso!";
        }

//        return "ERRO!";
    }

    @Override
    public String desvincular(Prefeitura prefeitura, String cpf) {
        Funcionario funcionario = pesquisarFuncionario(prefeitura, cpf);

        if (funcionario != null) {

            prefeitura.getFuncionarios().remove(funcionario);
            funcionario.getPrefeituras().remove(prefeitura);

            try {
                em.merge(prefeitura);
                em.merge(funcionario);
            } catch (Exception e) {
            }

            return "Removido";

        } else {
            return "Funcionario não vinculado com a prefeitura.";
        }

//        return "ERRO!";
    }

    @Override
    public List<Funcionario> funcionarios(Prefeitura prefeitura) {
        Query query = em.createQuery("SELECT f FROM Prefeitura p JOIN p.funcionarios f WHERE p.id = :id ORDER BY f.nome ASC");
        query.setParameter("id", prefeitura.getId());

        return query.getResultList();

    }

    @Override
    public long totalDeFuncionariosNaPrefeitura(String email) {
        Query query = em.createQuery("SELECT COUNT(f) FROM Prefeitura p JOIN p.funcionarios f WHERE p.email = :email");
        query.setParameter("email", email);

        List f = query.getResultList();

        if (f.size() > 0) {
            return (long) f.get(0);
        }

        return 0;
    }

    @Override
    public List<Long> dadosGeraisPrefeitura(String emailPrefeitura, String cidade, String estado) {
        long funcionarios = totalDeFuncionariosNaPrefeitura(emailPrefeitura);
        long usuarios = new CidadeService().totalDeUsuariosNaCidade(cidade, estado);
        long denuncias = new DenunciaService().totalDeDenunciasNaCidade(cidade, estado);
        long denunciasAtendidas = new DenunciaService().totalDeDenunciasAtendidasNaCidade(cidade, estado);

        List<Long> dados = new ArrayList<>();
        dados.add(0, denuncias);
        dados.add(1, denunciasAtendidas);
        dados.add(2, usuarios);
        dados.add(3, funcionarios);

        for (Long dado : dados) {
            System.out.println(dado);
        }

        return dados;
    }

    @Override
    public boolean prefeituraCadastrada(String cidade, String estado) {

        Query query = em.createQuery("SELECT p FROM Prefeitura p WHERE p.cidade.CidadePK.nomeCidade = :cidade and p.cidade.CidadePK.siglaEstado = :estado and p.ativo = TRUE");
        query.setParameter("cidade", cidade);
        query.setParameter("estado", estado);

        List<Prefeitura> p = query.getResultList();

        if (p.size() > 0) {
            return true;
        }

        return false;
    }

    @Override
    public List<Registro> registroDaPrefeitura(String codigoPrefeitura) {

        Query query = em.createQuery("SELECT r FROM Registro r JOIN r.denuncia d WHERE r.prefeitura.codigo = :codigo ORDER BY r.data DESC");
        query.setParameter("codigo", codigoPrefeitura);

        List<Registro> r = query.getResultList();

        if (r.size() > 0) {
            return r;
        }

        return new ArrayList<>();
    }

    @Override
    public List<Denuncia> denunciaDaPrefeitura(String codigoPrefeitura) {

        Query query = em.createQuery("SELECT d FROM Prefeitura p JOIN p.cidade c JOIN c.denuncias d WHERE p.codigo = :codigo ORDER BY d.data DESC");
        query.setParameter("codigo", codigoPrefeitura);

        List<Denuncia> d = query.getResultList();

        if (d.size() > 0) {
            return d;
        }

        return new ArrayList<>();
    }

    @Override
    public List<Funcionario> funcionarioDaPrefeitura(String codigoPrefeitura) {

        Query query = em.createQuery("SELECT f FROM Prefeitura p JOIN p.funcionarios f WHERE p.codigo = :codigo ORDER BY f.nome ASC");
        query.setParameter("codigo", codigoPrefeitura);

        List<Funcionario> f = query.getResultList();

        if (f.size() > 0) {
            return f;
        }

        return new ArrayList<>();
    }

    @Override
    public boolean novaMensagemEmPrefeitura(MensagemPrefeitura mensagemPrefeitura, Prefeitura prefeitura) {

        try {
            prefeitura = em.find(Prefeitura.class, prefeitura.getId());

            em.persist(mensagemPrefeitura);

            prefeitura.getMensagensPrefeitura().add(mensagemPrefeitura);
            em.merge(prefeitura);

            return true;

        } catch (Exception e) {
            System.out.println("ERRO EM GRAÇAR MENSAGEM EM PREFEITURA: " + e.getMessage());
        }

        return false;
    }

    @Override
    public List<MensagemPrefeitura> mensagensDaPrefeitura(String codigoPrefeitura) {
        Query query = em.createQuery("SELECT m FROM Prefeitura p JOIN p.mensagensPrefeitura m WHERE p.codigo = :codigo ORDER BY m.dataMensagem DESC");
        query.setParameter("codigo", codigoPrefeitura);

        List<MensagemPrefeitura> m = query.getResultList();

        if (m.size() > 0) {
            return m;
        }

        return new ArrayList<>();
    }

}
