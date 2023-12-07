package com.zoctan.api.entity;

import javax.persistence.*;
import java.util.Date;

@Table(name = "condition_db")
public class ConditionDb {
    /**
     * Id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 条件id
     */
    private Long conditionid;

    public String getSubconditionname() {
        return subconditionname;
    }

    public void setSubconditionname(String subconditionname) {
        this.subconditionname = subconditionname;
    }

    private String subconditionname;


    public String getConditionname() {
        return conditionname;
    }

    public void setConditionname(String conditionname) {
        this.conditionname = conditionname;
    }

    public Long getAssembleid() {
        return assembleid;
    }

    public void setAssembleid(Long assembleid) {
        this.assembleid = assembleid;
    }

    public String getAssemblename() {
        return assemblename;
    }

    public void setAssemblename(String assemblename) {
        this.assemblename = assemblename;
    }

    public String getEnviromentname() {
        return enviromentname;
    }

    public void setEnviromentname(String enviromentname) {
        this.enviromentname = enviromentname;
    }

    private String conditionname;
    private Long assembleid;
    private String assemblename;
    private String enviromentname;

    public String getConditiontype() {
        return conditiontype;
    }

    public void setConditiontype(String conditiontype) {
        this.conditiontype = conditiontype;
    }

    private String conditiontype;




    /**
     * 环境id
     */
    private Long enviromentid;

    /**
     * 数据库类型
     */
    private String dbtype;

    /**
     * db执行内容
     */
    private String dbcontent;

    /**
     * db连接字
     */
    private String connectstr;

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
     * 创建者
     */
    private String creator;

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
     * 获取条件id
     *
     * @return conditionid - 条件id
     */
    public Long getConditionid() {
        return conditionid;
    }

    /**
     * 设置条件id
     *
     * @param conditionid 条件id
     */
    public void setConditionid(Long conditionid) {
        this.conditionid = conditionid;
    }

    /**
     * 获取环境id
     *
     * @return enviromentid - 环境id
     */
    public Long getEnviromentid() {
        return enviromentid;
    }

    /**
     * 设置环境id
     *
     * @param enviromentid 环境id
     */
    public void setEnviromentid(Long enviromentid) {
        this.enviromentid = enviromentid;
    }

    /**
     * 获取数据库类型
     *
     * @return dbtype - 数据库类型
     */
    public String getDbtype() {
        return dbtype;
    }

    /**
     * 设置数据库类型
     *
     * @param dbtype 数据库类型
     */
    public void setDbtype(String dbtype) {
        this.dbtype = dbtype;
    }

    /**
     * 获取db执行内容
     *
     * @return dbcontent - db执行内容
     */
    public String getDbcontent() {
        return dbcontent;
    }

    /**
     * 设置db执行内容
     *
     * @param dbcontent db执行内容
     */
    public void setDbcontent(String dbcontent) {
        this.dbcontent = dbcontent;
    }

    /**
     * 获取db连接字
     *
     * @return connectstr - db连接字
     */
    public String getConnectstr() {
        return connectstr;
    }

    /**
     * 设置db连接字
     *
     * @param connectstr db连接字
     */
    public void setConnectstr(String connectstr) {
        this.connectstr = connectstr;
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

    /**
     * 获取创建者
     *
     * @return creator - 创建者
     */
    public String getCreator() {
        return creator;
    }

    /**
     * 设置创建者
     *
     * @param creator 创建者
     */
    public void setCreator(String creator) {
        this.creator = creator;
    }
}