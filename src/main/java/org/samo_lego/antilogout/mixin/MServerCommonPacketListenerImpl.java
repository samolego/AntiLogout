package org.samo_lego.antilogout.mixin;

import net.minecraft.network.DisconnectionDetails;
import net.minecraft.network.protocol.common.ServerCommonPacketListener;
import net.minecraft.server.network.ServerCommonPacketListenerImpl;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.samo_lego.antilogout.datatracker.ILogoutRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerCommonPacketListenerImpl.class)
public class MServerCommonPacketListenerImpl {
    @Inject(method = "disconnect(Lnet/minecraft/network/DisconnectionDetails;)V", at = @At("TAIL"))
    private void al$disconnect(DisconnectionDetails disconnectionDetails, CallbackInfo ci) {
        if (((Object) this) instanceof ServerGamePacketListenerImpl serverGamePacketListener) {
            if (((ILogoutRules) serverGamePacketListener.player).al_isFake()) {
                serverGamePacketListener.onDisconnect(disconnectionDetails);
            }
        }
    }
}
