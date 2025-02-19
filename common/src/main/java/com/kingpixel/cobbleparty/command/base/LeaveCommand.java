package com.kingpixel.cobbleparty.command.base;

import com.kingpixel.cobbleparty.CobbleParty;
import com.kingpixel.cobbleparty.database.DataBaseClientFactory;
import com.kingpixel.cobbleutils.util.PlayerUtils;
import com.kingpixel.cobbleutils.util.TypeMessage;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;

/**
 * @author Carlos Varas Alonso - 19/02/2025 5:49
 */
public class LeaveCommand {
  public static void register(CommandDispatcher<ServerCommandSource> dispatcher, LiteralArgumentBuilder<ServerCommandSource> base) {
    dispatcher.register(
      base.then(
        CommandManager.literal("leave")
          .executes(context -> {
              ServerPlayerEntity player = context.getSource().getPlayer();
              if (player == null) return 1;
              if ( DataBaseClientFactory.INSTANCE.leaveParty(player)){
                PlayerUtils.sendMessage(
                  player,
                  CobbleParty.language.getMessageLeaveParty(),
                  CobbleParty.language.getPrefix(),
                  TypeMessage.CHAT
                );
              } else {
                PlayerUtils.sendMessage(
                  player,
                  CobbleParty.language.getMessageNoParty(),
                  CobbleParty.language.getPrefix(),
                  TypeMessage.CHAT
                );
              }
              return 1;
            }
          )
      )
    );
  }
}
