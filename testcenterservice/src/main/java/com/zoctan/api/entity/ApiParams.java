package com.zoctan.api.entity;

import javax.persistence.*;
import java.util.Date;

@Table(name = "api_params")
public class ApiParams {
    /**
     * Id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public Long getCreatorid() {
        return creatorid;
    }

    public void setCreatorid(Long creatorid) {
        this.creatorid = creatorid;
    }

    private Long creatorid;


    public Long getApiid() {
        return apiid;
    }

    public void setApiid(Long apiid) {
        this.apiid = apiid;
    }

    private Long apiid;

    public Long getDeployunitid() {
        return deployunitid;
    }

    public void setDeployunitid(Long deployunitid) {
        this.deployunitid = deployunitid;
    }

    private Long deployunitid;

    /**
     * api名
     */
    private String apiname;

    /**
     * 微服务名
     */
    private String deployunitname;

    /**
     * api属性类型，header，body
     */
    private String propertytype;


    public String getKeytype() {
        return keytype;
    }

    public void setKeytype(String keytype) {
        this.keytype = keytype;
    }

    private String keytype;



    /**
     * key名
     */
    private String keyname;


    public String getKeydefaultvalue() {
        return keydefaultvalue;
    }

    public void setKeydefaultvalue(String keydefaultvalue) {
        this.keydefaultvalue = keydefaultvalue;
    }

    private String keydefaultvalue;



    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    private String creator;

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
     * 获取api名
     *
     * @return apiname - api名
     */
    public String getApiname() {
        return apiname;
    }

    /**
     * 设置api名
     *
     * @param apiname api名
     */
    public void setApiname(String apiname) {
        this.apiname = apiname;
    }

    /**
     * 获取微服务名
     *
     * @return deployunitname - 微服务名
     */
    public String getDeployunitname() {
        return deployunitname;
    }

    /**
     * 设置微服务名
     *
     * @param deployunitname 微服务名
     */
    public void setDeployunitname(String deployunitname) {
        this.deployunitname = deployunitname;
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
     * 获取key名
     *
     * @return keyname - key名
     */
    public String getKeyname() {
        return keyname;
    }

    /**
     * 设置key名
     *
     * @param keyname key名
     */
    public void setKeyname(String keyname) {
        this.keyname = keyname;
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