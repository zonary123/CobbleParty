package com.kingpixel.cobbleparty.api;

import com.kingpixel.cobbleparty.database.DataBaseClientFactory;
import com.kingpixel.cobbleparty.models.PartyData;
import com.kingpixel.cobbleparty.models.Role;
import com.kingpixel.cobbleparty.models.UserParty;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.List;

/**
 * @author Carlos Varas Alonso - 16/02/2025 2:32
 */
public class PartyApi {
  public static PartyData getParty(ServerPlayerEntity player) {
    return DataBaseClientFactory.INSTANCE.getParty(player);
  }

  public static boolean hasParty(ServerPlayerEntity player) {
    return getParty(player) != null;
  }

  public static String getPartyName(ServerPlayerEntity player) {
    return getParty(player).getName();
  }

  public static List<PartyData> getParties() {
    return DataBaseClientFactory.INSTANCE.getParties();
  }

  public static List<PartyData> getInvitations(ServerPlayerEntity player) {
    return getParties().stream().filter(partyData -> partyData.getInvites().contains(player.getUuid())).toList();
  }

  public static boolean isOwner(ServerPlayerEntity player) {
    return getParty(player).getMembers().stream().anyMatch(userParty -> userParty.getPlayerUUID().equals(player.getUuid()) && userParty.getRole().equals(Role.OWNER));
  }

  public static boolean hasElevatedRole(ServerPlayerEntity player) {
    UserParty userParty = getParty(player).getMembers().stream().filter(userParty1 -> userParty1.getPlayerUUID().equals(player.getUuid())).findFirst().orElse(null);
    if (userParty != null) {
      return userParty.getRole().equals(Role.OWNER) || userParty.getRole().equals(Role.ADMIN);
    }
    return false;
  }

  public static void acceptInvite(ServerPlayerEntity player, PartyData invitation) {
    DataBaseClientFactory.INSTANCE.acceptInvite(player, invitation);
  }

  public static void kickPlayer(ServerPlayerEntity player, ServerPlayerEntity target) {
    PartyData party = getParty(player);
    UserParty member = party.getMembers().stream().filter(userParty1 -> userParty1.getPlayerUUID().equals(target.getUuid())).findFirst().orElse(null);
    if (member == null) return;
    DataBaseClientFactory.INSTANCE.kickPlayer(player, member);
  }
}
