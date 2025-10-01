
package br.upf.projetojfprimefaces.controller;

import br.upf.projetojfprimefaces.entity.ConvenioEntity;
import br.upf.projetojfprimefaces.facade.ConvenioFacade;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

@Named(value = "convenioController")
@SessionScoped
public class ConvenioController implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private ConvenioFacade convenioFacade;

    private ConvenioEntity convenio;
    private List<ConvenioEntity> listaConvenios;

    public ConvenioController() {
        convenio = new ConvenioEntity();
    }

    public String salvar() {
        try {
            if (convenio.getId() == null) {
                convenioFacade.create(convenio);
                adicionarMensagemInfo("Convênio cadastrado com sucesso!");
            } else {
                convenioFacade.edit(convenio);
                adicionarMensagemInfo("Convênio atualizado com sucesso!");
            }
            convenio = new ConvenioEntity();
            listaConvenios = null;
            return "/convenio/lista.xhtml?faces-redirect=true";
        } catch (Exception e) {
            adicionarMensagemErro("Erro ao salvar convênio: " + e.getMessage());
            return null;
        }
    }

    public String editar(ConvenioEntity c) {
        this.convenio = c;
        return "/convenio/cadastro.xhtml?faces-redirect=true";
    }

    public String excluir(ConvenioEntity c) {
        try {
            convenioFacade.remove(c);
            listaConvenios = null;
            adicionarMensagemInfo("Convênio excluído com sucesso!");
            return "/convenio/lista.xhtml?faces-redirect=true";
        } catch (Exception e) {
            adicionarMensagemErro("Erro ao excluir convênio: " + e.getMessage());
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

    public ConvenioEntity getConvenio() {
        return convenio;
    }

    public void setConvenio(ConvenioEntity convenio) {
        this.convenio = convenio;
    }

    public List<ConvenioEntity> getListaConvenios() {
        if (listaConvenios == null) {
            listaConvenios = convenioFacade.findAll();
        }
        return listaConvenios;
    }

    public void setListaConvenios(List<ConvenioEntity> listaConvenios) {
        this.listaConvenios = listaConvenios;
    }
}

