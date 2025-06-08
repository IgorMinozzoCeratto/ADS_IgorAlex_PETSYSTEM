package br.upf.projetojfprimefaces.controller;

import br.upf.projetojfprimefaces.entity.AnimalEntity;
import br.upf.projetojfprimefaces.entity.TutorEntity;
import br.upf.projetojfprimefaces.facade.AnimalFacade;
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
public class AnimalController implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private AnimalFacade animalFacade;

    @EJB
    private TutorFacade tutorFacade;

    private AnimalEntity animal;
    private List<AnimalEntity> animais;
    private List<TutorEntity> tutores;
    private String filtroNome;
    private TutorEntity tutorSelecionado;

    @PostConstruct
    public void init() {
        animal = new AnimalEntity();
        animais = new ArrayList<>();
        tutores = new ArrayList<>();
        carregarTutores();
        carregarAnimais();
    }

    public void carregarTutores() {
        try {
            tutores = tutorFacade.buscarTodos();
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("tutores", tutores);
        } catch (Exception e) {
            adicionarMensagemErro("Erro ao carregar tutores: " + e.getMessage());
        }
    }

    public void carregarAnimais() {
        try {
            animais = animalFacade.buscarTodos();
        } catch (Exception e) {
            adicionarMensagemErro("Erro ao carregar animais: " + e.getMessage());
        }
    }

    public void filtrarAnimaisPorNome() {
        try {
            if (filtroNome != null && !filtroNome.isEmpty()) {
                animais = animalFacade.buscarPorNome(filtroNome);
            } else {
                carregarAnimais();
            }
        } catch (Exception e) {
            adicionarMensagemErro("Erro ao filtrar animais: " + e.getMessage());
        }
    }

    public void filtrarAnimaisPorTutor() {
        try {
            if (tutorSelecionado != null) {
                animais = animalFacade.buscarPorTutor(tutorSelecionado);
            } else {
                carregarAnimais();
            }
        } catch (Exception e) {
            adicionarMensagemErro("Erro ao filtrar por tutor: " + e.getMessage());
        }
    }

    public void prepararNovoAnimal() {
        animal = new AnimalEntity();
    }

    public void prepararEditarAnimal(AnimalEntity animalSelecionado) {
        this.animal = animalSelecionado;
    }

    public void salvarAnimal() {
        try {
            if (animal.getTutor() == null) {
                adicionarMensagemAviso("Selecione um tutor!");
                return;
            }

            if (animal.getId() == null) {
                animalFacade.create(animal);
                adicionarMensagemInfo("Animal cadastrado com sucesso!");
            } else {
                animalFacade.edit(animal);
                adicionarMensagemInfo("Animal atualizado com sucesso!");
            }

            carregarAnimais();
            prepararNovoAnimal(); // ← Limpa o formulário só DEPOIS de salvar

        } catch (Exception e) {
            adicionarMensagemErro("Erro ao salvar animal: " + e.getMessage());
        }
    }

    public void excluirAnimal(AnimalEntity animalSelecionado) {
        try {
            animalFacade.remove(animalSelecionado);
            adicionarMensagemInfo("Animal excluído com sucesso!");
            carregarAnimais();
        } catch (Exception e) {
            adicionarMensagemErro("Erro ao excluir animal: " + e.getMessage());
        }
    }

    public AnimalEntity.EspecieAnimal[] getEspecies() {
        return AnimalEntity.EspecieAnimal.values();
    }

    private void adicionarMensagemInfo(String mensagem) {
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", mensagem));
    }

    private void adicionarMensagemErro(String mensagem) {
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", mensagem));
    }

    private void adicionarMensagemAviso(String mensagem) {
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_WARN, "Aviso", mensagem));
    }

    // Getters e Setters
    public AnimalEntity getAnimal() {
        return animal;
    }

    public void setAnimal(AnimalEntity animal) {
        this.animal = animal;
    }

    public List<AnimalEntity> getAnimais() {
        return animais;
    }

    public void setAnimais(List<AnimalEntity> animais) {
        this.animais = animais;
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

    public TutorEntity getTutorSelecionado() {
        return tutorSelecionado;
    }

    public void setTutorSelecionado(TutorEntity tutorSelecionado) {
        this.tutorSelecionado = tutorSelecionado;
    }
}
