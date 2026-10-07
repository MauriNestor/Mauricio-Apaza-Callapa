package com.bnb_prueba_tecnica_mauricio_apaza.ui.common;

import android.view.View;

import androidx.annotation.DrawableRes;
import androidx.annotation.StringRes;

import com.bnb_prueba_tecnica_mauricio_apaza.R;
import com.bnb_prueba_tecnica_mauricio_apaza.databinding.ViewStepHeaderBinding;

public final class StepHeaderBinder {

    public static final int TOTAL_PASOS = 6;

    private StepHeaderBinder() {
    }

    public static void bind(
            ViewStepHeaderBinding header,
            @StringRes int titulo,
            int paso,
            @DrawableRes int icono,
            View.OnClickListener onAtras
    ) {
        header.tvTituloToolbar.setText(titulo);
        header.tvNombrePaso.setText(titulo);
        header.tvPaso.setText(header.getRoot().getContext().getString(R.string.paso_formato, paso, TOTAL_PASOS));
        header.ivIconoPaso.setImageResource(icono);
        header.progressPasos.setMax(TOTAL_PASOS);
        header.progressPasos.setProgress(paso);
        header.btnAtras.setOnClickListener(onAtras);
    }
}
