package org.samo_lego.antilogout.event;

import me.lucko.fabric.api.permissions.v0.Permissions;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import org.jetbrains.annotations.Nullable;
import org.samo_lego.antilogout.datatracker.ILogoutRules;

import static org.samo_lego.antilogout.AntiLogout.config;

public class EventHandler {


    /**
     * Marks attacker and target as "in combat state".
     *
     * @param attacker        player who attacked
     * @param level           world
     * @param interactionHand hand used to attack
     * @param target          targeted entity
     * @param entityHitResult hit result
     * @return {@link InteractionResult#PASS}
     */
    public static InteractionResult onAttack(Player attacker, Level level, InteractionHand interactionHand, Entity target, @Nullable EntityHitResult entityHitResult) {
        if (target instanceof ILogoutRules || !config.combatLog.playerHurtOnly) {
            long allowedDc = System.currentTimeMillis() + config.combatLog.combatTimeout;

            // Mark target
            if (target instanceof ILogoutRules && !Permissions.check(target, "antilogout.bypass.combat", config.combatLog.bypassPermissionLevel)) {
                ((ILogoutRules) target).al_setAllowDisconnectAt(allowedDc);
            }

            // Mark attacker
            if (!Permissions.check(attacker, "antilogout.bypass.combat", config.combatLog.bypassPermissionLevel)) {
                ((ILogoutRules) attacker).al_setAllowDisconnectAt(allowedDc);
            }
        }
        return InteractionResult.PASS;
    }

    /**
     * Disconnects afk player on death.
     *
     * @param deadEntity   entity that died
     * @param damageSource damage source of death
     */
    public static void onDeath(LivingEntity deadEntity, DamageSource damageSource) {
        if (deadEntity instanceof ILogoutRules player && player.al_isFake()) {
            // Remove player from online players
            ((ServerPlayer) player).connection.onDisconnect(Component.empty());
        }
    }

    /**
     * Marks player as "in combat state" if
     * enabled for that damage source.
     * If damage source is a projectile, shot by
     * a player, then that player is also marked.
     *
     * @param target       player who was hurt
     * @param damageSource damage source
     */
    public static void onHurt(ServerPlayer target, DamageSource damageSource) {
        long allowedDc = System.currentTimeMillis() + config.combatLog.combatTimeout;
        if (damageSource.getEntity() instanceof Projectile p && p.getOwner() instanceof ServerPlayer attacker) {
            if (!Permissions.check(attacker, "antilogout.bypass.combat", config.combatLog.bypassPermissionLevel)) {
                ((ILogoutRules) attacker).al_setAllowDisconnectAt(allowedDc);
            }

            if (!Permissions.check(target, "antilogout.bypass.combat", config.combatLog.bypassPermissionLevel)) {
                ((ILogoutRules) target).al_setAllowDisconnectAt(allowedDc);
            }
        } else if (damageSource.getEntity() instanceof Player || !config.combatLog.playerHurtOnly) {
            ((ILogoutRules) target).al_setAllowDisconnectAt(allowedDc);
        }
    }
}
