package br.upf.projetojfprimefaces.controller;

import br.upf.projetojfprimefaces.entity.AnimalEntity;
import br.upf.projetojfprimefaces.entity.ConsultaEntity;
import br.upf.projetojfprimefaces.entity.FuncionarioEntity;
import br.upf.projetojfprimefaces.facade.AnimalFacade;
import br.upf.projetojfprimefaces.facade.ConsultaFacade;
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
public class ConsultaController implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @EJB
    private ConsultaFacade consultaFacade;
    
    @EJB
    private AnimalFacade animalFacade;
    
    @EJB
    private FuncionarioFacade funcionarioFacade;
    
    private ConsultaEntity consulta;
    private List<ConsultaEntity> consultas;
    private List<AnimalEntity> animais;
    private List<FuncionarioEntity> funcionarios;
    private Date dataFiltro;
    private AnimalEntity animalSelecionado;
    
    @PostConstruct
    public void init() {
        consulta = new ConsultaEntity();
        consultas = new ArrayList<>();
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
    
    public void carregarConsultas() {
        try {
            consultas = consultaFacade.buscarTodas();
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, 
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Erro ao carregar consultas: " + e.getMessage()));
        }
    }
    
    public void filtrarConsultasPorData() {
        try {
            if (dataFiltro != null) {
                consultas = consultaFacade.buscarPorData(dataFiltro);
            } else {
                carregarConsultas();
            }
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, 
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Erro ao filtrar consultas: " + e.getMessage()));
        }
    }
    
    public void filtrarConsultasPorAnimal() {
        try {
            if (animalSelecionado != null) {
                consultas = consultaFacade.buscarPorAnimal(animalSelecionado);
            } else {
                carregarConsultas();
            }
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, 
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Erro ao filtrar consultas por animal: " + e.getMessage()));
        }
    }
    
    public void prepararNovaConsulta() {
        consulta = new ConsultaEntity();
        consulta.setRealizada(false);
    }
    
    public void prepararEditarConsulta(ConsultaEntity consultaSelecionada) {
        consulta = consultaSelecionada;
    }
    
    public void salvarConsulta() {
        try {
            if (consulta.getId() == null) {
                consultaFacade.create(consulta);
                FacesContext.getCurrentInstance().addMessage(null, 
                        new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", "Consulta agendada com sucesso!"));
            } else {
                consultaFacade.edit(consulta);
                FacesContext.getCurrentInstance().addMessage(null, 
                        new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", "Consulta atualizada com sucesso!"));
            }
            
            // Recarrega a lista de consultas
            carregarConsultas();
            
            // Limpa o formulário
            consulta = new ConsultaEntity();
            
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, 
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Erro ao salvar consulta: " + e.getMessage()));
        }
    }
    
    public void excluirConsulta(ConsultaEntity consultaSelecionada) {
        try {
            consultaFacade.remove(consultaSelecionada);
            FacesContext.getCurrentInstance().addMessage(null, 
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", "Consulta excluída com sucesso!"));
            
            // Recarrega a lista de consultas
            carregarConsultas();
            
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, 
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Erro ao excluir consulta: " + e.getMessage()));
        }
    }
    
    public void marcarComoRealizada(ConsultaEntity consultaSelecionada) {
        try {
            consultaSelecionada.setRealizada(true);
            consultaFacade.edit(consultaSelecionada);
            FacesContext.getCurrentInstance().addMessage(null, 
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", "Consulta marcada como realizada!"));
            
            // Recarrega a lista de consultas
            carregarConsultas();
            
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, 
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Erro ao atualizar status da consulta: " + e.getMessage()));
        }
    }

    // Getters e Setters
    public ConsultaEntity getConsulta() {
        return consulta;
    }

    public void setConsulta(ConsultaEntity consulta) {
        this.consulta = consulta;
    }

    public List<ConsultaEntity> getConsultas() {
        return consultas;
    }

    public void setConsultas(List<ConsultaEntity> consultas) {
        this.consultas = consultas;
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

    public Date getDataFiltro() {
        return dataFiltro;
    }

    public void setDataFiltro(Date dataFiltro) {
        this.dataFiltro = dataFiltro;
    }

    public AnimalEntity getAnimalSelecionado() {
        return animalSelecionado;
    }

    public void setAnimalSelecionado(AnimalEntity animalSelecionado) {
        this.animalSelecionado = animalSelecionado;
    }
}
