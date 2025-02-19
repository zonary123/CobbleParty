package com.kingpixel.cobbleparty.config;

import com.google.gson.Gson;
import com.kingpixel.cobbleparty.CobbleParty;
import com.kingpixel.cobbleutils.CobbleUtils;
import com.kingpixel.cobbleutils.Model.DataBaseConfig;
import com.kingpixel.cobbleutils.Model.DataBaseType;
import com.kingpixel.cobbleutils.util.Utils;
import lombok.Data;

import java.util.List;
import java.util.concurrent.CompletableFuture;


/**
 * @author Carlos Varas Alonso - 29/04/2024 0:14
 */
@Data
public class Config {
  private final boolean temporalParty;
  private final boolean partyleavewhenexit;
  private final int maxPartySize;
  private final int characterLimit;
  private final List<String> commands;
  private final List<String> bannedWords;
  private final DataBaseConfig database;


  public Config() {
    temporalParty = true;
    partyleavewhenexit = true;
    maxPartySize = 5;
    characterLimit = 30;
    commands = List.of("party");
    bannedWords = List.of(
      "nigger"
    );
    database = new DataBaseConfig(DataBaseType.JSON, "party", "", "", "");
  }

  public void init() {
    CompletableFuture<Boolean> futureRead = Utils.readFileAsync(CobbleParty.PATH, "config.json",
      el -> {
        Gson gson = Utils.newGson();
        CobbleParty.config = gson.fromJson(el, Config.class);
        String data = gson.toJson(CobbleParty.config);
        CompletableFuture<Boolean> futureWrite = Utils.writeFileAsync(CobbleParty.PATH, "config.json", data);
        if (!futureWrite.join()) {
          CobbleUtils.LOGGER.fatal("Could not write config.json file for " + CobbleParty.MOD_NAME + ".");
        }
      });

    if (!futureRead.join()) {
      CobbleUtils.LOGGER.info("No config.json file found for" + CobbleParty.MOD_NAME + ". Attempting to generate one.");
      Gson gson = Utils.newGson();
      CobbleParty.config = this;
      String data = gson.toJson(CobbleParty.config);
      CompletableFuture<Boolean> futureWrite = Utils.writeFileAsync(CobbleParty.PATH, "config.json",
        data);

      if (!futureWrite.join()) {
        CobbleUtils.LOGGER.fatal("Could not write config.json file for " + CobbleParty.MOD_NAME + ".");
      }
    }

  }

}