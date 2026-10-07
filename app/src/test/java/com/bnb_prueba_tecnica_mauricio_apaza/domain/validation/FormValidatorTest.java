package com.bnb_prueba_tecnica_mauricio_apaza.domain.validation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class FormValidatorTest {

    private FormValidator validator;

    @Before
    public void setUp() {
        validator = new FormValidator();
    }

    @Test
    public void celular_conOchoDigitos_esValido() {
        assertNull(validator.validarCelular("71234567"));
    }

    @Test
    public void celular_vacio_retornaVacio() {
        assertEquals(FieldError.VACIO, validator.validarCelular(""));
        assertEquals(FieldError.VACIO, validator.validarCelular(null));
        assertEquals(FieldError.VACIO, validator.validarCelular("   "));
    }

    @Test
    public void celular_conMenosDeOchoDigitos_retornaLongitudInvalida() {
        assertEquals(FieldError.LONGITUD_INVALIDA, validator.validarCelular("7123456"));
    }

    @Test
    public void celular_conMasDeOchoDigitos_retornaLongitudInvalida() {
        assertEquals(FieldError.LONGITUD_INVALIDA, validator.validarCelular("712345678"));
    }

    @Test
    public void celular_conLetras_retornaFormatoInvalido() {
        assertEquals(FieldError.FORMATO_INVALIDO, validator.validarCelular("7123456A"));
    }

    @Test
    public void carnet_conDigitos_esValido() {
        assertNull(validator.validarCarnet("412345"));
        assertNull(validator.validarCarnet("1234567890"));
    }

    @Test
    public void carnet_vacio_retornaVacio() {
        assertEquals(FieldError.VACIO, validator.validarCarnet(""));
        assertEquals(FieldError.VACIO, validator.validarCarnet(null));
    }

    @Test
    public void carnet_conMasDeDiezDigitos_retornaLongitudInvalida() {
        assertEquals(FieldError.LONGITUD_INVALIDA, validator.validarCarnet("12345678901"));
    }

    @Test
    public void carnet_conCaracteresNoNumericos_retornaFormatoInvalido() {
        assertEquals(FieldError.FORMATO_INVALIDO, validator.validarCarnet("4123-45"));
    }

    @Test
    public void complemento_sinCheckbox_seIgnora() {
        assertNull(validator.validarComplemento(false, ""));
        assertNull(validator.validarComplemento(false, "$$$"));
    }

    @Test
    public void complemento_alfanumericoDeDosCaracteres_esValido() {
        assertNull(validator.validarComplemento(true, "1D"));
        assertNull(validator.validarComplemento(true, "ab"));
        assertNull(validator.validarComplemento(true, "12"));
    }

    @Test
    public void complemento_activoYVacio_retornaVacio() {
        assertEquals(FieldError.VACIO, validator.validarComplemento(true, ""));
    }

    @Test
    public void complemento_deUnCaracter_retornaLongitudInvalida() {
        assertEquals(FieldError.LONGITUD_INVALIDA, validator.validarComplemento(true, "1"));
    }

    @Test
    public void complemento_conCaracterEspecial_retornaFormatoInvalido() {
        assertEquals(FieldError.FORMATO_INVALIDO, validator.validarComplemento(true, "1-"));
        assertEquals(FieldError.FORMATO_INVALIDO, validator.validarComplemento(true, "Ñ1"));
    }

    @Test
    public void validar_formularioCompleto_esValido() {
        ValidationResult result = validator.validar("71234567", "412345", true, "1D");

        assertTrue(result.isValid());
    }

    @Test
    public void validar_formularioConErrores_reportaCadaCampo() {
        ValidationResult result = validator.validar("123", "", true, "#");

        assertFalse(result.isValid());
        assertEquals(FieldError.LONGITUD_INVALIDA, result.getCelularError());
        assertEquals(FieldError.VACIO, result.getCarnetError());
        assertEquals(FieldError.FORMATO_INVALIDO, result.getComplementoError());
    }
}
