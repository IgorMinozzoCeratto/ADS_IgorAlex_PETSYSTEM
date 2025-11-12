package br.upf.projetojfprimefaces.converter;

import br.upf.projetojfprimefaces.entity.FuncionarioEntity;
import br.upf.projetojfprimefaces.facade.FuncionarioFacade;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named("funcionarioConverter")
@FacesConverter(value = "funcionarioConverter", managed = true)
public class FuncionarioConverter implements Converter<Object> {

    @Inject
    private FuncionarioFacade funcionarioFacade;

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null) return null;
        String v = value.trim();
        if (v.isEmpty() || "null".equalsIgnoreCase(v)) return null;

        try {
            Integer id = Integer.valueOf(v);
            return funcionarioFacade.find(id);
        } catch (NumberFormatException e) {
            // Valor não numérico: não converte
            return null;
        } catch (Exception e) {
            System.err.println("[FuncionarioConverter] Erro ao buscar FuncionarioEntity: " + e.getMessage());
            return null;
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (value == null) return "";

        // Java 11: usar instanceof + cast explícito
        if (value instanceof FuncionarioEntity) {
            FuncionarioEntity f = (FuncionarioEntity) value;
            return f.getId() != null ? f.getId().toString() : "";
        }
        if (value instanceof String) {
            // O renderer às vezes chama com String
            return (String) value;
        }
        return "";
    }
}
