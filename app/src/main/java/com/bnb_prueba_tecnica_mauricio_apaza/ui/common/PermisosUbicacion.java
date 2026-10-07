package com.bnb_prueba_tecnica_mauricio_apaza.ui.common;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.Map;

public final class PermisosUbicacion {

    private static final String[] PERMISOS = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };

    private PermisosUbicacion() {
    }

    public static String[] permisos() {
        return PERMISOS.clone();
    }

    public static boolean estaConcedido(Context context) {
        for (String permiso : PERMISOS) {
            if (ContextCompat.checkSelfPermission(context, permiso) == PackageManager.PERMISSION_GRANTED) {
                return true;
            }
        }
        return false;
    }

    public static boolean estaConcedido(Map<String, Boolean> resultado) {
        for (String permiso : PERMISOS) {
            if (Boolean.TRUE.equals(resultado.get(permiso))) {
                return true;
            }
        }
        return false;
    }

    public static boolean estaDenegadoPermanentemente(Activity activity) {
        for (String permiso : PERMISOS) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permiso)) {
                return false;
            }
        }
        return true;
    }
}
