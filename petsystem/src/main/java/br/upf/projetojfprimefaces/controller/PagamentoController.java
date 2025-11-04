package br.upf.projetojfprimefaces.controller;

import br.upf.projetojfprimefaces.entity.FinanceiroMovimentacaoEntity;
import br.upf.projetojfprimefaces.entity.PagamentoEntity;
import br.upf.projetojfprimefaces.facade.FinanceiroMovimentacaoFacade;
import br.upf.projetojfprimefaces.facade.PagamentoFacade;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Named("pagamentoController")
@ViewScoped
public class PagamentoController implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @EJB
    private PagamentoFacade pagamentoFacade;

    @EJB
    private FinanceiroMovimentacaoFacade movimentacaoFacade;

    /** Lista de pagamentos para a tela de cadastro/listagem (se houver) */
    private List<PagamentoEntity> lista;

    /** Pagamento em edição */
    private PagamentoEntity pagamento;

    /** Exigido pelo xhtml: value="#{pagamentoController.movimentacaoSelecionada}" em <p:selectOneMenu> */
    private FinanceiroMovimentacaoEntity movimentacaoSelecionada;

    /** Para popular o selectOneMenu de movimentações */
    private List<FinanceiroMovimentacaoEntity> movimentacoes;

    @PostConstruct
    public void init() {
        refreshListas();
        novo();
    }

    public void refreshListas() {
        try {
            lista = pagamentoFacade.findAll();
            if (lista == null) lista = new ArrayList<>();

            movimentacoes = movimentacaoFacade.findAll();
            if (movimentacoes == null) movimentacoes = new ArrayList<>();
        } catch (Exception e) {
            lista = new ArrayList<>();
            movimentacoes = new ArrayList<>();
            addMsg(FacesMessage.SEVERITY_ERROR, "Erro ao carregar dados", e.getMessage());
        }
    }

    public void novo() {
        pagamento = new PagamentoEntity();
        movimentacaoSelecionada = null;
    }

    public void salvar() {
        try {
            // Garanta que o relacionamento seja setado antes de persistir
            // Ajuste o setter conforme o nome do atributo em PagamentoEntity (ex.: setMovimentacao, setFinanceiroMovimentacao, etc.)
            pagamento.setMovimentacao(movimentacaoSelecionada);

            if (pagamento.getId() == null) {
                pagamentoFacade.create(pagamento);
                addMsg(FacesMessage.SEVERITY_INFO, "Pagamento criado", null);
            } else {
                pagamentoFacade.edit(pagamento);
                addMsg(FacesMessage.SEVERITY_INFO, "Pagamento atualizado", null);
            }
            refreshListas();
            novo();
        } catch (Exception e) {
            addMsg(FacesMessage.SEVERITY_ERROR, "Falha ao salvar pagamento", e.getMessage());
        }
    }

    public void editar(PagamentoEntity item) {
        if (item != null) {
            this.pagamento = item;
            // alinha o select com a movimentação já vinculada
            this.movimentacaoSelecionada = item.getMovimentacao();
        }
    }

    public void excluir(PagamentoEntity item) {
        try {
            pagamentoFacade.remove(item);
            addMsg(FacesMessage.SEVERITY_INFO, "Pagamento excluído", null);
            refreshListas();
        } catch (Exception e) {
            addMsg(FacesMessage.SEVERITY_ERROR, "Falha ao excluir", e.getMessage());
        }
    }

    private void addMsg(FacesMessage.Severity sev, String sum, String det) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(sev, sum, det));
    }

    /* ===== GETTERS/SETTERS exigidos pelos xhtml ===== */

    public List<PagamentoEntity> getLista() {
        return lista;
    }

    public PagamentoEntity getPagamento() {
        return pagamento;
    }

    public void setPagamento(PagamentoEntity pagamento) {
        this.pagamento = pagamento;
    }

    /** Necessário pelo xhtml @45,74 (selectOneMenu) */
    public FinanceiroMovimentacaoEntity getMovimentacaoSelecionada() {
        return movimentacaoSelecionada;
    }

    public void setMovimentacaoSelecionada(FinanceiroMovimentacaoEntity movimentacaoSelecionada) {
        this.movimentacaoSelecionada = movimentacaoSelecionada;
    }

    /** Para popular o <p:selectOneMenu> das movimentações */
    public List<FinanceiroMovimentacaoEntity> getMovimentacoes() {
        return movimentacoes;
    }
}
