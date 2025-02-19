package com.kingpixel.cobbleparty.database;

import com.kingpixel.cobbleparty.CobbleParty;
import com.kingpixel.cobbleparty.models.PartyData;
import com.kingpixel.cobbleparty.models.Result;
import com.kingpixel.cobbleparty.models.UserParty;
import com.kingpixel.cobbleutils.util.PlayerUtils;
import com.kingpixel.cobbleutils.util.TypeMessage;
import com.mojang.datafixers.kinds.IdF;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Carlos Varas Alonso - 27/07/2024 8:26
 */
public class DataBaseJson implements DataBaseClient {
  private final List<PartyData> partys = new ArrayList<>();

  @Override public void connect() {

  }

  @Override public void disconnect() {

  }

  @Override public PartyData getParty(ServerPlayerEntity player) {
    return partys.stream().filter(party -> party.getMembers().stream().anyMatch(userParty -> userParty.getPlayerUUID().equals(player.getUuid()))).findFirst().orElse(null);
  }

  @Override public PartyData updateParty(ServerPlayerEntity player, PartyData party) {
    return null;
  }

  @Override public void createParty(ServerPlayerEntity player, String partyName) {
    PartyData partyData = DataBaseClientFactory.createParty(player, partyName);
    if (partyData == null) return;
    partys.add(partyData);
    updateParty(player, partyData);
  }

  @Override public boolean leaveParty(ServerPlayerEntity player) {
    PartyData party = getParty(player);
    if (party == null) {
      return false;
    }
    Result result = party.leaveParty(player);
    if (result.equals(Result.WITHOUT_MEMBERS)) {
      partys.remove(party);
    }
    return true;
  }

  @Override public boolean invitePlayer(ServerPlayerEntity player, ServerPlayerEntity target) {
    PartyData party = getParty(player);
    if (party == null){
      CobbleParty.language.sendYouNeedToBeInParty(player);
      return false;
    }
    Result result = party.invitePlayer(player,target);
    return true;
  }

  @Override public List<PartyData> getParties() {
    return partys;
  }

  @Override public void declineInvite(ServerPlayerEntity player, PartyData invitation) {
    invitation.declineInvite(player);
  }

  @Override public void acceptInvite(ServerPlayerEntity player, PartyData invitation) {
    invitation.acceptInvite(player);
  }

  @Override public void kickPlayer(ServerPlayerEntity player, UserParty member) {
    PartyData party = getParty(player);
    if (party == null) return;
    party.kickPlayer(player, member);
  }


}
