package com.api.autotest.common.utils;

import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;

import java.net.URI;

public class HttpDeleteWithBody extends HttpEntityEnclosingRequestBase {
    public static final String METHOD_NAME = "DELETE";

    @Override
    public String getMethod(){
        return METHOD_NAME;
    }

    public HttpDeleteWithBody(final String uri){
        super();
        setURI(URI.create(uri));
    }

    public HttpDeleteWithBody(final URI uri){
        super();
        setURI(uri);
    }

    public HttpDeleteWithBody(){
        super();
    }

}
