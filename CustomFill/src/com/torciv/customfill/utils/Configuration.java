
package com.torciv.customfill.utils;

import com.torciv.customfill.CustomFill;


public class Configuration {
    
    private CustomFill instance;
    
    private String noperms, noTnt, noFilledDisp, noDispAround, filledDisp, lowRad, lowAmount, afilled, nreload, nfill, nclear, dispcleared, nocleardisp, invfull, acleared;
    private int maxRadius, maxTnt, clearRad;
    private double ver;

    public Configuration(CustomFill pl) {
        instance = pl;
        noperms = instance.getConfig().getString("messages-events.no-perm-use");
        noTnt = instance.getConfig().getString("messages-events.not-tnt");
        noFilledDisp = instance.getConfig().getString("messages-events.not-filled-dispensers");
        noDispAround = instance.getConfig().getString("messages-events.not-dispensers-around");
        filledDisp = instance.getConfig().getString("messages-events.filled-dispensers");
        lowRad = instance.getConfig().getString("messages-events.low-radius");
        lowAmount = instance.getConfig().getString("messages-events.low-tnt-amount");
        maxRadius = instance.getConfig().getInt("max-radius");
        maxTnt = instance.getConfig().getInt("max-tnt");
        clearRad = instance.getConfig().getInt("default-clear-rad");
        afilled = instance.getConfig().getString("messages-events.already-filled");
        nreload = instance.getConfig().getString("messages-events.no-perm-reload");
        nfill = instance.getConfig().getString("messages-events.no-perm-fill");
        nclear = instance.getConfig().getString("messages-events.no-perm-clear");
        invfull = instance.getConfig().getString("messages-events.inv-full");
        dispcleared = instance.getConfig().getString("messages-events.dispensers-cleared");
        nocleardisp = instance.getConfig().getString("messages-events.dispensers-to-clear");
        acleared = instance.getConfig().getString("messages-events.already-cleared");
        ver = instance.getConfig().getDouble("version");
    }

    public double getVer() {
        return ver;
    }

    public String getAcleared() {
        return acleared;
    }

    public String getDispcleared() {
        return dispcleared;
    }

    public String getNocleardisp() {
        return nocleardisp;
    }

    public int getClearRad() {
        return clearRad;
    }

    public String getInvfull() {
        return invfull;
    }

    public String getNclear() {
        return nclear;
    }

    public String getNfill() {
        return nfill;
    }

    public String getNReload() {
        return nreload;
    }

    public String getAfilled() {
        return afilled;
    }

    public String getNoperms() {
        return noperms;
    }

    public String getNoTnt() {
        return noTnt;
    }

    public String getNoFilledDisp() {
        return noFilledDisp;
    }

    public String getNoDispAround() {
        return noDispAround;
    }

    public String getFilledDisp() {
        return filledDisp;
    }

    public String getLowRad() {
        return lowRad;
    }

    public String getLowAmount() {
        return lowAmount;
    }

    public int getMaxRadius() {
        return maxRadius;
    }

    public int getMaxTnt() {
        return maxTnt;
    }
    
    
}
