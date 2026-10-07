package com.bnb_prueba_tecnica_mauricio_apaza.di;

import com.bnb_prueba_tecnica_mauricio_apaza.domain.validation.FormValidator;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public final class AppModule {

    private AppModule() {
    }

    @Provides
    @Singleton
    public static Executor provideIoExecutor() {
        return Executors.newSingleThreadExecutor();
    }

    @Provides
    public static FormValidator provideFormValidator() {
        return new FormValidator();
    }
}
