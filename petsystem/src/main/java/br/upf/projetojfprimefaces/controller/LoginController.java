package br.upf.projetojfprimefaces.controller;

import br.upf.projetojfprimefaces.entity.FuncionarioEntity;
import br.upf.projetojfprimefaces.facade.FuncionarioFacade;
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
public class LoginController implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @EJB
    private FuncionarioFacade funcionarioFacade;
    
    private String login;
    private String senha;
    private FuncionarioEntity funcionarioLogado;
    
    @PostConstruct
    public void init() {
        // Inicialização, se necessário
    }
    
    public String autenticar() {
        try {
            funcionarioLogado = funcionarioFacade.buscarPorLogin(login, senha);
            
            if (funcionarioLogado != null) {
                // Autenticação bem-sucedida
                FacesContext.getCurrentInstance().addMessage(null, 
                        new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", "Login realizado com sucesso!"));
                
                // Redireciona para a página principal
                return "/animal.xhtml?faces-redirect=true";
            } else {
                // Autenticação falhou
                FacesContext.getCurrentInstance().addMessage(null, 
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Login ou senha inválidos!"));
                return null;
            }
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, 
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Erro ao realizar login: " + e.getMessage()));
            return null;
        }
    }
    
    public String logout() {
        // Invalida a sessão
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        return "/login.xhtml?faces-redirect=true";
    }
    
    public boolean isLogado() {
        return funcionarioLogado != null;
    }
    
    public boolean isFuncionario1() {
        return isLogado() && funcionarioLogado.getTipo() == FuncionarioEntity.TipoFuncionario.FUNCIONARIO_1;
    }
    
    public boolean isFuncionario2() {
        return isLogado() && funcionarioLogado.getTipo() == FuncionarioEntity.TipoFuncionario.FUNCIONARIO_2;
    }
    
    public boolean isVeterinario() {
        return isLogado() && funcionarioLogado.getTipo() == FuncionarioEntity.TipoFuncionario.VETERINARIO;
    }

    // Getters e Setters
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
