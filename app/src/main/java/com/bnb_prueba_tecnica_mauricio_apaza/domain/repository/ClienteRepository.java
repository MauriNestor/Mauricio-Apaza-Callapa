package com.bnb_prueba_tecnica_mauricio_apaza.domain.repository;

import com.bnb_prueba_tecnica_mauricio_apaza.domain.model.Cliente;

public interface ClienteRepository {

    void registrarCliente(Cliente cliente, RepositoryCallback<Cliente> callback);
}
