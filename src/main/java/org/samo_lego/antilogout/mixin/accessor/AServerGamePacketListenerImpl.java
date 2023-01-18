package org.samo_lego.antilogout.mixin.accessor;

import net.minecraft.network.Connection;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ServerGamePacketListenerImpl.class)
public interface AServerGamePacketListenerImpl {
    @Mutable
    @Accessor("connection")
    void setConnection(Connection connection);
}
