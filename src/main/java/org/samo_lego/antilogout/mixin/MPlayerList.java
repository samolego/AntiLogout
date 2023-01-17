package org.samo_lego.antilogout.mixin;

import com.mojang.authlib.GameProfile;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import org.samo_lego.antilogout.datatracker.ILogoutRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;
import java.util.UUID;

@Mixin(PlayerList.class)
public class MPlayerList {

    @Inject(method = "getPlayerForLogin",
            at = @At(value = "INVOKE",
                    target = "Ljava/util/List;iterator()Ljava/util/Iterator;"),
            locals = LocalCapture.CAPTURE_FAILHARD)
    private void onPlayerLogin(GameProfile gameProfile, CallbackInfoReturnable<ServerPlayer> cir, UUID uUID, List<ServerPlayer> matchingPlayers, ServerPlayer serverPlayer2) {
        for (ServerPlayer player : matchingPlayers) {
            ((ILogoutRules) player).al_setAllowDisconnect(true);
        }
    }
}
