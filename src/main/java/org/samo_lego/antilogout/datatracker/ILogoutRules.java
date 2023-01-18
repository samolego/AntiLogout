package org.samo_lego.antilogout.datatracker;

public interface ILogoutRules {
    boolean al_allowDisconnect();

    void al_setAllowDisconnectAt(long systemTime);

    void al_setAllowDisconnect(boolean allow);
}
