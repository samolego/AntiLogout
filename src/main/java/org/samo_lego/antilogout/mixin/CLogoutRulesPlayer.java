package org.samo_lego.antilogout.mixin;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.damagesource.DamageSource;
import org.samo_lego.antilogout.AntiLogout;
import org.samo_lego.antilogout.datatracker.ILogoutRules;
import org.samo_lego.antilogout.event.EventHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.ObjectInputFilter.Config;

/**
 * Implements {@link ILogoutRules} for {@link ServerPlayer}.
 */
@Mixin(ServerPlayer.class)
public abstract class CLogoutRulesPlayer implements ILogoutRules {
    @Shadow
    private boolean disconnected;

    @Unique
    private long allowDisconnectTime = 0;

    @Shadow
    public ServerGamePacketListenerImpl connection;
    @Unique
    private boolean executedDisconnect = false;
    @Unique
    private Runnable delayedTask;
    @Unique
    private long taskTime;

    @Override
    public boolean al_allowDisconnect() {   
        return this.allowDisconnectTime != -1 && this.allowDisconnectTime <= System.currentTimeMillis() && !AntiLogout.config.disableAllLogouts;
    }

    @Override
    public void al_setAllowDisconnectAt(long systemTime) {
        this.allowDisconnectTime = systemTime;
    }

    @Override
    public void al_setAllowDisconnect(boolean allow) {
        this.allowDisconnectTime = allow ? 0 : -1;
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

    @Inject(method = "doTick",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/server/level/ServerPlayer;getInventory()Lnet/minecraft/world/entity/player/Inventory;"),
            cancellable = true)
    private void onTick(CallbackInfo ci) {
        if (this.al_isFake()) {
            if (this.al_allowDisconnect() && !this.executedDisconnect) {
                this.connection.disconnect(Component.empty());
                this.executedDisconnect = true;  // Prevent disconnecting twice
            }
            ci.cancel();
        } else if (this.delayedTask != null && this.taskTime <= System.currentTimeMillis()) {
            this.delayedTask.run();
            this.delayedTask = null;
        }
    }


    @Inject(method = "disconnect", at = @At("TAIL"))
    private void al_disconnect(CallbackInfo ci) {
        DISCONNECTED_PLAYERS.remove((ServerPlayer) (Object) this);
    }

    @Inject(method = "hurt", at = @At("TAIL"))
    private void onHurt(DamageSource damageSource, float f, CallbackInfoReturnable<Boolean> cir) {
        EventHandler.onHurt((ServerPlayer) (Object) this, damageSource);
    }

    @Override
    public void al$delay(long tickDuration, Runnable task) {
        this.delayedTask = task;
        this.taskTime = tickDuration;
    }
}
