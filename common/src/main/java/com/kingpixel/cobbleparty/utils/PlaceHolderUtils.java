package com.kingpixel.cobbleparty.utils;

import com.kingpixel.cobbleparty.api.PartyApi;
import eu.pb4.placeholders.api.PlaceholderResult;
import eu.pb4.placeholders.api.Placeholders;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

/**
 * @author Carlos Varas Alonso - 19/02/2025 4:17
 */
public class PlaceHolderUtils {
  public static void register() {
    try {
      Placeholders.register(Identifier.of("cobbleparty:party_name"), (source, args) -> {
        if (!source.hasPlayer()) return PlaceholderResult.invalid();
        ServerPlayerEntity player = source.source().getPlayer();
        if (!PartyApi.hasParty(player)) return PlaceholderResult.invalid();
        return PlaceholderResult.value(PartyApi.getPartyName(player));
      });
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
