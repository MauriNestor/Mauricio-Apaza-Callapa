package com.bnb_prueba_tecnica_mauricio_apaza.ui.informacion;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bnb_prueba_tecnica_mauricio_apaza.databinding.BottomSheetUbicacionBinding;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class UbicacionBottomSheet extends BottomSheetDialogFragment {

    public static final String TAG = "UbicacionBottomSheet";
    public static final String REQUEST_KEY_CONTINUAR = "ubicacion_bottom_sheet_continuar";

    private BottomSheetUbicacionBinding binding;

    public static UbicacionBottomSheet newInstance() {
        return new UbicacionBottomSheet();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = BottomSheetUbicacionBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.btnContinuar.setOnClickListener(v -> {
            getParentFragmentManager().setFragmentResult(REQUEST_KEY_CONTINUAR, new Bundle());
            dismiss();
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
