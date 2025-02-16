package com.kingpixel.cobbleparty.command;

import com.kingpixel.cobbleparty.CobbleParty;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;

public class CommandTree {


  public static void register(
    CommandDispatcher<ServerCommandSource> dispatcher
  ) {
    for (String literal : CobbleParty.config.getCommands()) {
      LiteralArgumentBuilder<ServerCommandSource> base = CommandManager.literal(literal);


    }
  }
}
