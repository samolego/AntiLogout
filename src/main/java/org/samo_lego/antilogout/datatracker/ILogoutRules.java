package org.samo_lego.antilogout.datatracker;

public interface ILogoutRules {
    boolean al_allowDisconnect();

    void al_setAllowDisconnect(boolean allowDisconnect);
}
