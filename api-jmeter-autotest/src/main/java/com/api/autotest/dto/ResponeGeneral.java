package com.api.autotest.dto;

public class ResponeGeneral {

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getProtocal() {
        return protocal;
    }

    public void setProtocal(String protocal) {
        this.protocal = protocal;
    }

    public String getApistyle() {
        return apistyle;
    }

    public void setApistyle(String apistyle) {
        this.apistyle = apistyle;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    private String url;
    private String protocal;
    private String apistyle;
    private String method;

    public String getPostData() {
        return PostData;
    }

    public void setPostData(String postData) {
        PostData = postData;
    }

    private String PostData;
}
