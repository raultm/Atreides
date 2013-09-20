package com.project.app.api;

import android.content.Context;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.Future;
import com.project.app.api.interfaces.ApiResponse;

import java.util.concurrent.ExecutionException;

public class DefaultApiResponse implements ApiResponse {

    private JsonObject object;
    private JsonElement objects;
    private JsonElement meta;

    public DefaultApiResponse(JsonObject jsonObject){
        this.object = jsonObject;
        parseJsonObject();
    }

    public DefaultApiResponse(Future<JsonObject> future) {
        this.object = parseFutureToJsonObject(future);
        parseJsonObject();
    }

    private void parseJsonObject(){
        this.objects = this.object.get("objects");
        this.meta = this.object.get("meta");
    }

    public JsonArray getObjects(){
        return this.objects.getAsJsonArray();
    }

    public JsonObject getMeta(){
        return this.meta.getAsJsonObject();
    }

    public JsonObject getResponse(){
        return this.object;
    }

    private JsonObject parseFutureToJsonObject(Future<JsonObject> future){
        try {
            return future.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return new JsonObject();
    }

    @Override
    public JsonObject execute(Context context) {
        JsonArray jsonArray = getObjects();
        int objectsCount = jsonArray.size();

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("status", "OK");
        jsonObject.addProperty("message", "We found " + objectsCount + " objects.");

        return jsonObject;
    }

}
