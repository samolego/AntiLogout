package org.samo_lego.antilogout.event;

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
    public static InteractionResult onAttack(Player player, Level level, InteractionHand interactionHand, Entity entity, @Nullable EntityHitResult entityHitResult) {
        if (entity instanceof ILogoutRules) {
            long allowedDc = System.currentTimeMillis() + config.combatTimeout;
            ((ILogoutRules) player).al_setAllowDisconnectAt(allowedDc);
            ((ILogoutRules) entity).al_setAllowDisconnectAt(allowedDc);
        }
        return InteractionResult.PASS;
    }

    public static void onDeath(LivingEntity livingEntity, DamageSource damageSource) {
        if (livingEntity instanceof ILogoutRules player && player.al_isFake()) {
            // Remove player from online players
            ((ServerPlayer) player).connection.onDisconnect(Component.empty());
        }
    }

    public static void onHurt(ServerPlayer target, DamageSource damageSource) {
        long allowedDc = System.currentTimeMillis() + config.combatTimeout;
        if (damageSource.getEntity() instanceof Projectile p && p.getOwner() instanceof ServerPlayer pl) {
            ((ILogoutRules) pl).al_setAllowDisconnectAt(allowedDc);
            ((ILogoutRules) target).al_setAllowDisconnectAt(allowedDc);

        } else if (damageSource.getEntity() instanceof Player || !config.combatLog.playerHurtOnly) {
            ((ILogoutRules) target).al_setAllowDisconnectAt(allowedDc);
        }
    }
}
