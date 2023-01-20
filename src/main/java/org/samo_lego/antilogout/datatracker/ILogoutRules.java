package org.samo_lego.antilogout.datatracker;

import net.minecraft.server.level.ServerPlayer;

import java.util.HashSet;
import java.util.Set;

public interface ILogoutRules {

    /**
     * Set of players that have disconnected,
     * but are still present in the world.
     */
    Set<ServerPlayer> DISCONNECTED_PLAYERS = new HashSet<>();

    /**
     * Whether to allow disconnect for this player.
     *
     * @return true if allowed, false otherwise
     */
    boolean al_allowDisconnect();

    /**
     * Sets the time when the player can disconnect.
     *
     * @param systemTime time in milliseconds at which the player can disconnect without staying in the world.
     */
    void al_setAllowDisconnectAt(long systemTime);

    /**
     * Sets whether the player can disconnect.
     *
     * @param allow true if disconnect is allowed, false otherwise
     */

    void al_setAllowDisconnect(boolean allow);

    /**
     * Whether the player is fake (present in the world, but not connected).
     *
     * @return true if fake, false otherwise
     */
    boolean al_isFake();


    /**
     * Called when the player disconnects.
     */
    void al_onRealDisconnect();
}
