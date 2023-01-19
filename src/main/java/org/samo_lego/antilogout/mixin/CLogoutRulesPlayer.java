package org.samo_lego.antilogout.mixin;

import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.damagesource.DamageSource;
import org.samo_lego.antilogout.datatracker.ILogoutRules;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerPlayer.class)
public abstract class CLogoutRulesPlayer implements ILogoutRules {
    @Shadow
    private boolean disconnected;

    @Unique
    private long allowDisconnect = 0;

    @Shadow
    public ServerGamePacketListenerImpl connection;

    @Shadow
    @Final
    public MinecraftServer server;

    @Shadow
    public abstract void disconnect();

    @Override
    public boolean al_allowDisconnect() {
        return this.allowDisconnect != -1 && this.allowDisconnect <= System.currentTimeMillis();
    }

    @Override
    public void al_setAllowDisconnectAt(long systemTime) {
        this.allowDisconnect = systemTime;
    }

    @Override
    public void al_setAllowDisconnect(boolean allow) {
        this.allowDisconnect = allow ? 0 : -1;
    }

    @Override
    public boolean al_isFake() {
        return this.disconnected;
    }

    @Override
    public void al_onRealDisconnect() {
        this.disconnected = true;

        if (!this.al_allowDisconnect()) {
            DISCONNECTED_PLAYERS.add((ServerPlayer) (Object) this);
        }
    }

    @Inject(method = "hasDisconnected", at = @At("HEAD"), cancellable = true)
    public void hasDisconnected(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(this.al_allowDisconnect() && this.disconnected);
    }

    @Inject(method = "doTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerPlayer;getInventory()Lnet/minecraft/world/entity/player/Inventory;"), cancellable = true)
    private void onTick(CallbackInfo ci) {
        if (this.al_isFake()) {
            if (this.al_allowDisconnect()) {
                this.connection.disconnect(Component.empty());
            }
            ci.cancel();
        }
    }


    @Inject(method = "die", at = @At("TAIL"))
    private void onDeath(DamageSource damageSource, CallbackInfo ci) {
        if (this.al_isFake()) {
            // Remove player from online players
            this.connection.onDisconnect(Component.empty());
        }
    }

    @Inject(method = "disconnect", at = @At("TAIL"))
    private void al_disconnect(CallbackInfo ci) {
        DISCONNECTED_PLAYERS.remove((ServerPlayer) (Object) this);
    }
}
