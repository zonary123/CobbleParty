package com.kingpixel.cobbleparty.command.admin;

import com.kingpixel.cobbleparty.CobbleParty;
import com.kingpixel.cobbleparty.api.PartyApi;
import com.kingpixel.cobbleparty.models.PartyData;
import com.kingpixel.cobbleutils.api.PermissionApi;
import com.kingpixel.cobbleutils.util.PlayerUtils;
import com.kingpixel.cobbleutils.util.TypeMessage;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Carlos Varas Alonso - 19/02/2025 5:51
 */
public class ViewPartysCommand {
  public static void register(CommandDispatcher<ServerCommandSource> dispatcher, LiteralArgumentBuilder<ServerCommandSource> base) {
    dispatcher.register(
      base.then(
        CommandManager.literal("view")
          .requires(source -> PermissionApi.hasPermission(source, "cobbleparty.admin.view", 2))
          .executes(context -> {
            ServerPlayerEntity player = context.getSource().getPlayer();
            if (player == null) return 1;
            List<PartyData> partys = PartyApi.getParties();
            String s = partys.stream().map(PartyData::getName).collect(Collectors.joining(", "));
            PlayerUtils.sendMessage(
              player,
              "Partys: " + s,
              CobbleParty.language.getPrefix(),
              TypeMessage.CHAT
            );
            return 1;
          })
      )
    );
  }
}
