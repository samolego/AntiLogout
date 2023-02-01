package org.samo_lego.antilogout.datatracker;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.ApiStatus;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import static org.samo_lego.antilogout.AntiLogout.config;

public interface ILogoutRules {

    /**
     * Set of players that have disconnected,
     * but are still present in the world.
     */
    Set<ServerPlayer> DISCONNECTED_PLAYERS = new HashSet<>();

    Map<UUID, Component> SKIPPED_DEATH_MESSAGES = new HashMap<>();

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
     * Marks the player as in combat state until the specified time.
     *
     * @param systemTime time in milliseconds at which the player leaves state.
     */
    default void al_setInCombatUntil(long systemTime) {
        this.al_setAllowDisconnectAt(systemTime);

        if (config.combatLog.notifyOnCombat) {
            // Inform player
            long duration = (long) Math.ceil((systemTime - System.currentTimeMillis()) / 1000.0D);
            ((ServerPlayer) this).displayClientMessage(this.al$getStartCombatMessage(duration), true);

            this.al$delay(systemTime, () ->
                    ((ServerPlayer) this).displayClientMessage(this.al$getEndCombatMessage(duration), true));
        }
    }

    /**
     * Schedules a task execution after the specified delay.
     * Only one can be scheduled at a time.
     * (Scheduling new task will cancel the previous one)
     *
     * @param at   system time at which the task should be executed
     * @param task task to execute
     */
    void al$delay(long at, Runnable task);

    /**
     * Gets the combat message.
     *
     * @param duration duration of combat state in seconds
     * @return combat message
     */
    @ApiStatus.Internal
    default Component al$getStartCombatMessage(long duration) {
        return Component.literal("[AL] ").withStyle(ChatFormatting.DARK_RED).append(
                Component.translatable(config.combatLog.combatEnterMessage, duration)
                        .withStyle(ChatFormatting.RED));
    }


    @ApiStatus.Internal
    default Component al$getEndCombatMessage(long duration) {
        return Component.literal("[AL] ").withStyle(ChatFormatting.DARK_GREEN).append(
                Component.translatable(config.combatLog.combatEndMessage, duration)
                        .withStyle(ChatFormatting.GREEN));
    }

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
