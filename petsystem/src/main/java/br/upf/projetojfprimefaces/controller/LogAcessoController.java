
package br.upf.projetojfprimefaces.controller;

import br.upf.projetojfprimefaces.entity.LogAcessoEntity;
import br.upf.projetojfprimefaces.facade.LogAcessoFacade;
import jakarta.ejb.EJB;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.List;

@Named(value = "logAcessoController")
@SessionScoped
public class LogAcessoController implements Serializable {

    @EJB
    private LogAcessoFacade logAcessoFacade;

    private LogAcessoEntity logAcesso;
    private List<LogAcessoEntity> listaLogAcessos;

    public LogAcessoController() {
        logAcesso = new LogAcessoEntity();
    }

    public String salvar() {
        if (logAcesso.getId() == null) {
            logAcessoFacade.create(logAcesso);
        } else {
            logAcessoFacade.edit(logAcesso);
        }
        logAcesso = new LogAcessoEntity();
        listaLogAcessos = null;
        return "/logAcesso/lista.xhtml?faces-redirect=true";
    }

    public String editar(LogAcessoEntity l) {
        this.logAcesso = l;
        return "/logAcesso/cadastro.xhtml?faces-redirect=true";
    }

    public String excluir(LogAcessoEntity l) {
        logAcessoFacade.remove(l);
        listaLogAcessos = null;
        return "/logAcesso/lista.xhtml?faces-redirect=true";
    }

    public LogAcessoEntity getLogAcesso() {
        return logAcesso;
    }

    public void setLogAcesso(LogAcessoEntity logAcesso) {
        this.logAcesso = logAcesso;
    }

    public List<LogAcessoEntity> getListaLogAcessos() {
        if (listaLogAcessos == null) {
            listaLogAcessos = logAcessoFacade.findAll();
        }
        return listaLogAcessos;
    }

    public void setListaLogAcessos(List<LogAcessoEntity> listaLogAcessos) {
        this.listaLogAcessos = listaLogAcessos;
    }

}

