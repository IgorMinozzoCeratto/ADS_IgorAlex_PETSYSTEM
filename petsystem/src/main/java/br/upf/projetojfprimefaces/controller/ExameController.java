package br.upf.projetojfprimefaces.controller;

import br.upf.projetojfprimefaces.entity.ExameEntity;
import br.upf.projetojfprimefaces.entity.FuncionarioEntity;
import br.upf.projetojfprimefaces.entity.ProntuarioEntity;
import br.upf.projetojfprimefaces.facade.ExameFacade;
import br.upf.projetojfprimefaces.facade.FuncionarioFacade;
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
public class ExameController implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB private ExameFacade exameFacade;
    @EJB private ProntuarioFacade prontuarioFacade;
    @EJB private FuncionarioFacade funcionarioFacade;

    private ExameEntity exame;
    private List<ExameEntity> exames;
    private List<ProntuarioEntity> prontuarios;
    private List<FuncionarioEntity> veterinarios;

    private ProntuarioEntity filtroProntuario;
    private int tabIndex = 0;

    @PostConstruct
    public void init() {
        exame = new ExameEntity();
        exame.setDataExame(new Date());
        exames = new ArrayList<>();
        prontuarios = new ArrayList<>();
        veterinarios = new ArrayList<>();
        carregarProntuarios();
        carregarVeterinarios();
        carregarExames();
    }

    public void prepararNovo() {
        exame = new ExameEntity();
        exame.setDataExame(new Date());
        tabIndex = 0;
    }

    public void prepararEdicao(ExameEntity e) {
        this.exame = exameFacade.find(e.getId());
        tabIndex = 0;
    }

    public void salvar() {
        try {
            if (exame.getProntuario() == null) {
                addMsg(FacesMessage.SEVERITY_WARN, "Selecione um prontuário!");
                return;
            }
            if (exame.getVeterinario() == null) {
                addMsg(FacesMessage.SEVERITY_WARN, "Selecione um veterinário!");
                return;
            }

            if (exame.getId() == null) {
                exameFacade.create(exame);
                addMsg(FacesMessage.SEVERITY_INFO, "Exame registrado com sucesso!");
            } else {
                exameFacade.edit(exame);
                addMsg(FacesMessage.SEVERITY_INFO, "Exame atualizado com sucesso!");
            }
            carregarExames();
            tabIndex = 1;
            prepararNovo();
        } catch (Exception e) {
            addMsg(FacesMessage.SEVERITY_ERROR, "Erro ao salvar: " + e.getMessage());
        }
    }

    public void excluir(ExameEntity e) {
        try {
            exameFacade.remove(e);
            addMsg(FacesMessage.SEVERITY_INFO, "Registro excluído!");
            carregarExames();
        } catch (Exception ex) {
            addMsg(FacesMessage.SEVERITY_ERROR, "Erro ao excluir: " + ex.getMessage());
        }
    }

    public void filtrar() {
        try {
            if (filtroProntuario != null) {
                List<ExameEntity> todos = exameFacade.findAll();
                List<ExameEntity> filtrados = new ArrayList<>();
                for (ExameEntity e : todos) {
                    if (e.getProntuario() != null && e.getProntuario().getId().equals(filtroProntuario.getId())) {
                        filtrados.add(e);
                    }
                }
                exames = filtrados;
            } else {
                carregarExames();
            }
        } catch (Exception e) {
            addMsg(FacesMessage.SEVERITY_ERROR, "Erro ao filtrar: " + e.getMessage());
        }
    }

    public void limparFiltro() {
        filtroProntuario = null;
        carregarExames();
    }

    public void carregarExames() {
        try {
            exames = exameFacade.findAll();
        } catch (Exception e) {
            addMsg(FacesMessage.SEVERITY_ERROR, "Erro ao carregar exames: " + e.getMessage());
            exames = new ArrayList<>();
        }
    }

    public void carregarProntuarios() {
        try {
            prontuarios = prontuarioFacade.findAll();
        } catch (Exception e) {
            addMsg(FacesMessage.SEVERITY_ERROR, "Erro ao carregar prontuários: " + e.getMessage());
            prontuarios = new ArrayList<>();
        }
    }

    public void carregarVeterinarios() {
        try {
            veterinarios = funcionarioFacade.findAll();
        } catch (Exception e) {
            addMsg(FacesMessage.SEVERITY_ERROR, "Erro ao carregar veterinários: " + e.getMessage());
            veterinarios = new ArrayList<>();
        }
    }

    private void addMsg(FacesMessage.Severity s, String m) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(s, m, m));
    }

    // Getters/Setters
    public ExameEntity getExame() { return exame; }
    public void setExame(ExameEntity exame) { this.exame = exame; }
    public List<ExameEntity> getExames() { return exames; }
    public List<ProntuarioEntity> getProntuarios() { return prontuarios; }
    public List<FuncionarioEntity> getVeterinarios() { return veterinarios; }
    public ProntuarioEntity getFiltroProntuario() { return filtroProntuario; }
    public void setFiltroProntuario(ProntuarioEntity filtroProntuario) { this.filtroProntuario = filtroProntuario; }
    public int getTabIndex() { return tabIndex; }
    public void setTabIndex(int tabIndex) { this.tabIndex = tabIndex; }
}
