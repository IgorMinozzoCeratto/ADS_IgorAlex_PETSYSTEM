
package br.upf.projetojfprimefaces.controller;

import br.upf.projetojfprimefaces.entity.ExameEntity;
import br.upf.projetojfprimefaces.entity.FuncionarioEntity;
import br.upf.projetojfprimefaces.entity.ProntuarioEntity;
import br.upf.projetojfprimefaces.facade.ExameFacade;
import br.upf.projetojfprimefaces.facade.FuncionarioFacade;
import br.upf.projetojfprimefaces.facade.ProntuarioFacade;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
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
    private ProntuarioFacade prontuarioFacade;
    
    @EJB
    private FuncionarioFacade funcionarioFacade;
    
    private ExameEntity exame;
    private List<ExameEntity> exames;
    private List<ProntuarioEntity> prontuarios;
    private List<FuncionarioEntity> veterinarios;
    private ProntuarioEntity prontuarioSelecionado;
    private FuncionarioEntity veterinarioSelecionado;
    private String filtroTipoExame;
    
    @PostConstruct
    public void init() {
        exame = new ExameEntity();
        exames = new ArrayList<>();
        prontuarios = new ArrayList<>();
        veterinarios = new ArrayList<>();
        carregarProntuarios();
        carregarVeterinarios();
        carregarExames();
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
            // Ou que todos os funcionários podem ser veterinários para fins de exame
            veterinarios = funcionarioFacade.findAll(); // TODO: Filtrar por perfil de veterinário se aplicável
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, 
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Erro ao carregar veterinários: " + e.getMessage()));
        }
    }
    
    public void carregarExames() {
        try {
            exames = exameFacade.findAll();
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, 
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Erro ao carregar exames: " + e.getMessage()));
        }
    }
    
    public void filtrarExamesPorProntuario() {
        try {
            if (prontuarioSelecionado != null) {
                // exames = exameFacade.buscarPorProntuario(prontuarioSelecionado);
                adicionarMensagemAviso("Filtro por prontuário ainda não implementado na fachada.");
            } else {
                carregarExames();
            }
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, 
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Erro ao filtrar exames por prontuário: " + e.getMessage()));
        }
    }
    
    public void filtrarExamesPorTipo() {
        try {
            if (filtroTipoExame != null && !filtroTipoExame.isEmpty()) {
                // exames = exameFacade.buscarPorTipo(filtroTipoExame);
                adicionarMensagemAviso("Filtro por tipo de exame ainda não implementado na fachada.");
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
        prontuarioSelecionado = null;
        veterinarioSelecionado = null;
    }
    
    public void prepararEditarExame(ExameEntity exameSelecionado) {
        exame = exameSelecionado;
        prontuarioSelecionado = exameSelecionado.getProntuario();
        veterinarioSelecionado = exameSelecionado.getVeterinario();
    }
    
    public void salvarExame() {
        try {
            if (prontuarioSelecionado == null) {
                adicionarMensagemAviso("Selecione um prontuário!");
                return;
            }
            if (veterinarioSelecionado == null) {
                adicionarMensagemAviso("Selecione um veterinário!");
                return;
            }
            exame.setProntuario(prontuarioSelecionado);
            exame.setVeterinario(veterinarioSelecionado);
            
            if (exame.getId() == null) {
                exameFacade.create(exame);
                FacesContext.getCurrentInstance().addMessage(null, 
                        new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", "Exame registrado com sucesso!"));
            } else {
                exameFacade.edit(exame);
                FacesContext.getCurrentInstance().addMessage(null, 
                        new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", "Exame atualizado com sucesso!"));
            }
            
            carregarExames();
            prepararNovoExame();
            
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
    
    public String getFiltroTipoExame() {
        return filtroTipoExame;
    }

    public void setFiltroTipoExame(String filtroTipoExame) {
        this.filtroTipoExame = filtroTipoExame;
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

