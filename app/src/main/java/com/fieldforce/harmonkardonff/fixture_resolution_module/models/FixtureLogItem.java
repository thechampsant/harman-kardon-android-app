package com.fieldforce.harmonkardonff.fixture_resolution_module.models;

import org.json.JSONObject;

public class FixtureLogItem {

    public int complaintId;
    public String storeId;
    public String fixtureType;
    public String issueType;
    public String remark;
    public String issueAgingDate;
    public String issueCloseDate;
    public String status;
    public String photoUrl;

    public static FixtureLogItem fromJson(JSONObject item) {
        FixtureLogItem log = new FixtureLogItem();
        log.complaintId = item.optInt("ComplaintID", 0);
        log.storeId = item.optString("StoreID", "");
        log.fixtureType = item.optString("FixtureType", "");
        log.issueType = item.optString("IssueType", "");
        log.remark = item.optString("Remark", "");
        log.issueAgingDate = item.optString("IssueAgingDate", "");
        if (item.isNull("IssueCloseDate")) {
            log.issueCloseDate = "";
        } else {
            log.issueCloseDate = item.optString("IssueCloseDate", "");
        }
        log.status = item.optString("Status", "");
        if (item.isNull("PhotoUrl")) {
            log.photoUrl = "";
        } else {
            log.photoUrl = item.optString("PhotoUrl", "");
        }
        return log;
    }

    public boolean isOpen() {
        return "Open".equalsIgnoreCase(status);
    }
}
