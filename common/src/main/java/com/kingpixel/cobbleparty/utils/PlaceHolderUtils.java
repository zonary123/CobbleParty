package com.kingpixel.cobbleparty.utils;

import com.kingpixel.cobbleparty.CobbleParty;
import eu.pb4.placeholders.api.PlaceholderResult;
import eu.pb4.placeholders.api.Placeholders;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

/**
 * Author: Carlos Varas Alonso - 04/01/2025 2:33
 */
public class PlaceHolderUtils {
  public static void register() {
    try {
      Placeholders.register(
        Identifier.of(CobbleParty.MOD_ID, "party"),
        (context, args) -> {
          if (!context.hasPlayer()) return PlaceholderResult.invalid();
          ServerPlayerEntity player = context.player();
          return PlaceholderResult.value("");
        }
      );
    } catch (NoSuchMethodError | Exception | NoClassDefFoundError e) {
    }
  }
}