package org.samo_lego.antilogout.mixin.accessor;

import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ServerPlayer.class)
public interface AServerPlayer {
    @Accessor("disconnected")
    void setDisconnected(boolean disconnected);
}
