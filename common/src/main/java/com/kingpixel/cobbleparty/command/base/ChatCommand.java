package com.kingpixel.cobbleparty.command.base;

import com.kingpixel.cobbleparty.CobbleParty;
import com.kingpixel.cobbleparty.database.DataBaseClientFactory;
import com.kingpixel.cobbleparty.models.PartyData;
import com.kingpixel.cobbleutils.util.PlayerUtils;
import com.kingpixel.cobbleutils.util.TypeMessage;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;

/**
 * @author Carlos Varas Alonso - 19/02/2025 23:06
 */
public class ChatCommand {
  public static void register(CommandDispatcher<ServerCommandSource> dispatcher, LiteralArgumentBuilder<ServerCommandSource> base) {
    dispatcher.register(
      base.then(
        CommandManager.literal("chat")
          .then(
            CommandManager.argument("message", StringArgumentType.greedyString())
              .executes(context -> {
                ServerPlayerEntity player = context.getSource().getPlayer();
                if (player == null) return 1;
                PartyData partyData = DataBaseClientFactory.INSTANCE.getParty(player);
                if (partyData == null) {
                  PlayerUtils.sendMessage(
                    player,
                    CobbleParty.language.getMessageNoParty(),
                    CobbleParty.language.getPrefix(),
                    TypeMessage.CHAT
                  );
                  return 1;
                }
                String message = StringArgumentType.getString(context, "message");
                if (message == null || message.isEmpty()) return 1;
                partyData.partyMessage(player, message);
                return 1;
              })
          )
      )
    );
  }
}
