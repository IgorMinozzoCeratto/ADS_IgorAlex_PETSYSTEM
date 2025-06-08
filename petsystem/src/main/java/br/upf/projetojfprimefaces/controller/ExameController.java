package br.upf.projetojfprimefaces.controller;

import br.upf.projetojfprimefaces.entity.AnimalEntity;
import br.upf.projetojfprimefaces.entity.ExameEntity;
import br.upf.projetojfprimefaces.entity.FuncionarioEntity;
import br.upf.projetojfprimefaces.facade.AnimalFacade;
import br.upf.projetojfprimefaces.facade.ExameFacade;
import br.upf.projetojfprimefaces.facade.FuncionarioFacade;
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
public class ExameController implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @EJB
    private ExameFacade exameFacade;
    
    @EJB
    private AnimalFacade animalFacade;
    
    @EJB
    private FuncionarioFacade funcionarioFacade;
    
    private ExameEntity exame;
    private List<ExameEntity> exames;
    private List<AnimalEntity> animais;
    private List<FuncionarioEntity> funcionarios;
    private AnimalEntity animalSelecionado;
    private String filtroTipoExame;
    
    @PostConstruct
    public void init() {
        exame = new ExameEntity();
        exames = new ArrayList<>();
        animais = new ArrayList<>();
        funcionarios = new ArrayList<>();
        carregarAnimais();
        carregarFuncionarios();
    }
    
    public void carregarAnimais() {
        try {
            animais = animalFacade.buscarTodos();
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, 
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Erro ao carregar animais: " + e.getMessage()));
        }
    }
    
    public void carregarFuncionarios() {
        try {
            funcionarios = funcionarioFacade.buscarTodos();
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, 
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Erro ao carregar funcionários: " + e.getMessage()));
        }
    }
    
    public void carregarExames() {
        try {
            exames = exameFacade.buscarTodos();
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, 
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Erro ao carregar exames: " + e.getMessage()));
        }
    }
    
    public void filtrarExamesPorAnimal() {
        try {
            if (animalSelecionado != null) {
                exames = exameFacade.buscarPorAnimal(animalSelecionado);
            } else {
                carregarExames();
            }
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, 
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Erro ao filtrar exames por animal: " + e.getMessage()));
        }
    }
    
    public void filtrarExamesPorTipo() {
        try {
            if (filtroTipoExame != null && !filtroTipoExame.isEmpty()) {
                exames = exameFacade.buscarPorTipo(filtroTipoExame);
            } else {
                carregarExames();
            }
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, 
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Erro ao filtrar exames por tipo: " + e.getMessage()));
        }
    }
    
    public void prepararNovoExame() {
        exame = new ExameEntity();
        exame.setDataExame(new Date()); // Data atual como padrão
    }
    
    public void prepararEditarExame(ExameEntity exameSelecionado) {
        exame = exameSelecionado;
    }
    
    public void salvarExame() {
        try {
            if (exame.getId() == null) {
                exameFacade.create(exame);
                FacesContext.getCurrentInstance().addMessage(null, 
                        new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", "Exame registrado com sucesso!"));
            } else {
                exameFacade.edit(exame);
                FacesContext.getCurrentInstance().addMessage(null, 
                        new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", "Exame atualizado com sucesso!"));
            }
            
            // Recarrega a lista de exames
            carregarExames();
            
            // Limpa o formulário
            exame = new ExameEntity();
            
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, 
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Erro ao salvar exame: " + e.getMessage()));
        }
    }
    
    public void excluirExame(ExameEntity exameSelecionado) {
        try {
            exameFacade.remove(exameSelecionado);
            FacesContext.getCurrentInstance().addMessage(null, 
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", "Registro de exame excluído com sucesso!"));
            
            // Recarrega a lista de exames
            carregarExames();
            
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, 
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Erro ao excluir exame: " + e.getMessage()));
        }
    }

    // Getters e Setters
    public ExameEntity getExame() {
        return exame;
    }

    public void setExame(ExameEntity exame) {
        this.exame = exame;
    }

    public List<ExameEntity> getExames() {
        return exames;
    }

    public void setExames(List<ExameEntity> exames) {
        this.exames = exames;
    }

    public List<AnimalEntity> getAnimais() {
        return animais;
    }

    public void setAnimais(List<AnimalEntity> animais) {
        this.animais = animais;
    }

    public List<FuncionarioEntity> getFuncionarios() {
        return funcionarios;
    }

    public void setFuncionarios(List<FuncionarioEntity> funcionarios) {
        this.funcionarios = funcionarios;
    }

    public AnimalEntity getAnimalSelecionado() {
        return animalSelecionado;
    }

    public void setAnimalSelecionado(AnimalEntity animalSelecionado) {
        this.animalSelecionado = animalSelecionado;
    }

    public String getFiltroTipoExame() {
        return filtroTipoExame;
    }

    public void setFiltroTipoExame(String filtroTipoExame) {
        this.filtroTipoExame = filtroTipoExame;
    }
}
