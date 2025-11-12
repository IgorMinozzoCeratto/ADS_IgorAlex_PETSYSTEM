package br.upf.projetojfprimefaces.converter;

import br.upf.projetojfprimefaces.entity.FinanceiroMovimentacaoEntity;
import br.upf.projetojfprimefaces.facade.FinanceiroMovimentacaoFacade;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;
import jakarta.inject.Inject;

@ApplicationScoped
@FacesConverter(value = "financeiroMovimentacaoConverter", managed = true)
public class FinanceiroMovimentacaoConverter implements Converter<FinanceiroMovimentacaoEntity> {

    @Inject
    private FinanceiroMovimentacaoFacade facade;

    @Override
    public FinanceiroMovimentacaoEntity getAsObject(FacesContext ctx, UIComponent comp, String value) {
        if (value == null || value.isBlank()) return null;
        try {
            Integer id = Integer.valueOf(value);
            return facade.find(id);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    @Override
    public String getAsString(FacesContext ctx, UIComponent comp, FinanceiroMovimentacaoEntity value) {
        if (value == null || value.getId() == null) return "";
        return String.valueOf(value.getId());
    }
}
