package com.bnb_prueba_tecnica_mauricio_apaza.domain.validation;

public final class ValidationResult {

    private final FieldError celularError;
    private final FieldError carnetError;
    private final FieldError complementoError;

    public ValidationResult(FieldError celularError, FieldError carnetError, FieldError complementoError) {
        this.celularError = celularError;
        this.carnetError = carnetError;
        this.complementoError = complementoError;
    }

    public FieldError getCelularError() {
        return celularError;
    }

    public FieldError getCarnetError() {
        return carnetError;
    }

    public FieldError getComplementoError() {
        return complementoError;
    }

    public boolean isValid() {
        return celularError == null && carnetError == null && complementoError == null;
    }
}
