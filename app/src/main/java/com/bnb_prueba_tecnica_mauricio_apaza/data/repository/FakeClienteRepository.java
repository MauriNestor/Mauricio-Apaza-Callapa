package com.bnb_prueba_tecnica_mauricio_apaza.data.repository;

import com.bnb_prueba_tecnica_mauricio_apaza.domain.model.Cliente;
import com.bnb_prueba_tecnica_mauricio_apaza.domain.repository.ClienteRepository;
import com.bnb_prueba_tecnica_mauricio_apaza.domain.repository.RepositoryCallback;

import java.util.concurrent.Executor;

import javax.inject.Inject;

public final class FakeClienteRepository implements ClienteRepository {

    static final long RETARDO_SIMULADO_MS = 1500L;

    private final Executor ioExecutor;

    @Inject
    public FakeClienteRepository(Executor ioExecutor) {
        this.ioExecutor = ioExecutor;
    }

    @Override
    public void registrarCliente(Cliente cliente, RepositoryCallback<Cliente> callback) {
        ioExecutor.execute(() -> {
            try {
                Thread.sleep(RETARDO_SIMULADO_MS);
                callback.onSuccess(cliente);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                callback.onError(e);
            }
        });
    }
}
