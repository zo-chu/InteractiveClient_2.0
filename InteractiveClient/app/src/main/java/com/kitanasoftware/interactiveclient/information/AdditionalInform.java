package com.kitanasoftware.interactiveclient.information;

import com.kitanasoftware.interactiveclient.information.Information;

/**
 * Created by dasha on 29/01/16.
 */
public class AdditionalInform extends Information {
    String companyName;

    public AdditionalInform(InformType type, String companyName) {
        super(type);
        this.companyName = companyName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}
