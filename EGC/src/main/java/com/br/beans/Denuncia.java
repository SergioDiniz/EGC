/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.br.beans;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Sergio Diniz
 */
@Entity
public class Denuncia implements Serializable {

    @Id
    @GeneratedValue
    private int id;
    @Column(nullable = false, length = 2048)
    private String descricao;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private EstadoDeAcompanhamento estadoDeAcompanhamento;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TipoDeDenuncia tipoDeDenuncia;
    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date data;
    @Column(nullable = false)
    private String foto;
    @Column(nullable = false)
    private int numeroAjuda;
    @Column(nullable = false, unique = true)
    private String codigo;
    @Column(nullable = false)
    private boolean ativo = true;

    @OneToOne(cascade = CascadeType.ALL)
    private EnderecoDenuncia enderecoDenuncia;
    @ManyToOne
    private Cidade cidade;
    @OneToOne(cascade = CascadeType.ALL)
    private InformacaoDeAtendida informacaoDeAtendida;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<ConteudoInapropriado> conteudoInapropriados;
    
    @OneToMany(mappedBy = "denuncia", fetch = FetchType.EAGER)
    private List<Registro> registros;

    public Denuncia() {
    }

    public Denuncia(String descricao, String foto, EnderecoDenuncia enderecoDenuncia, TipoDeDenuncia tipoDeDenuncia) {
        this.descricao = descricao;
        this.foto = foto;
        this.enderecoDenuncia = enderecoDenuncia;
        this.cidade = new Cidade(new CidadePK(enderecoDenuncia.getCidade(), enderecoDenuncia.getEstado()));
        this.estadoDeAcompanhamento = EstadoDeAcompanhamento.AGUARDANDO;
        this.tipoDeDenuncia = tipoDeDenuncia;
        this.data = new Date();
        this.numeroAjuda = 0;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public EstadoDeAcompanhamento getEstadoDeAcompanhamento() {
        return estadoDeAcompanhamento;
    }

    public void setEstadoDeAcompanhamento(EstadoDeAcompanhamento estadoDeAcompanhamento) {
        this.estadoDeAcompanhamento = estadoDeAcompanhamento;
    }
    
    public boolean tagAtendida(){
        if (estadoDeAcompanhamento == EstadoDeAcompanhamento.ATENDIDA){
            return true;
        }
        
        return false;
    }
    
    public boolean tagEmAndamento(){
        if (estadoDeAcompanhamento == EstadoDeAcompanhamento.EM_TRABALHO){
            return true;
        }
        
        return false;
    }
    
    public boolean tagAguardando(){
        if (estadoDeAcompanhamento == EstadoDeAcompanhamento.AGUARDANDO){
            return true;
        }
        
        return false;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public EnderecoDenuncia getEnderecoDenuncia() {
        return enderecoDenuncia;
    }

    public void setEnderecoDenuncia(EnderecoDenuncia enderecoDenuncia) {
        this.enderecoDenuncia = enderecoDenuncia;
    }

    public Cidade getCidade() {
        return cidade;
    }

    public void setCidade(Cidade cidade) {
        this.cidade = cidade;
    }

    public InformacaoDeAtendida getInformacaoDeAtendida() {
        return informacaoDeAtendida;
    }

    public void setInformacaoDeAtendida(InformacaoDeAtendida informacaoDeAtendida) {
        this.informacaoDeAtendida = informacaoDeAtendida;
    }

    public TipoDeDenuncia getTipoDeDenuncia() {
        return tipoDeDenuncia;
    }

    public void setTipoDeDenuncia(TipoDeDenuncia tipoDeDenuncia) {
        this.tipoDeDenuncia = tipoDeDenuncia;
    }
    
    public String getTipoDaDenunciaFormatado(){
        switch(getTipoDeDenuncia()){
            case COLETA_DE_LIXO:
                return "Coleta de Lixo";
            case DISTRIBUICAO_E_QUALIDADE_DA_AGUA:
                return "Distribuição e Qualidade da Agua";
            case ILUMINACAO_PUBLICA:
                return "Iluminação Publica";
            case OBRAS_PUBLICAS:
                return "Limpeza Urbana";
            case MANUTENCAO_DE_CANAIS_E_REDES_DE_ESGOTOS:
                return "Manutenção de Canais e Redes de Esgotos";
            case MANUTENCAO_E_CONSERVACAO_DE_VIAS_PUBLICAS:
                return "Manuetenção e Conservação de Vias Publicas";
            case PODA_E_MANUTENCAO_DAS_ARVORES:
                return "Poda e Manutenção das Arvores";
            case POLUICAO_VISUAL:
                return "Poluição Visual";
            case TRANSITO_SINALIZACAO_E_PLACAS:
                return "Trânsito, Sinalização e Placas";
            case ACESSIBILIDADE:
                return "Acessibilidade";
            case EDUCACAO:
                return "Educação";
            case SAUDE:
                return "Saude";
        }
        
        return "";
        
    }
    
    
    public String getIconeDenunica(){
        switch(getTipoDeDenuncia()){
            case COLETA_DE_LIXO:
                return "icone-lixo.png";
            case DISTRIBUICAO_E_QUALIDADE_DA_AGUA:
                return "icone-agua-esgoto.png";
            case ILUMINACAO_PUBLICA:
                return "icone-iluminação.png";
            case OBRAS_PUBLICAS:
                return "icone-obras-publicas.png";
            case MANUTENCAO_DE_CANAIS_E_REDES_DE_ESGOTOS:
                return "icone-manutecao-esgoto.png";
            case MANUTENCAO_E_CONSERVACAO_DE_VIAS_PUBLICAS:
                return "icone-manutencao-vias-publicas.png";
            case PODA_E_MANUTENCAO_DAS_ARVORES:
                return "icone-arvores.png";
            case POLUICAO_VISUAL:
                return "icone-pulicao-visual.png";
            case TRANSITO_SINALIZACAO_E_PLACAS:
                return "icone-transito.png";
            case ACESSIBILIDADE:
                return "icone-acessibilidade.png";
            case EDUCACAO:
                return "icone-educacao.png";
            case SAUDE:
                return "icone-saude.png";
        }
        
        return "";
    }

    @Override
    public String toString() {
        return "Denuncia{" + "id=" + id + ", descricao=" + descricao + ", estadoDeAcompanhamento=" + estadoDeAcompanhamento + ", tipoDeDenuncia=" + tipoDeDenuncia + ", data=" + data + ", foto=" + foto + ", enderecoDenuncia=" + enderecoDenuncia + ", cidade=" + cidade + ", informacaoDeAtendida=" + informacaoDeAtendida + ", conteudoInapropriados=" + conteudoInapropriados + '}';
    }

    public List<ConteudoInapropriado> getConteudoInapropriados() {
        return conteudoInapropriados;
    }

    public void setConteudoInapropriados(List<ConteudoInapropriado> conteudoInapropriados) {
        this.conteudoInapropriados = conteudoInapropriados;
    }

    public String getDataDenuncaFormatado() {
        SimpleDateFormat formato = new SimpleDateFormat("HH:mm - dd/MM/yyyy");
        return formato.format(getData());
    }

    public int getNumeroAjuda() {
        return numeroAjuda;
    }

    public void setNumeroAjuda(int numeroAjuda) {
        this.numeroAjuda = numeroAjuda;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public List<Registro> getRegistros() {
        return registros;
    }

    public void setRegistros(List<Registro> registros) {
        this.registros = registros;
    }
    
    
    
    

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + this.id;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Denuncia other = (Denuncia) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

}
