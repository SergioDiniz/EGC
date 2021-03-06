/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.br.service;

import com.br.beans.ConteudoInapropriado;
import com.br.beans.Denuncia;
import com.br.beans.EnderecoDenuncia;
import com.br.beans.EstadoDeAcompanhamento;
import com.br.beans.Funcionario;
import com.br.beans.InformacaoDeAtendida;
import com.br.beans.MensagemPrefeitura;
import com.br.beans.Prefeitura;
import com.br.beans.Registro;
import com.br.beans.TipoDeDenuncia;
import com.br.beans.Usuario;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Iterator;
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
@Remote(DenunciaServiceIT.class)
public class DenunciaService implements DenunciaServiceIT {

    @PersistenceContext(unitName = "jdbc/EGC")
    private EntityManager em;

    @Override
    public String novaDenuncia(Usuario usuario, EnderecoDenuncia enderecoDenuncia, String denucia, String foto, TipoDeDenuncia tipoDeDenuncia) {

        try {
            Denuncia d = new Denuncia(denucia, foto, enderecoDenuncia, tipoDeDenuncia);
            d.setCodigo(String.valueOf(totalDeDenuncias() + 1));

            usuario = em.find(Usuario.class, usuario.getId());

            usuario.getDenuncias().size();
            usuario.getDenuncias().add(d);

//            usuario.novaDenuncia(d);
            em.persist(enderecoDenuncia);
            em.persist(d);
            em.merge(usuario);

            return "ok";
        } catch (Exception e) {
        }

        return "ERRO";
    }

    @Override
    public Long totalDeDenuncias() {
        Query query = em.createQuery("SELECT COUNT(d) FROM Denuncia d");
        List<Long> d = query.getResultList();

        return d.get(0);
    }

    @Override
    public List<Denuncia> pesquisarPorCidade(String cidade, String estado, String ordem) {

        Query query;

//        String sql = "SELECT d.* FROM denuncia d LEFT OUTER JOIN enderecodenuncia ed on d.enderecodenuncia_id = ed.id WHERE upper(TRANSLATE(d.nomecidade,'ÀÁáàÉÈéèÍíÓóÒòÚú','AAaaEEeeIiOoOoUu')) = ?1 AND upper(TRANSLATE(d.siglaestado,'ÀÁáàÉÈéèÍíÓóÒòÚú','AAaaEEeeIiOoOoUu')) = ?2 ORDER BY d.data DESC";
        if (ordem.equalsIgnoreCase("data")) {
//            query = em.createNativeQuery(sql);
            query = em.createQuery("SELECT d FROM Denuncia d WHERE d.ativo = true AND d.cidade.CidadePK.nomeCidade = :cidade AND d.cidade.CidadePK.siglaEstado = :estado AND d.ativo = TRUE ORDER BY d.data DESC");
//            query = em.createNativeQuery(sql, Denuncia.class);
        } else {
            query = em.createQuery("SELECT d FROM Denuncia d WHERE d.ativo = true AND d.cidade.CidadePK.nomeCidade = :cidade AND d.cidade.CidadePK.siglaEstado = :estado AND d.ativo = TRUE ORDER BY d.data ASC");
        }

        query.setParameter("cidade", cidade);
        query.setParameter("estado", estado);

        List d = query.getResultList();

        if (d.size() > 0) {
            return d;
        }

        return new ArrayList<>();
    }

    @Override
    public List<Denuncia> pesquisarPorCidadeComFiltro(String cidade, String estado, TipoDeDenuncia tipoDeDenuncia, String ordem) {

        if (cidade.equals("Brasil")) {
            return pesquisarNoBrasil(cidade, tipoDeDenuncia, ordem);
        }

        Query query;

        if (ordem.equals("data")) {
            query = em.createQuery("SELECT d FROM Denuncia d WHERE d.ativo = true AND d.cidade.CidadePK.nomeCidade = :cidade AND d.cidade.CidadePK.siglaEstado = :estado AND d.tipoDeDenuncia = :tipo ORDER BY d.data DESC");
        } else {
            query = em.createQuery("SELECT d FROM Denuncia d WHERE d.ativo = true AND d.cidade.CidadePK.nomeCidade = :cidade AND d.cidade.CidadePK.siglaEstado = :estado AND d.tipoDeDenuncia = :tipo ORDER BY d.data ASC");
        }

        query.setParameter("cidade", cidade);
        query.setParameter("estado", estado);
        query.setParameter("tipo", tipoDeDenuncia);

        List d = query.getResultList();

        if (d.size() > 0) {
            return d;
        }

        return new ArrayList<>();

    }

