package br.upf.projetojfprimefaces.converter;

import br.upf.projetojfprimefaces.entity.ProntuarioEntity;
import br.upf.projetojfprimefaces.facade.ProntuarioFacade;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;
import jakarta.inject.Inject;

@FacesConverter(value = "prontuarioConverter", managed = true)
public class ProntuarioConverter implements Converter<ProntuarioEntity> {

    @Inject
    private ProntuarioFacade prontuarioFacade;

    @Override
    public ProntuarioEntity getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.isBlank() || "null".equals(value)) return null;
        try {
            Integer id = Integer.valueOf(value);
            return prontuarioFacade.find(id);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, ProntuarioEntity value) {
        if (value == null || value.getId() == null) return "";
        return String.valueOf(value.getId());
    }
}
