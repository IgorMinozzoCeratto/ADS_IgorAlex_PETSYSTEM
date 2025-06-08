package br.upf.projetojfprimefaces.controller;

import br.upf.projetojfprimefaces.entity.AnimalEntity;
import br.upf.projetojfprimefaces.entity.FuncionarioEntity;
import br.upf.projetojfprimefaces.entity.VacinacaoEntity;
import br.upf.projetojfprimefaces.facade.AnimalFacade;
import br.upf.projetojfprimefaces.facade.FuncionarioFacade;
import br.upf.projetojfprimefaces.facade.VacinacaoFacade;
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
public class VacinacaoController implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @EJB
    private VacinacaoFacade vacinacaoFacade;
    
    @EJB
    private AnimalFacade animalFacade;
    
    @EJB
    private FuncionarioFacade funcionarioFacade;
    
    private VacinacaoEntity vacinacao;
    private List<VacinacaoEntity> vacinacoes;
    private List<AnimalEntity> animais;
    private List<FuncionarioEntity> veterinarios;
    private AnimalEntity animalSelecionado;
    private String filtroTipoVacina;
    
    @PostConstruct
    public void init() {
        vacinacao = new VacinacaoEntity();
        vacinacoes = new ArrayList<>();
        animais = new ArrayList<>();
        veterinarios = new ArrayList<>();
        carregarAnimais();
        carregarVeterinarios();
    }
    
    public void carregarAnimais() {
        try {
            animais = animalFacade.buscarTodos();
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, 
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Erro ao carregar animais: " + e.getMessage()));
        }
    }
    
    public void carregarVeterinarios() {
        try {
            veterinarios = funcionarioFacade.buscarPorTipo(FuncionarioEntity.TipoFuncionario.VETERINARIO);
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, 
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Erro ao carregar veterinários: " + e.getMessage()));
        }
    }
    
    public void carregarVacinacoes() {
        try {
            vacinacoes = vacinacaoFacade.buscarTodas();
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, 
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Erro ao carregar vacinações: " + e.getMessage()));
        }
    }
    
    public void filtrarVacinacoesPorAnimal() {
        try {
            if (animalSelecionado != null) {
                vacinacoes = vacinacaoFacade.buscarPorAnimal(animalSelecionado);
            } else {
                carregarVacinacoes();
            }
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, 
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Erro ao filtrar vacinações por animal: " + e.getMessage()));
        }
    }
    
    public void filtrarVacinacoesPorTipo() {
        try {
            if (filtroTipoVacina != null && !filtroTipoVacina.isEmpty()) {
                vacinacoes = vacinacaoFacade.buscarPorTipoVacina(filtroTipoVacina);
            } else {
                carregarVacinacoes();
            }
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, 
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Erro ao filtrar vacinações por tipo: " + e.getMessage()));
        }
    }
    
    public void prepararNovaVacinacao() {
        vacinacao = new VacinacaoEntity();
        vacinacao.setDataAplicacao(new Date()); // Data atual como padrão
    }
    
    public void prepararEditarVacinacao(VacinacaoEntity vacinacaoSelecionada) {
        vacinacao = vacinacaoSelecionada;
    }
    
    public void salvarVacinacao() {
        try {
            if (vacinacao.getId() == null) {
                vacinacaoFacade.create(vacinacao);
                FacesContext.getCurrentInstance().addMessage(null, 
                        new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", "Vacinação registrada com sucesso!"));
            } else {
                vacinacaoFacade.edit(vacinacao);
                FacesContext.getCurrentInstance().addMessage(null, 
                        new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", "Vacinação atualizada com sucesso!"));
            }
            
            // Recarrega a lista de vacinações
            carregarVacinacoes();
            
            // Limpa o formulário
            vacinacao = new VacinacaoEntity();
            
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, 
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Erro ao salvar vacinação: " + e.getMessage()));
        }
    }
    
    public void excluirVacinacao(VacinacaoEntity vacinacaoSelecionada) {
        try {
            vacinacaoFacade.remove(vacinacaoSelecionada);
            FacesContext.getCurrentInstance().addMessage(null, 
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", "Registro de vacinação excluído com sucesso!"));
            
            // Recarrega a lista de vacinações
            carregarVacinacoes();
            
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, 
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Erro ao excluir vacinação: " + e.getMessage()));
        }
    }

    // Getters e Setters
    public VacinacaoEntity getVacinacao() {
        return vacinacao;
    }

    public void setVacinacao(VacinacaoEntity vacinacao) {
        this.vacinacao = vacinacao;
    }

    public List<VacinacaoEntity> getVacinacoes() {
        return vacinacoes;
    }

    public void setVacinacoes(List<VacinacaoEntity> vacinacoes) {
        this.vacinacoes = vacinacoes;
    }

    public List<AnimalEntity> getAnimais() {
        return animais;
    }

    public void setAnimais(List<AnimalEntity> animais) {
        this.animais = animais;
    }

    public List<FuncionarioEntity> getVeterinarios() {
        return veterinarios;
    }

    public void setVeterinarios(List<FuncionarioEntity> veterinarios) {
        this.veterinarios = veterinarios;
    }

    public AnimalEntity getAnimalSelecionado() {
        return animalSelecionado;
    }

    public void setAnimalSelecionado(AnimalEntity animalSelecionado) {
        this.animalSelecionado = animalSelecionado;
    }

    public String getFiltroTipoVacina() {
        return filtroTipoVacina;
    }

    public void setFiltroTipoVacina(String filtroTipoVacina) {
        this.filtroTipoVacina = filtroTipoVacina;
    }
}