    public List<Denuncia> pesquisarNoBrasil(String cidade, TipoDeDenuncia tipoDeDenuncia, String ordem) {

//        if (tipoDeDenuncia == TipoDeDenuncia.BRASIL){
//            tipoDeDenuncia = TipoDeDenuncia.TODOS;
//        }
        System.out.println("Ordem: " + ordem);
        System.out.println("Tipo de Denuncia: " + tipoDeDenuncia);

        Query query;
        if (ordem.equals("data")) {
            if (tipoDeDenuncia == TipoDeDenuncia.BRASIL) {
                query = em.createQuery("SELECT d FROM Denuncia d JOIN d.enderecoDenuncia ed WHERE d.ativo = true AND ed.pais = 'Brazil' ORDER BY d.data DESC");
            } else {
                query = em.createQuery("SELECT d FROM Denuncia d JOIN d.enderecoDenuncia ed WHERE d.ativo = true AND ed.pais = 'Brazil' AND d.tipoDeDenuncia = :tipo ORDER BY d.data DESC");
                query.setParameter("tipo", tipoDeDenuncia);
            }
        } else {
            if (tipoDeDenuncia == TipoDeDenuncia.BRASIL) {
                query = em.createQuery("SELECT d FROM Denuncia d JOIN d.enderecoDenuncia ed WHERE d.ativo = true AND ed.pais = 'Brazil' ORDER BY d.data ASC");
            } else {
                query = em.createQuery("SELECT d FROM Denuncia d JOIN d.enderecoDenuncia ed WHERE d.ativo = true AND ed.pais = 'Brazil' AND d.tipoDeDenuncia = :tipo ORDER BY d.data ASC");
                query.setParameter("tipo", tipoDeDenuncia);
            }
        }

        List d = query.getResultList();

        if (d.size() > 0) {
            return d;
        }

        return new ArrayList<>();

    }

    @Override
    public long totalDeDenunciasNaCidade(String cidade, String estado) {
        Query query;

        if (cidade.equals("Brasil")) {
            query = em.createQuery("SELECT COUNT(d) FROM Denuncia d JOIN d.enderecoDenuncia ed WHERE d.ativo = true AND ed.pais = 'Brazil'");
        } else {
            query = em.createQuery("SELECT COUNT(d) FROM Denuncia d WHERE d.ativo = true AND d.cidade.CidadePK.nomeCidade = :cidade AND d.cidade.CidadePK.siglaEstado = :estado");
            query.setParameter("cidade", cidade);
            query.setParameter("estado", estado);
        }

        List d = query.getResultList();

        if (d.size() > 0) {
            return (long) d.get(0);
        }
        return 0;

    }

    @Override
    public long totalDeDenunciasAtendidasNaCidade(String cidade, String estado) {
        Query query;

        if (cidade.equals("Brasil")) {
            query = em.createQuery("SELECT COUNT(d) FROM Denuncia d JOIN d.enderecoDenuncia ed JOIN d.informacaoDeAtendida ia WHERE d.ativo = true AND ed.pais = 'Brazil' AND ia.id > -1 ");
        } else {
            query = em.createQuery("SELECT COUNT(d) FROM Denuncia d JOIN d.informacaoDeAtendida ia WHERE d.ativo = true AND d.cidade.CidadePK.nomeCidade = :cidade AND d.cidade.CidadePK.siglaEstado = :estado AND ia.id > -1");
            query.setParameter("cidade", cidade);
            query.setParameter("estado", estado);
        }

        List d = query.getResultList();

        if (d.size() > 0) {
            return (long) d.get(0);
        }

        return 0;

    }

    public static String removerAcento(String str) {
        str = Normalizer.normalize(str, Normalizer.Form.NFD);
        str = str.replaceAll("[^\\p{ASCII}]", "");
        return str.toUpperCase();
    }

