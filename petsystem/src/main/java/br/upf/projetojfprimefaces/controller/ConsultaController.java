
package br.upf.projetojfprimefaces.controller;

import br.upf.projetojfprimefaces.entity.ConsultaEntity;
import br.upf.projetojfprimefaces.entity.FuncionarioEntity;
import br.upf.projetojfprimefaces.entity.ProntuarioEntity;
import br.upf.projetojfprimefaces.facade.ConsultaFacade;
import br.upf.projetojfprimefaces.facade.FuncionarioFacade;
import br.upf.projetojfprimefaces.facade.ProntuarioFacade;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Named
@SessionScoped
public class ConsultaController implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @EJB
    private ConsultaFacade consultaFacade;
    
    @EJB
    private ProntuarioFacade prontuarioFacade;
    
    @EJB
    private FuncionarioFacade funcionarioFacade;
    
    private ConsultaEntity consulta;
    private List<ConsultaEntity> consultas;
    private List<ProntuarioEntity> prontuarios;
    private List<FuncionarioEntity> veterinarios;
    private OffsetDateTime dataFiltro;
    private ProntuarioEntity prontuarioSelecionado;
    private FuncionarioEntity veterinarioSelecionado;
    
    @PostConstruct
    public void init() {
        consulta = new ConsultaEntity();
        consultas = new ArrayList<>();
        prontuarios = new ArrayList<>();
        veterinarios = new ArrayList<>();
        carregarProntuarios();
        carregarVeterinarios();
        carregarConsultas();
    }
    
    public void carregarProntuarios() {
        try {
            prontuarios = prontuarioFacade.findAll();
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, 
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Erro ao carregar prontuários: " + e.getMessage()));
        }
    }
    
    public void carregarVeterinarios() {
        try {
            // Assumindo que veterinários são funcionários com um perfil específico
            // Ou que todos os funcionários podem ser veterinários para fins de consulta
            veterinarios = funcionarioFacade.findAll(); // TODO: Filtrar por perfil de veterinário se aplicável
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, 
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Erro ao carregar veterinários: " + e.getMessage()));
        }
    }
    
    public void carregarConsultas() {
        try {
            consultas = consultaFacade.findAll();
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, 
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Erro ao carregar consultas: " + e.getMessage()));
        }
    }
    
    public void filtrarConsultasPorData() {
        try {
            if (dataFiltro != null) {
                // consultas = consultaFacade.buscarPorData(dataFiltro);
                adicionarMensagemAviso("Filtro por data ainda não implementado na fachada.");
            } else {
                carregarConsultas();
            }
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, 
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Erro ao filtrar consultas: " + e.getMessage()));
        }
    }
    
    public void filtrarConsultasPorProntuario() {
        try {
            if (prontuarioSelecionado != null) {
                // consultas = consultaFacade.buscarPorProntuario(prontuarioSelecionado);
                adicionarMensagemAviso("Filtro por prontuário ainda não implementado na fachada.");
            } else {
                carregarConsultas();
            }
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, 
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Erro ao filtrar consultas por prontuário: " + e.getMessage()));
        }
    }
    
    public void prepararNovaConsulta() {
        consulta = new ConsultaEntity();
        consulta.setRealizada(false);
        prontuarioSelecionado = null;
        veterinarioSelecionado = null;
    }
    
    public void prepararEditarConsulta(ConsultaEntity consultaSelecionada) {
        consulta = consultaSelecionada;
        prontuarioSelecionado = consultaSelecionada.getProntuario();
        veterinarioSelecionado = consultaSelecionada.getVeterinario();
    }
    
    public void salvarConsulta() {
        try {
            if (prontuarioSelecionado == null) {
                adicionarMensagemAviso("Selecione um prontuário!");
                return;
            }
            if (veterinarioSelecionado == null) {
                adicionarMensagemAviso("Selecione um veterinário!");
                return;
            }
            consulta.setProntuario(prontuarioSelecionado);
            consulta.setVeterinario(veterinarioSelecionado);
            
            if (consulta.getId() == null) {
                consultaFacade.create(consulta);
                FacesContext.getCurrentInstance().addMessage(null, 
                        new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", "Consulta agendada com sucesso!"));
            } else {
                consultaFacade.edit(consulta);
                FacesContext.getCurrentInstance().addMessage(null, 
                        new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", "Consulta atualizada com sucesso!"));
            }
            
            carregarConsultas();
            prepararNovaConsulta();
            
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

    public List<ProntuarioEntity> getProntuarios() {
        return prontuarios;
    }

    public void setProntuarios(List<ProntuarioEntity> prontuarios) {
        this.prontuarios = prontuarios;
    }

    public List<FuncionarioEntity> getVeterinarios() {
        return veterinarios;
    }

    public void setVeterinarios(List<FuncionarioEntity> veterinarios) {
        this.veterinarios = veterinarios;
    }

    public OffsetDateTime getDataFiltro() {
        return dataFiltro;
    }

    public void setDataFiltro(OffsetDateTime dataFiltro) {
        this.dataFiltro = dataFiltro;
    }

    public ProntuarioEntity getProntuarioSelecionado() {
        return prontuarioSelecionado;
    }

    public void setProntuarioSelecionado(ProntuarioEntity prontuarioSelecionado) {
        this.prontuarioSelecionado = prontuarioSelecionado;
    }

    public FuncionarioEntity getVeterinarioSelecionado() {
        return veterinarioSelecionado;
    }

    public void setVeterinarioSelecionado(FuncionarioEntity veterinarioSelecionado) {
        this.veterinarioSelecionado = veterinarioSelecionado;
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
}

