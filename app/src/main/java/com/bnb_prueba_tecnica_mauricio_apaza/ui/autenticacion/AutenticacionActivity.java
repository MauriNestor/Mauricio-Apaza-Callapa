package com.bnb_prueba_tecnica_mauricio_apaza.ui.autenticacion;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.activity.SystemBarStyle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bnb_prueba_tecnica_mauricio_apaza.R;
import com.bnb_prueba_tecnica_mauricio_apaza.databinding.ActivityAutenticacionBinding;
import com.bnb_prueba_tecnica_mauricio_apaza.ui.common.StepHeaderBinder;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.Arrays;
import java.util.List;

public class AutenticacionActivity extends AppCompatActivity {

    private static final int PASO_ACTUAL = 2;

    private ActivityAutenticacionBinding binding;
    private ConsejosAdapter consejosAdapter;

    public static Intent newIntent(Context context) {
        return new Intent(context, AutenticacionActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(
                this,
                SystemBarStyle.dark(Color.TRANSPARENT),
                SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT)
        );
        binding = ActivityAutenticacionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        aplicarInsets();
        configurarHeader();
        configurarCarrusel();
        binding.btnSiguiente.setOnClickListener(v -> onSiguienteClicked());
    }

    private void aplicarInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.main, (view, insets) -> {
            Insets barras = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            view.setPadding(barras.left, barras.top, barras.right, 0);
            binding.contenido.setPadding(0, 0, 0, barras.bottom);
            return insets;
        });
    }

    private void configurarHeader() {
        StepHeaderBinder.bind(
                binding.header,
                R.string.autenticacion_titulo,
                PASO_ACTUAL,
                R.drawable.ic_person,
                v -> getOnBackPressedDispatcher().onBackPressed()
        );
    }

    private void configurarCarrusel() {
        consejosAdapter = new ConsejosAdapter(crearConsejos());
        binding.vpConsejos.setAdapter(consejosAdapter);
        new TabLayoutMediator(binding.tabIndicador, binding.vpConsejos, (tab, position) ->
                tab.setContentDescription(getString(
                        R.string.consejo_posicion, position + 1, consejosAdapter.getItemCount()
                ))
        ).attach();
    }

    private void onSiguienteClicked() {
        int paginaActual = binding.vpConsejos.getCurrentItem();
        if (paginaActual < consejosAdapter.getItemCount() - 1) {
            binding.vpConsejos.setCurrentItem(paginaActual + 1);
        } else {
            Snackbar.make(binding.getRoot(), R.string.autenticacion_fin, Snackbar.LENGTH_SHORT)
                    .setAnchorView(binding.btnSiguiente)
                    .show();
        }
    }

    private static List<Consejo> crearConsejos() {
        return Arrays.asList(
                new Consejo(R.string.consejo_iluminacion, R.drawable.ic_image_placeholder),
                new Consejo(R.string.consejo_accesorios, R.drawable.ic_image_placeholder),
                new Consejo(R.string.consejo_encuadre, R.drawable.ic_image_placeholder),
                new Consejo(R.string.consejo_carnet, R.drawable.ic_image_placeholder)
        );
    }
}