    @Override
    public boolean setAjudarDenuncia(Denuncia denuncia, Usuario usuario) {

        System.out.println("Denuncia ID DS: " + denuncia.getId());

        if (!usuario.getDenunciasAjudadas().contains(denuncia)) {
            int valor = denuncia.getNumeroAjuda();
            denuncia.setNumeroAjuda(++valor);

            usuario.getDenunciasAjudadas().add(denuncia);
            em.merge(denuncia);
            em.merge(usuario);
            return true;

        }
        return false;

    }

    @Override
    public int getAjudarDenuncia(Denuncia denuncia) {
        Query query = em.createQuery("SELECT d.numeroAjuda FROM Denuncia d WHERE d.id = :id");
        query.setParameter("id", denuncia.getId());

        int valor = (int) query.getSingleResult();

        return valor;
    }

    @Override
    public boolean setReclamarDenuncia(Denuncia denuncia, ConteudoInapropriado conteudoInapropriado) {

        try {
            denuncia.getConteudoInapropriados().size();
            denuncia.getConteudoInapropriados().add(conteudoInapropriado);
            em.merge(conteudoInapropriado);
            em.merge(denuncia);

            return true;
        } catch (Exception e) {
            System.out.println("erro ao atualizar reclamação de denunica: " + e.getMessage());
        }

        return false;
    }

    @Override
    public long getReclamarDenuncia(Denuncia denuncia) {
        Query query = em.createQuery("SELECT COUNT(c) from Denuncia d JOIN d.conteudoInapropriados c WHERE d.id = :id");
        query.setParameter("id", denuncia.getId());

        List e = query.getResultList();

        if (e.size() > 0) {
            return (long) e.get(0);
        }

        return 0;
    }

    @Override
    public List<Denuncia> denunciasComMaisAjudasPorCidade(String cidade, String estado) {
        Query query = em.createQuery("SELECT d from Denuncia d JOIN d.enderecoDenuncia ed WHERE d.ativo = true AND ed.cidade = :cidade and ed.estado = :estado ORDER BY d.numeroAjuda DESC");
        query.setParameter("cidade", cidade);
        query.setParameter("estado", estado);
        query.setMaxResults(6);

        List<Denuncia> r = query.getResultList();

        if (r.size() > 0) {
            return r;
        }

        return new ArrayList<>();

    }

    @Override
    public List<Denuncia> denunciasMaisRecentesPorCidade(String cidade, String estado) {
        Query query = em.createQuery("SELECT d from Denuncia d JOIN d.enderecoDenuncia ed WHERE d.ativo = true AND ed.cidade = :cidade and ed.estado = :estado ORDER BY d.data DESC");
        query.setParameter("cidade", cidade);
        query.setParameter("estado", estado);
        query.setMaxResults(6);

        List<Denuncia> r = query.getResultList();

        if (r.size() > 0) {
            return r;
        }

        return new ArrayList<>();
    }

    @Override
    public List<Denuncia> denunciasComReclamacoes() {

        Query query = em.createQuery("SELECT DISTINCT d FROM Denuncia d INNER JOIN d.conteudoInapropriados c");

        List<Denuncia> d = query.getResultList();

        if (d.size() > 0) {
            return d;
        }

        return new ArrayList<>();
    }

    @Override
    public List<ConteudoInapropriado> comentariosDeConteudoInapropriadoEmDenuncia(Denuncia denuncia) {

        Query query = em.createQuery("SELECT c from Denuncia d JOIN d.conteudoInapropriados c WHERE d.id = :id");
        query.setParameter("id", denuncia.getId());

        List<ConteudoInapropriado> c = query.getResultList();

        if (c.size() > 0) {
            return c;
        }

        return new ArrayList<>();
    }

    @Override
    public Denuncia pesquisarDenunicaCodigo(String codigo) {

        Query query = em.createQuery("SELECT d from Denuncia d WHERE d.codigo = :codigo and d.ativo = true");
        query.setParameter("codigo", codigo);

        List<Denuncia> d = query.getResultList();

        if (d.size() > 0) {
            d.get(0).getRegistros().size();
            return d.get(0);
        }

        return null;

    }

