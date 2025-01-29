package org.samo_lego.antilogout.command;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import org.samo_lego.antilogout.AntiLogout;
import org.samo_lego.antilogout.config.LogoutConfig;

import static net.minecraft.commands.Commands.literal;
import static org.samo_lego.antilogout.AntiLogout.config;

public class AntiLogoutCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        config.generateReloadableConfigCommand(AntiLogout.MOD_ID, dispatcher, LogoutConfig::readConfigFile);

        var rootNode = dispatcher.getRoot().getChild("antilogout");
        dispatcher.register(literal("al").redirect(rootNode));
    }
}
