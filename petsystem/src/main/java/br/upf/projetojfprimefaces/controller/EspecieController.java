
package br.upf.projetojfprimefaces.controller;

import br.upf.projetojfprimefaces.entity.EspecieEntity;
import br.upf.projetojfprimefaces.facade.EspecieFacade;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

@Named(value = "especieController")
@SessionScoped
public class EspecieController implements Serializable {

    @EJB
    private EspecieFacade especieFacade;

    private EspecieEntity especie;
    private List<EspecieEntity> listaEspecies;

    public EspecieController() {
        especie = new EspecieEntity();
    }

    public String salvar() {
        if (especie.getId() == null) {
            especieFacade.create(especie);
        } else {
            especieFacade.edit(especie);
        }
        especie = new EspecieEntity();
        listaEspecies = null;
        return "/especie/lista.xhtml?faces-redirect=true";
    }

    public String editar(EspecieEntity e) {
        this.especie = e;
        return "/especie/cadastro.xhtml?faces-redirect=true";
    }

    public String excluir(EspecieEntity e) {
        especieFacade.remove(e);
        listaEspecies = null;
        return "/especie/lista.xhtml?faces-redirect=true";
    }

    public EspecieEntity getEspecie() {
        return especie;
    }

    public void setEspecie(EspecieEntity especie) {
        this.especie = especie;
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