    @Override
    public boolean atualizarDenunciaGerenciada(Registro registro) {

        try {

            em.merge(registro.getDenuncia());
            em.merge(registro);
            return true;

        } catch (Exception e) {
            System.out.println("ERRO AO ATUALIZAR DENUNCIA GERENCIADA: " + e.getMessage());
        }

        return false;
    }

    @Override
    public List<Registro> registroDeUmaDenuncia(String codigoDenuncia) {

        try {

            Query query = em.createQuery("SELECT r from Denuncia d JOIN d.registros r WHERE d.codigo = :codigoDenuncia ORDER BY r.data DESC");
            query.setParameter("codigoDenuncia", codigoDenuncia);

            List<Registro> r = query.getResultList();

            if (r.size() > 0) {
                return r;
            }

        } catch (Exception e) {
            System.out.println("ERRO EM RETORNA REGISTRO DE UMA DENUNCIA: " + e.getMessage());
        }

        return new ArrayList<>();

    }

    @Override
    public boolean atenderDenuncia(InformacaoDeAtendida informacaoDeAtendida, Registro registro) {

        try {

            registro.getDenuncia().setInformacaoDeAtendida(informacaoDeAtendida);

            em.merge(informacaoDeAtendida);
            em.merge(registro.getDenuncia());
            em.merge(registro);

        } catch (Exception e) {
            System.out.println("ERRO AO ATENDER DENUNCIA: " + e.getMessage());
        }

        return false;
    }

    @Override
    public List<Denuncia> gerenciarDenunciasFiltro(String cidade, String estado, String ordem, String filtroQuery, String filtro) {

        //ordem
        String sqlOrdem = "";
        switch (ordem) {
            case "DATA_DESC":
                sqlOrdem = " ORDER BY d.data DESC";
                break;
            case "DATA_ASC":
                sqlOrdem = " ORDER BY d.data ASC";
                break;
            case "AJUDA_DESC":
                sqlOrdem = " ORDER BY d.numeroAjuda DESC";
                break;
            case "AJUDA_ASC":
                sqlOrdem = " ORDER BY d.numeroAjuda ASC";
                break;
            default:
                sqlOrdem = " ORDER BY d.data DESC";
                break;
        }

        // filtro
        String sqlFiltro = "";
        switch (filtro) {
            case "ESTADO":
                sqlFiltro = " and d.estadoDeAcompanhamento = :filtroQuery";
                filtroQuery = filtroQuery.replaceAll(" ", "_"); //substituindo " " por "_"
                break;
            case "CATEGORIA":
                sqlFiltro = " and d.tipoDeDenuncia = :filtroQuery";
                filtroQuery = filtroQuery.replaceAll(" ", "_"); //substituindo " " por "_"
//                            filtroQuery = TipoDeDenuncia.valueOf(filtroQuery);
                break;
            case "RUA":
                sqlFiltro = " and ed.rua = :filtroQuery";
                break;
            case "CEP":
                sqlFiltro = " and ed.cep = :filtroQuery";
                break;
        }

        System.out.println("ordem: " + ordem);
        System.out.println("SQL Ordem: " + sqlOrdem);
        System.out.println("Filto: " + filtro);
        System.out.println("SQL Filtro: " + sqlFiltro);
        System.out.println("Filtro Query: " + filtroQuery);

        Query query = em.createQuery("SELECT d from Denuncia d JOIN d.enderecoDenuncia ed WHERE ed.cidade = :cidade AND ed.estado = :estado " + sqlFiltro + sqlOrdem);
        query.setParameter("cidade", cidade);
        query.setParameter("estado", estado);
        if (sqlFiltro.length() > 2) {
            switch (filtro) {
                case "CATEGORIA":
                    query.setParameter("filtroQuery", TipoDeDenuncia.valueOf(filtroQuery));
                    break;
                case "ESTADO":
                    query.setParameter("filtroQuery", EstadoDeAcompanhamento.valueOf(filtroQuery));
                    break;
                default:
                    query.setParameter("filtroQuery", filtroQuery);
                    break;
            }
        }

        List<Denuncia> d = query.getResultList();

        if (d.size() > 0) {
            return d;
        }

        return new ArrayList<>();
    }

