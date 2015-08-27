/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.br.controle;

import com.br.beans.Denuncia;
import com.br.fachada.Fachada;
import java.io.Serializable;
import java.text.NumberFormat;
import java.text.ParseException;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;

/**
 *
 * @author SergioD
 */
@ManagedBean(name = "controladorDenuncia")
@SessionScoped
public class ControladorDenuncia implements Serializable {

    private MapModel simpleModel;
    @EJB
    private Fachada fachada;

    public MapModel marcaPonto(Denuncia denuncia) throws ParseException {
        simpleModel = new DefaultMapModel();

        float lat = Float.parseFloat(denuncia.getEnderecoDenuncia().getLatitude());
        float lng = Float.parseFloat(denuncia.getEnderecoDenuncia().getLongitude());
        String end = denuncia.getEnderecoDenuncia().getRua() + ", " + denuncia.getEnderecoDenuncia().getNumero();

        LatLng coord1;
        coord1 = new LatLng(lat, lng);

        //Basic marker
        simpleModel.addOverlay(new Marker(coord1, end));

        return simpleModel;
    }

    public MapModel marcaPonto(String latidude, String longitude) throws ParseException {
        simpleModel = new DefaultMapModel();

        float lat = Float.parseFloat(latidude);
        float lng = Float.parseFloat(longitude);

        LatLng coord1;
        coord1 = new LatLng(lat, lng);

        //Basic marker
        simpleModel.addOverlay(new Marker(coord1, "Denuncia"));

        return simpleModel;
    }

    public MapModel getSimpleModel() {
        return simpleModel;
    }

    public void setAjudarDenuncia(Denuncia denuncia) {
        System.out.println("denuncia: " + denuncia.getDescricao());
        fachada.setAjudarDenuncia(denuncia);
    }

    public int getAjudarDenuncia(Denuncia denuncia) {
        return fachada.getAjudarDenuncia(denuncia);
    }

}