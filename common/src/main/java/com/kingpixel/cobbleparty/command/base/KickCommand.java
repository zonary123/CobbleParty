package com.kingpixel.cobbleparty.command.base;

import com.kingpixel.cobbleparty.api.PartyApi;
import com.kingpixel.cobbleparty.database.DataBaseClientFactory;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;

/**
 * @author Carlos Varas Alonso - 19/02/2025 7:34
 */
public class KickCommand {
  public static void register(CommandDispatcher<ServerCommandSource> dispatcher, LiteralArgumentBuilder<ServerCommandSource> base) {
    dispatcher.register(
      base.then(
        CommandManager.literal("kick")
          .then(
            CommandManager.argument("player", EntityArgumentType.player())
              .executes(context -> {
                if (context.getSource().getPlayer() == null) return 1;
                ServerPlayerEntity player = context.getSource().getPlayer();
                ServerPlayerEntity target = EntityArgumentType.getPlayer(context, "player");
                PartyApi.kickPlayer(player, target);
                return 1;
              })
          )
      )
    );
  }
}
