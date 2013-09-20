package com.project.app.api;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.Future;
import com.project.app.Api;
import com.project.app.api.interfaces.ApiResponse;

public class ApiResponseFactory {

    public static ApiResponse getApiResponse(int action, Future<JsonObject> httpFuture){
        if(action == Api.API_ACTION_PARTY){
            return new PartiesApiResponse(httpFuture);
        }
        return new DefaultApiResponse(httpFuture);
    }

    public static ApiResponse getApiResponse(int action, JsonObject jsonResponse){
        if(action == Api.API_ACTION_PARTY){
            return new PartiesApiResponse(jsonResponse);
        }
        return new DefaultApiResponse(jsonResponse);
    }



}
