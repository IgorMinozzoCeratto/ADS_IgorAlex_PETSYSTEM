
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
    
    @EJB
    private VacinacaoFacade vacinacaoFacade;
    
    @EJB
    private ProntuarioFacade prontuarioFacade;
    
    @EJB
    private FuncionarioFacade funcionarioFacade;
    
    private VacinacaoEntity vacinacao;
    private List<VacinacaoEntity> vacinacoes;
    private List<ProntuarioEntity> prontuarios;
    private List<FuncionarioEntity> funcionariosAplicadores;
    private ProntuarioEntity prontuarioSelecionado;
    private FuncionarioEntity funcionarioAplicadorSelecionado;
    private String filtroTipoVacina;
    
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
            FacesContext.getCurrentInstance().addMessage(null, 
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Erro ao carregar prontuários: " + e.getMessage()));
        }
    }
    
    public void carregarFuncionariosAplicadores() {
        try {
            // Assumindo que todos os funcionários podem aplicar vacinas
            funcionariosAplicadores = funcionarioFacade.findAll(); 
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, 
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Erro ao carregar funcionários aplicadores: " + e.getMessage()));
        }
    }
    
    public void carregarVacinacoes() {
        try {
            vacinacoes = vacinacaoFacade.findAll();
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, 
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Erro ao carregar vacinações: " + e.getMessage()));
        }
    }
    
    public void filtrarVacinacoesPorProntuario() {
        try {
            if (prontuarioSelecionado != null) {
                // vacinacoes = vacinacaoFacade.buscarPorProntuario(prontuarioSelecionado);
                adicionarMensagemAviso("Filtro por prontuário ainda não implementado na fachada.");
            } else {
                carregarVacinacoes();
            }
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, 
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Erro ao filtrar vacinações por prontuário: " + e.getMessage()));
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
            FacesContext.getCurrentInstance().addMessage(null, 
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Erro ao filtrar vacinações por tipo: " + e.getMessage()));
        }
    }
    
    public void prepararNovaVacinacao() {
        vacinacao = new VacinacaoEntity();
        vacinacao.setDataAplicacao(new Date()); // Data atual como padrão
        prontuarioSelecionado = null;
        funcionarioAplicadorSelecionado = null;
    }
    
    public void prepararEditarVacinacao(VacinacaoEntity vacinacaoSelecionada) {
        vacinacao = vacinacaoSelecionada;
        prontuarioSelecionado = vacinacaoSelecionada.getProntuario();
        funcionarioAplicadorSelecionado = vacinacaoSelecionada.getFuncionarioAplicador();
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
            vacinacao.setProntuario(prontuarioSelecionado);
            vacinacao.setFuncionarioAplicador(funcionarioAplicadorSelecionado);
            
            if (vacinacao.getId() == null) {
                vacinacaoFacade.create(vacinacao);
                FacesContext.getCurrentInstance().addMessage(null, 
                        new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", "Vacinação registrada com sucesso!"));
            } else {
                vacinacaoFacade.edit(vacinacao);
                FacesContext.getCurrentInstance().addMessage(null, 
                        new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", "Vacinação atualizada com sucesso!"));
            }
            
            carregarVacinacoes();
            prepararNovaVacinacao();
            
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, 
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Erro ao salvar vacinação: " + e.getMessage()));
        }
    }
    
    public void excluirVacinacao(VacinacaoEntity vacinacaoSelecionada) {
        try {
            vacinacaoFacade.remove(vacinacaoSelecionada);
            FacesContext.getCurrentInstance().addMessage(null, 
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", "Registro de vacinação excluído com sucesso!"));
            
            carregarVacinacoes();
            
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, 
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Erro ao excluir vacinação: " + e.getMessage()));
        }
    }

    // Getters e Setters
    public VacinacaoEntity getVacinacao() {
        return vacinacao;
    }

    public void setVacinacao(VacinacaoEntity vacinacao) {
        this.vacinacao = vacinacao;
    }

    public List<VacinacaoEntity> getVacinacoes() {
        return vacinacoes;
    }

    public void setVacinacoes(List<VacinacaoEntity> vacinacoes) {
        this.vacinacoes = vacinacoes;
    }

    public List<ProntuarioEntity> getProntuarios() {
        return prontuarios;
    }

    public void setProntuarios(List<ProntuarioEntity> prontuarios) {
        this.prontuarios = prontuarios;
    }

    public List<FuncionarioEntity> getFuncionariosAplicadores() {
        return funcionariosAplicadores;
    }

    public void setFuncionariosAplicadores(List<FuncionarioEntity> funcionariosAplicadores) {
        this.funcionariosAplicadores = funcionariosAplicadores;
    }

    public ProntuarioEntity getProntuarioSelecionado() {
        return prontuarioSelecionado;
    }

    public void setProntuarioSelecionado(ProntuarioEntity prontuarioSelecionado) {
        this.prontuarioSelecionado = prontuarioSelecionado;
    }

    public FuncionarioEntity getFuncionarioAplicadorSelecionado() {
        return funcionarioAplicadorSelecionado;
    }

    public void setFuncionarioAplicadorSelecionado(FuncionarioEntity funcionarioAplicadorSelecionado) {
        this.funcionarioAplicadorSelecionado = funcionarioAplicadorSelecionado;
    }

    public String getFiltroTipoVacina() {
        return filtroTipoVacina;
    }

    public void setFiltroTipoVacina(String filtroTipoVacina) {
        this.filtroTipoVacina = filtroTipoVacina;
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
}

