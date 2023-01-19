package org.samo_lego.antilogout.mixin;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerConnectionListener;
import org.samo_lego.antilogout.datatracker.ILogoutRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerConnectionListener.class)
public class MServerConnectionListener {

    /**
     * Ticks all the players that are in {@link ILogoutRules#DISCONNECTED_PLAYERS}
     *
     * @param ci
     */
    @Inject(method = "tick", at = @At("TAIL"))
    private void onTickConnections(CallbackInfo ci) {
        // Tick "disconnected" players as well
        ILogoutRules.DISCONNECTED_PLAYERS.forEach(ServerPlayer::doTick);
    }
}
