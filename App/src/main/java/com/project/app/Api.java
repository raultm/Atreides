package com.project.app;

import android.content.Context;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.Future;
import com.koushikdutta.ion.Ion;
import com.project.app.api.ApiResponseFactory;
import com.project.app.api.interfaces.ApiResponse;

import java.util.concurrent.ExecutionException;

public class Api {

    // http://proyectocolibri.es/api/v1/party/?api_key=special-key
    protected final static String API_BASEURLLOCAL = "http://proyectocolibri.es/api/v1";
    protected final static String API_KEY = "special-key";

    public final static int API_ACTION_PARTY = 1;
    protected final static String API_ACTION_PARTY_STRING = "/party/?limit=40";

    public static JsonObject action(Context context, int action) {
        String actionURI = getActionURI(action);

        Future<JsonObject> future =
                Ion.with(context, actionURI)
                    .asJsonObject();
        return handleResponse(context, action, future);
    }

    public static JsonObject handleResponse(Context context, int action, Future<JsonObject> future) {
        ApiResponse apiResponse = ApiResponseFactory.getApiResponse(action, future);
        JsonObject jsonObject = new JsonObject();
        try {
            jsonObject = apiResponse.execute(context);
        } catch (Exception e) {
            e.printStackTrace();
            jsonObject.addProperty("status", "Exception");
            jsonObject.addProperty("message", e.toString());
        }
        return jsonObject;

    }

    public static JsonObject handleResponse(Context context, int action, JsonObject json) {
        ApiResponse apiResponse = ApiResponseFactory.getApiResponse(action, json);
        JsonObject jsonObject = new JsonObject();
        try {
            jsonObject = apiResponse.execute(context);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public static String getActionURI(int action){
        String uri = API_BASEURLLOCAL;
        switch (action){
            case API_ACTION_PARTY:
                uri+= API_ACTION_PARTY_STRING;
                break;
        }
        //uri+= "/?api_key=" + API_KEY;
        AppConfig.log(uri);
        return uri;
    }

}
