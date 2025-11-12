package br.upf.projetojfprimefaces.controller;

import br.upf.projetojfprimefaces.entity.PagamentoEntity;
import br.upf.projetojfprimefaces.entity.TutorEntity;
import br.upf.projetojfprimefaces.entity.FinanceiroMovimentacaoEntity;
import br.upf.projetojfprimefaces.entity.ConsultaEntity;
import br.upf.projetojfprimefaces.facade.PagamentoFacade;
import br.upf.projetojfprimefaces.facade.TutorFacade;
import br.upf.projetojfprimefaces.facade.FinanceiroMovimentacaoFacade;
import br.upf.projetojfprimefaces.facade.ConsultaFacade;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Named
@SessionScoped
public class PagamentoController implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB private PagamentoFacade pagamentoFacade;
    @EJB private TutorFacade tutorFacade;
    @EJB private FinanceiroMovimentacaoFacade movimentacaoFacade;
    @EJB private ConsultaFacade consultaFacade; // se houver campo consulta

    private PagamentoEntity pagamento;
    private List<PagamentoEntity> lista = new ArrayList<>();

    @PostConstruct
    public void init() {
        recarregarLista();
    }

    // ---------- inicialização preguiçosa (abre cadastro mesmo por URL) ----------
    public PagamentoEntity getPagamento() {
        if (pagamento == null) {
            pagamento = new PagamentoEntity();
            pagamento.setDataPagamento(new Date());
            pagamento.setValorPago(BigDecimal.ZERO);
            pagamento.setFormaPagamento("");
        }
        return pagamento;
    }
    public void setPagamento(PagamentoEntity pagamento) { this.pagamento = pagamento; }

    // --------------------- Navegação / ciclo de tela ---------------------------
    public String novo() {
        pagamento = new PagamentoEntity();
        pagamento.setDataPagamento(new Date());
        pagamento.setValorPago(BigDecimal.ZERO);
        pagamento.setFormaPagamento("");
        return "/pagamento/cadastro.xhtml?faces-redirect=true";
    }

    public String editar(PagamentoEntity p) {
        pagamento = p;
        return "/pagamento/cadastro.xhtml?faces-redirect=true";
    }

    public String cancelar() {
        pagamento = null;
        return "/pagamento/lista.xhtml?faces-redirect=true";
    }

    public void recarregarLista() {
        lista = pagamentoFacade.findAll();
    }

    // ------------------------------- CRUD -------------------------------------
    public String salvar() {
        if (pagamento == null) {
            addMsg(FacesMessage.SEVERITY_ERROR, "Pagamento não inicializado.");
            return null;
        }
        if (pagamento.getValorPago() == null) {
            addMsg(FacesMessage.SEVERITY_ERROR, "Informe o valor pago.");
            return null;
        }
        if (pagamento.getDataPagamento() == null) {
            addMsg(FacesMessage.SEVERITY_ERROR, "Informe a data de pagamento.");
            return null;
        }
        if (pagamento.getFormaPagamento() == null || pagamento.getFormaPagamento().isBlank()) {
            addMsg(FacesMessage.SEVERITY_ERROR, "Informe a forma de pagamento.");
            return null;
        }
        if (pagamento.getTutor() == null || pagamento.getTutor().getId() == null) {
            addMsg(FacesMessage.SEVERITY_ERROR, "Selecione o tutor.");
            return null;
        }
        if (pagamento.getMovimentacao() == null || pagamento.getMovimentacao().getId() == null) {
            addMsg(FacesMessage.SEVERITY_ERROR, "Selecione a movimentação financeira.");
            return null;
        }

        try {
            if (pagamento.getId() == null) {
                pagamentoFacade.create(pagamento);
                addMsg(FacesMessage.SEVERITY_INFO, "Pagamento criado com sucesso!");
            } else {
                pagamentoFacade.edit(pagamento);
                addMsg(FacesMessage.SEVERITY_INFO, "Pagamento atualizado com sucesso!");
            }
            recarregarLista();
            pagamento = null;
            return "/pagamento/lista.xhtml?faces-redirect=true";
        } catch (Exception ex) {
            handleConstraintViolations(ex);
            addMsg(FacesMessage.SEVERITY_ERROR, "Erro ao salvar: " + rootMessage(ex));
            return null;
        }
    }

    public void excluir(PagamentoEntity p) {
        try {
            pagamentoFacade.remove(p);
            recarregarLista();
            addMsg(FacesMessage.SEVERITY_INFO, "Pagamento excluído.");
        } catch (Exception ex) {
            handleConstraintViolations(ex);
            addMsg(FacesMessage.SEVERITY_ERROR, "Erro ao excluir: " + rootMessage(ex));
        }
    }

    // ------------------------ Utilidades/Mensagens ----------------------------
    private void addMsg(FacesMessage.Severity s, String m) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(s, m, null));
    }

    private String rootMessage(Throwable t) {
        Throwable x = t;
        while (x.getCause() != null && !Objects.equals(x.getCause(), x)) {
            x = x.getCause();
        }
        return (x.getMessage() != null ? x.getMessage() : x.toString());
    }

    private void handleConstraintViolations(Exception ex) {
        Throwable x = ex;
        while (x != null) {
            if (x instanceof ConstraintViolationException) {
                ConstraintViolationException cve = (ConstraintViolationException) x;
                for (ConstraintViolation<?> cv : cve.getConstraintViolations()) {
                    addMsg(FacesMessage.SEVERITY_ERROR,
                            (cv.getPropertyPath() != null ? cv.getPropertyPath().toString() + ": " : "")
                                    + cv.getMessage());
                }
                break;
            }
            x = x.getCause();
        }
    }

    // ----------------------------- Listas -------------------------------------
    public List<PagamentoEntity> getLista() { return lista; }
    public List<TutorEntity> getTutores() { return tutorFacade.findAll(); }
    public List<FinanceiroMovimentacaoEntity> getMovimentacoes() { return movimentacaoFacade.findAll(); }
    public List<ConsultaEntity> getConsultas() {
        return consultaFacade != null ? consultaFacade.findAll() : new ArrayList<>();
    }
}
