package com.bnb_prueba_tecnica_mauricio_apaza.domain.repository;

public interface RepositoryCallback<T> {

    void onSuccess(T result);

    void onError(Throwable error);
}
