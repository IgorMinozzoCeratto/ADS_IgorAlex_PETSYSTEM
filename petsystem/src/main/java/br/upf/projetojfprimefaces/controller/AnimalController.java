
package br.upf.projetojfprimefaces.controller;

import br.upf.projetojfprimefaces.entity.AnimalEntity;
import br.upf.projetojfprimefaces.entity.RacaEntity;
import br.upf.projetojfprimefaces.entity.TutorEntity;
import br.upf.projetojfprimefaces.facade.AnimalFacade;
import br.upf.projetojfprimefaces.facade.RacaFacade;
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
    
    @EJB
    private RacaFacade racaFacade;

    private AnimalEntity animal;
    private List<AnimalEntity> animais;
    private List<TutorEntity> tutores;
    private List<RacaEntity> racas;
    private String filtroNome;
    private TutorEntity tutorSelecionado;
    private RacaEntity racaSelecionada;

    @PostConstruct
    public void init() {
        animal = new AnimalEntity();
        animais = new ArrayList<>();
        tutores = new ArrayList<>();
        racas = new ArrayList<>();
        carregarTutores();
        carregarRacas();
        carregarAnimais();
    }

    public void carregarTutores() {
        try {
            tutores = tutorFacade.findAll();
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("tutores", tutores);
        } catch (Exception e) {
            adicionarMensagemErro("Erro ao carregar tutores: " + e.getMessage());
        }
    }
    
    public void carregarRacas() {
        try {
            racas = racaFacade.findAll();
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("racas", racas);
        } catch (Exception e) {
            adicionarMensagemErro("Erro ao carregar raças: " + e.getMessage());
        }
    }

    public void carregarAnimais() {
        try {
            animais = animalFacade.findAll();
        } catch (Exception e) {
            adicionarMensagemErro("Erro ao carregar animais: " + e.getMessage());
        }
    }

    public void filtrarAnimaisPorNome() {
        try {
            if (filtroNome != null && !filtroNome.isEmpty()) {
                // Implementar busca por nome na fachada se necessário
                // animais = animalFacade.buscarPorNome(filtroNome);
                adicionarMensagemAviso("Filtro por nome ainda não implementado.");
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
                // Implementar busca por tutor na fachada se necessário
                // animais = animalFacade.buscarPorTutor(tutorSelecionado);
                adicionarMensagemAviso("Filtro por tutor ainda não implementado.");
            } else {
                carregarAnimais();
            }
        } catch (Exception e) {
            adicionarMensagemErro("Erro ao filtrar por tutor: " + e.getMessage());
        }
    }

    public void carregarRacasPorEspecie() {
        if (animal.getRaca() != null && animal.getRaca().getEspecie() != null) {
            racas = racaFacade.findByEspecie(animal.getRaca().getEspecie());
        } else {
            racas = new ArrayList<>();
        }
    }

    public void prepararNovoAnimal() {
        animal = new AnimalEntity();
        tutorSelecionado = null;
        racaSelecionada = null;
    }

    public void prepararEditarAnimal(AnimalEntity animalSelecionado) {
        this.animal = animalSelecionado;
        this.tutorSelecionado = animalSelecionado.getTutor();
        this.racaSelecionada = animalSelecionado.getRaca();
    }

    public void salvarAnimal() {
        try {
            if (tutorSelecionado == null) {
                adicionarMensagemAviso("Selecione um tutor!");
                return;
            }
            if (racaSelecionada == null) {
                adicionarMensagemAviso("Selecione uma raça!");
                return;
            }
            animal.setTutor(tutorSelecionado);
            animal.setRaca(racaSelecionada);

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

    public List<RacaEntity> getRacas() {
        return racas;
    }

    public void setRacas(List<RacaEntity> racas) {
        this.racas = racas;
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

    public RacaEntity getRacaSelecionada() {
        return racaSelecionada;
    }

    public void setRacaSelecionada(RacaEntity racaSelecionada) {
        this.racaSelecionada = racaSelecionada;
    }
}

