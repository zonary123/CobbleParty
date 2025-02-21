package com.kingpixel.cobbleparty.command;

import com.kingpixel.cobbleparty.CobbleParty;
import com.kingpixel.cobbleparty.command.admin.ViewPartysCommand;
import com.kingpixel.cobbleparty.command.base.*;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;

public class CommandTree {


  public static void register(
    CommandDispatcher<ServerCommandSource> dispatcher
  ) {
    for (String literal : CobbleParty.config.getCommands()) {
      LiteralArgumentBuilder<ServerCommandSource> base = CommandManager.literal(literal);
      dispatcher.register(
        base.executes(context -> {
          if (context.getSource().isExecutedByPlayer()) {
            ServerPlayerEntity player = context.getSource().getPlayer();
            CobbleParty.language.getMenu().open(player);
            return 1;
          }
          return 0;
        })
      );
      CreateCommand.register(dispatcher, base);
      LeaveCommand.register(dispatcher, base);
      ViewPartysCommand.register(dispatcher, base);
      InviteCommand.register(dispatcher, base);
      KickCommand.register(dispatcher, base);
      ChatCommand.register(dispatcher, base);

    }
  }
}
