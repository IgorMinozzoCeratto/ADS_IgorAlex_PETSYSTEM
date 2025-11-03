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
    private List<FuncionarioEntity> funcionariosAplicadores;

    // Campos do FORMULÁRIO
    private ProntuarioEntity prontuarioSelecionado;
    private FuncionarioEntity funcionarioAplicadorSelecionado;

    // Campos de FILTRO (separados para não sobrescrever os do formulário)
    private ProntuarioEntity prontuarioFiltroSelecionado;
    private String filtroTipoVacina;

    /** 0 = formulário, 1 = histórico (lista) */
    private int tabIndex = 1;

    @PostConstruct
    public void init() {
        vacinacao = new VacinacaoEntity();
        vacinacoes = new ArrayList<>();
        prontuarios = new ArrayList<>();
        funcionariosAplicadores = new ArrayList<>();
        carregarProntuarios();
        carregarFuncionariosAplicadores();
        carregarVacinacoes();
    }

    public void carregarProntuarios() {
        try {
            prontuarios = prontuarioFacade.findAll();
        } catch (Exception e) {
            adicionarMensagemErro("Erro ao carregar prontuários: " + e.getMessage());
        }
    }

    public void carregarFuncionariosAplicadores() {
        try {
            funcionariosAplicadores = funcionarioFacade.findAll(); // filtre se tiver perfil próprio
        } catch (Exception e) {
            adicionarMensagemErro("Erro ao carregar funcionários aplicadores: " + e.getMessage());
        }
    }

    public void carregarVacinacoes() {
        try {
            vacinacoes = vacinacaoFacade.findAll();
        } catch (Exception e) {
            adicionarMensagemErro("Erro ao carregar vacinações: " + e.getMessage());
        }
    }

    // ====== FILTROS ======

    public void filtrarVacinacoesPorProntuario() {
        try {
            if (prontuarioFiltroSelecionado != null) {
                // vacinacoes = vacinacaoFacade.buscarPorProntuario(prontuarioFiltroSelecionado);
                adicionarMensagemAviso("Filtro por prontuário ainda não implementado na fachada.");
            } else {
                carregarVacinacoes();
            }
        } catch (Exception e) {
            adicionarMensagemErro("Erro ao filtrar vacinações por prontuário: " + e.getMessage());
        }
    }

    public void filtrarVacinacoesPorTipo() {
        try {
            if (filtroTipoVacina != null && !filtroTipoVacina.isEmpty()) {
                // vacinacoes = vacinacaoFacade.buscarPorTipoVacina(filtroTipoVacina);
                adicionarMensagemAviso("Filtro por tipo de vacina ainda não implementado na fachada.");
            } else {
                carregarVacinacoes();
            }
        } catch (Exception e) {
            adicionarMensagemErro("Erro ao filtrar vacinações por tipo: " + e.getMessage());
        }
    }

    // ====== FLUXO FORM ======

    public void prepararNovaVacinacao() {
        vacinacao = new VacinacaoEntity();
        vacinacao.setDataAplicacao(new Date()); // padrão: hoje
        prontuarioSelecionado = null;
        funcionarioAplicadorSelecionado = null;
        // NÃO zera prontuarioFiltroSelecionado
        tabIndex = 0; // vai para o formulário
    }

    public void prepararEditarVacinacao(VacinacaoEntity vacinacaoSelecionada) {
        try {
            // carrega gerenciada pelo ID (evita detached/lazy)
            this.vacinacao = vacinacaoFacade.find(vacinacaoSelecionada.getId());
            this.prontuarioSelecionado = vacinacao.getProntuario();
            this.funcionarioAplicadorSelecionado = vacinacao.getFuncionarioAplicador();
            tabIndex = 0; // abre a aba do formulário
        } catch (Exception e) {
            adicionarMensagemErro("Erro ao preparar edição: " + e.getMessage());
        }
    }

    public void salvarVacinacao() {
        try {
            if (prontuarioSelecionado == null) {
                adicionarMensagemAviso("Selecione um prontuário!");
                return;
            }
            if (funcionarioAplicadorSelecionado == null) {
                adicionarMensagemAviso("Selecione um funcionário aplicador!");
                return;
            }

            // sincroniza selecionados -> entity
            vacinacao.setProntuario(prontuarioSelecionado);
            vacinacao.setFuncionarioAplicador(funcionarioAplicadorSelecionado);

            if (vacinacao.getId() == null) {
                vacinacaoFacade.create(vacinacao);
                adicionarMensagemInfo("Vacinação registrada com sucesso!");
            } else {
                vacinacaoFacade.edit(vacinacao);
                adicionarMensagemInfo("Vacinação atualizada com sucesso!");
            }

            carregarVacinacoes();
            tabIndex = 1;            // volta para a aba histórico
            prepararNovaVacinacao();  // deixa pronto para novo registro (opcional)

        } catch (Exception e) {
            adicionarMensagemErro("Erro ao salvar vacinação: " + e.getMessage());
        }
    }

    public void excluirVacinacao(VacinacaoEntity vacinacaoSelecionada) {
        try {
            vacinacaoFacade.remove(vacinacaoSelecionada);
            adicionarMensagemInfo("Registro de vacinação excluído com sucesso!");
            carregarVacinacoes();
        } catch (Exception e) {
            adicionarMensagemErro("Erro ao excluir vacinação: " + e.getMessage());
        }
    }

    // ====== Getters/Setters ======

    public VacinacaoEntity getVacinacao() { return vacinacao; }
    public void setVacinacao(VacinacaoEntity vacinacao) { this.vacinacao = vacinacao; }

    public List<VacinacaoEntity> getVacinacoes() { return vacinacoes; }
    public void setVacinacoes(List<VacinacaoEntity> vacinacoes) { this.vacinacoes = vacinacoes; }

    public List<ProntuarioEntity> getProntuarios() { return prontuarios; }
    public void setProntuarios(List<ProntuarioEntity> prontuarios) { this.prontuarios = prontuarios; }

    public List<FuncionarioEntity> getFuncionariosAplicadores() { return funcionariosAplicadores; }
    public void setFuncionariosAplicadores(List<FuncionarioEntity> funcionariosAplicadores) { this.funcionariosAplicadores = funcionariosAplicadores; }

    // formulário
    public ProntuarioEntity getProntuarioSelecionado() { return prontuarioSelecionado; }
    public void setProntuarioSelecionado(ProntuarioEntity prontuarioSelecionado) { this.prontuarioSelecionado = prontuarioSelecionado; }

    public FuncionarioEntity getFuncionarioAplicadorSelecionado() { return funcionarioAplicadorSelecionado; }
    public void setFuncionarioAplicadorSelecionado(FuncionarioEntity funcionarioAplicadorSelecionado) { this.funcionarioAplicadorSelecionado = funcionarioAplicadorSelecionado; }

    // filtro
    public ProntuarioEntity getProntuarioFiltroSelecionado() { return prontuarioFiltroSelecionado; }
    public void setProntuarioFiltroSelecionado(ProntuarioEntity prontuarioFiltroSelecionado) { this.prontuarioFiltroSelecionado = prontuarioFiltroSelecionado; }

    public String getFiltroTipoVacina() { return filtroTipoVacina; }
    public void setFiltroTipoVacina(String filtroTipoVacina) { this.filtroTipoVacina = filtroTipoVacina; }

    public int getTabIndex() { return tabIndex; }
    public void setTabIndex(int tabIndex) { this.tabIndex = tabIndex; }

    // ====== mensagens ======
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
