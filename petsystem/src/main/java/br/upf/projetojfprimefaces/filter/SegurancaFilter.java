package br.upf.projetojfprimefaces.filter;

import br.upf.projetojfprimefaces.entity.FuncionarioEntity;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SegurancaFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest  req  = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        final String ctx = req.getContextPath();             // ex: /petsystem
        final String uri = req.getRequestURI();              // ex: /petsystem/faces/animal.xhtml

        // 1) LIBERAR RECURSOS (JSF/PrimeFaces/Manhattan)
        // cobre /faces/javax.faces.resource/... e /javax.faces.resource/...
        if (uri.contains("/javax.faces.resource/")) {
            chain.doFilter(request, response);
            return;
        }

        // (opcional) assets próprios
        if (uri.startsWith(ctx + "/resources/")
         || uri.startsWith(ctx + "/assets/")
         || uri.startsWith(ctx + "/static/")
         || uri.startsWith(ctx + "/webjars/")) {
            chain.doFilter(request, response);
            return;
        }

        // 2) LIBERAR APENAS O LOGIN (home NÃO é pública)
        // usa endsWith para não quebrar com ;jsessionid ou query string
        boolean isLogin =
            uri.endsWith("/login.xhtml")      // /login.xhtml
         || uri.endsWith("/faces/login.xhtml"); // /faces/login.xhtml

        if (isLogin) {
            chain.doFilter(request, response);
            return;
        }

        // 3) BLOQUEAR TUDO QUE SEJA .xhtml SEM SESSÃO
        boolean isXhtml = uri.endsWith(".xhtml"); // cobre *.xhtml (com ou sem /faces prefix)
        FuncionarioEntity usuario =
            (FuncionarioEntity) req.getSession().getAttribute("usuarioAutenticado");

        if (isXhtml && usuario == null) {
            // guarda URL original p/ pós-login
            String original = req.getRequestURL().toString();
            String qs = req.getQueryString();
            if (qs != null) original += "?" + qs;
            req.getSession().setAttribute("afterLoginGoTo", original);

            resp.sendRedirect(ctx + "/faces/login.xhtml");
            return;
        }

        // 4) SEGURO: segue o fluxo
        chain.doFilter(request, response);
    }
}
