package br.upf.projetojfprimefaces.converter;

import br.upf.projetojfprimefaces.entity.EspecieEntity;
import br.upf.projetojfprimefaces.facade.EspecieFacade;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;
import jakarta.inject.Inject;

@FacesConverter(value = "especieConverter", managed = true)
@ApplicationScoped
public class EspecieConverter implements Converter<EspecieEntity> {

    @Inject
    private EspecieFacade especieFacade;

    @Override
    public EspecieEntity getAsObject(FacesContext ctx, UIComponent comp, String value) {
        if (value == null || value.isBlank()) return null;
        try { return especieFacade.find(Integer.valueOf(value)); }
        catch (NumberFormatException ex) { return null; }
    }

    @Override
    public String getAsString(FacesContext ctx, UIComponent comp, EspecieEntity value) {
        return (value == null || value.getId() == null) ? "" : value.getId().toString();
    }
}
