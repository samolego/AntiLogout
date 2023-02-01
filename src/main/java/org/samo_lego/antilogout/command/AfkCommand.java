package org.samo_lego.antilogout.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.server.level.ServerPlayer;
import org.samo_lego.antilogout.AntiLogout;
import org.samo_lego.antilogout.datatracker.ILogoutRules;

import java.util.Collections;

import static net.minecraft.commands.Commands.literal;
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
     */
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(literal("afk")
                .requires(src -> Permissions.check(src, "antilogout.command.afk", config.afk.permissionLevel))
                .then(literal("players")
                        .requires(src -> Permissions.check(src, "antilogout.command.afk.players", config.afk.permissionLevel))
                        .then(Commands.argument("targets", EntityArgument.players())
                                .then(literal("time")
                                        .requires(src -> Permissions.check(src, "antilogout.command.afk.players.time", config.afk.permissionLevel))
                                        .then(Commands.argument("time", DoubleArgumentType.doubleArg(5, config.afk.maxAfkTime == -1 ? Double.MAX_VALUE : config.afk.maxAfkTime))
                                                .executes(ctx ->
                                                        AfkCommand.afkPlayers(EntityArgument.getPlayers(ctx, "targets"),
                                                                DoubleArgumentType.getDouble(ctx, "time")))
                                        )
                                )
                                .executes(ctx -> AfkCommand.afkPlayers(EntityArgument.getPlayers(ctx, "targets"), config.afk.maxAfkTime))
                        )
                )

                .then(literal("time")
                        .requires(src -> Permissions.check(src, "antilogout.command.afk.time", config.afk.permissionLevel))
                        .then(Commands.argument("time", DoubleArgumentType.doubleArg(5, config.afk.maxAfkTime == -1 ? Double.MAX_VALUE : config.afk.maxAfkTime))
                                .executes(ctx ->
                                        AfkCommand.afkPlayers(Collections.singletonList(ctx.getSource().getPlayerOrException()),
                                                DoubleArgumentType.getDouble(ctx, "time")))
                        )
                )
                .executes(ctx -> AfkCommand.afkPlayers(Collections.singleton(ctx.getSource().getPlayerOrException()), config.afk.maxAfkTime)));
    }

    /**
     * Puts the specified players
     * in afk mode (disconnects them but
     * keeps entities online)
     *
     * @param players players to afk
     * @return 1 as success, 0 if possible players to afk are empty
     */
    private static int afkPlayers(Iterable<ServerPlayer> players, double timeLimit) {
        for (var player : players) {
            long disconnectAt = timeLimit == -1 ? -1 : System.currentTimeMillis() + Math.round(timeLimit * 1000);
            ((ILogoutRules) player).al_setAllowDisconnectAt(disconnectAt);
            player.connection.disconnect(AntiLogout.AFK_MESSAGE);
        }
        return players.iterator().hasNext() ? 0 : 1;
    }
}
