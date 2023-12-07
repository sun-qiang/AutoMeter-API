package com.zoctan.api.entity;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

public class Macdepunit {
    /**
     * Id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public Long getProjectid() {
        return projectid;
    }

    public void setProjectid(Long projectid) {
        this.projectid = projectid;
    }

    private Long projectid;

    /**
     * 微服务Id
     */
    private Long depunitid;

    public Long getAssembleid() {
        return assembleid;
    }

    public void setAssembleid(Long assembleid) {
        this.assembleid = assembleid;
    }

    private Long assembleid;

    /**
     * 微服务名
     */
    private String deployunitname;

    public String getConnectstr() {
        return connectstr;
    }

    public void setConnectstr(String connectstr) {
        this.connectstr = connectstr;
    }

    private String connectstr;





//    public String getIp() {
//        return ip;
//    }
//
//    public void setIp(String ip) {
//        this.ip = ip;
//    }
//
//    private String ip;



    public String getAssembletype() {
        return assembletype;
    }

    public void setAssembletype(String assembletype) {
        this.assembletype = assembletype;
    }

    private String assembletype;

    /**
     * 服务器Id
     */
    private Long machineid;

    /**
     * 机器名
     */
    private String machinename;

    /**
     * 环境Id
     */
    private Long envid;

    /**
     * 环境名
     */
    private String enviromentname;

    /**
     * 服务域名
     */
    private String domain;

    public String getVisittype() {
        return visittype;
    }

    public void setVisittype(String visittype) {
        this.visittype = visittype;
    }

    private String visittype;

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
     * 获取微服务Id
     *
     * @return depunitid - 微服务Id
     */
    public Long getDepunitid() {
        return depunitid;
    }

    /**
     * 设置微服务Id
     *
     * @param depunitid 微服务Id
     */
    public void setDepunitid(Long depunitid) {
        this.depunitid = depunitid;
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
     * 获取服务器Id
     *
     * @return machineid - 服务器Id
     */
    public Long getMachineid() {
        return machineid;
    }

    /**
     * 设置服务器Id
     *
     * @param machineid 服务器Id
     */
    public void setMachineid(Long machineid) {
        this.machineid = machineid;
    }

    /**
     * 获取机器名
     *
     * @return machinename - 机器名
     */
    public String getMachinename() {
        return machinename;
    }

    /**
     * 设置机器名
     *
     * @param machinename 机器名
     */
    public void setMachinename(String machinename) {
        this.machinename = machinename;
    }

    /**
     * 获取环境Id
     *
     * @return envid - 环境Id
     */
    public Long getEnvid() {
        return envid;
    }

    /**
     * 设置环境Id
     *
     * @param envid 环境Id
     */
    public void setEnvid(Long envid) {
        this.envid = envid;
    }

    /**
     * 获取环境名
     *
     * @return enviromentname - 环境名
     */
    public String getEnviromentname() {
        return enviromentname;
    }

    /**
     * 设置环境名
     *
     * @param enviromentname 环境名
     */
    public void setEnviromentname(String enviromentname) {
        this.enviromentname = enviromentname;
    }

    /**
     * 获取服务域名
     *
     * @return domain - 服务域名
     */
    public String getDomain() {
        return domain;
    }

    /**
     * 设置服务域名
     *
     * @param domain 服务域名
     */
    public void setDomain(String domain) {
        this.domain = domain;
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