package org.samo_lego.antilogout;

import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.minecraft.network.chat.Component;
import org.samo_lego.antilogout.command.AfkCommand;
import org.samo_lego.antilogout.config.LogoutConfig;
import org.samo_lego.antilogout.event.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AntiLogout implements DedicatedServerModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("antilogout");

    public static final LogoutConfig config;
    public static final Component AFK_MESSAGE = Component.translatable("You're now afk!");

    static {
        config = new LogoutConfig();
    }

    @Override
    public void onInitializeServer() {
        AttackEntityCallback.EVENT.register(EventHandler::onAttack);
        ServerLivingEntityEvents.AFTER_DEATH.register(EventHandler::onDeath);
        CommandRegistrationCallback.EVENT.register(AfkCommand::register);
    }
}