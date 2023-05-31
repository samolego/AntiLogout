package org.samo_lego.antilogout;

import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.network.chat.Component;
import org.samo_lego.antilogout.command.AfkCommand;
import org.samo_lego.antilogout.command.AntiLogoutCommand;
import org.samo_lego.antilogout.config.LogoutConfig;
import org.samo_lego.antilogout.event.EventHandler;
import org.samo_lego.config2brigadier.IBrigadierConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class AntiLogout implements DedicatedServerModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("antilogout");

    public static final LogoutConfig config;
    public static final Component AFK_MESSAGE;

    static {
        config = IBrigadierConfigurator.loadConfigFile(LogoutConfig.CONFIG_FILE, LogoutConfig.class, LogoutConfig::new);

        AFK_MESSAGE = Component.translatable(config.afk.afkMessage);
    }

    @Override
    public void onInitializeServer() {
        AttackEntityCallback.EVENT.register(EventHandler::onAttack);
        ServerLivingEntityEvents.AFTER_DEATH.register(EventHandler::onDeath);
        ServerPlayConnectionEvents.JOIN.register(EventHandler::onPlayerJoin);

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            AfkCommand.register(dispatcher);
            AntiLogoutCommand.register(dispatcher);
        });

        LOGGER.info("AntiLogout initialized.");
    }
}