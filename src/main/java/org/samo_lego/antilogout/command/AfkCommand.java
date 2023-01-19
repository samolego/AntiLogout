package org.samo_lego.antilogout.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.server.level.ServerPlayer;
import org.samo_lego.antilogout.AntiLogout;
import org.samo_lego.antilogout.datatracker.ILogoutRules;

import java.util.Collection;
import java.util.Collections;

import static org.samo_lego.antilogout.AntiLogout.config;


public class AfkCommand {

    /**
     * Registers /afk command.
     * Usage: /afk              := puts executor afk for max time.
     * [players]     := puts all specified players afk for max time.
     * [time]    := puts all specified players afk for specified time.
     * [time]        := puts executor afk for specified time.
     *
     * @param dispatcher command dispatcher
     * @param context    cmd build context
     * @param selection  command selection
     */
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext context, Commands.CommandSelection selection) {
        dispatcher.register(Commands.literal("afk")
                .requires(src -> src.hasPermission(2))
                .then(Commands.argument("targets", EntityArgument.players())
                        .then(Commands.argument("time", IntegerArgumentType.integer(5, config.maxAfkTime == -1 ? Integer.MAX_VALUE : config.maxAfkTime / 1000))
                                .executes(ctx ->
                                        AfkCommand.afkPlayers(EntityArgument.getPlayers(ctx, "targets"),
                                                IntegerArgumentType.getInteger(ctx, "time") * 1000L))
                        )
                        .executes(ctx ->
                                AfkCommand.afkPlayers(EntityArgument.getPlayers(ctx, "targets"), config.maxAfkTime))
                )
                .then(Commands.argument("time", IntegerArgumentType.integer(5, config.maxAfkTime == -1 ? Integer.MAX_VALUE : config.maxAfkTime / 1000))
                        .executes(ctx ->
                                AfkCommand.afkPlayers(EntityArgument.getPlayers(ctx, "targets"),
                                        IntegerArgumentType.getInteger(ctx, "time") * 1000L))
                )
                .executes(ctx -> AfkCommand.afkPlayers(Collections.singleton(ctx.getSource().getPlayerOrException()), config.maxAfkTime)));
    }

    /**
     * Puts the specified players
     * in afk mode (disconnects them but
     * keeps entities online)
     *
     * @param players players to afk
     * @return 1 as success, 0 if possible players to afk are empty
     */
    private static int afkPlayers(Collection<ServerPlayer> players, long timeLimit) {
        for (var player : players) {
            ((ILogoutRules) player).al_setAllowDisconnectAt(System.currentTimeMillis() + timeLimit);
            player.connection.disconnect(AntiLogout.AFK_MESSAGE);
        }
        return players.isEmpty() ? 0 : 1;
    }
}
