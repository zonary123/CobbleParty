package com.kingpixel.cobbleparty.config;

import com.google.gson.Gson;
import com.kingpixel.cobbleparty.CobbleParty;
import com.kingpixel.cobbleutils.CobbleUtils;
import com.kingpixel.cobbleutils.util.Utils;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.concurrent.CompletableFuture;

/**
 * @author Carlos Varas Alonso - 28/04/2024 23:58
 */
@Getter
@Setter
@ToString
public class Lang {
  private String prefix;


  public Lang() {
    prefix = "&7[&cBattlePass&7] &8&l»&r";
  }

  public void init() {
    CompletableFuture<Boolean> futureRead = Utils.readFileAsync(CobbleParty.PATH + "lang/",
      CobbleParty.config.getLang() + ".json",
      el -> {
        Gson gson = Utils.newGson();
        CobbleParty.language = gson.fromJson(el, Lang.class);
        String data = gson.toJson(CobbleParty.language);
        CompletableFuture<Boolean> futureWrite = Utils.writeFileAsync(CobbleParty.PATH + "lang/", CobbleParty.config.getLang() + ".json",
          data);
        if (!futureWrite.join()) {
          CobbleUtils.LOGGER.fatal(CobbleParty.MOD_ID, "Could not write lang.json file for CobbleDex.");
        }
      });

    if (!futureRead.join()) {
      CobbleUtils.LOGGER.info(CobbleParty.MOD_ID, "No lang.json file found for" + CobbleParty.MOD_NAME + ". " +
        "Attempting to generate " +
        "one.");
      Gson gson = Utils.newGson();
      String data = gson.toJson(this);
      CompletableFuture<Boolean> futureWrite = Utils.writeFileAsync(CobbleParty.PATH + "lang/", CobbleParty.config.getLang() + ".json",
        data);

      if (!futureWrite.join()) {
        CobbleUtils.LOGGER.fatal(CobbleParty.MOD_ID, "Could not write lang.json file for CobbleDex.");
      }
    }
  }

}
