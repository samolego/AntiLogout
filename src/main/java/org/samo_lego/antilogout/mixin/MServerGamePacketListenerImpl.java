package org.samo_lego.antilogout.mixin;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.samo_lego.antilogout.AntiLogout;
import org.samo_lego.antilogout.datatracker.ILogoutRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerGamePacketListenerImpl.class)
public abstract class MServerGamePacketListenerImpl {
    @Shadow
    public ServerPlayer player;

    @Shadow
    public abstract void onDisconnect(Component component);

    @Shadow
    public abstract ServerPlayer getPlayer();

    @Inject(method = "disconnect", at = @At("TAIL"))
    private void al$disconnect(Component component, CallbackInfo ci) {
        if (((ILogoutRules) this.player).al_isFake()) {
            this.onDisconnect(component);
        }
    }

    /**
     * Hooks in the disconnect method, so that /afk works properly
     *
     * @param dcReason
     * @param ci
     */
    @Inject(method = "onDisconnect", at = @At("HEAD"), cancellable = true)
    private void al$onDisconnect(Component dcReason, CallbackInfo ci) {
        // Generic disconnect is handled by MConnection#al_handleDisconnection
        if (!((ILogoutRules) this.getPlayer()).al_allowDisconnect() && dcReason == AntiLogout.AFK_MESSAGE) {
            ((ILogoutRules) this.player).al_onRealDisconnect();

            // Disable disconnecting in this case
            ci.cancel();
        }
    }
}
