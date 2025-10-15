package br.upf.projetojfprimefaces.controller;

import br.upf.projetojfprimefaces.entity.FuncionarioEntity;
import br.upf.projetojfprimefaces.entity.PerfilEntity;
import br.upf.projetojfprimefaces.facade.FuncionarioFacade;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import java.io.Serializable;

@Named
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
        // Pode ser usado para carregar dados iniciais, se necessário
    }

    // ==========================
    // ===== Autenticação =======
    // ==========================
    public String autenticar() {
        try {
            funcionarioLogado = funcionarioFacade.buscarPorLogin(login, senha);

            if (funcionarioLogado != null) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", "Login realizado com sucesso!"));

                // Redireciona para a tela principal (ajuste se necessário)
                return "/animal.xhtml?faces-redirect=true";
            } else {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Login ou senha inválidos!"));
                return null;
            }
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Falha ao realizar login."));
            e.printStackTrace();
            return null;
        }
    }

    public String logout() {
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        return "/login.xhtml?faces-redirect=true";
    }

    // ==========================
    // ===== Situação ===========
    // ==========================
    public boolean isLogado() {
        return funcionarioLogado != null;
    }

    // ==========================
    // ===== Perfis =============
    // ==========================

    public boolean isAdministrador() {
        return isLogado()
                && funcionarioLogado.getPerfil() != null
                && PerfilEntity.ADMINISTRADOR_ID == funcionarioLogado.getPerfil().getId();
    }

    public boolean isVeterinario() {
        return isLogado()
                && funcionarioLogado.getPerfil() != null
                && PerfilEntity.VETERINARIO_ID == funcionarioLogado.getPerfil().getId();
    }

    public boolean isRecepcionista() {
        return isLogado()
                && funcionarioLogado.getPerfil() != null
                && PerfilEntity.RECEPCIONISTA_ID == funcionarioLogado.getPerfil().getId();
    }

    public boolean isCliente() {
        return isLogado()
                && funcionarioLogado.getPerfil() != null
                && PerfilEntity.CLIENTE_ID == funcionarioLogado.getPerfil().getId();
    }

    // ==========================
    // ===== Getters/Setters ====
    // ==========================
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
