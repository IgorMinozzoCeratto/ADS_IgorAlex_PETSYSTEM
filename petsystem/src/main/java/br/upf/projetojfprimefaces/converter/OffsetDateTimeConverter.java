package br.upf.projetojfprimefaces.converter;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;
import java.time.*;
import java.time.format.DateTimeFormatter;

@FacesConverter(value = "offsetDateTimeConverter", managed = true)
@ApplicationScoped
public class OffsetDateTimeConverter implements Converter<OffsetDateTime> {

    // Ajuste o padrão conforme o pattern do datePicker
    private static final String PATTERN = "dd/MM/yyyy HH:mm";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern(PATTERN);

    @Override
    public OffsetDateTime getAsObject(FacesContext ctx, UIComponent comp, String value) {
        if (value == null || value.trim().isEmpty()) return null;

        // Interpreta o texto como LocalDateTime no fuso local e adiciona o offset do sistema
        LocalDateTime ldt = LocalDateTime.parse(value, FORMATTER);
        ZoneId zone = ZoneId.systemDefault();
        return ldt.atZone(zone).toOffsetDateTime();
    }

    @Override
    public String getAsString(FacesContext ctx, UIComponent comp, OffsetDateTime value) {
        if (value == null) return "";
        // Converte para a zona local antes de formatar
        ZonedDateTime zdt = value.atZoneSameInstant(ZoneId.systemDefault());
        return zdt.format(FORMATTER);
    }
}
