package com.zoctan.api.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * @author hacker li
 * @since 08/02/2022 12:02
 */
@Setter
@Getter
public class FunctionConditionStatis {

    public Long getTestCollectionConditionsNUms() {
        return TestCollectionConditionsNUms;
    }

    public void setTestCollectionConditionsNUms(Long testCollectionConditionsNUms) {
        TestCollectionConditionsNUms = testCollectionConditionsNUms;
    }

    public Long getCaseConditionNums() {
        return CaseConditionNums;
    }

    public void setCaseConditionNums(Long caseConditionNums) {
        CaseConditionNums = caseConditionNums;
    }

    private Long TestCollectionConditionsNUms;
    private Long CaseConditionNums;

    public Long getSceneConditionNums() {
        return SceneConditionNums;
    }

    public void setSceneConditionNums(Long sceneConditionNums) {
        SceneConditionNums = sceneConditionNums;
    }

    private Long SceneConditionNums;




}
