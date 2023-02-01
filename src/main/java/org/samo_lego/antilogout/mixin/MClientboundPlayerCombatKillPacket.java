package org.samo_lego.antilogout.mixin;

import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundPlayerCombatKillPacket;
import net.minecraft.world.damagesource.CombatTracker;
import org.samo_lego.antilogout.datatracker.ILogoutRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientboundPlayerCombatKillPacket.class)
public class MClientboundPlayerCombatKillPacket {
    @Inject(method = "<init>(Lnet/minecraft/world/damagesource/CombatTracker;Lnet/minecraft/network/chat/Component;)V", at = @At("RETURN"))
    private void constructor(CombatTracker combatTracker, Component component, CallbackInfo ci) {
        if (combatTracker.getMob() instanceof ILogoutRules rules && rules.al_isFake()) {
            // Player won't see death message, we must save it for later (#1)
            ILogoutRules.SKIPPED_DEATH_MESSAGES.put(combatTracker.getMob().getUUID(), component);
        }
    }
}
