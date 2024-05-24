package com.api.autotest.service;

import com.api.autotest.dto.RequestObject;

public interface SubCondition {
    void DoSubCondition(long ConditionID, RequestObject requestObject);
}
