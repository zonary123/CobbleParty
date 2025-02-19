package com.kingpixel.cobbleparty.database;

import com.kingpixel.cobbleparty.models.PartyData;
import com.kingpixel.cobbleparty.models.UserParty;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.List;

/**
 * @author Carlos Varas Alonso - 27/07/2024 7:36
 */
public interface DataBaseClient {
  void connect();

  void disconnect();

  PartyData getParty(ServerPlayerEntity player);

  PartyData updateParty(ServerPlayerEntity player, PartyData party);

  void createParty(ServerPlayerEntity player, String partyName);

  boolean leaveParty(ServerPlayerEntity player);

  boolean invitePlayer(ServerPlayerEntity player, ServerPlayerEntity target);

  List<PartyData> getParties();

  void declineInvite(ServerPlayerEntity player, PartyData invitation);

  void acceptInvite(ServerPlayerEntity player, PartyData invitation);

  void kickPlayer(ServerPlayerEntity player, UserParty member);
}
