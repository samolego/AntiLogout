package org.samo_lego.antilogout.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.minecraft.commands.CommandSourceStack;
import org.samo_lego.antilogout.config.LogoutConfig;
import org.samo_lego.config2brigadier.IBrigadierConfigurator;

import static net.minecraft.commands.Commands.literal;
import static org.samo_lego.antilogout.AntiLogout.config;

public class AntiLogoutCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        final var rootNode = dispatcher.register(literal("antilogout")
                .requires(src -> Permissions.check(src, "antilogout.command.antilogout", src.hasPermission(4)))
                .then(literal("reload")
                        .requires(src -> Permissions.check(src, "antilogout.command.antilogout.reload", src.hasPermission(4)))
                        .executes(AntiLogoutCommand::reloadConfig)
                )
        );

        final var editNode = literal("edit").requires(src -> Permissions.check(src, "antilogout.command.antilogout.edit", src.hasPermission(4))).build();
        config.generateCommand(editNode);

        rootNode.addChild(editNode);

        dispatcher.register(literal("al").redirect(rootNode));
    }

    private static int reloadConfig(CommandContext<CommandSourceStack> context) {
        final var cfg = IBrigadierConfigurator.loadConfigFile(LogoutConfig.CONFIG_FILE, LogoutConfig.class, LogoutConfig::new);
        config.reload(cfg);
        return 1;
    }
}
