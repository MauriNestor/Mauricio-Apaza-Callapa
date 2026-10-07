package com.bnb_prueba_tecnica_mauricio_apaza.ui.informacion;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;

import com.bnb_prueba_tecnica_mauricio_apaza.R;
import com.bnb_prueba_tecnica_mauricio_apaza.domain.model.Cliente;
import com.bnb_prueba_tecnica_mauricio_apaza.domain.repository.ClienteRepository;
import com.bnb_prueba_tecnica_mauricio_apaza.domain.repository.RepositoryCallback;
import com.bnb_prueba_tecnica_mauricio_apaza.domain.validation.FieldError;
import com.bnb_prueba_tecnica_mauricio_apaza.domain.validation.FormValidator;
import com.bnb_prueba_tecnica_mauricio_apaza.domain.validation.ValidationResult;
import com.bnb_prueba_tecnica_mauricio_apaza.ui.common.Event;

import java.util.Locale;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class InformacionViewModel extends ViewModel {

    private static final String KEY_CELULAR = "celular";
    private static final String KEY_CARNET = "carnet";
    private static final String KEY_COMPLEMENTO = "complemento";
    private static final String KEY_TIENE_COMPLEMENTO = "tiene_complemento";

    private final SavedStateHandle savedStateHandle;
    private final FormValidator formValidator;
    private final ClienteRepository clienteRepository;

    private final MutableLiveData<Integer> celularError = new MutableLiveData<>();
    private final MutableLiveData<Integer> carnetError = new MutableLiveData<>();
    private final MutableLiveData<Integer> complementoError = new MutableLiveData<>();
    private final MutableLiveData<Boolean> loading = new MutableLiveData<>(false);
    private final MutableLiveData<Event<InformacionUiEvent>> uiEvent = new MutableLiveData<>();
    private final MutableLiveData<Boolean> tieneComplemento;

    @Inject
    public InformacionViewModel(
            SavedStateHandle savedStateHandle,
            FormValidator formValidator,
            ClienteRepository clienteRepository
    ) {
        this.savedStateHandle = savedStateHandle;
        this.formValidator = formValidator;
        this.clienteRepository = clienteRepository;
        this.tieneComplemento = savedStateHandle.getLiveData(KEY_TIENE_COMPLEMENTO, false);
    }

    public LiveData<Integer> getCelularError() {
        return celularError;
    }

    public LiveData<Integer> getCarnetError() {
        return carnetError;
    }

    public LiveData<Integer> getComplementoError() {
        return complementoError;
    }

    public LiveData<Boolean> getTieneComplemento() {
        return tieneComplemento;
    }

    public LiveData<Boolean> getLoading() {
        return loading;
    }

    public LiveData<Event<InformacionUiEvent>> getUiEvent() {
        return uiEvent;
    }

    public void onCelularChanged(String celular) {
        actualizarCampo(KEY_CELULAR, celular, celularError);
    }

    public void onCarnetChanged(String carnet) {
        actualizarCampo(KEY_CARNET, carnet, carnetError);
    }

    public void onComplementoChanged(String complemento) {
        actualizarCampo(KEY_COMPLEMENTO, complemento, complementoError);
    }

    public void onTieneComplementoChanged(boolean checked) {
        if (checked == isTieneComplemento()) {
            return;
        }
        tieneComplemento.setValue(checked);
        if (!checked) {
            limpiarError(complementoError);
        }
    }

    public void onSiguienteClicked(boolean tienePermisoUbicacion) {
        if (Boolean.TRUE.equals(loading.getValue())) {
            return;
        }
        ValidationResult result = formValidator.validar(
                getCelular(),
                getCarnet(),
                isTieneComplemento(),
                getComplemento()
        );
        celularError.setValue(mensajeCelular(result.getCelularError()));
        carnetError.setValue(mensajeCarnet(result.getCarnetError()));
        complementoError.setValue(mensajeComplemento(result.getComplementoError()));

        if (!result.isValid()) {
            return;
        }
        if (tienePermisoUbicacion) {
            registrarCliente();
        } else {
            emitir(InformacionUiEvent.MOSTRAR_DIALOGO_UBICACION);
        }
    }

    public void onPermisoUbicacionResultado(boolean concedido, boolean denegadoPermanentemente) {
        if (concedido) {
            registrarCliente();
        } else if (denegadoPermanentemente) {
            emitir(InformacionUiEvent.MOSTRAR_DIALOGO_AJUSTES);
        } else {
            emitir(InformacionUiEvent.MOSTRAR_DIALOGO_UBICACION);
        }
    }

    private void registrarCliente() {
        loading.setValue(true);
        clienteRepository.registrarCliente(construirCliente(), new RepositoryCallback<Cliente>() {
            @Override
            public void onSuccess(Cliente result) {
                loading.postValue(false);
                uiEvent.postValue(new Event<>(InformacionUiEvent.NAVEGAR_A_AUTENTICACION));
            }

            @Override
            public void onError(Throwable error) {
                loading.postValue(false);
                uiEvent.postValue(new Event<>(InformacionUiEvent.MOSTRAR_ERROR_SERVICIO));
            }
        });
    }

    private Cliente construirCliente() {
        String complemento = isTieneComplemento()
                ? getComplemento().toUpperCase(Locale.ROOT)
                : null;
        return new Cliente(getCelular(), getCarnet(), complemento);
    }

    private void actualizarCampo(String key, String valor, MutableLiveData<Integer> error) {
        if (valor.equals(valorTexto(key))) {
            return;
        }
        savedStateHandle.set(key, valor);
        limpiarError(error);
    }

    private void emitir(InformacionUiEvent event) {
        uiEvent.setValue(new Event<>(event));
    }

    private static void limpiarError(MutableLiveData<Integer> error) {
        if (error.getValue() != null) {
            error.setValue(null);
        }
    }

    private String getCelular() {
        return valorTexto(KEY_CELULAR);
    }

    private String getCarnet() {
        return valorTexto(KEY_CARNET);
    }

    private String getComplemento() {
        return valorTexto(KEY_COMPLEMENTO);
    }

    private boolean isTieneComplemento() {
        return Boolean.TRUE.equals(tieneComplemento.getValue());
    }

    private String valorTexto(String key) {
        String value = savedStateHandle.get(key);
        return value != null ? value : "";
    }

    private static Integer mensajeCelular(FieldError error) {
        if (error == null) {
            return null;
        }
        switch (error) {
            case VACIO:
                return R.string.error_celular_vacio;
            case LONGITUD_INVALIDA:
                return R.string.error_celular_longitud;
            default:
                return R.string.error_celular_formato;
        }
    }

    private static Integer mensajeCarnet(FieldError error) {
        if (error == null) {
            return null;
        }
        switch (error) {
            case VACIO:
                return R.string.error_carnet_vacio;
            case LONGITUD_INVALIDA:
                return R.string.error_carnet_longitud;
            default:
                return R.string.error_carnet_formato;
        }
    }

    private static Integer mensajeComplemento(FieldError error) {
        if (error == null) {
            return null;
        }
        switch (error) {
            case VACIO:
                return R.string.error_complemento_vacio;
            case LONGITUD_INVALIDA:
                return R.string.error_complemento_longitud;
            default:
                return R.string.error_complemento_formato;
        }
    }
}
