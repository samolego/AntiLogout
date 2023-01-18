package org.samo_lego.antilogout.mixin;

import net.minecraft.Util;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.samo_lego.antilogout.datatracker.ILogoutRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ServerGamePacketListenerImpl.class)
public class MServerGamePacketListenerImpl {
    @Shadow
    public ServerPlayer player;

    /**
     * Disables keepAlive timeout for players that shouldn't be logged out.
     *
     * @return 0 if player shouldn't be logged out, otherwise the original value.
     */
    @Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/Util;getMillis()J"))
    private long disableKeepAlive() {
        if (!((ILogoutRules) this.player).al_allowDisconnect()) {
            return 0;
        }
        return Util.getMillis();
    }
}
