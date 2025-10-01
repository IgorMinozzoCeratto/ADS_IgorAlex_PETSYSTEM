package br.upf.projetojfprimefaces.converter;

import br.upf.projetojfprimefaces.entity.FuncionarioEntity;
import br.upf.projetojfprimefaces.facade.FuncionarioFacade;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;
import javax.inject.Named;

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
