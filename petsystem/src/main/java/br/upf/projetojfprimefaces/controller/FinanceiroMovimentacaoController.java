package br.upf.projetojfprimefaces.controller;

import br.upf.projetojfprimefaces.entity.FinanceiroMovimentacaoEntity;
import br.upf.projetojfprimefaces.entity.FuncionarioEntity;
import br.upf.projetojfprimefaces.facade.FinanceiroMovimentacaoFacade;
import br.upf.projetojfprimefaces.facade.FuncionarioFacade;
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
public class FinanceiroMovimentacaoController implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private FinanceiroMovimentacaoFacade facade;

    @EJB
    private FuncionarioFacade funcionarioFacade;

    private FinanceiroMovimentacaoEntity movimentacao;
    private List<FinanceiroMovimentacaoEntity> lista = new ArrayList<>();
    private List<FuncionarioEntity> funcionarios = new ArrayList<>();

    @PostConstruct
    public void init() {
        recarregarLista();
        carregarFuncionarios();
        if (movimentacao == null) {
            prepararNovo();
        }
    }

    private void prepararNovo() {
        movimentacao = new FinanceiroMovimentacaoEntity();
        // define agora como padrão
        movimentacao.setDataMovimentacao(OffsetDateTime.now());
    }

    public void recarregarLista() {
        lista = facade.findAll();
    }

    public void carregarFuncionarios() {
        funcionarios = funcionarioFacade.findAll();
    }

    public String novo() {
        prepararNovo();
        return "/financeiro/cadastro.xhtml?faces-redirect=true";
    }

    public String editar(FinanceiroMovimentacaoEntity m) {
        this.movimentacao = (m != null) ? m : new FinanceiroMovimentacaoEntity();
        if (this.movimentacao.getDataMovimentacao() == null) {
            this.movimentacao.setDataMovimentacao(OffsetDateTime.now());
        }
        return "/financeiro/cadastro.xhtml?faces-redirect=true";
    }

    public void excluir(FinanceiroMovimentacaoEntity m) {
        try {
            facade.remove(m);
            recarregarLista();
            addInfo("Excluído com sucesso.");
        } catch (Exception e) {
            addErro("Erro ao excluir: " + e.getMessage());
        }
    }

    public String salvar() {
        try {
            if (movimentacao.getId() == null) {
                facade.create(movimentacao);
                addInfo("Movimentação criada com sucesso.");
            } else {
                facade.edit(movimentacao);
                addInfo("Movimentação atualizada com sucesso.");
            }
            recarregarLista();
            return "/financeiro/lista.xhtml?faces-redirect=true";
        } catch (Exception e) {
            addErro("Erro ao salvar: " + e.getMessage());
            return null;
        }
    }

    public FinanceiroMovimentacaoEntity getMovimentacao() {
        if (movimentacao == null) prepararNovo();
        return movimentacao;
    }
    public void setMovimentacao(FinanceiroMovimentacaoEntity movimentacao) {
        this.movimentacao = movimentacao;
        if (this.movimentacao != null && this.movimentacao.getDataMovimentacao() == null) {
            this.movimentacao.setDataMovimentacao(OffsetDateTime.now());
        }
    }

    public List<FinanceiroMovimentacaoEntity> getLista() { return lista; }
    public void setLista(List<FinanceiroMovimentacaoEntity> lista) { this.lista = lista; }
    public List<FuncionarioEntity> getFuncionarios() { return funcionarios; }

    private void addInfo(String msg) {
        FacesContext.getCurrentInstance()
                .addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", msg));
    }
    private void addErro(String msg) {
        FacesContext.getCurrentInstance()
                .addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", msg));
    }
}
