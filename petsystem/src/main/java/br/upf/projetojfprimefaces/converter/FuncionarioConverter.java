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
public class FuncionarioConverter implements Converter<FuncionarioEntity> {

    @Inject
    private FuncionarioFacade funcionarioFacade;

    @Override
    public FuncionarioEntity getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }

        try {
            Integer id = Integer.valueOf(value); // ✅ Correção aqui
            return funcionarioFacade.find(id);
        } catch (NumberFormatException e) {
            System.err.println("Conversão inválida para FuncionarioEntity: valor não numérico -> " + value);
        } catch (Exception e) {
            System.err.println("Erro ao buscar FuncionarioEntity: " + e.getMessage());
        }

        return null;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, FuncionarioEntity funcionario) {
        if (funcionario == null || funcionario.getId() == null) {
            return "";
        }
        return funcionario.getId().toString();
    }
}
