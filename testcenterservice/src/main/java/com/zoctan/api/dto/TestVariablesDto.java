package com.zoctan.api.dto;

import com.zoctan.api.entity.Apicases;
import com.zoctan.api.entity.Testvariables;

/**
 * @author Zoctan
 * @date 2018/10/16
 */
public class TestVariablesDto extends Testvariables {
  public String getApiname() {
    return apiname;
  }

  public void setApiname(String apiname) {
    this.apiname = apiname;
  }

  public String getDeployunitname() {
    return deployunitname;
  }

  public void setDeployunitname(String deployunitname) {
    this.deployunitname = deployunitname;
  }

  private String apiname;
  private String deployunitname;

}