    @Override
    public List<Denuncia> denunciasAtendidasEmCidade(String cidade, String estado) {

        Query query = em.createQuery("SELECT d from Denuncia d JOIN d.enderecoDenuncia ed JOIN d.informacaoDeAtendida ia WHERE ed.cidade = :cidade and ed.estado = :estado ORDER BY ia.data DESC");
        query.setParameter("cidade", cidade);
        query.setParameter("estado", estado);

        List<Denuncia> d = query.getResultList();

        if (d.size() > 0) {
            return d;
        }

        return new ArrayList<>();

    }

    @Override
    public List<Denuncia> denunciasNaoAtendidasEmCidade(String cidade, String estado) {

        Query query = em.createQuery("SELECT d from Denuncia d JOIN d.enderecoDenuncia ed  WHERE ed.cidade = :cidade and ed.estado = :estado and d.estadoDeAcompanhamento = :atendida or d.estadoDeAcompanhamento = :trabalho ");
        query.setParameter("cidade", cidade);
        query.setParameter("estado", estado);
        query.setParameter("atendida", EstadoDeAcompanhamento.valueOf("ATENDIDA"));
        query.setParameter("trabalho", EstadoDeAcompanhamento.valueOf("EM_TRABALHO"));
        List<Denuncia> d = query.getResultList();

        if (d.size() > 0) {
            return d;
        }

        return new ArrayList<>();

    }

    @Override
    public List<List> ruasDeUmaCidadeTipoDenunciaNumerosDeDenuncia(String cidade, String estado) {
        System.out.println("Cidade: " + cidade);
        System.out.println("Estado: " + estado);
        //Query query = em.createQuery("SELECT d.enderecoDenuncia.rua, COUNT(d.enderecoDenuncia.rua) FROM Cidade c JOIN c.denuncias d WHERE c.CidadePK.nomeCidade = :cidade and c.CidadePK.siglaEstado = :estado GROUP BY d.enderecoDenuncia.rua ORDER BY d.enderecoDenuncia.rua ASC");
        Query query = em.createQuery("SELECT ed.rua, d.tipoDeDenuncia, COUNT(d.tipoDeDenuncia) FROM Cidade c JOIN c.denuncias d JOIN d.enderecoDenuncia ed WHERE c.CidadePK.nomeCidade = :cidade and c.CidadePK.siglaEstado = :estado GROUP BY ed.rua, d.tipoDeDenuncia ORDER BY ed.rua");
        query.setParameter("cidade", cidade);
        query.setParameter("estado", estado);

        List resultado = query.getResultList();

        List<List> ruas = new ArrayList<>();
        Iterator i = resultado.iterator();

        while (i.hasNext()) {
            Object[] r = (Object[]) i.next();

            List l = new ArrayList();
            l.add(r[0]);
            String tipo = (String) r[1].toString();
            l.add(tipo.replaceAll("_", " "));
            l.add(r[2]);

            ruas.add(l);

        }

        if (ruas.size() > 0) {
            return ruas;
        }

        return new ArrayList<>();

    }

    public Long totalDeDenunciasAtendidasPorTipoERua(String cidade, String estado, String rua, String tipoDeDenuncia) {
        Query query = em.createQuery("SELECT COUNT(d.tipoDeDenuncia) FROM Cidade c JOIN c.denuncias d JOIN d.enderecoDenuncia ed WHERE c.CidadePK.nomeCidade = :cidade AND c.CidadePK.siglaEstado = :estado AND ed.rua = :rua and d.tipoDeDenuncia = :tipo and d.estadoDeAcompanhamento = :atendida");
        query.setParameter("cidade", cidade);
        query.setParameter("estado", estado);
        query.setParameter("rua", rua);

        String tipo = tipoDeDenuncia.replaceAll(" ", "_");
        query.setParameter("tipo", TipoDeDenuncia.valueOf(tipo));

        query.setParameter("atendida", EstadoDeAcompanhamento.valueOf("ATENDIDA"));

        List<Long> d = query.getResultList();

        return d.get(0);
    }

    @Override
    public List<MensagemPrefeitura> denunciasPorPrefeituraLimitado(String codigo, int limite) {

        Query query = em.createQuery("SELECT m from Prefeitura p JOIN p.mensagensPrefeitura m WHERE p.codigo = :codigo ORDER BY m.dataMensagem DESC");
        query.setParameter("codigo", codigo);
        query.setMaxResults(4);

        List<MensagemPrefeitura> d = query.getResultList();

        if (d.size() > 0) {
            return d;
        }

        return new ArrayList<>();
    }

