package br.upf.projetojfprimefaces.converter;

import br.upf.projetojfprimefaces.entity.ConsultaEntity;
import br.upf.projetojfprimefaces.facade.ConsultaFacade;
import jakarta.ejb.EJB;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;

import java.util.logging.Level;
import java.util.logging.Logger;

@FacesConverter(value = "consultaConverter", managed = true)
public class ConsultaConverter implements Converter<ConsultaEntity> {

    private static final Logger LOG = Logger.getLogger(ConsultaConverter.class.getName());

    @EJB
    private ConsultaFacade consultaFacade;

    @Override
    public ConsultaEntity getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null) return null;

        String v = value.trim();
        if (v.isEmpty() || "null".equalsIgnoreCase(v)) return null;

        try {
            Integer id = Integer.valueOf(v);
            return consultaFacade.find(id);
        } catch (NumberFormatException e) {
            // acontece quando o usuário está no item "Selecione..." ou algo não-numérico
            LOG.log(Level.FINE, "ConsultaConverter: valor não numérico recebido: \"{0}\"", v);
        } catch (Exception e) {
            LOG.log(Level.WARNING, "Erro ao converter ConsultaEntity com valor \"" + v + "\": " + e.getMessage(), e);
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, ConsultaEntity consulta) {
        if (consulta == null || consulta.getId() == null) return "";
        return consulta.getId().toString();
    }
}
