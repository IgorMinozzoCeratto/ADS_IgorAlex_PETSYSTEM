package br.upf.projetojfprimefaces.controller;

import br.upf.projetojfprimefaces.entity.EspecieEntity;
import br.upf.projetojfprimefaces.entity.RacaEntity;
import br.upf.projetojfprimefaces.facade.EspecieFacade;
import br.upf.projetojfprimefaces.facade.RacaFacade;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

@Named(value = "racaController")
@SessionScoped
public class RacaController implements Serializable {

    @EJB
    private RacaFacade racaFacade;
    @EJB
    private EspecieFacade especieFacade;

    private RacaEntity raca;
    private List<RacaEntity> listaRacas;
    private List<EspecieEntity> listaEspecies;

    public RacaController() {
        raca = new RacaEntity();
    }

    public String salvar() {
        if (raca.getId() == null) {
            racaFacade.create(raca);
        } else {
            racaFacade.edit(raca);
        }
        raca = new RacaEntity();
        listaRacas = null;
        return "/raca/lista.xhtml?faces-redirect=true";
    }

    public String editar(RacaEntity r) {
        this.raca = r;
        return "/raca/cadastro.xhtml?faces-redirect=true";
    }

    public String excluir(RacaEntity r) {
        racaFacade.remove(r);
        listaRacas = null;
        return "/raca/lista.xhtml?faces-redirect=true";
    }

    public RacaEntity getRaca() {
        return raca;
    }

    public void setRaca(RacaEntity raca) {
        this.raca = raca;
    }

    public List<RacaEntity> getListaRacas() {
        if (listaRacas == null) {
            listaRacas = racaFacade.findAll();
        }
        return listaRacas;
    }

    public void setListaRacas(List<RacaEntity> listaRacas) {
        this.listaRacas = listaRacas;
    }

    public List<EspecieEntity> getListaEspecies() {
        if (listaEspecies == null) {
            listaEspecies = especieFacade.findAll();
        }
        return listaEspecies;
    }

    public void setListaEspecies(List<EspecieEntity> listaEspecies) {
        this.listaEspecies = listaEspecies;
    }

}