    @Override
    public List<List> denunciasRealizadasPorMesChart(String codigo) {

        
        
        String q = "SELECT "
                + "case "
                + "when to_char(d.data, 'MM')  =  '01' then 'Janeiro' "
                + "when to_char(d.data, 'MM')  =  '02' then 'Fevereiro' "
                + "when to_char(d.data, 'MM')  =  '03' then 'Março' "
                + "when to_char(d.data, 'MM')  =  '04' then 'Abril' "
                + "when to_char(d.data, 'MM')  =  '05' then 'Maio' "
                + "when to_char(d.data, 'MM')  =  '06' then 'Junho' "
                + "when to_char(d.data, 'MM')  =  '07' then 'Julho' "
                + "when to_char(d.data, 'MM')  =  '08' then 'Agosto' "
                + "when to_char(d.data, 'MM')  =  '09' then 'Setembro' "
                + "when to_char(d.data, 'MM')  =  '10' then 'Outubro' "
                + "when to_char(d.data, 'MM')  =  '11' then 'Novembro' "
                + "when to_char(d.data, 'MM')  =  '12' then 'Dezembro' "
                + "end as mes "
                + ", count(to_char(d.data, 'MM')) "
                + "from denuncia d join enderecodenuncia ed on d.enderecodenuncia_id = ed.id  "
                + "join prefeitura p on ed.cidade = p.cidade and ed.estado = p.estado "
                + "where p.codigo = ?1 "
                + "group by mes order by mes asc ";
        
        Query query = em.createNativeQuery(q);
        query.setParameter(1, codigo);

        
        List resultado = query.getResultList();

        List<List> denuncias = new ArrayList<>();
        
        Iterator i = resultado.iterator();

        while (i.hasNext()) {
            Object[] r = (Object[]) i.next();

            List l = new ArrayList();
            l.add(r[0]);
            l.add(r[1]);

            denuncias.add(l);

        }

        if (denuncias.size() > 0) {
            return denuncias;
        }
        
        
        return new ArrayList<>();
    }
    
    @Override
    public List<List> denunciasAtendidasPorMesChart(String codigo) {       
        
        String q = "SELECT "
                + "case "
                + "when to_char(d.data, 'MM')  =  '01' then 'Janeiro' "
                + "when to_char(d.data, 'MM')  =  '02' then 'Fevereiro' "
                + "when to_char(d.data, 'MM')  =  '03' then 'Março' "
                + "when to_char(d.data, 'MM')  =  '04' then 'Abril' "
                + "when to_char(d.data, 'MM')  =  '05' then 'Maio' "
                + "when to_char(d.data, 'MM')  =  '06' then 'Junho' "
                + "when to_char(d.data, 'MM')  =  '07' then 'Julho' "
                + "when to_char(d.data, 'MM')  =  '08' then 'Agosto' "
                + "when to_char(d.data, 'MM')  =  '09' then 'Setembro' "
                + "when to_char(d.data, 'MM')  =  '10' then 'Outubro' "
                + "when to_char(d.data, 'MM')  =  '11' then 'Novembro' "
                + "when to_char(d.data, 'MM')  =  '12' then 'Dezembro' "
                + "end as mes "
                + ", count(to_char(d.data, 'MM')) "
                + "from denuncia d join enderecodenuncia ed on d.enderecodenuncia_id = ed.id  "
                + "join prefeitura p on ed.cidade = p.cidade and ed.estado = p.estado "
                + "where p.codigo = ?1 "
                + "and d.estadodeacompanhamento = 'ATENDIDA' "
                + "group by mes order by mes asc ";
        
        Query query = em.createNativeQuery(q);
        query.setParameter(1, codigo);
        

        
        List resultado = query.getResultList();

        List<List> denuncias = new ArrayList<>();
        
        Iterator i = resultado.iterator();

        while (i.hasNext()) {
            Object[] r = (Object[]) i.next();

            List l = new ArrayList();
            l.add(r[0]);
            l.add(r[1]);

            denuncias.add(l);

        }

        if (denuncias.size() > 0) {
            return denuncias;
        }
        
        
        return new ArrayList<>();
    }

}
