package org.samo_lego.antilogout.mixin;

import io.netty.channel.Channel;
import net.minecraft.network.Connection;
import net.minecraft.network.PacketListener;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.samo_lego.antilogout.datatracker.ILogoutRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(Connection.class)
public abstract class MConnection {

    @Shadow
    private Channel channel;

    @Shadow
    public abstract PacketListener getPacketListener();

    /**
     * This method gets called when PLAYER wants to disconnect
     *
     * @param ci
     */
    @Inject(method = "handleDisconnection", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/PacketListener;onDisconnect(Lnet/minecraft/network/chat/Component;)V", ordinal = 1), cancellable = true)
    private void al_handleDisconnection(CallbackInfo ci) {
        if (this.getPacketListener() instanceof ServerGamePacketListenerImpl listener) {
            if (!((ILogoutRules) listener.getPlayer()).al_allowDisconnect()) {
                this.channel.close();
                ((ILogoutRules) listener.getPlayer()).al_onRealDisconnect();
                ci.cancel();
            }
        }
    }
}
