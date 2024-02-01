package com.biserv.victorp.research.phdedhec;

import java.util.Properties;

public class PhDEDHECProp {

    private String name;
    private String value;

    private Properties prop;

    public PhDEDHECProp(Properties prop, String name, String value) {
        this.prop = prop;
        this.name = name;
        this.value = value;
    }

    public Properties getProp() {
        return prop;
    }

    public void setProp(Properties prop) {
        this.prop = prop;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
