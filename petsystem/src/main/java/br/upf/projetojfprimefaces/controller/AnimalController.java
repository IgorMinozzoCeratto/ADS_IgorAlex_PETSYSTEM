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
        carregarRacas(); // lista inicial de raças
        carregarAnimais();
    }

    // Carrega listas
    public void carregarTutores() {
        try {
            tutores = tutorFacade.findAll();
        } catch (Exception e) {
            adicionarMensagemErro("Erro ao carregar tutores: " + e.getMessage());
        }
    }

    public void carregarRacas() {
        try {
            racas = racaFacade.findAll();
        } catch (Exception e) {
            adicionarMensagemErro("Erro ao carregar raças: " + e.getMessage());
        }
    }

    public void carregarEspecies() {
        try {
            especies = especieFacade.findAll();
        } catch (Exception e) {
            adicionarMensagemErro("Erro ao carregar espécies: " + e.getMessage());
        }
    }

    public void carregarAnimais() {
        try {
            animais = animalFacade.findAll();
        } catch (Exception e) {
            adicionarMensagemErro("Erro ao carregar animais: " + e.getMessage());
        }
    }

    // Atualiza lista de raças com base na espécie selecionada
    public void carregarRacasPorEspecie() {
        try {
            if (especieSelecionada != null) {
                racas = racaFacade.findByEspecie(especieSelecionada);
            } else {
                racas = new ArrayList<>();
            }
            racaSelecionada = null; // resetar raça ao mudar espécie
        } catch (Exception e) {
            adicionarMensagemErro("Erro ao carregar raças por espécie: " + e.getMessage());
        }
    }

    // Prepara novo animal
    public void prepararNovoAnimal() {
        animal = new AnimalEntity();
        animal.setRaca(new RacaEntity());
        tutorSelecionado = null;
        racaSelecionada = null;
        especieSelecionada = null;
    }

    // Prepara edição de animal
    public void prepararEditarAnimal(AnimalEntity animalSelecionado) {
        this.animal = animalSelecionado != null ? animalSelecionado : new AnimalEntity();
        if (this.animal.getRaca() == null) {
            this.animal.setRaca(new RacaEntity());
        }
        this.racaSelecionada = this.animal.getRaca();
        this.tutorSelecionado = this.animal.getTutor();
        if (this.racaSelecionada != null) {
            this.especieSelecionada = this.racaSelecionada.getEspecie();
            carregarRacasPorEspecie(); // garante que as raças corretas sejam carregadas
        }
    }

    // Salvar animal
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
            prepararNovoAnimal();

        } catch (Exception e) {
            adicionarMensagemErro("Erro ao salvar animal: " + e.getMessage());
        }
    }

    // Excluir animal
    public void excluirAnimal(AnimalEntity animalSelecionado) {
        try {
            animalFacade.remove(animalSelecionado);
            adicionarMensagemInfo("Animal excluído com sucesso!");
            carregarAnimais();
        } catch (Exception e) {
            adicionarMensagemErro("Erro ao excluir animal: " + e.getMessage());
        }
    }

    // Mensagens
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
        if (animal == null) {
            animal = new AnimalEntity();
            animal.setRaca(new RacaEntity());
        } else if (animal.getRaca() == null) {
            animal.setRaca(new RacaEntity());
        }
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
