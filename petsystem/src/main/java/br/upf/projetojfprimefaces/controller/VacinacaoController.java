package br.upf.projetojfprimefaces.controller;

import br.upf.projetojfprimefaces.entity.FuncionarioEntity;
import br.upf.projetojfprimefaces.entity.ProntuarioEntity;
import br.upf.projetojfprimefaces.entity.VacinacaoEntity;
import br.upf.projetojfprimefaces.facade.FuncionarioFacade;
import br.upf.projetojfprimefaces.facade.ProntuarioFacade;
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

    @EJB private VacinacaoFacade vacinacaoFacade;
    @EJB private ProntuarioFacade prontuarioFacade;
    @EJB private FuncionarioFacade funcionarioFacade;

    private VacinacaoEntity vacinacao;
    private List<VacinacaoEntity> vacinacoes;
    private List<ProntuarioEntity> prontuarios;
    private List<FuncionarioEntity> funcionarios;

    // filtro da aba "Histórico"
    private ProntuarioEntity filtroProntuario;

    /** 0=formulário, 1=histórico */
    private int tabIndex = 0;

    @PostConstruct
    public void init() {
        vacinacao = new VacinacaoEntity();
        vacinacao.setDataAplicacao(new Date());
        vacinacoes = new ArrayList<>();
        prontuarios = new ArrayList<>();
        funcionarios = new ArrayList<>();
        carregarProntuarios();
        carregarFuncionarios();
        carregarVacinacoes();
    }

    public void prepararNova() {
        vacinacao = new VacinacaoEntity();
        vacinacao.setDataAplicacao(new Date());
        tabIndex = 0;
    }

    public void prepararEdicao(VacinacaoEntity v) {
        this.vacinacao = vacinacaoFacade.find(v.getId());
        tabIndex = 0;
    }

    public void salvar() {
        try {
            if (vacinacao.getProntuario() == null) {
                addMsg(FacesMessage.SEVERITY_WARN, "Selecione um prontuário!");
                return;
            }
            if (vacinacao.getFuncionarioAplicador() == null) {
                addMsg(FacesMessage.SEVERITY_WARN, "Selecione um funcionário aplicador!");
                return;
            }

            if (vacinacao.getId() == null) {
                vacinacaoFacade.create(vacinacao);
                addMsg(FacesMessage.SEVERITY_INFO, "Vacinação registrada com sucesso!");
            } else {
                vacinacaoFacade.edit(vacinacao);
                addMsg(FacesMessage.SEVERITY_INFO, "Vacinação atualizada com sucesso!");
            }
            carregarVacinacoes();
            tabIndex = 1; // volta pro histórico
            prepararNova(); // limpa form
        } catch (Exception e) {
            addMsg(FacesMessage.SEVERITY_ERROR, "Erro ao salvar: " + e.getMessage());
        }
    }

    public void excluir(VacinacaoEntity v) {
        try {
            vacinacaoFacade.remove(v);
            addMsg(FacesMessage.SEVERITY_INFO, "Registro excluído!");
            carregarVacinacoes();
        } catch (Exception e) {
            addMsg(FacesMessage.SEVERITY_ERROR, "Erro ao excluir: " + e.getMessage());
        }
    }

    public void filtrar() {
        try {
            if (filtroProntuario != null) {
                // Se não houver método específico na facade, filtra em memória
                List<VacinacaoEntity> todas = vacinacaoFacade.findAll();
                List<VacinacaoEntity> filtradas = new ArrayList<>();
                for (VacinacaoEntity v : todas) {
                    if (v.getProntuario() != null && v.getProntuario().getId().equals(filtroProntuario.getId())) {
                        filtradas.add(v);
                    }
                }
                vacinacoes = filtradas;
            } else {
                carregarVacinacoes();
            }
        } catch (Exception e) {
            addMsg(FacesMessage.SEVERITY_ERROR, "Erro ao filtrar: " + e.getMessage());
        }
    }

    public void limparFiltro() {
        filtroProntuario = null;
        carregarVacinacoes();
    }

    public void carregarVacinacoes() {
        try {
            vacinacoes = vacinacaoFacade.findAll();
        } catch (Exception e) {
            addMsg(FacesMessage.SEVERITY_ERROR, "Erro ao carregar vacinação: " + e.getMessage());
            vacinacoes = new ArrayList<>();
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

    public void carregarFuncionarios() {
        try {
            funcionarios = funcionarioFacade.findAll();
        } catch (Exception e) {
            addMsg(FacesMessage.SEVERITY_ERROR, "Erro ao carregar funcionários: " + e.getMessage());
            funcionarios = new ArrayList<>();
        }
    }

    private void addMsg(FacesMessage.Severity s, String m) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(s, m, m));
    }

    // Getters/Setters
    public VacinacaoEntity getVacinacao() { return vacinacao; }
    public void setVacinacao(VacinacaoEntity vacinacao) { this.vacinacao = vacinacao; }
    public List<VacinacaoEntity> getVacinacoes() { return vacinacoes; }
    public List<ProntuarioEntity> getProntuarios() { return prontuarios; }
    public List<FuncionarioEntity> getFuncionarios() { return funcionarios; }
    public ProntuarioEntity getFiltroProntuario() { return filtroProntuario; }
    public void setFiltroProntuario(ProntuarioEntity filtroProntuario) { this.filtroProntuario = filtroProntuario; }
    public int getTabIndex() { return tabIndex; }
    public void setTabIndex(int tabIndex) { this.tabIndex = tabIndex; }
}
