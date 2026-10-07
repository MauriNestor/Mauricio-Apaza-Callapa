package com.bnb_prueba_tecnica_mauricio_apaza.domain.model;

public final class Cliente {

    private final String celular;
    private final String carnet;
    private final String complemento;

    public Cliente(String celular, String carnet, String complemento) {
        this.celular = celular;
        this.carnet = carnet;
        this.complemento = complemento;
    }

    public String getCelular() {
        return celular;
    }

    public String getCarnet() {
        return carnet;
    }

    public String getComplemento() {
        return complemento;
    }

    public boolean tieneComplemento() {
        return complemento != null && !complemento.isEmpty();
    }
}
