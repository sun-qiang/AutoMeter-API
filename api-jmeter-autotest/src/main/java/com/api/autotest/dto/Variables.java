package com.api.autotest.dto;

import java.util.Date;

public class Variables {

    private Long id;

    /**
     * 变量名
     */
    private String variablesname;

    public String getVariablecondition() {
        return variablecondition;
    }

    public void setVariablecondition(String variablecondition) {
        this.variablecondition = variablecondition;
    }

    private String variablecondition;

    public String getVariablestype() {
        return variablestype;
    }

    public void setVariablestype(String variablestype) {
        this.variablestype = variablestype;
    }

    private String variablestype;

    /**
     * 变量描述
     */

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    private String memo;



    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 上一次修改时间
     */
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
     * 获取变量名
     *
     * @return variablesname - 变量名
     */
    public String getVariablesname() {
        return variablesname;
    }

    /**
     * 设置变量名
     *
     * @param variablesname 变量名
     */
    public void setVariablesname(String variablesname) {
        this.variablesname = variablesname;
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