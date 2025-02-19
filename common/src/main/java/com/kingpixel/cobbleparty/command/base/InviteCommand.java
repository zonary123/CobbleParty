package com.kingpixel.cobbleparty.command.base;

import com.kingpixel.cobbleparty.database.DataBaseClientFactory;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;

/**
 * @author Carlos Varas Alonso - 19/02/2025 6:35
 */
public class InviteCommand {
  public static void register(CommandDispatcher<ServerCommandSource> dispatcher, LiteralArgumentBuilder<ServerCommandSource> base) {
    dispatcher.register(
      base.then(
        CommandManager.literal("invite")
          .executes(context -> {
            return 1;
          }).then(
            CommandManager.argument("target", EntityArgumentType.player())
              .executes(context -> {
                if (context.getSource().getPlayer() == null) return 1;
                ServerPlayerEntity player = context.getSource().getPlayer();
                ServerPlayerEntity target = EntityArgumentType.getPlayer(context, "target");
                DataBaseClientFactory.INSTANCE.invitePlayer(player, target);
                return 1;
              }
          )
        )
      )
    );
  }
}
