package com.kingpixel.cobbleparty.command.base;

import com.kingpixel.cobbleparty.database.DataBaseClientFactory;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;

/**
 * @author Carlos Varas Alonso - 19/02/2025 4:04
 */
public class CreateCommand {

  public static void register(CommandDispatcher<ServerCommandSource> dispatcher, LiteralArgumentBuilder<ServerCommandSource> base) {
    dispatcher.register(
      base.then(
        CommandManager.literal("create")
          .then(
            CommandManager.argument("partyName", StringArgumentType.string())
              .executes(context -> {
                ServerPlayerEntity player = context.getSource().getPlayer();
                if (player == null) return 1;
                String partyName = StringArgumentType.getString(context, "partyName");
                DataBaseClientFactory.INSTANCE.createParty(player, partyName);
                return 1;
              })
          )
      )
    );
  }
}
