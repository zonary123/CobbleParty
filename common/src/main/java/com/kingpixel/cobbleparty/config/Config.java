package com.kingpixel.cobbleparty.config;

import com.google.gson.Gson;
import com.kingpixel.cobbleparty.CobbleParty;
import com.kingpixel.cobbleutils.CobbleUtils;
import com.kingpixel.cobbleutils.Model.DataBaseConfig;
import com.kingpixel.cobbleutils.Model.DataBaseType;
import com.kingpixel.cobbleutils.util.Utils;
import lombok.Getter;
import lombok.ToString;

import java.util.List;
import java.util.concurrent.CompletableFuture;


/**
 * @author Carlos Varas Alonso - 29/04/2024 0:14
 */
@Getter
@ToString
public class Config {
  private boolean debug;
  private String lang;
  private List<String> commands;
  private DataBaseConfig dataBaseConfig;

  public Config() {
    debug = false;
    lang = "en";
    commands = List.of("party");
    dataBaseConfig = new DataBaseConfig();
    dataBaseConfig.setType(DataBaseType.JSON);
    dataBaseConfig.setDatabase("CobbleParty");
  }


  public void init() {
    CompletableFuture<Boolean> futureRead = Utils.readFileAsync(CobbleParty.PATH, "config.json", el -> {
      Gson gson = Utils.newGson();
      CobbleParty.config = gson.fromJson(el, Config.class);
      String data = gson.toJson(CobbleParty.config);
      CompletableFuture<Boolean> futureWrite = Utils.writeFileAsync(CobbleParty.PATH, "config.json", data);
      if (!futureWrite.join()) {
        CobbleUtils.LOGGER.fatal(CobbleParty.MOD_ID, "Could not write lang.json file for CobbleDex.");
      }
    });

    if (!futureRead.join()) {
      CobbleUtils.LOGGER.info(CobbleParty.MOD_ID, "No config.json file found for " + CobbleParty.MOD_NAME +
        ". " +
        "Attempting to " +
        "generate one.");
      Gson gson = Utils.newGson();
      String data = gson.toJson(this);
      CompletableFuture<Boolean> futureWrite = Utils.writeFileAsync(CobbleParty.PATH, "config.json", data);

      if (!futureWrite.join()) {
        CobbleUtils.LOGGER.fatal(CobbleParty.MOD_ID, "Could not write config.json file for CobbleDex.");
      }
    }
  }


}

