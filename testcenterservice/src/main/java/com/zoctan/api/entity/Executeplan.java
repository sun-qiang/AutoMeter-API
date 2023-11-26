package com.zoctan.api.entity;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

public class Executeplan {
    /**
     * 执行计划Id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public Long getScenenums() {
        return scenenums;
    }

    public void setScenenums(Long scenenums) {
        this.scenenums = scenenums;
    }

    private Long scenenums;



    public Long getProjectid() {
        return projectid;
    }

    public void setProjectid(Long projectid) {
        this.projectid = projectid;
    }

    private Long projectid;

    public Long getCasecounts() {
        return casecounts;
    }

    public void setCasecounts(Long casecounts) {
        this.casecounts = casecounts;
    }

    private Long casecounts;

    /**
     * 执行计划名
     */
    private String executeplanname;

    public String getBusinesstype() {
        return businesstype;
    }

    public void setBusinesstype(String businesstype) {
        this.businesstype = businesstype;
    }

    private String businesstype;


    /**
     * 状态，new，waiting，running，pause，finish
     */
    private String status;

    public String getUsetype() {
        return usetype;
    }

    public void setUsetype(String usetype) {
        this.usetype = usetype;
    }

    /**
     * 运行类型
     */
    private String usetype;


    public String getDingdingtoken() {
        return dingdingtoken;
    }

    public void setDingdingtoken(String dingdingtoken) {
        this.dingdingtoken = dingdingtoken;
    }

    private String dingdingtoken;



    /**
     * 备注
     */
    private String memo;

    public String getEnviromentname() {
        return enviromentname;
    }

    public void setEnviromentname(String enviromentname) {
        this.enviromentname = enviromentname;
    }

    private String enviromentname;


    public Long getEnvid() {
        return envid;
    }

    public void setEnvid(Long envid) {
        this.envid = envid;
    }

    private Long envid;

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    private String creator;

    public String getRunmode() {
        return runmode;
    }

    public void setRunmode(String runmode) {
        this.runmode = runmode;
    }

    private String runmode;


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
     * 获取执行计划Id
     *
     * @return id - 执行计划Id
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置执行计划Id
     *
     * @param id 执行计划Id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取执行计划名
     *
     * @return executeplanname - 执行计划名
     */
    public String getExecuteplanname() {
        return executeplanname;
    }

    /**
     * 设置执行计划名
     *
     * @param executeplanname 执行计划名
     */
    public void setExecuteplanname(String executeplanname) {
        this.executeplanname = executeplanname;
    }

    /**
     * 获取状态，new，waiting，running，pause，finish
     *
     * @return status - 状态，new，waiting，running，pause，finish
     */
    public String getStatus() {
        return status;
    }

    /**
     * 设置状态，new，waiting，running，pause，finish
     *
     * @param status 状态，new，waiting，running，pause，finish
     */
    public void setStatus(String status) {
        this.status = status;
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