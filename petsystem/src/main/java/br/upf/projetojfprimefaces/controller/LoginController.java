package br.upf.projetojfprimefaces.controller;

import br.upf.projetojfprimefaces.entity.FuncionarioEntity;
import br.upf.projetojfprimefaces.entity.PerfilEntity;
import br.upf.projetojfprimefaces.facade.FuncionarioFacade;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import java.io.IOException;
import java.io.Serializable;

@Named("loginController")
@SessionScoped
public class LoginController implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private FuncionarioFacade funcionarioFacade;

    private String login;
    private String senha;
    private FuncionarioEntity funcionarioLogado;

    @PostConstruct
    public void init() {
        login = "";
        senha = "";
        funcionarioLogado = null;
    }

    public String autenticar() {
        try {
            funcionarioLogado = funcionarioFacade.buscarPorLogin(login, senha);

            if (funcionarioLogado == null) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Login ou senha inválidos!"));
                return null;
            }

            ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
            ec.getSessionMap().put("usuarioAutenticado", funcionarioLogado);
            ec.getSessionMap().put("funcionarioLogado", funcionarioLogado);

            String ctx = ec.getRequestContextPath();
            String after = (String) ec.getSessionMap().remove("afterLoginGoTo");

            if (after != null && !after.isBlank()
                    && !after.endsWith("/login.xhtml")
                    && !after.endsWith("/faces/login.xhtml")) {
                ec.redirect(after);
            } else {
                ec.redirect(ctx + "/faces/home.xhtml");
            }
            return null;

        } catch (IOException e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Falha ao redirecionar."));
            return null;
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Falha ao realizar login."));
            return null;
        }
    }

    public String logout() {
        try {
            ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
            ec.invalidateSession();
            ec.redirect(ec.getRequestContextPath() + "/faces/login.xhtml");
        } catch (IOException e) {
            return "/login.xhtml?faces-redirect=true";
        }
        return null;
    }

    public boolean isLogado() {
        return funcionarioLogado != null;
    }

    public boolean isAdministrador() {
        return funcionarioLogado != null
                && funcionarioLogado.getPerfil() != null
                && PerfilEntity.ADMINISTRADOR_ID == funcionarioLogado.getPerfil().getId();
    }

    public boolean isVeterinario() {
        return funcionarioLogado != null
                && funcionarioLogado.getPerfil() != null
                && PerfilEntity.VETERINARIO_ID == funcionarioLogado.getPerfil().getId();
    }

    public boolean isRecepcionista() {
        return funcionarioLogado != null
                && funcionarioLogado.getPerfil() != null
                && PerfilEntity.RECEPCIONISTA_ID == funcionarioLogado.getPerfil().getId();
    }

    public boolean isCliente() {
        return funcionarioLogado != null
                && funcionarioLogado.getPerfil() != null
                && PerfilEntity.CLIENTE_ID == funcionarioLogado.getPerfil().getId();
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public FuncionarioEntity getFuncionarioLogado() {
        return funcionarioLogado;
    }

    public void setFuncionarioLogado(FuncionarioEntity funcionarioLogado) {
        this.funcionarioLogado = funcionarioLogado;
    }
}
