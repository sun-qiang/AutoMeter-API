package com.api.autotest.dto;

import java.util.Date;

public class ApicasesAssert {
    private Long id;

    /**
     * 用例id
     */
    private Long caseid;

    /**
     * 断言类型
     */
    private String asserttype;

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public String getAssertcondition() {
        return assertcondition;
    }

    public void setAssertcondition(String assertcondition) {
        this.assertcondition = assertcondition;
    }

    private String expression;

    private String assertcondition;


    public String getAssertvaluetype() {
        return assertvaluetype;
    }

    public void setAssertvaluetype(String assertvaluetype) {
        this.assertvaluetype = assertvaluetype;
    }

    private String assertvaluetype;
    /**
     * 断言子类型
     */
    private String assertsubtype;

    /**
     * 断言值
     */
    private String assertvalues;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 上一次修改时间
     */
    private Date lastmodifyTime;

    /**
     * 创建者
     */
    private String creator;

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
     * 获取断言类型
     *
     * @return asserttype - 断言类型
     */
    public String getAsserttype() {
        return asserttype;
    }

    /**
     * 设置断言类型
     *
     * @param asserttype 断言类型
     */
    public void setAsserttype(String asserttype) {
        this.asserttype = asserttype;
    }

    /**
     * 获取断言子类型
     *
     * @return assertsubtype - 断言子类型
     */
    public String getAssertsubtype() {
        return assertsubtype;
    }

    /**
     * 设置断言子类型
     *
     * @param assertsubtype 断言子类型
     */
    public void setAssertsubtype(String assertsubtype) {
        this.assertsubtype = assertsubtype;
    }

    /**
     * 获取断言值
     *
     * @return assertvalues - 断言值
     */
    public String getAssertvalues() {
        return assertvalues;
    }

    /**
     * 设置断言值
     *
     * @param assertvalues 断言值
     */
    public void setAssertvalues(String assertvalues) {
        this.assertvalues = assertvalues;
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
