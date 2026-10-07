package com.bnb_prueba_tecnica_mauricio_apaza.di;

import com.bnb_prueba_tecnica_mauricio_apaza.data.repository.FakeClienteRepository;
import com.bnb_prueba_tecnica_mauricio_apaza.domain.repository.ClienteRepository;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public abstract class RepositoryModule {

    @Binds
    @Singleton
    public abstract ClienteRepository bindClienteRepository(FakeClienteRepository impl);
}
