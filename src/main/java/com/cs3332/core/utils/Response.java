package com.cs3332.core.utils;

import com.mrnatsu.luminexquirkscore.utils.Utils;

public class Response {
    private final boolean state;
    private final String response;

    public Response(boolean state, String response) {
        this.state = state;
        this.response = Utils.parse(response).getText();
    }

    public Response(String response) {
        this.state = false;
        this.response = Utils.parse(response).getText();
    }

    public Response() {
        this.state = true;
        this.response = "SUCCESSFUL";
    }

    public boolean getState() {
        return this.state;
    }

    public String getResponse() {
        return Utils.parse(this.response).getText();
    }

    public void on(boolean state, ResponseHandler responseHandler) {
        if (state == this.state) {
            responseHandler.execute(this);
        }
    }

    public Response onFail(ResponseHandler responseHandler) {
        if (!this.state)
            responseHandler.execute(this);
        return this;
    }

    public Response onSuccess(ResponseHandler responseHandler) {
        if (this.state)
            responseHandler.execute(this);
        return this;
    }
}
