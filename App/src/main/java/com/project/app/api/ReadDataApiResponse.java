package com.project.app.api;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.Future;
import com.project.app.api.interfaces.ApiResponse;

public class ReadDataApiResponse extends DefaultApiResponse implements ApiResponse {

    public ReadDataApiResponse(Future<JsonObject> future) {
        super(future);
    }

    public ReadDataApiResponse(JsonObject jsonResponse) {
        super(jsonResponse);
    }

}
