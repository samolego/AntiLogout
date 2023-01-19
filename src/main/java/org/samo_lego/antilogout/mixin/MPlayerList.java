package org.samo_lego.antilogout.mixin;

import com.mojang.authlib.GameProfile;
import net.minecraft.network.Connection;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import org.samo_lego.antilogout.datatracker.ILogoutRules;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;
import java.util.UUID;

@Mixin(PlayerList.class)
public class MPlayerList {

    @Shadow
    @Final
    private MinecraftServer server;

    /**
     * When a player wants to connect but is still online,
     * we allow players with same uuid to be disconnected.
     *
     * @param gameProfile
     * @param cir
     * @param uUID
     * @param matchingPlayers
     * @param serverPlayer2
     */
    @Inject(method = "getPlayerForLogin",
            at = @At(value = "INVOKE",
                    target = "Ljava/util/List;iterator()Ljava/util/Iterator;"),
            locals = LocalCapture.CAPTURE_FAILHARD)
    private void onPlayerLogin(GameProfile gameProfile, CallbackInfoReturnable<ServerPlayer> cir, UUID uUID, List<ServerPlayer> matchingPlayers, ServerPlayer serverPlayer2) {
        for (ServerPlayer player : matchingPlayers) {
            // Allows disconnect
            ((ILogoutRules) player).al_setAllowDisconnect(true);

            // Removes player so that the internal finite state machine in ServerLoginPacketListenerImpl can continue
            this.server.getPlayerList().remove(player);
        }
    }

    @Inject(method = "placeNewPlayer", at = @At("RETURN"))
    private void onPlayerJoin(Connection connection, ServerPlayer player, CallbackInfo ci) {
        //((AServerGamePacketListenerImpl) player.connection).setConnection(connection);
    }
}
