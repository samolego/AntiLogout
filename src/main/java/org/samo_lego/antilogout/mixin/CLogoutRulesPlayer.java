package org.samo_lego.antilogout.mixin;

import net.minecraft.server.level.ServerPlayer;
import org.samo_lego.antilogout.datatracker.ILogoutRules;
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


    @Inject(method = "hasDisconnected", at = @At("HEAD"), cancellable = true)
    public void hasDisconnected(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(this.al_allowDisconnect() && this.disconnected);
    }

    @Inject(method = "doTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerPlayer;getInventory()Lnet/minecraft/world/entity/player/Inventory;"), cancellable = true)
    private void onTick(CallbackInfo ci) {
        if (this.disconnected) {
            if (this.al_allowDisconnect()) {
                this.disconnect();
            }
            ci.cancel();
        }
    }
}
