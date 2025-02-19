package com.kingpixel.cobbleparty.database;

import com.kingpixel.cobbleparty.CobbleParty;
import com.kingpixel.cobbleparty.api.PartyApi;
import com.kingpixel.cobbleparty.events.CreatePartyEvent;
import com.kingpixel.cobbleparty.models.PartyData;
import com.kingpixel.cobbleutils.Model.DataBaseConfig;
import com.kingpixel.cobbleutils.util.PlayerUtils;
import com.kingpixel.cobbleutils.util.TypeMessage;
import net.minecraft.server.network.ServerPlayerEntity;

/**
 * @author Carlos Varas Alonso - 27/07/2024 7:36
 */
public class DataBaseClientFactory {
  public static DataBaseClient INSTANCE;

  public static DataBaseClient create(DataBaseConfig config) {
    if (INSTANCE != null) {
      INSTANCE.disconnect();
    }
    switch (config.getType()) {
      case JSON -> INSTANCE = new DataBaseJson();
      default -> INSTANCE = new DataBaseJson();
    }
    INSTANCE.connect();
    return INSTANCE;
  }

  public static boolean leaveParty(ServerPlayerEntity player) {
    PartyData party = INSTANCE.getParty(player);
    if (party == null) {

      return false;
    }
    return true;
  }

  public static PartyData createParty(ServerPlayerEntity player, String partyName) {
    CobbleParty.config.getBannedWords().forEach(bannedWord -> {
      if (partyName.contains(bannedWord)) {
        PlayerUtils.sendMessage(
          player,
          CobbleParty.language.getMessageBannedWord(),
          CobbleParty.language.getPrefix(),
          TypeMessage.CHAT
        );
        return;
      }
    });
    if (PartyApi.hasParty(player)) {
      PlayerUtils.sendMessage(
        player,
        CobbleParty.language.getMessageHasParty(),
        CobbleParty.language.getPrefix(),
        TypeMessage.CHAT
      );
      return null;
    }
    PlayerUtils.sendMessage(
      player,
      CobbleParty.language.getMessagePartyCreated()
        .replace("%party%", partyName),
      CobbleParty.language.getPrefix(),
      TypeMessage.CHAT
    );
    PartyData created = new PartyData(partyName, player);
    CreatePartyEvent.CREATE_PARTY_EVENT.emit(created);
    return created;
  }
}
