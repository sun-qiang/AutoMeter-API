package com.zoctan.api.dto;

import com.zoctan.api.entity.ApiCasedata;

import java.util.List;

/**
 * @author Zoctan
 * @date 2018/10/16
 */
public class SceneCaseTreeData  {


  public Long getCaseid() {
    return caseid;
  }

  public void setCaseid(Long caseid) {
    this.caseid = caseid;
  }

  private Long caseid;

  /**
   * 用例Id
   */
  private Long id;

  public Long getApiid() {
    return apiid;
  }

  public void setApiid(Long apiid) {
    this.apiid = apiid;
  }

  private Long apiid;


  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getLabel() {
    return label;
  }

  public void setLabel(String label) {
    this.label = label;
  }

  /**
   * 用例名
   */
  private String label;
}
