
package br.upf.projetojfprimefaces.controller;

import br.upf.projetojfprimefaces.entity.AnimalEntity;
import br.upf.projetojfprimefaces.entity.ProntuarioEntity;
import br.upf.projetojfprimefaces.facade.AnimalFacade;
import br.upf.projetojfprimefaces.facade.ProntuarioFacade;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Named(value = "prontuarioController")
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
        prontuario = new ProntuarioEntity();
        listaProntuarios = new ArrayList<>();
        listaAnimais = new ArrayList<>();
        carregarAnimais();
        carregarProntuarios();
    }

    public void carregarAnimais() {
        try {
            listaAnimais = animalFacade.findAll();
        } catch (Exception e) {
            adicionarMensagemErro("Erro ao carregar animais: " + e.getMessage());
        }
    }

    public void carregarProntuarios() {
        try {
            listaProntuarios = prontuarioFacade.findAll();
        } catch (Exception e) {
            adicionarMensagemErro("Erro ao carregar prontuários: " + e.getMessage());
        }
    }

    public String salvar() {
        try {
            if (animalSelecionado == null) {
                adicionarMensagemAviso("Selecione um animal para o prontuário!");
                return null;
            }
            prontuario.setAnimal(animalSelecionado);

            if (prontuario.getId() == null) {
                prontuarioFacade.create(prontuario);
                adicionarMensagemInfo("Prontuário criado com sucesso!");
            } else {
                prontuarioFacade.edit(prontuario);
                adicionarMensagemInfo("Prontuário atualizado com sucesso!");
            }
            prontuario = new ProntuarioEntity();
            animalSelecionado = null;
            listaProntuarios = null;
            return "/prontuario/lista.xhtml?faces-redirect=true";
        } catch (Exception e) {
            adicionarMensagemErro("Erro ao salvar prontuário: " + e.getMessage());
            return null;
        }
    }

    public String editar(ProntuarioEntity p) {
        this.prontuario = p;
        this.animalSelecionado = p.getAnimal();
        return "/prontuario/cadastro.xhtml?faces-redirect=true";
    }

    public String excluir(ProntuarioEntity p) {
        try {
            prontuarioFacade.remove(p);
            listaProntuarios = null;
            adicionarMensagemInfo("Prontuário excluído com sucesso!");
            return "/prontuario/lista.xhtml?faces-redirect=true";
        } catch (Exception e) {
            adicionarMensagemErro("Erro ao excluir prontuário: " + e.getMessage());
            return null;
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

    public ProntuarioEntity getProntuario() {
        return prontuario;
    }

    public void setProntuario(ProntuarioEntity prontuario) {
        this.prontuario = prontuario;
    }

    public List<ProntuarioEntity> getListaProntuarios() {
        if (listaProntuarios == null) {
            carregarProntuarios();
        }
        return listaProntuarios;
    }

    public void setListaProntuarios(List<ProntuarioEntity> listaProntuarios) {
        this.listaProntuarios = listaProntuarios;
    }

    public List<AnimalEntity> getListaAnimais() {
        return listaAnimais;
    }

    public void setListaAnimais(List<AnimalEntity> listaAnimais) {
        this.listaAnimais = listaAnimais;
    }

    public AnimalEntity getAnimalSelecionado() {
        return animalSelecionado;
    }

    public void setAnimalSelecionado(AnimalEntity animalSelecionado) {
        this.animalSelecionado = animalSelecionado;
    }
}

