package com.api.autotest.dto;

import com.api.autotest.common.utils.HttpHeader;
import com.api.autotest.common.utils.HttpParamers;

import java.util.List;

/**
 * Created by fanseasn on 2020/11/30.
 */
/*
 @author Season
 @DESCRIPTION 
 @create 2020/11/30
*/
public class RequestObject {


    public String getStopflag() {
        return stopflag;
    }

    public void setStopflag(String stopflag) {
        this.stopflag = stopflag;
    }

    private String stopflag;

    public Long getSceneid() {
        return sceneid;
    }

    public void setSceneid(Long sceneid) {
        this.sceneid = sceneid;
    }

    public String getScenename() {
        return scenename;
    }

    public void setScenename(String scenename) {
        this.scenename = scenename;
    }

    private Long sceneid;
    private String scenename;
    public List<ApicasesAssert> getApicasesAssertList() {
        return apicasesAssertList;
    }

    public void setApicasesAssertList(List<ApicasesAssert> apicasesAssertList) {
        this.apicasesAssertList = apicasesAssertList;
    }

    private List<ApicasesAssert> apicasesAssertList;

    public List<ApicasesDBAssert> getApicasesDBAssertList() {
        return apicasesDBAssertList;
    }

    public void setApicasesDBAssertList(List<ApicasesDBAssert> apicasesDBAssertList) {
        this.apicasesDBAssertList = apicasesDBAssertList;
    }

    private List<ApicasesDBAssert> apicasesDBAssertList;


    public String getTestplanid() {
        return testplanid;
    }

    public void setTestplanid(String testplanid) {
        this.testplanid = testplanid;
    }

    public String getCaseid() {
        return caseid;
    }

    public void setCaseid(String caseid) {
        this.caseid = caseid;
    }

//    public String getBatchid() {
//        return batchid;
//    }
//
//    public void setBatchid(String batchid) {
//        this.batchid = batchid;
//    }

    public String getSlaverid() {
        return slaverid;
    }

    public void setSlaverid(String slaverid) {
        this.slaverid = slaverid;
    }

    public String getBatchname() {
        return batchname;
    }

    public void setBatchname(String batchname) {
        this.batchname = batchname;
    }

    public String getCasetype() {
        return casetype;
    }

    public void setCasetype(String casetype) {
        this.casetype = casetype;
    }

    public String getExpect() {
        return expect;
    }

    public void setExpect(String expect) {
        this.expect = expect;
    }

    public String getProjectid() {
        return projectid;
    }

    public void setProjectid(String projectid) {
        this.projectid = projectid;
    }

    private String projectid = "";

    private String testplanid = "";

    public String getDeployunitid() {
        return deployunitid;
    }

    public void setDeployunitid(String deployunitid) {
        this.deployunitid = deployunitid;
    }

    private String deployunitid = "";

    private String caseid = "";
//    private String batchid = "";
    private String slaverid = "";
    private String batchname = "";

    public String getCasename() {
        return casename;
    }

    public void setCasename(String casename) {
        this.casename = casename;
    }

    private String casename = "";


    public String getTestplanname() {
        return testplanname;
    }

    public void setTestplanname(String testplanname) {
        this.testplanname = testplanname;
    }

    private String testplanname = "";

    private String casetype = "";
    private String expect = "";

    public int getLoop() {
        return loop;
    }

    public void setLoop(int loop) {
        this.loop = loop;
    }

    private int loop;




    public String getRequestmMthod() {
        return RequestmMthod;
    }

    public void setRequestmMthod(String requestmMthod) {
        RequestmMthod = requestmMthod;
    }

    private String RequestmMthod;

    public String getProtocal() {
        return protocal;
    }

    public void setProtocal(String protocal) {
        this.protocal = protocal;
    }

    private String protocal;


    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    private String resource;

    public String getApistyle() {
        return apistyle;
    }

    public void setApistyle(String apistyle) {
        this.apistyle = apistyle;
    }

    private String apistyle;


    public HttpParamers getParamers() {
        return paramers;
    }

    public void setParamers(HttpParamers paramers) {
        this.paramers = paramers;
    }

    private HttpParamers paramers;

    public String getPostData() {
        return PostData;
    }

    public void setPostData(String postData) {
        PostData = postData;
    }

    private String PostData;



    public String getRequestcontenttype() {
        return requestcontenttype;
    }

    public void setRequestcontenttype(String requestcontenttype) {
        this.requestcontenttype = requestcontenttype;
    }

    private String requestcontenttype;

    public String getResponecontenttype() {
        return responecontenttype;
    }

    public void setResponecontenttype(String responecontenttype) {
        this.responecontenttype = responecontenttype;
    }

    private String responecontenttype;



    public HttpHeader getHeader() {
        return header;
    }

    public void setHeader(HttpHeader header) {
        this.header = header;
    }

    private HttpHeader header;

    public String getHeadjson() {
        return headjson;
    }

    public void setHeadjson(String headjson) {
        this.headjson = headjson;
    }

    public String getParamjson() {
        return paramjson;
    }

    public void setParamjson(String paramjson) {
        this.paramjson = paramjson;
    }

    public String getBodyjson() {
        return bodyjson;
    }

    public void setBodyjson(String bodyjson) {
        this.bodyjson = bodyjson;
    }

    private String headjson;
    private String paramjson;
    private String bodyjson;

    public String getVariablesjson() {
        return variablesjson;
    }

    public void setVariablesjson(String variablesjson) {
        this.variablesjson = variablesjson;
    }

    private String variablesjson;


    public String getDeployunitvisittype() {
        return deployunitvisittype;
    }

    public void setDeployunitvisittype(String deployunitvisittype) {
        this.deployunitvisittype = deployunitvisittype;
    }

    private String deployunitvisittype;

    public String getMachineip() {
        return machineip;
    }

    public void setMachineip(String machineip) {
        this.machineip = machineip;
    }

    private String machineip;


}
