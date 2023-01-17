package org.samo_lego.antilogout.mixin;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.samo_lego.antilogout.datatracker.ILogoutRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerGamePacketListenerImpl.class)
public class MServerGamePacketListenerImpl {
    @Shadow
    public ServerPlayer player;

    /**
     * Cancels kicking out the player if needed.
     *
     * @param ci
     */
    @Inject(method = "onDisconnect", at = @At("HEAD"), cancellable = true)
    private void onDisconnect(CallbackInfo ci) {
        if (!((ILogoutRules) this.player).al_allowDisconnect()) {
            ci.cancel();
        }
    }
}