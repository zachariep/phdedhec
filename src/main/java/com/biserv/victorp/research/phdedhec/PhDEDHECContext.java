package com.biserv.victorp.research.phdedhec;

public class PhDEDHECContext {

    private PhDEDHECConfig phDEDHECConfig;

    public PhDEDHECContext(PhDEDHECController phDEDHECController) {
        init(phDEDHECController);
    }

    public void init(PhDEDHECController phDEDHECController) {
        this.phDEDHECConfig = new PhDEDHECConfig(phDEDHECController);
    }

    public PhDEDHECConfig getPhDEDHECConfig() {
        return phDEDHECConfig;
    }
}
