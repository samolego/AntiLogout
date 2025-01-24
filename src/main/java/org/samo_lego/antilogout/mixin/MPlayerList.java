package org.samo_lego.antilogout.mixin;

import com.mojang.authlib.GameProfile;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ClientInformation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import org.samo_lego.antilogout.datatracker.ILogoutRules;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;
import java.util.UUID;

/**
 * Kicks same players that are in {@link ILogoutRules#DISCONNECTED_PLAYERS} list
 * when player with same UUID joins.
 */
@Mixin(PlayerList.class)
public abstract class MPlayerList {

    @Shadow
    @Final
    private MinecraftServer server;

    @Shadow public abstract List<ServerPlayer> getPlayers();

    /**
     * When a player wants to connect but is still online,
     * we allow players with same uuid to be disconnected.
     */
    @Inject(method = "getPlayerForLogin", at = @At("HEAD"))
    private void onPlayerLogin(GameProfile gameProfile, ClientInformation clientInformation, CallbackInfoReturnable<ServerPlayer> cir) {
        var matchingPlayers = getPlayers().stream().filter(player -> player.getUUID().equals(gameProfile.getId())).toList();

        for (ServerPlayer player : matchingPlayers) {
            // Allows disconnect
            ((ILogoutRules) player).al_setAllowDisconnect(true);

            // Removes player so that the internal finite state machine in ServerLoginPacketListenerImpl can continue
            this.server.getPlayerList().remove(player);

            ILogoutRules.DISCONNECTED_PLAYERS.remove(player);
        }
    }
}
