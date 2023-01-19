package org.samo_lego.antilogout.command;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.server.level.ServerPlayer;
import org.samo_lego.antilogout.AntiLogout;
import org.samo_lego.antilogout.datatracker.ILogoutRules;

import java.util.Collection;
import java.util.Collections;


public class AfkCommand {

    /**
     * Registers /afk command.
     * Usage: /afk [<players>]
     *
     * @param dispatcher command dispatcher
     * @param context    cmd build context
     * @param selection  command selection
     */
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext context, Commands.CommandSelection selection) {
        dispatcher.register(Commands.literal("afk")
                .requires(src -> src.hasPermission(2))
                .then(Commands.argument("targets", EntityArgument.players())
                        .executes(ctx -> AfkCommand.afkPlayers(EntityArgument.getPlayers(ctx, "targets")))
                )
                .executes(ctx -> AfkCommand.afkPlayers(Collections.singleton(ctx.getSource().getPlayerOrException()))));
    }

    /**
     * Puts the specified players
     * in afk mode (disconnects them but
     * keeps entities online)
     *
     * @param players players to afk
     * @return 1 as success, 0 if possible players to afk are empty
     */
    private static int afkPlayers(Collection<ServerPlayer> players) {
        for (var player : players) {
            ((ILogoutRules) player).al_setAllowDisconnect(false);
            player.connection.disconnect(AntiLogout.AFK_MESSAGE);
        }
        return players.isEmpty() ? 0 : 1;
    }
}
