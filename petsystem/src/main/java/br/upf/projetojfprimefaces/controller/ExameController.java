package br.upf.projetojfprimefaces.controller;

import br.upf.projetojfprimefaces.entity.ExameEntity;
import br.upf.projetojfprimefaces.entity.FuncionarioEntity;
import br.upf.projetojfprimefaces.entity.ProntuarioEntity;
import br.upf.projetojfprimefaces.facade.ExameFacade;
import br.upf.projetojfprimefaces.facade.FuncionarioFacade;
import br.upf.projetojfprimefaces.facade.ProntuarioFacade;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Named
@ViewScoped
public class ExameController implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB private ExameFacade exameFacade;
    @EJB private ProntuarioFacade prontuarioFacade;
    @EJB private FuncionarioFacade funcionarioFacade;

    private ExameEntity exame;
    private List<ExameEntity> exames;

    private List<ProntuarioEntity> prontuarios;
    private List<FuncionarioEntity> veterinarios;

    // selecionados nos combos (ligam com os converters)
    private ProntuarioEntity prontuarioSelecionado;
    private FuncionarioEntity veterinarioSelecionado;

    /** 0 = formulário, 1 = histórico */
    private int tabIndex = 0;

    @PostConstruct
    public void init() {
        exames = new ArrayList<>();
        prontuarios = new ArrayList<>();
        veterinarios = new ArrayList<>();
        carregarProntuarios();
        carregarVeterinarios();
        carregarExames();
        prepararNovoExame();
    }

    public void carregarProntuarios() {
        try {
            prontuarios = prontuarioFacade.findAll();
        } catch (Exception e) {
            addMsg(FacesMessage.SEVERITY_ERROR, "Erro ao carregar prontuários: " + e.getMessage());
        }
    }

    public void carregarVeterinarios() {
        try {
            veterinarios = funcionarioFacade.findAll(); // filtre por perfil se tiver
        } catch (Exception e) {
            addMsg(FacesMessage.SEVERITY_ERROR, "Erro ao carregar veterinários: " + e.getMessage());
        }
    }

    public void carregarExames() {
        try {
            exames = exameFacade.findAll();
        } catch (Exception e) {
            addMsg(FacesMessage.SEVERITY_ERROR, "Erro ao carregar exames: " + e.getMessage());
        }
    }

    public void prepararNovoExame() {
        exame = new ExameEntity();
        exame.setDataExame(new Date());
        exame.setResultado(null);
        exame.setDocumentoAnexoUrl(null);

        prontuarioSelecionado = null;
        veterinarioSelecionado = null;

        tabIndex = 0; // fica na aba do formulário
    }

    public void prepararEditarExame(ExameEntity selecionado) {
        try {
            this.exame = exameFacade.find(selecionado.getId()); // garante entidade gerenciada
            this.prontuarioSelecionado = exame.getProntuario();
            this.veterinarioSelecionado = exame.getVeterinario();
            tabIndex = 0;
        } catch (Exception e) {
            addMsg(FacesMessage.SEVERITY_ERROR, "Erro ao preparar edição: " + e.getMessage());
        }
    }

    public void salvarExame() {
        try {
            if (prontuarioSelecionado == null || prontuarioSelecionado.getId() == null) {
                addMsg(FacesMessage.SEVERITY_WARN, "Selecione um prontuário!");
                return;
            }
            if (veterinarioSelecionado == null || veterinarioSelecionado.getId() == null) {
                addMsg(FacesMessage.SEVERITY_WARN, "Selecione um veterinário!");
                return;
            }

            // sincroniza selecionados -> entity
            exame.setProntuario(prontuarioSelecionado);
            exame.setVeterinario(veterinarioSelecionado);

            if (exame.getId() == null) {
                exameFacade.create(exame);
                addMsg(FacesMessage.SEVERITY_INFO, "Exame registrado com sucesso!");
            } else {
                exameFacade.edit(exame);
                addMsg(FacesMessage.SEVERITY_INFO, "Exame atualizado com sucesso!");
            }

            carregarExames();
            tabIndex = 1;      // vai para a lista
            prepararNovoExame();

        } catch (Exception e) {
            addMsg(FacesMessage.SEVERITY_ERROR, "Erro ao salvar exame: " + e.getMessage());
        }
    }

    public void excluirExame(ExameEntity selecionado) {
        try {
            exameFacade.remove(selecionado);
            addMsg(FacesMessage.SEVERITY_INFO, "Exame excluído com sucesso!");
            carregarExames();
        } catch (Exception e) {
            addMsg(FacesMessage.SEVERITY_ERROR, "Erro ao excluir exame: " + e.getMessage());
        }
    }

    // ---- util ----
    private void addMsg(FacesMessage.Severity s, String m) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(s, m, null));
    }

    // ---- getters/setters ----
    public ExameEntity getExame() { return exame; }
    public void setExame(ExameEntity exame) { this.exame = exame; }

    public List<ExameEntity> getExames() { return exames; }
    public void setExames(List<ExameEntity> exames) { this.exames = exames; }

    public List<ProntuarioEntity> getProntuarios() { return prontuarios; }
    public void setProntuarios(List<ProntuarioEntity> prontuarios) { this.prontuarios = prontuarios; }

    public List<FuncionarioEntity> getVeterinarios() { return veterinarios; }
    public void setVeterinarios(List<FuncionarioEntity> veterinarios) { this.veterinarios = veterinarios; }

    public ProntuarioEntity getProntuarioSelecionado() { return prontuarioSelecionado; }
    public void setProntuarioSelecionado(ProntuarioEntity prontuarioSelecionado) { this.prontuarioSelecionado = prontuarioSelecionado; }

    public FuncionarioEntity getVeterinarioSelecionado() { return veterinarioSelecionado; }
    public void setVeterinarioSelecionado(FuncionarioEntity veterinarioSelecionado) { this.veterinarioSelecionado = veterinarioSelecionado; }

    public int getTabIndex() { return tabIndex; }
    public void setTabIndex(int tabIndex) { this.tabIndex = tabIndex; }
}
