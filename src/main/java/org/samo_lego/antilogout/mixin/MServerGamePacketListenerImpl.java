package org.samo_lego.antilogout.mixin;

import net.minecraft.network.Connection;
import net.minecraft.network.DisconnectionDetails;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.CommonListenerCookie;
import net.minecraft.server.network.ServerCommonPacketListenerImpl;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.samo_lego.antilogout.AntiLogout;
import org.samo_lego.antilogout.datatracker.ILogoutRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerGamePacketListenerImpl.class)
public abstract class MServerGamePacketListenerImpl extends ServerCommonPacketListenerImpl {
    @Shadow
    public ServerPlayer player;

    public MServerGamePacketListenerImpl(MinecraftServer minecraftServer, Connection connection, CommonListenerCookie commonListenerCookie) {
        super(minecraftServer, connection, commonListenerCookie);
    }

    @Shadow
    public abstract ServerPlayer getPlayer();

    /**
     * Hooks in the disconnect method, so that /afk works properly
     *
     * @param disconnectionDetails
     * @param ci
     */
    @Inject(method = "onDisconnect", at = @At("HEAD"), cancellable = true)
    private void al$onDisconnect(DisconnectionDetails disconnectionDetails, CallbackInfo ci) {
        // Generic disconnect is handled by MConnection#al_handleDisconnection
        if (!((ILogoutRules) this.getPlayer()).al_allowDisconnect() && disconnectionDetails.reason() == AntiLogout.AFK_MESSAGE) {
            ((ILogoutRules) this.player).al_onRealDisconnect();

            // Disable disconnecting in this case
            ci.cancel();
        }
    }
}
