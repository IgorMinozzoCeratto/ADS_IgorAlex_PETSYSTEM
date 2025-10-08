package br.upf.projetojfprimefaces.converter;

import br.upf.projetojfprimefaces.entity.TutorEntity;
import br.upf.projetojfprimefaces.facade.TutorFacade;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named("tutorConverter")
@FacesConverter(value = "tutorConverter", managed = true)
public class TutorConverter implements Converter<TutorEntity> {

    @Inject
    private TutorFacade tutorFacade;

    @Override
    public TutorEntity getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.isBlank()) return null;
        return tutorFacade.find(Integer.valueOf(value));
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, TutorEntity value) {
        return (value == null || value.getId() == null) ? "" : String.valueOf(value.getId());
    }
}
