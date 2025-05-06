package com.cs3332.core.utils;

import lombok.Getter;

public class Response {
    private final boolean state;
    @Getter
    private final String response;

    public Response(boolean state, String response) {
        this.state = state;
        this.response = response;
    }

    public Response(String response) {
        this.state = false;
        this.response = response;
    }

    public Response() {
        this.state = true;
        this.response = "SUCCESSFUL";
    }

    public boolean getState() {
        return this.state;
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
