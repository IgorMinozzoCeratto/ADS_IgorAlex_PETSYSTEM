
package br.upf.projetojfprimefaces.controller;

import br.upf.projetojfprimefaces.entity.ConsultaEntity;
import br.upf.projetojfprimefaces.entity.FinanceiroMovimentacaoEntity;
import br.upf.projetojfprimefaces.entity.PagamentoEntity;
import br.upf.projetojfprimefaces.entity.TutorEntity;
import br.upf.projetojfprimefaces.facade.ConsultaFacade;
import br.upf.projetojfprimefaces.facade.FinanceiroMovimentacaoFacade;
import br.upf.projetojfprimefaces.facade.PagamentoFacade;
import br.upf.projetojfprimefaces.facade.TutorFacade;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Named(value = "pagamentoController")
@SessionScoped
public class PagamentoController implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private PagamentoFacade pagamentoFacade;
    @EJB
    private TutorFacade tutorFacade;
    @EJB
    private FinanceiroMovimentacaoFacade financeiroMovimentacaoFacade;
    @EJB
    private ConsultaFacade consultaFacade;

    private PagamentoEntity pagamento;
    private List<PagamentoEntity> listaPagamentos;
    private List<TutorEntity> listaTutores;
    private List<FinanceiroMovimentacaoEntity> listaMovimentacoes;
    private List<ConsultaEntity> listaConsultas;
    private TutorEntity tutorSelecionado;
    private FinanceiroMovimentacaoEntity movimentacaoSelecionada;
    private ConsultaEntity consultaSelecionada;

    @PostConstruct
    public void init() {
        pagamento = new PagamentoEntity();
        listaPagamentos = new ArrayList<>();
        listaTutores = new ArrayList<>();
        listaMovimentacoes = new ArrayList<>();
        listaConsultas = new ArrayList<>();
        carregarTutores();
        carregarMovimentacoes();
        carregarConsultas();
        carregarPagamentos();
    }

    public void carregarTutores() {
        try {
            listaTutores = tutorFacade.findAll();
        } catch (Exception e) {
            adicionarMensagemErro("Erro ao carregar tutores: " + e.getMessage());
        }
    }

    public void carregarMovimentacoes() {
        try {
            listaMovimentacoes = financeiroMovimentacaoFacade.findAll();
        } catch (Exception e) {
            adicionarMensagemErro("Erro ao carregar movimentações financeiras: " + e.getMessage());
        }
    }

    public void carregarConsultas() {
        try {
            listaConsultas = consultaFacade.findAll();
        } catch (Exception e) {
            adicionarMensagemErro("Erro ao carregar consultas: " + e.getMessage());
        }
    }

    public void carregarPagamentos() {
        try {
            listaPagamentos = pagamentoFacade.findAll();
        } catch (Exception e) {
            adicionarMensagemErro("Erro ao carregar pagamentos: " + e.getMessage());
        }
    }

    public String salvar() {
        try {
            if (tutorSelecionado == null) {
                adicionarMensagemAviso("Selecione um tutor!");
                return null;
            }
            if (movimentacaoSelecionada == null) {
                adicionarMensagemAviso("Selecione uma movimentação financeira!");
                return null;
            }
            pagamento.setTutor(tutorSelecionado);
            pagamento.setMovimentacao(movimentacaoSelecionada);
            pagamento.setConsulta(consultaSelecionada); // Pode ser nulo

            if (pagamento.getId() == null) {
                pagamentoFacade.create(pagamento);
                adicionarMensagemInfo("Pagamento registrado com sucesso!");
            } else {
                pagamentoFacade.edit(pagamento);
                adicionarMensagemInfo("Pagamento atualizado com sucesso!");
            }
            pagamento = new PagamentoEntity();
            tutorSelecionado = null;
            movimentacaoSelecionada = null;
            consultaSelecionada = null;
            listaPagamentos = null;
            return "/pagamento/lista.xhtml?faces-redirect=true";
        } catch (Exception e) {
            adicionarMensagemErro("Erro ao salvar pagamento: " + e.getMessage());
            return null;
        }
    }

    public String editar(PagamentoEntity p) {
        this.pagamento = p;
        this.tutorSelecionado = p.getTutor();
        this.movimentacaoSelecionada = p.getMovimentacao();
        this.consultaSelecionada = p.getConsulta();
        return "/pagamento/cadastro.xhtml?faces-redirect=true";
    }

    public String excluir(PagamentoEntity p) {
        try {
            pagamentoFacade.remove(p);
            listaPagamentos = null;
            adicionarMensagemInfo("Pagamento excluído com sucesso!");
            return "/pagamento/lista.xhtml?faces-redirect=true";
        } catch (Exception e) {
            adicionarMensagemErro("Erro ao excluir pagamento: " + e.getMessage());
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

    public PagamentoEntity getPagamento() {
        return pagamento;
    }

    public void setPagamento(PagamentoEntity pagamento) {
        this.pagamento = pagamento;
    }

    public List<PagamentoEntity> getListaPagamentos() {
        if (listaPagamentos == null) {
            carregarPagamentos();
        }
        return listaPagamentos;
    }

    public void setListaPagamentos(List<PagamentoEntity> listaPagamentos) {
        this.listaPagamentos = listaPagamentos;
    }

    public List<TutorEntity> getListaTutores() {
        return listaTutores;
    }

    public void setListaTutores(List<TutorEntity> listaTutores) {
        this.listaTutores = listaTutores;
    }

    public List<FinanceiroMovimentacaoEntity> getListaMovimentacoes() {
        return listaMovimentacoes;
    }

    public void setListaMovimentacoes(List<FinanceiroMovimentacaoEntity> listaMovimentacoes) {
        this.listaMovimentacoes = listaMovimentacoes;
    }

    public List<ConsultaEntity> getListaConsultas() {
        return listaConsultas;
    }

    public void setListaConsultas(List<ConsultaEntity> listaConsultas) {
        this.listaConsultas = listaConsultas;
    }

    public TutorEntity getTutorSelecionado() {
        return tutorSelecionado;
    }

    public void setTutorSelecionado(TutorEntity tutorSelecionado) {
        this.tutorSelecionado = tutorSelecionado;
    }

    public FinanceiroMovimentacaoEntity getMovimentacaoSelecionada() {
        return movimentacaoSelecionada;
    }

    public void setMovimentacaoSelecionada(FinanceiroMovimentacaoEntity movimentacaoSelecionada) {
        this.movimentacaoSelecionada = movimentacaoSelecionada;
    }

    public ConsultaEntity getConsultaSelecionada() {
        return consultaSelecionada;
    }

    public void setConsultaSelecionada(ConsultaEntity consultaSelecionada) {
        this.consultaSelecionada = consultaSelecionada;
    }
}

