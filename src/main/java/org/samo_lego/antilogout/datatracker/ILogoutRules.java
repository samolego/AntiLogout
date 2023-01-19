package org.samo_lego.antilogout.datatracker;

import net.minecraft.server.level.ServerPlayer;

import java.util.HashSet;
import java.util.Set;

public interface ILogoutRules {
    Set<ServerPlayer> DISCONNECTED_PLAYERS = new HashSet<>();

    boolean al_allowDisconnect();

    void al_setAllowDisconnectAt(long systemTime);

    void al_setAllowDisconnect(boolean allow);

    boolean al_isFake();

    void al_onRealDisconnect();
}
