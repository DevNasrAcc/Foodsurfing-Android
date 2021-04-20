package com.adeel.foodsurfing.GetSetMethod;

import java.util.List;

/**
 * Created by Muhammad Adeel on 9/7/2017.
 */

public class ListSettings {

    private List<SettingsHeaderResponse> headerResponses ;
    private List<SettingsChildResponse> childResponses ;

    public List<SettingsHeaderResponse> getHeaderResponses() {
        return headerResponses;
    }

    public void setHeaderResponses(List<SettingsHeaderResponse> headerResponses) {
        this.headerResponses = headerResponses;
    }

    public List<SettingsChildResponse> getChildResponses() {
        return childResponses;
    }

    public void setChildResponses(List<SettingsChildResponse> childResponses) {
        this.childResponses = childResponses;
    }
}
