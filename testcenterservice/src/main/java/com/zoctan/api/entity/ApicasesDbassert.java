package com.zoctan.api.entity;

import java.util.Date;
import javax.persistence.*;

@Table(name = "apicases_dbassert")
public class ApicasesDbassert {
    /**
     * 断言Id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 用例id
     */
    private Long caseid;

    public Long getExpectrecordsnums() {
        return expectrecordsnums;
    }

    public void setExpectrecordsnums(Long expectrecordsnums) {
        this.expectrecordsnums = expectrecordsnums;
    }

    private Long expectrecordsnums;


    /**
     * 断言类型
     */

    public Long getEnvid() {
        return envid;
    }

    public void setEnvid(Long envid) {
        this.envid = envid;
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

    /**
     * 断言子类型
     */
    private Long envid;

    private Long assembleid;

    private String assemblename;


    /**
     * 断言值
     */
    private String enviroment;



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
     * 断言sql表达式
     */
    private String expression;

    /**
     * 获取断言Id
     *
     * @return id - 断言Id
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置断言Id
     *
     * @param id 断言Id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取用例id
     *
     * @return caseid - 用例id
     */
    public Long getCaseid() {
        return caseid;
    }

    /**
     * 设置用例id
     *
     * @param caseid 用例id
     */
    public void setCaseid(Long caseid) {
        this.caseid = caseid;
    }

    /**
     * 获取断言值
     *
     * @return enviroment - 断言值
     */
    public String getEnviroment() {
        return enviroment;
    }

    /**
     * 设置断言值
     *
     * @param enviroment 断言值
     */
    public void setEnviroment(String enviroment) {
        this.enviroment = enviroment;
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

    /**
     * 获取断言sql表达式
     *
     * @return expression - 断言sql表达式
     */
    public String getExpression() {
        return expression;
    }

    /**
     * 设置断言sql表达式
     *
     * @param expression 断言sql表达式
     */
    public void setExpression(String expression) {
        this.expression = expression;
    }
}