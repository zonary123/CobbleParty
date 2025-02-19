package com.kingpixel.cobbleparty.command.base;

import com.kingpixel.cobbleparty.CobbleParty;
import com.kingpixel.cobbleutils.CobbleUtils;
import com.kingpixel.cobbleutils.util.PlayerUtils;
import com.kingpixel.cobbleutils.util.TypeMessage;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;

/**
 * @author Carlos Varas Alonso - 19/02/2025 4:04
 */
public class ReloadCommand {

  public static void register(CommandDispatcher<ServerCommandSource> dispatcher, LiteralArgumentBuilder<ServerCommandSource> base) {
    dispatcher.register(
      base.then(
        CommandManager.literal("reload")
          .executes(context -> {
            CobbleParty.load();
            if (context.getSource().isExecutedByPlayer()) {
              ServerPlayerEntity player = context.getSource().getPlayer();
              PlayerUtils.sendMessage(
                player,
                CobbleParty.language.getReload(),
                CobbleParty.language.getPrefix(),
                TypeMessage.CHAT
              );
            } else {
              CobbleUtils.LOGGER.info(CobbleParty.MOD_ID, CobbleParty.language.getReload()
                .replace("%prefix%", CobbleParty.language.getPrefix())
              );
            }
            return 1;
          })
      )
    );
  }
}
