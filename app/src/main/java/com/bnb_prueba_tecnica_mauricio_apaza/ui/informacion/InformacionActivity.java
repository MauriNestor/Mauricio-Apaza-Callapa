package com.bnb_prueba_tecnica_mauricio_apaza.ui.informacion;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.text.InputFilter;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.SystemBarStyle;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.bnb_prueba_tecnica_mauricio_apaza.R;
import com.bnb_prueba_tecnica_mauricio_apaza.databinding.ActivityInformacionBinding;
import com.bnb_prueba_tecnica_mauricio_apaza.domain.validation.FormValidator;
import com.bnb_prueba_tecnica_mauricio_apaza.ui.autenticacion.AutenticacionActivity;
import com.bnb_prueba_tecnica_mauricio_apaza.ui.common.AfterTextChangedWatcher;
import com.bnb_prueba_tecnica_mauricio_apaza.ui.common.AlphanumericInputFilter;
import com.bnb_prueba_tecnica_mauricio_apaza.ui.common.Event;
import com.bnb_prueba_tecnica_mauricio_apaza.ui.common.PermisosUbicacion;
import com.bnb_prueba_tecnica_mauricio_apaza.ui.common.StepHeaderBinder;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;

import java.util.Map;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class InformacionActivity extends AppCompatActivity {

    private static final int PASO_ACTUAL = 1;

    private final ActivityResultLauncher<String[]> permisoUbicacionLauncher = registerForActivityResult(
            new ActivityResultContracts.RequestMultiplePermissions(),
            this::onResultadoPermisoUbicacion
    );

    private final ActivityResultLauncher<Intent> ajustesLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> onRegresoDeAjustes()
    );

    private ActivityInformacionBinding binding;
    private InformacionViewModel viewModel;
    private AlertDialog dialogoAjustes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(
                this,
                SystemBarStyle.dark(Color.TRANSPARENT),
                SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT)
        );
        binding = ActivityInformacionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        viewModel = new ViewModelProvider(this).get(InformacionViewModel.class);

        aplicarInsets();
        configurarHeader();
        configurarCampos();
        configurarListeners();
        observarViewModel();
        escucharBottomSheetUbicacion();
    }

    @Override
    protected void onDestroy() {
        if (dialogoAjustes != null) {
            dialogoAjustes.dismiss();
        }
        super.onDestroy();
    }

    private void aplicarInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.main, (view, insets) -> {
            Insets barras = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            Insets teclado = insets.getInsets(WindowInsetsCompat.Type.ime());
            view.setPadding(barras.left, barras.top, barras.right, 0);
            binding.contenido.setPadding(0, 0, 0, Math.max(barras.bottom, teclado.bottom));
            return insets;
        });
    }

    private void configurarHeader() {
        StepHeaderBinder.bind(
                binding.header,
                R.string.informacion_titulo,
                PASO_ACTUAL,
                R.drawable.ic_add,
                v -> getOnBackPressedDispatcher().onBackPressed()
        );
    }

    private void configurarCampos() {
        binding.etCelular.setFilters(new InputFilter[]{
                new InputFilter.LengthFilter(FormValidator.CELULAR_LONGITUD)
        });
        binding.etCarnet.setFilters(new InputFilter[]{
                new InputFilter.LengthFilter(FormValidator.CARNET_LONGITUD_MAXIMA)
        });
        binding.etComplemento.setFilters(new InputFilter[]{
                new AlphanumericInputFilter(),
                new InputFilter.AllCaps(),
                new InputFilter.LengthFilter(FormValidator.COMPLEMENTO_LONGITUD)
        });
    }

    private void configurarListeners() {
        binding.etCelular.addTextChangedListener(new AfterTextChangedWatcher(viewModel::onCelularChanged));
        binding.etCarnet.addTextChangedListener(new AfterTextChangedWatcher(viewModel::onCarnetChanged));
        binding.etComplemento.addTextChangedListener(new AfterTextChangedWatcher(viewModel::onComplementoChanged));
        binding.cbTieneComplemento.setOnCheckedChangeListener(
                (buttonView, isChecked) -> viewModel.onTieneComplementoChanged(isChecked)
        );
        binding.btnSiguiente.setOnClickListener(
                v -> viewModel.onSiguienteClicked(PermisosUbicacion.estaConcedido(this))
        );
    }

    private void observarViewModel() {
        viewModel.getCelularError().observe(this, error -> mostrarError(binding.tvErrorCelular, error));
        viewModel.getCarnetError().observe(this, error -> mostrarError(binding.tvErrorCarnet, error));
        viewModel.getComplementoError().observe(this, error -> mostrarError(binding.tvErrorComplemento, error));
        viewModel.getTieneComplemento().observe(this, this::mostrarComplemento);
        viewModel.getLoading().observe(this, loading -> mostrarLoading(Boolean.TRUE.equals(loading)));
        viewModel.getUiEvent().observe(this, this::manejarEvento);
    }

    private void escucharBottomSheetUbicacion() {
        getSupportFragmentManager().setFragmentResultListener(
                UbicacionBottomSheet.REQUEST_KEY_CONTINUAR,
                this,
                (requestKey, result) -> permisoUbicacionLauncher.launch(PermisosUbicacion.permisos())
        );
    }

    private void manejarEvento(Event<InformacionUiEvent> event) {
        InformacionUiEvent contenido = event.getContentIfNotHandled();
        if (contenido == null) {
            return;
        }
        switch (contenido) {
            case MOSTRAR_DIALOGO_UBICACION:
                mostrarBottomSheetUbicacion();
                break;
            case MOSTRAR_DIALOGO_AJUSTES:
                mostrarDialogoAjustes();
                break;
            case NAVEGAR_A_AUTENTICACION:
                startActivity(AutenticacionActivity.newIntent(this));
                break;
            case MOSTRAR_ERROR_SERVICIO:
                Snackbar.make(binding.getRoot(), R.string.error_servicio, Snackbar.LENGTH_LONG)
                        .setAnchorView(binding.btnSiguiente)
                        .show();
                break;
        }
    }

    private void onResultadoPermisoUbicacion(Map<String, Boolean> resultado) {
        boolean concedido = PermisosUbicacion.estaConcedido(resultado);
        boolean denegadoPermanentemente = !concedido && PermisosUbicacion.estaDenegadoPermanentemente(this);
        viewModel.onPermisoUbicacionResultado(concedido, denegadoPermanentemente);
    }

    private void onRegresoDeAjustes() {
        if (PermisosUbicacion.estaConcedido(this)) {
            viewModel.onPermisoUbicacionResultado(true, false);
        }
    }

    private void mostrarBottomSheetUbicacion() {
        if (getSupportFragmentManager().findFragmentByTag(UbicacionBottomSheet.TAG) == null) {
            UbicacionBottomSheet.newInstance().show(getSupportFragmentManager(), UbicacionBottomSheet.TAG);
        }
    }

    private void mostrarDialogoAjustes() {
        if (dialogoAjustes != null && dialogoAjustes.isShowing()) {
            return;
        }
        dialogoAjustes = new MaterialAlertDialogBuilder(this)
                .setTitle(R.string.ubicacion_denegada_titulo)
                .setMessage(R.string.ubicacion_denegada_descripcion)
                .setPositiveButton(R.string.ir_a_ajustes, (dialog, which) -> abrirAjustesDeLaApp())
                .setNegativeButton(R.string.cancelar, null)
                .show();
    }

    private void abrirAjustesDeLaApp() {
        Intent intent = new Intent(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.fromParts("package", getPackageName(), null)
        );
        ajustesLauncher.launch(intent);
    }

    private void mostrarError(TextView errorView, Integer mensaje) {
        if (mensaje == null) {
            errorView.setText(null);
            errorView.setVisibility(View.GONE);
        } else {
            errorView.setText(mensaje);
            errorView.setVisibility(View.VISIBLE);
        }
    }

    private void mostrarComplemento(Boolean visible) {
        boolean mostrar = Boolean.TRUE.equals(visible);
        if (binding.cbTieneComplemento.isChecked() != mostrar) {
            binding.cbTieneComplemento.setChecked(mostrar);
        }
        binding.grupoComplemento.setVisibility(mostrar ? View.VISIBLE : View.GONE);
    }

    private void mostrarLoading(boolean loading) {
        binding.progressSiguiente.setVisibility(loading ? View.VISIBLE : View.GONE);
        binding.btnSiguiente.setText(loading ? "" : getString(R.string.siguiente));
        binding.btnSiguiente.setClickable(!loading);
        binding.etCelular.setEnabled(!loading);
        binding.etCarnet.setEnabled(!loading);
        binding.etComplemento.setEnabled(!loading);
        binding.cbTieneComplemento.setEnabled(!loading);
    }
}
