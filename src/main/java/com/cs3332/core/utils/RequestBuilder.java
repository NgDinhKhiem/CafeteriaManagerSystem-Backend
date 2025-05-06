package com.cs3332.core.utils;


import com.cs3332.core.object.RequestParameter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RequestBuilder {
    private final String uri;
    private List<RequestParameter> parameters = new ArrayList<>();

    public RequestBuilder(String uri) {
        this.uri = uri;
    }

    public RequestBuilder(String uri, RequestParameter... parameters) {
        this.uri = uri;
        Collections.addAll(this.parameters, parameters);
    }

    public void add(RequestParameter... para){
        this.parameters.addAll(List.of(para));
    }

    public String getRequest(){
        int count = 0;
        StringBuilder uriStringBuilder = new StringBuilder(uri);
        uriStringBuilder.append("?");
        for(RequestParameter para: parameters) {
            count += 1;
            uriStringBuilder.append(para.toURI());
            if (count < parameters.size()){
                uriStringBuilder.append("&");
            }

        }

        return uriStringBuilder.toString();
    }
}
