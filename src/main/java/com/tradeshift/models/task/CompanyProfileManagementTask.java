package com.tradeshift.models.task;

import com.tradeshift.models.CompanyProfile;

public class CompanyProfileManagementTask extends Task {
    private CompanyProfile _companyProfile;

    public CompanyProfile getCompanyProfile() {
        return _companyProfile;
    }

    public void setSompanyProfile(CompanyProfile companyProfile) {
        _companyProfile = companyProfile;
    }

    @Override
    public TaskType getTaskType() {
        return TaskType.COMPANY_PROFILE;
    }
}
