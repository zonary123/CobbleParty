package com.kingpixel.cobbleparty.config;

import com.google.gson.Gson;
import com.kingpixel.cobbleparty.CobbleParty;
import com.kingpixel.cobbleparty.gui.Menu;
import com.kingpixel.cobbleparty.gui.MenuInvitations;
import com.kingpixel.cobbleutils.CobbleUtils;
import com.kingpixel.cobbleutils.util.PlayerUtils;
import com.kingpixel.cobbleutils.util.TypeMessage;
import com.kingpixel.cobbleutils.util.Utils;
import lombok.Getter;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.concurrent.CompletableFuture;

@Getter
public class Lang {
  private final String prefix;
  private final String reload;
  private final String messageHasParty;
  private final String messageNoParty;
  private final String messagePartyCreated;
  private final String messageLeaveParty;
  private final String messageNoInvitation;
  private final String messageYouNeedToBeInParty;
  private final String messagePlayerYetInParty;
  private final String messagePlayerYetInvited;
  private final String messagePlayerCantInviteHimSelf;
  private final String messagePlayerWasInvited;
  private final String messagePlayerInvited;
  private final String messagePlayerKicked;
  private final String messagePlayerKickedBy;
  private final String messageBannedWord;
  private final Menu menu;
  private final MenuInvitations menuInvitations;
  /**
   * Constructor to generate a file if one doesn't exist.
   */
  public Lang() {
    prefix = "&8[&6CobbleParty&8] &7";
    reload = "%prefix% &aReloaded!";
    messageHasParty = "%prefix% &cYou already have a party!";
    messageNoParty = "%prefix% &cYou don't have a party!";
    messagePartyCreated = "%prefix% &aParty created!";
    messageLeaveParty = "%prefix% &cYou left the party!";
    messageNoInvitation = "%prefix% &cYou don't have any invitations!";
    messagePlayerYetInParty = "%prefix% &cThe player is already in a party!";
    messageYouNeedToBeInParty = "%prefix% &cYou need to be in a party!";
    messagePlayerYetInvited = "%prefix% &cThe player is already invited!";
    messagePlayerCantInviteHimSelf = "%prefix% &cYou can't invite yourself!";
    messagePlayerWasInvited = "%prefix% &aYou were invited to the party %party%!";
    messagePlayerInvited = "%prefix% &aPlayer %player% invited to the party!";
    messagePlayerKicked = "%prefix% &cPlayer %player% was kicked from the party!";
    messagePlayerKickedBy = "%prefix% &cYou were kicked from the party!";
    messageBannedWord = "%prefix% &cYou can't use that word!";
    menu = new Menu();
    menuInvitations = new MenuInvitations();
  }

  /**
   * Method to initialize the config.
   */
  public void init() {
    CompletableFuture<Boolean> futureRead = Utils.readFileAsync(CobbleParty.PATH_LANG,
      CobbleUtils.config.getLang() + ".json",
      el -> {
        Gson gson = Utils.newGson();
        CobbleParty.language = gson.fromJson(el, Lang.class);
        String data = gson.toJson(CobbleParty.language);
        CompletableFuture<Boolean> futureWrite = Utils.writeFileAsync(CobbleParty.PATH_LANG, CobbleUtils.config.getLang() +
            ".json",
          data);
        if (!futureWrite.join()) {
          CobbleUtils.LOGGER.fatal("Could not write lang.json file for " + CobbleParty.MOD_NAME + ".");
        }
      });

    if (!futureRead.join()) {
      CobbleUtils.LOGGER.info("No lang.json file found for" + CobbleParty.MOD_NAME + ". Attempting to generate one.");
      Gson gson = Utils.newGson();
      CobbleParty.language = this;
      String data = gson.toJson(CobbleParty.language);
      CompletableFuture<Boolean> futureWrite = Utils.writeFileAsync(CobbleParty.PATH_LANG, CobbleUtils.config.getLang() +
          ".json",
        data);
      if (!futureWrite.join()) {
        CobbleUtils.LOGGER.fatal("Could not write lang.json file for " + CobbleParty.MOD_NAME + ".");
      }
    }
  }

  public void sendYouNeedToBeInParty(ServerPlayerEntity player) {
    PlayerUtils.sendMessage(
      player,
      messageYouNeedToBeInParty,
      prefix,
      TypeMessage.CHAT
    );

  }
}
