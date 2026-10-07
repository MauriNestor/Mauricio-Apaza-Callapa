package com.bnb_prueba_tecnica_mauricio_apaza.ui.autenticacion;

import androidx.annotation.DrawableRes;
import androidx.annotation.StringRes;

public final class Consejo {

    @StringRes
    private final int texto;
    @DrawableRes
    private final int ilustracion;

    public Consejo(@StringRes int texto, @DrawableRes int ilustracion) {
        this.texto = texto;
        this.ilustracion = ilustracion;
    }

    @StringRes
    public int getTexto() {
        return texto;
    }

    @DrawableRes
    public int getIlustracion() {
        return ilustracion;
    }
}
