package br.upf.projetojfprimefaces.controller;

import br.upf.projetojfprimefaces.entity.TutorEntity;
import br.upf.projetojfprimefaces.facade.TutorFacade;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Named
@SessionScoped
public class TutorController implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @EJB
    private TutorFacade tutorFacade;
    
    private TutorEntity tutor;
    private List<TutorEntity> tutores;
    private String filtroNome;
    
    @PostConstruct
    public void init() {
        tutor = new TutorEntity();
        tutores = new ArrayList<>();
    }
    
    public void carregarTutores() {
        try {
            tutores = tutorFacade.buscarTodos();
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, 
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Erro ao carregar tutores: " + e.getMessage()));
        }
    }
    
    public void filtrarTutoresPorNome() {
        try {
            if (filtroNome != null && !filtroNome.isEmpty()) {
                tutores = tutorFacade.buscarPorNome(filtroNome);
            } else {
                carregarTutores();
            }
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, 
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Erro ao filtrar tutores: " + e.getMessage()));
        }
    }
    
    public void prepararNovoTutor() {
        tutor = new TutorEntity();
    }
    
    public void prepararEditarTutor(TutorEntity tutorSelecionado) {
        tutor = tutorSelecionado;
    }
    
    public void salvarTutor() {
        try {
            if (tutor.getId() == null) {
                tutorFacade.create(tutor);
                FacesContext.getCurrentInstance().addMessage(null, 
                        new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", "Tutor cadastrado com sucesso!"));
            } else {
                tutorFacade.edit(tutor);
                FacesContext.getCurrentInstance().addMessage(null, 
                        new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", "Tutor atualizado com sucesso!"));
            }
            
            // Recarrega a lista de tutores
            carregarTutores();
            
            // Limpa o formulário
            tutor = new TutorEntity();
            
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, 
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Erro ao salvar tutor: " + e.getMessage()));
        }
    }
    
    public void excluirTutor(TutorEntity tutorSelecionado) {
        try {
            tutorFacade.remove(tutorSelecionado);
            FacesContext.getCurrentInstance().addMessage(null, 
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", "Tutor excluído com sucesso!"));
            
            // Recarrega a lista de tutores
            carregarTutores();
            
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, 
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Erro ao excluir tutor: " + e.getMessage()));
        }
    }

    // Getters e Setters
    public TutorEntity getTutor() {
        return tutor;
    }

    public void setTutor(TutorEntity tutor) {
        this.tutor = tutor;
    }

    public List<TutorEntity> getTutores() {
        return tutores;
    }

    public void setTutores(List<TutorEntity> tutores) {
        this.tutores = tutores;
    }

    public String getFiltroNome() {
        return filtroNome;
    }

    public void setFiltroNome(String filtroNome) {
        this.filtroNome = filtroNome;
    }
}
