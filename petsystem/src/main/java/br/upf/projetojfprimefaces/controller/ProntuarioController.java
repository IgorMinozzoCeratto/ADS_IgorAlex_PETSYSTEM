package br.upf.projetojfprimefaces.controller;

import br.upf.projetojfprimefaces.entity.AnimalEntity;
import br.upf.projetojfprimefaces.entity.ProntuarioEntity;
import br.upf.projetojfprimefaces.facade.AnimalFacade;
import br.upf.projetojfprimefaces.facade.ProntuarioFacade;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Named
@SessionScoped
public class ProntuarioController implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private ProntuarioFacade prontuarioFacade;

    @EJB
    private AnimalFacade animalFacade;

    private ProntuarioEntity prontuario;
    private List<ProntuarioEntity> listaProntuarios;
    private List<AnimalEntity> listaAnimais;
    private AnimalEntity animalSelecionado;

    @PostConstruct
    public void init() {
        listaProntuarios = new ArrayList<>();
        listaAnimais = new ArrayList<>();
        carregarAnimais();
        carregarProntuarios();
        novoModelo();
    }

    private void novoModelo() {
        prontuario = new ProntuarioEntity();
        prontuario.setDataCriacao(new Date());
        animalSelecionado = null;
    }

    // Navegação
    public String novo() {
        novoModelo();
        return "/prontuario/cadastro?faces-redirect=true";
    }

    public String editar(ProntuarioEntity p) {
        this.prontuario = p;
        this.animalSelecionado = p.getAnimal();
        return "/prontuario/cadastro?faces-redirect=true";
    }

    // Ações
    public String salvar() {
        try {
            if (animalSelecionado == null) {
                addMsg(FacesMessage.SEVERITY_WARN, "Selecione um animal.");
                return null;
            }

            // Garante 1 prontuário por animal (tua tabela tem UNIQUE em id_animal)
            if (prontuario.getId() == null) {
                if (existeProntuarioParaAnimal(animalSelecionado.getId())) {
                    addMsg(FacesMessage.SEVERITY_ERROR, "Este animal já possui prontuário.");
                    return null;
                }
            } else {
                if (!animalSelecionado.getId().equals(prontuario.getAnimal().getId())
                        && existeProntuarioParaAnimal(animalSelecionado.getId())) {
                    addMsg(FacesMessage.SEVERITY_ERROR, "O animal selecionado já possui prontuário.");
                    return null;
                }
            }

            prontuario.setAnimal(animalSelecionado);

            if (prontuario.getId() == null) {
                prontuarioFacade.create(prontuario);
                addMsg(FacesMessage.SEVERITY_INFO, "Prontuário salvo com sucesso.");
            } else {
                prontuarioFacade.edit(prontuario);
                addMsg(FacesMessage.SEVERITY_INFO, "Prontuário atualizado com sucesso.");
            }

            carregarProntuarios();
            return "/prontuario/lista?faces-redirect=true";

        } catch (Exception e) {
            addMsg(FacesMessage.SEVERITY_ERROR, "Erro ao salvar: " + e.getMessage());
            return null;
        }
    }

    public void excluir(ProntuarioEntity p) {
        try {
            prontuarioFacade.remove(p);
            addMsg(FacesMessage.SEVERITY_INFO, "Prontuário excluído.");
            carregarProntuarios();
        } catch (Exception e) {
            addMsg(FacesMessage.SEVERITY_ERROR, "Erro ao excluir: " + e.getMessage());
        }
    }

    // Carregamentos
    public void carregarProntuarios() {
        try {
            listaProntuarios = prontuarioFacade.findAll();
        } catch (Exception e) {
            addMsg(FacesMessage.SEVERITY_ERROR, "Erro ao carregar prontuários: " + e.getMessage());
            listaProntuarios = new ArrayList<>();
        }
    }

    public void carregarAnimais() {
        try {
            listaAnimais = animalFacade.findAll();
        } catch (Exception e) {
            addMsg(FacesMessage.SEVERITY_ERROR, "Erro ao carregar animais: " + e.getMessage());
            listaAnimais = new ArrayList<>();
        }
    }

    // Util
    private boolean existeProntuarioParaAnimal(Integer idAnimal) {
        if (idAnimal == null || listaProntuarios == null) return false;
        for (ProntuarioEntity p : listaProntuarios) {
            if (p.getAnimal() != null && idAnimal.equals(p.getAnimal().getId())) {
                return true;
            }
        }
        return false;
    }

    private void addMsg(FacesMessage.Severity s, String m) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(s, m, null));
    }

    // Getters/Setters
    public ProntuarioEntity getProntuario() { return prontuario; }
    public void setProntuario(ProntuarioEntity prontuario) { this.prontuario = prontuario; }
    public List<ProntuarioEntity> getListaProntuarios() { return listaProntuarios; }
    public void setListaProntuarios(List<ProntuarioEntity> listaProntuarios) { this.listaProntuarios = listaProntuarios; }
    public List<AnimalEntity> getListaAnimais() { return listaAnimais; }
    public void setListaAnimais(List<AnimalEntity> listaAnimais) { this.listaAnimais = listaAnimais; }
    public AnimalEntity getAnimalSelecionado() { return animalSelecionado; }
    public void setAnimalSelecionado(AnimalEntity animalSelecionado) { this.animalSelecionado = animalSelecionado; }
}
