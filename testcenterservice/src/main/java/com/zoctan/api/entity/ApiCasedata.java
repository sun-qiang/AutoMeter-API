package com.zoctan.api.entity;

import java.util.Date;
import javax.persistence.*;

@Table(name = "api_casedata")
public class ApiCasedata {
    /**
     * Id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public Long getMid() {
        return mid;
    }

    public void setMid(Long mid) {
        this.mid = mid;
    }

    private Long mid;

    /**
     * 用例Id
     */
    private Long caseid;

    /**
     * 用例名
     */
    private String casename;

    /**
     * api参数
     */
    private String apiparam;

    /**
     * 用例参数值
     */
    private String apiparamvalue;

    /**
     * api属性类型，header，body
     */
    private String propertytype;

    public String getParamstype() {
        return paramstype;
    }

    public void setParamstype(String paramstype) {
        this.paramstype = paramstype;
    }

    private String paramstype;



    /**
     * 备注
     */
    private String memo;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 上一次修改时间
     */
    @Column(name = "lastmodify_time")
    private Date lastmodifyTime;

    /**
     * 获取Id
     *
     * @return id - Id
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置Id
     *
     * @param id Id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取用例Id
     *
     * @return caseid - 用例Id
     */
    public Long getCaseid() {
        return caseid;
    }

    /**
     * 设置用例Id
     *
     * @param caseid 用例Id
     */
    public void setCaseid(Long caseid) {
        this.caseid = caseid;
    }

    /**
     * 获取用例名
     *
     * @return casename - 用例名
     */
    public String getCasename() {
        return casename;
    }

    /**
     * 设置用例名
     *
     * @param casename 用例名
     */
    public void setCasename(String casename) {
        this.casename = casename;
    }

    /**
     * 获取api参数
     *
     * @return apiparam - api参数
     */
    public String getApiparam() {
        return apiparam;
    }

    /**
     * 设置api参数
     *
     * @param apiparam api参数
     */
    public void setApiparam(String apiparam) {
        this.apiparam = apiparam;
    }

    /**
     * 获取用例参数值
     *
     * @return apiparamvalue - 用例参数值
     */
    public String getApiparamvalue() {
        return apiparamvalue;
    }

    /**
     * 设置用例参数值
     *
     * @param apiparamvalue 用例参数值
     */
    public void setApiparamvalue(String apiparamvalue) {
        this.apiparamvalue = apiparamvalue;
    }

    /**
     * 获取api属性类型，header，body
     *
     * @return propertytype - api属性类型，header，body
     */
    public String getPropertytype() {
        return propertytype;
    }

    /**
     * 设置api属性类型，header，body
     *
     * @param propertytype api属性类型，header，body
     */
    public void setPropertytype(String propertytype) {
        this.propertytype = propertytype;
    }

    /**
     * 获取备注
     *
     * @return memo - 备注
     */
    public String getMemo() {
        return memo;
    }

    /**
     * 设置备注
     *
     * @param memo 备注
     */
    public void setMemo(String memo) {
        this.memo = memo;
    }

    /**
     * 获取创建时间
     *
     * @return create_time - 创建时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置创建时间
     *
     * @param createTime 创建时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取上一次修改时间
     *
     * @return lastmodify_time - 上一次修改时间
     */
    public Date getLastmodifyTime() {
        return lastmodifyTime;
    }

    /**
     * 设置上一次修改时间
     *
     * @param lastmodifyTime 上一次修改时间
     */
    public void setLastmodifyTime(Date lastmodifyTime) {
        this.lastmodifyTime = lastmodifyTime;
    }
}