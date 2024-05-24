package com.api.autotest.dto;

import java.util.Date;

public class Testvariables {
    /**
     * Id
     */
    private Long id;

    /**
     * 变量名
     */
    private String testvariablesname;

    /**
     * 变量类型，用例变量，全局变量
     */
    private String testvariablestype;

    /**
     * 变量表达
     */
    private String variablesexpress;

    /**
     * 备注
     */
    private String memo;

    public String getVariablesdes() {
        return variablesdes;
    }

    public void setVariablesdes(String variablesdes) {
        this.variablesdes = variablesdes;
    }

    private String variablesdes;


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
     * @return testvariablesname - 变量名
     */
    public String getTestvariablesname() {
        return testvariablesname;
    }

    /**
     * 设置变量名
     *
     * @param testvariablesname 变量名
     */
    public void setTestvariablesname(String testvariablesname) {
        this.testvariablesname = testvariablesname;
    }

    /**
     * 获取变量类型，用例变量，全局变量
     *
     * @return testvariablestype - 变量类型，用例变量，全局变量
     */
    public String getTestvariablestype() {
        return testvariablestype;
    }

    /**
     * 设置变量类型，用例变量，全局变量
     *
     * @param testvariablestype 变量类型，用例变量，全局变量
     */
    public void setTestvariablestype(String testvariablestype) {
        this.testvariablestype = testvariablestype;
    }

    /**
     * 获取变量表达
     *
     * @return variablesexpress - 变量表达
     */
    public String getVariablesexpress() {
        return variablesexpress;
    }

    /**
     * 设置变量表达
     *
     * @param variablesexpress 变量表达
     */
    public void setVariablesexpress(String variablesexpress) {
        this.variablesexpress = variablesexpress;
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