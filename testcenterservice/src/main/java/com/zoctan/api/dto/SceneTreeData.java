package com.zoctan.api.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Zoctan
 * @date 2018/10/16
 */
public class SceneTreeData {

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

  private Long id;

  /**
   * 用例名
   */
  private String label;


  public List<SceneCaseTreeData> getChildren() {
    return children;
  }

  public void setChildren(List<SceneCaseTreeData> children) {
    this.children = children;
  }

  private List<SceneCaseTreeData> children;

}
