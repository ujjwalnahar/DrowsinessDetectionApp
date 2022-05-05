package com.example.drowsinessdetectionapp;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleRegistry;

public class ServiceLifecycleOwner implements LifecycleOwner {
    LifecycleRegistry lifecycleRegistry=new LifecycleRegistry(this);

    public ServiceLifecycleOwner() {
        lifecycleRegistry.setCurrentState(Lifecycle.State.CREATED);
    }
    public void start(){
        lifecycleRegistry.setCurrentState(Lifecycle.State.STARTED);
    }
    public void stop(){
        lifecycleRegistry.setCurrentState(Lifecycle.State.DESTROYED);
    }
    @NonNull
    @Override
    public Lifecycle getLifecycle() {
        return lifecycleRegistry;
    }
}
