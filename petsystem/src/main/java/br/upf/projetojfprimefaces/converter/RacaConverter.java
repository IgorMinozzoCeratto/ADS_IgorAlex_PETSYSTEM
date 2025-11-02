package br.upf.projetojfprimefaces.converter;

import br.upf.projetojfprimefaces.entity.RacaEntity;
import br.upf.projetojfprimefaces.facade.RacaFacade;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;
import jakarta.inject.Inject;

@FacesConverter(value = "racaConverter", managed = true)
@ApplicationScoped
public class RacaConverter implements Converter<RacaEntity> {

    @Inject
    private RacaFacade racaFacade;

    @Override
    public RacaEntity getAsObject(FacesContext ctx, UIComponent comp, String value) {
        if (value == null || value.isBlank()) return null;
        try { return racaFacade.find(Integer.valueOf(value)); }
        catch (NumberFormatException ex) { return null; }
    }

    @Override
    public String getAsString(FacesContext ctx, UIComponent comp, RacaEntity value) {
        return (value == null || value.getId() == null) ? "" : value.getId().toString();
    }
}
