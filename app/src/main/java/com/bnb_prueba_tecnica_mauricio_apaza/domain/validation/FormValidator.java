package com.bnb_prueba_tecnica_mauricio_apaza.domain.validation;

import java.util.regex.Pattern;

public final class FormValidator {

    public static final int CELULAR_LONGITUD = 8;
    public static final int CARNET_LONGITUD_MAXIMA = 10;
    public static final int COMPLEMENTO_LONGITUD = 2;

    private static final Pattern SOLO_DIGITOS = Pattern.compile("\\d+");
    private static final Pattern ALFANUMERICO = Pattern.compile("[A-Za-z0-9]+");

    public ValidationResult validar(String celular, String carnet, boolean tieneComplemento, String complemento) {
        return new ValidationResult(
                validarCelular(celular),
                validarCarnet(carnet),
                validarComplemento(tieneComplemento, complemento)
        );
    }

    public FieldError validarCelular(String celular) {
        if (isBlank(celular)) {
            return FieldError.VACIO;
        }
        if (!SOLO_DIGITOS.matcher(celular).matches()) {
            return FieldError.FORMATO_INVALIDO;
        }
        if (celular.length() != CELULAR_LONGITUD) {
            return FieldError.LONGITUD_INVALIDA;
        }
        return null;
    }

    public FieldError validarCarnet(String carnet) {
        if (isBlank(carnet)) {
            return FieldError.VACIO;
        }
        if (!SOLO_DIGITOS.matcher(carnet).matches()) {
            return FieldError.FORMATO_INVALIDO;
        }
        if (carnet.length() > CARNET_LONGITUD_MAXIMA) {
            return FieldError.LONGITUD_INVALIDA;
        }
        return null;
    }

    public FieldError validarComplemento(boolean tieneComplemento, String complemento) {
        if (!tieneComplemento) {
            return null;
        }
        if (isBlank(complemento)) {
            return FieldError.VACIO;
        }
        if (!ALFANUMERICO.matcher(complemento).matches()) {
            return FieldError.FORMATO_INVALIDO;
        }
        if (complemento.length() != COMPLEMENTO_LONGITUD) {
            return FieldError.LONGITUD_INVALIDA;
        }
        return null;
    }

    private static boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
