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

    @EJB private ConsultaFacade consultaFacade;
    @EJB private ProntuarioFacade prontuarioFacade;
    @EJB private FuncionarioFacade funcionarioFacade;

    private ConsultaEntity consulta;
    private List<ConsultaEntity> consultas;
    private List<ProntuarioEntity> prontuarios;
    private List<FuncionarioEntity> veterinarios;

    private OffsetDateTime dataFiltro;

    private ProntuarioEntity prontuarioSelecionado;
    private FuncionarioEntity veterinarioSelecionado;

    /** 0 = formulário | 1 = lista */
    private int tabIndex = 1; // inicia mostrando a lista; mude para 0 se quiser iniciar no formulário

    @PostConstruct
    public void init() {
        consulta     = new ConsultaEntity();
        consultas    = new ArrayList<>();
        prontuarios  = new ArrayList<>();
        veterinarios = new ArrayList<>();
        carregarProntuarios();
        carregarVeterinarios();
        carregarConsultas();
    }

    public void carregarProntuarios() {
        try {
            prontuarios = prontuarioFacade.findAll();
        } catch (Exception e) {
            adicionarMensagemErro("Erro ao carregar prontuários: " + e.getMessage());
        }
    }

    public void carregarVeterinarios() {
        try {
            veterinarios = funcionarioFacade.findAll(); // filtre se tiver perfil específico
        } catch (Exception e) {
            adicionarMensagemErro("Erro ao carregar veterinários: " + e.getMessage());
        }
    }

    public void carregarConsultas() {
        try {
            consultas = consultaFacade.findAll();
        } catch (Exception e) {
            adicionarMensagemErro("Erro ao carregar consultas: " + e.getMessage());
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
            adicionarMensagemErro("Erro ao filtrar consultas: " + e.getMessage());
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
            adicionarMensagemErro("Erro ao filtrar por prontuário: " + e.getMessage());
        }
    }

    public void prepararNovaConsulta() {
        consulta = new ConsultaEntity();
        consulta.setRealizada(false);
        prontuarioSelecionado = null;
        veterinarioSelecionado = null;
        tabIndex = 0; // vai para o formulário
    }

    /** Chamado pelo botão lápis na tabela */
    public void prepararEditarConsulta(ConsultaEntity consultaSelecionada) {
        try {
            // Carrega a entidade gerenciada pelo ID (evita detached/lazy)
            this.consulta = consultaFacade.find(consultaSelecionada.getId());

            // Preenche selecionados para os combos
            this.prontuarioSelecionado  = consulta.getProntuario();
            this.veterinarioSelecionado = consulta.getVeterinario();

            // Vai para a aba do formulário
            this.tabIndex = 0;
        } catch (Exception e) {
            adicionarMensagemErro("Erro ao preparar edição: " + e.getMessage());
        }
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

            // Sincroniza selecionados -> entity
            consulta.setProntuario(prontuarioSelecionado);
            consulta.setVeterinario(veterinarioSelecionado);

            if (consulta.getId() == null) {
                consultaFacade.create(consulta);
                adicionarMensagemInfo("Consulta agendada com sucesso!");
            } else {
                consultaFacade.edit(consulta);
                adicionarMensagemInfo("Consulta atualizada com sucesso!");
            }

            carregarConsultas();
            tabIndex = 1;        // volta para a lista
            prepararNovaConsulta(); // opcional: já deixa pronto p/ novo cadastro

        } catch (Exception e) {
            adicionarMensagemErro("Erro ao salvar consulta: " + e.getMessage());
        }
    }

    public void excluirConsulta(ConsultaEntity consultaSelecionada) {
        try {
            consultaFacade.remove(consultaSelecionada);
            adicionarMensagemInfo("Consulta excluída com sucesso!");
            carregarConsultas();
        } catch (Exception e) {
            adicionarMensagemErro("Erro ao excluir consulta: " + e.getMessage());
        }
    }

    public void marcarComoRealizada(ConsultaEntity consultaSelecionada) {
        try {
            consultaSelecionada.setRealizada(true);
            consultaFacade.edit(consultaSelecionada);
            adicionarMensagemInfo("Consulta marcada como realizada!");
            carregarConsultas();
        } catch (Exception e) {
            adicionarMensagemErro("Erro ao atualizar status da consulta: " + e.getMessage());
        }
    }

    // Getters/Setters
    public ConsultaEntity getConsulta() { return consulta; }
    public void setConsulta(ConsultaEntity consulta) { this.consulta = consulta; }

    public List<ConsultaEntity> getConsultas() { return consultas; }
    public void setConsultas(List<ConsultaEntity> consultas) { this.consultas = consultas; }

    public List<ProntuarioEntity> getProntuarios() { return prontuarios; }
    public void setProntuarios(List<ProntuarioEntity> prontuarios) { this.prontuarios = prontuarios; }

    public List<FuncionarioEntity> getVeterinarios() { return veterinarios; }
    public void setVeterinarios(List<FuncionarioEntity> veterinarios) { this.veterinarios = veterinarios; }

    public OffsetDateTime getDataFiltro() { return dataFiltro; }
    public void setDataFiltro(OffsetDateTime dataFiltro) { this.dataFiltro = dataFiltro; }

    public ProntuarioEntity getProntuarioSelecionado() { return prontuarioSelecionado; }
    public void setProntuarioSelecionado(ProntuarioEntity prontuarioSelecionado) { this.prontuarioSelecionado = prontuarioSelecionado; }

    public FuncionarioEntity getVeterinarioSelecionado() { return veterinarioSelecionado; }
    public void setVeterinarioSelecionado(FuncionarioEntity veterinarioSelecionado) { this.veterinarioSelecionado = veterinarioSelecionado; }

    public int getTabIndex() { return tabIndex; }
    public void setTabIndex(int tabIndex) { this.tabIndex = tabIndex; }

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
