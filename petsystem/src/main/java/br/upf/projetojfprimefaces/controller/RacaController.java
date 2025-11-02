package br.upf.projetojfprimefaces.controller;

import br.upf.projetojfprimefaces.entity.EspecieEntity;
import br.upf.projetojfprimefaces.entity.RacaEntity;
import br.upf.projetojfprimefaces.facade.EspecieFacade;
import br.upf.projetojfprimefaces.facade.RacaFacade;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.List;

@Named("racaController")
@SessionScoped
public class RacaController implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private RacaFacade racaFacade;
    @EJB
    private EspecieFacade especieFacade;

    private RacaEntity raca;
    private List<RacaEntity> listaRacas;
    private List<EspecieEntity> listaEspecies;

    @PostConstruct
    public void init() {
        raca = new RacaEntity();
        recarregarListas();
    }

    private void recarregarListas() {
        listaRacas = racaFacade.findAll();
        listaEspecies = especieFacade.findAll();
    }

    // Navegação/ações
    public String editar(RacaEntity r) {
        this.raca = (r != null) ? r : new RacaEntity();
        return "cadastro.xhtml?faces-redirect=true";
    }

    public void excluir(RacaEntity r) {
        try {
            racaFacade.remove(r);
            info("Raça excluída com sucesso!");
            recarregarListas();
        } catch (Exception e) {
            erro("Erro ao excluir raça: " + raiz(e));
        }
    }

    public String salvar() {
        try {
            if (raca.getEspecie() == null) {
                warn("Selecione uma espécie.");
                return null;
            }

            if (raca.getId() == null) {
                racaFacade.create(raca);
                info("Raça cadastrada com sucesso!");
            } else {
                racaFacade.edit(raca);
                info("Raça atualizada com sucesso!");
            }

            raca = new RacaEntity();
            recarregarListas();
            return null; // permanece no cadastro

        } catch (Exception e) {
            erro("Erro ao salvar raça: " + raiz(e));
            return null;
        }
    }

    // Mensagens util
    private void info(String m) {
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", m));
    }

    private void warn(String m) {
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_WARN, "Atenção", m));
    }

    private void erro(String m) {
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", m));
    }

    private String raiz(Throwable t) {
        Throwable c = t;
        while (c.getCause() != null) {
            c = c.getCause();
        }
        return c.getMessage();
    }

    // Getters/Setters
    public RacaEntity getRaca() {
        return raca;
    }

    public void setRaca(RacaEntity raca) {
        this.raca = raca;
    }

    public List<RacaEntity> getListaRacas() {
        return listaRacas;
    }

    public void setListaRacas(List<RacaEntity> listaRacas) {
        this.listaRacas = listaRacas;
    }

    public List<EspecieEntity> getListaEspecies() {
        return listaEspecies;
    }

    public void setListaEspecies(List<EspecieEntity> listaEspecies) {
        this.listaEspecies = listaEspecies;
    }
}
