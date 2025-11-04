package br.upf.projetojfprimefaces.converter;

import br.upf.projetojfprimefaces.entity.FinanceiroMovimentacaoEntity;
import br.upf.projetojfprimefaces.facade.FinanceiroMovimentacaoFacade;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;
import jakarta.inject.Inject;

@FacesConverter(value = "movimentacaoConverter", managed = true)
@ApplicationScoped
public class MovimentacaoConverter implements Converter<FinanceiroMovimentacaoEntity> {

    @Inject
    private FinanceiroMovimentacaoFacade facade;

    @Override
    public FinanceiroMovimentacaoEntity getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.trim().isEmpty()) return null;
        try {
            Integer id = Integer.valueOf(value);
            return facade.find(id);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, FinanceiroMovimentacaoEntity value) {
        if (value == null || value.getId() == null) return "";
        return value.getId().toString();
    }
}
