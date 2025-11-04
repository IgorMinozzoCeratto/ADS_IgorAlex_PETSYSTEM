package br.upf.projetojfprimefaces.controller;

import br.upf.projetojfprimefaces.entity.FinanceiroMovimentacaoEntity;
import br.upf.projetojfprimefaces.facade.FinanceiroMovimentacaoFacade;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.enterprise.context.Dependent;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Named("financeiroMovimentacaoController")
@ViewScoped
public class FinanceiroMovimentacaoController implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @EJB
    private FinanceiroMovimentacaoFacade movimentacaoFacade;

    /** Exigido pelo xhtml: value="#{financeiroMovimentacaoController.lista}" */
    private List<FinanceiroMovimentacaoEntity> lista;

    /** Item atualmente em edição/novo */
    private FinanceiroMovimentacaoEntity selecionado;

    @PostConstruct
    public void init() {
        refreshLista();
        novo();
    }

    public void refreshLista() {
        try {
            lista = movimentacaoFacade.findAll();
            if (lista == null) lista = new ArrayList<>();
        } catch (Exception e) {
            lista = new ArrayList<>();
            addMsg(FacesMessage.SEVERITY_ERROR, "Erro ao carregar lista", e.getMessage());
        }
    }

    public void novo() {
        selecionado = new FinanceiroMovimentacaoEntity();
    }

    public void salvar() {
        try {
            if (selecionado.getId() == null) {
                movimentacaoFacade.create(selecionado);
                addMsg(FacesMessage.SEVERITY_INFO, "Movimentação criada", null);
            } else {
                movimentacaoFacade.edit(selecionado);
                addMsg(FacesMessage.SEVERITY_INFO, "Movimentação atualizada", null);
            }
            refreshLista();
            novo();
        } catch (Exception e) {
            addMsg(FacesMessage.SEVERITY_ERROR, "Falha ao salvar", e.getMessage());
        }
    }

    public void editar(FinanceiroMovimentacaoEntity item) {
        if (item != null) {
            this.selecionado = item;
        }
    }

    public void excluir(FinanceiroMovimentacaoEntity item) {
        try {
            movimentacaoFacade.remove(item);
            addMsg(FacesMessage.SEVERITY_INFO, "Movimentação excluída", null);
            refreshLista();
        } catch (Exception e) {
            addMsg(FacesMessage.SEVERITY_ERROR, "Falha ao excluir", e.getMessage());
        }
    }

    private void addMsg(FacesMessage.Severity sev, String sum, String det) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(sev, sum, det));
    }

    /* ===== GETTERS/SETTERS exigidos pelos xhtml ===== */

    public List<FinanceiroMovimentacaoEntity> getLista() {
        return lista;
    }

    public FinanceiroMovimentacaoEntity getSelecionado() {
        return selecionado;
    }

    public void setSelecionado(FinanceiroMovimentacaoEntity selecionado) {
        this.selecionado = selecionado;
    }
}
