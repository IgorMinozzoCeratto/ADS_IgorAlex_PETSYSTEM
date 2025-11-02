package br.upf.projetojfprimefaces.converter;

import br.upf.projetojfprimefaces.entity.AnimalEntity;
import br.upf.projetojfprimefaces.facade.AnimalFacade;
import jakarta.ejb.EJB;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;

@FacesConverter(value = "animalConverter", managed = true)
public class AnimalConverter implements Converter<AnimalEntity> {

    @EJB
    private AnimalFacade animalFacade;

    @Override
    public AnimalEntity getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.isBlank() || "null".equals(value)) return null;
        try {
            Integer id = Integer.valueOf(value);
            return animalFacade.find(id);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, AnimalEntity value) {
        if (value == null || value.getId() == null) return "";
        return String.valueOf(value.getId());
        }
}
