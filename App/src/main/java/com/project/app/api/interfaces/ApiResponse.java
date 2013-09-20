package com.project.app.api.interfaces;

import android.content.Context;

import com.google.gson.JsonObject;

import java.util.concurrent.ExecutionException;

public interface ApiResponse {

    public abstract JsonObject execute(Context context) throws ExecutionException, InterruptedException;
}
