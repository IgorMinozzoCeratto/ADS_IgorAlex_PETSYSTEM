
package br.upf.projetojfprimefaces.controller;

import br.upf.projetojfprimefaces.entity.PerfilEntity;
import br.upf.projetojfprimefaces.facade.PerfilFacade;
import jakarta.ejb.EJB;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.List;

@Named(value = "perfilController")
@SessionScoped
public class PerfilController implements Serializable {

    @EJB
    private PerfilFacade perfilFacade;

    private PerfilEntity perfil;
    private List<PerfilEntity> listaPerfis;

    public PerfilController() {
        perfil = new PerfilEntity();
    }

    public String salvar() {
        if (perfil.getId() == null) {
            perfilFacade.create(perfil);
        } else {
            perfilFacade.edit(perfil);
        }
        perfil = new PerfilEntity();
        listaPerfis = null;
        return "/perfil/lista.xhtml?faces-redirect=true";
    }

    public String editar(PerfilEntity p) {
        this.perfil = p;
        return "/perfil/cadastro.xhtml?faces-redirect=true";
    }

    public String excluir(PerfilEntity p) {
        perfilFacade.remove(p);
        listaPerfis = null;
        return "/perfil/lista.xhtml?faces-redirect=true";
    }

    public PerfilEntity getPerfil() {
        return perfil;
    }

    public void setPerfil(PerfilEntity perfil) {
        this.perfil = perfil;
    }

    public List<PerfilEntity> getListaPerfis() {
        if (listaPerfis == null) {
            listaPerfis = perfilFacade.findAll();
        }
        return listaPerfis;
    }

    public void setListaPerfis(List<PerfilEntity> listaPerfis) {
        this.listaPerfis = listaPerfis;
    }

}

