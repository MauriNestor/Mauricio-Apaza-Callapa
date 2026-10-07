package com.bnb_prueba_tecnica_mauricio_apaza.ui.common;

import android.text.Editable;
import android.text.TextWatcher;

public final class AfterTextChangedWatcher implements TextWatcher {

    public interface Listener {
        void onTextChanged(String text);
    }

    private final Listener listener;

    public AfterTextChangedWatcher(Listener listener) {
        this.listener = listener;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable editable) {
        listener.onTextChanged(editable.toString());
    }
}
