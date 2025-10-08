
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
import java.util.ArrayList;
import java.util.List;

@Named(value = "financeiroMovimentacaoController")
@SessionScoped
public class FinanceiroMovimentacaoController implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private FinanceiroMovimentacaoFacade financeiroMovimentacaoFacade;
    @EJB
    private FuncionarioFacade funcionarioFacade;

    private FinanceiroMovimentacaoEntity movimentacao;
    private List<FinanceiroMovimentacaoEntity> listaMovimentacoes;
    private List<FuncionarioEntity> listaFuncionarios;
    private FuncionarioEntity funcionarioResponsavelSelecionado;

    @PostConstruct
    public void init() {
        movimentacao = new FinanceiroMovimentacaoEntity();
        listaMovimentacoes = new ArrayList<>();
        listaFuncionarios = new ArrayList<>();
        carregarFuncionarios();
        carregarMovimentacoes();
    }

    public void carregarFuncionarios() {
        try {
            listaFuncionarios = funcionarioFacade.findAll();
        } catch (Exception e) {
            adicionarMensagemErro("Erro ao carregar funcionários: " + e.getMessage());
        }
    }

    public void carregarMovimentacoes() {
        try {
            listaMovimentacoes = financeiroMovimentacaoFacade.findAll();
        } catch (Exception e) {
            adicionarMensagemErro("Erro ao carregar movimentações financeiras: " + e.getMessage());
        }
    }

    public String salvar() {
        try {
            if (funcionarioResponsavelSelecionado == null) {
                adicionarMensagemAviso("Selecione um funcionário responsável!");
                return null;
            }
            movimentacao.setFuncionarioResponsavel(funcionarioResponsavelSelecionado);

            if (movimentacao.getId() == null) {
                financeiroMovimentacaoFacade.create(movimentacao);
                adicionarMensagemInfo("Movimentação financeira registrada com sucesso!");
            } else {
                financeiroMovimentacaoFacade.edit(movimentacao);
                adicionarMensagemInfo("Movimentação financeira atualizada com sucesso!");
            }
            movimentacao = new FinanceiroMovimentacaoEntity();
            funcionarioResponsavelSelecionado = null;
            listaMovimentacoes = null;
            return "/financeiro/lista.xhtml?faces-redirect=true";
        } catch (Exception e) {
            adicionarMensagemErro("Erro ao salvar movimentação financeira: " + e.getMessage());
            return null;
        }
    }

    public String editar(FinanceiroMovimentacaoEntity fm) {
        this.movimentacao = fm;
        this.funcionarioResponsavelSelecionado = fm.getFuncionarioResponsavel();
        return "/financeiro/cadastro.xhtml?faces-redirect=true";
    }

    public String excluir(FinanceiroMovimentacaoEntity fm) {
        try {
            financeiroMovimentacaoFacade.remove(fm);
            listaMovimentacoes = null;
            adicionarMensagemInfo("Movimentação financeira excluída com sucesso!");
            return "/financeiro/lista.xhtml?faces-redirect=true";
        } catch (Exception e) {
            adicionarMensagemErro("Erro ao excluir movimentação financeira: " + e.getMessage());
            return null;
        }
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

    public FinanceiroMovimentacaoEntity getMovimentacao() {
        return movimentacao;
    }

    public void setMovimentacao(FinanceiroMovimentacaoEntity movimentacao) {
        this.movimentacao = movimentacao;
    }

    public List<FinanceiroMovimentacaoEntity> getListaMovimentacoes() {
        if (listaMovimentacoes == null) {
            carregarMovimentacoes();
        }
        return listaMovimentacoes;
    }

    public void setListaMovimentacoes(List<FinanceiroMovimentacaoEntity> listaMovimentacoes) {
        this.listaMovimentacoes = listaMovimentacoes;
    }

    public List<FuncionarioEntity> getListaFuncionarios() {
        return listaFuncionarios;
    }

    public void setListaFuncionarios(List<FuncionarioEntity> listaFuncionarios) {
        this.listaFuncionarios = listaFuncionarios;
    }

    public FuncionarioEntity getFuncionarioResponsavelSelecionado() {
        return funcionarioResponsavelSelecionado;
    }

    public void setFuncionarioResponsavelSelecionado(FuncionarioEntity funcionarioResponsavelSelecionado) {
        this.funcionarioResponsavelSelecionado = funcionarioResponsavelSelecionado;
    }
}

