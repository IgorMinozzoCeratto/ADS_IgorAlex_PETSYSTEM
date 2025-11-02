package br.upf.projetojfprimefaces.controller;

import br.upf.projetojfprimefaces.entity.AnimalEntity;
import br.upf.projetojfprimefaces.entity.EspecieEntity;
import br.upf.projetojfprimefaces.entity.RacaEntity;
import br.upf.projetojfprimefaces.entity.TutorEntity;
import br.upf.projetojfprimefaces.facade.AnimalFacade;
import br.upf.projetojfprimefaces.facade.EspecieFacade;
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

@Named("animalController") // <<< precisa bater com #{animalController...} no XHTML
@SessionScoped
public class AnimalController implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private AnimalFacade animalFacade;
    @EJB
    private TutorFacade tutorFacade;
    @EJB
    private RacaFacade racaFacade;
    @EJB
    private EspecieFacade especieFacade;

    private AnimalEntity animal;
    private List<AnimalEntity> animais;
    private List<TutorEntity> tutores;
    private List<RacaEntity> racas;
    private List<EspecieEntity> especies;

    private String filtroNome;
    private TutorEntity tutorSelecionado;
    private RacaEntity racaSelecionada;
    private EspecieEntity especieSelecionada;

    @PostConstruct
    public void init() {
        prepararNovoAnimal();
        tutores = new ArrayList<>();
        racas = new ArrayList<>();
        especies = new ArrayList<>();
        animais = new ArrayList<>();
        carregarTutores();
        carregarEspecies();
        carregarRacas(); // lista inicial (pode ficar vazia/geral)
        carregarAnimais();
    }

    // ----------------- cargas -----------------
    public void carregarTutores() {
        try {
            tutores = tutorFacade.findAll();
        } catch (Exception e) {
            erro("Erro ao carregar tutores: " + raiz(e));
        }
    }

    public void carregarRacas() {
        try {
            racas = racaFacade.findAll();
        } catch (Exception e) {
            erro("Erro ao carregar raças: " + raiz(e));
        }
    }

    public void carregarEspecies() {
        try {
            especies = especieFacade.findAll();
        } catch (Exception e) {
            erro("Erro ao carregar espécies: " + raiz(e));
        }
    }

    public void carregarAnimais() {
        try {
            animais = animalFacade.findAll();
        } catch (Exception e) {
            erro("Erro ao carregar animais: " + raiz(e));
        }
    }

    // filtra raças pela espécie selecionada
    public void carregarRacasPorEspecie() {
        try {
            if (especieSelecionada != null) {
                racas = racaFacade.findByEspecie(especieSelecionada);
            } else {
                racas = new ArrayList<>();
            }
            racaSelecionada = null; // reset ao mudar espécie
        } catch (Exception e) {
            erro("Erro ao carregar raças por espécie: " + raiz(e));
        }
    }

    // ----------------- preparar -----------------
    public void prepararNovoAnimal() {
        animal = new AnimalEntity();
        // NÃO setar new RacaEntity() aqui para evitar persistência/transiente indevida
        tutorSelecionado = null;
        racaSelecionada = null;
        especieSelecionada = null;
    }

    public void prepararEditarAnimal(AnimalEntity animalSelecionado) {
        this.animal = (animalSelecionado != null) ? animalSelecionado : new AnimalEntity();
        this.racaSelecionada = this.animal.getRaca();
        this.tutorSelecionado = this.animal.getTutor();
        if (this.racaSelecionada != null) {
            this.especieSelecionada = this.racaSelecionada.getEspecie();
            carregarRacasPorEspecie();
            // garante que a raça do animal exista na lista carregada
        }
    }

    // ----------------- ações -----------------
    public void salvarAnimal() {
        try {
            if (tutorSelecionado == null) {
                aviso("Selecione um tutor!");
                return;
            }
            if (racaSelecionada == null) {
                aviso("Selecione uma raça!");
                return;
            }

            // associa efetivamente as seleções
            animal.setTutor(tutorSelecionado);
            animal.setRaca(racaSelecionada);
            // se AnimalEntity tiver campo especie (opcional):
            // animal.setEspecie(especieSelecionada);

            if (animal.getId() == null) {
                animalFacade.create(animal);
                info("Animal cadastrado com sucesso!");
            } else {
                animalFacade.edit(animal);
                info("Animal atualizado com sucesso!");
            }

            carregarAnimais();
            prepararNovoAnimal();

        } catch (Exception e) {
            erro("Erro ao salvar animal: " + raiz(e)); // mostra a causa real (FK, validação, etc.)
        }
    }

    public void excluirAnimal(AnimalEntity animalSelecionado) {
        try {
            animalFacade.remove(animalSelecionado);
            info("Animal excluído com sucesso!");
            carregarAnimais();
        } catch (Exception e) {
            erro("Erro ao excluir animal: " + raiz(e));
        }
    }

    // ----------------- mensagens util -----------------
    private void info(String msg) {
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", msg));
    }

    private void erro(String msg) {
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", msg));
    }

    private void aviso(String msg) {
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_WARN, "Aviso", msg));
    }

    private String raiz(Throwable t) {
        Throwable c = t;
        while (c.getCause() != null) {
            c = c.getCause();
        }
        return c.getMessage();
    }

    // ----------------- getters/setters -----------------
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

    public List<EspecieEntity> getEspecies() {
        return especies;
    }

    public void setEspecies(List<EspecieEntity> especies) {
        this.especies = especies;
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

    public EspecieEntity getEspecieSelecionada() {
        return especieSelecionada;
    }

    public void setEspecieSelecionada(EspecieEntity especieSelecionada) {
        this.especieSelecionada = especieSelecionada;
    }
}
