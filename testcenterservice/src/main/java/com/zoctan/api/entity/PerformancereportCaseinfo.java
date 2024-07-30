package com.zoctan.api.entity;

import java.util.Date;
import javax.persistence.*;

@Table(name = "performancereport_caseinfo")
public class PerformancereportCaseinfo {
    /**
     * Id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 执行计划Id
     */
    private Long execplanid;

    /**
     * 批次名
     */
    private String batchname;

    /**
     * 用例名
     */
    private String casename;

    /**
     * 执行机Id
     */
    private Long slaverid;

    /**
     * 场景id
     */
    private Long testsceneid;

    /**
     * 类型
     */
    private String contenttype;

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
     * 内容
     */
    private String content;

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
     * 获取执行计划Id
     *
     * @return execplanid - 执行计划Id
     */
    public Long getExecplanid() {
        return execplanid;
    }

    /**
     * 设置执行计划Id
     *
     * @param execplanid 执行计划Id
     */
    public void setExecplanid(Long execplanid) {
        this.execplanid = execplanid;
    }

    /**
     * 获取批次名
     *
     * @return batchname - 批次名
     */
    public String getBatchname() {
        return batchname;
    }

    /**
     * 设置批次名
     *
     * @param batchname 批次名
     */
    public void setBatchname(String batchname) {
        this.batchname = batchname;
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
     * 获取执行机Id
     *
     * @return slaverid - 执行机Id
     */
    public Long getSlaverid() {
        return slaverid;
    }

    /**
     * 设置执行机Id
     *
     * @param slaverid 执行机Id
     */
    public void setSlaverid(Long slaverid) {
        this.slaverid = slaverid;
    }

    /**
     * 获取场景id
     *
     * @return testsceneid - 场景id
     */
    public Long getTestsceneid() {
        return testsceneid;
    }

    /**
     * 设置场景id
     *
     * @param testsceneid 场景id
     */
    public void setTestsceneid(Long testsceneid) {
        this.testsceneid = testsceneid;
    }

    /**
     * 获取类型
     *
     * @return contenttype - 类型
     */
    public String getContenttype() {
        return contenttype;
    }

    /**
     * 设置类型
     *
     * @param contenttype 类型
     */
    public void setContenttype(String contenttype) {
        this.contenttype = contenttype;
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

    /**
     * 获取内容
     *
     * @return content - 内容
     */
    public String getContent() {
        return content;
    }

    /**
     * 设置内容
     *
     * @param content 内容
     */
    public void setContent(String content) {
        this.content = content;
    }
}