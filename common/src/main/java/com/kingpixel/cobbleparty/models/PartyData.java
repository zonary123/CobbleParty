package com.kingpixel.cobbleparty.models;

import com.kingpixel.cobbleparty.CobbleParty;
import com.kingpixel.cobbleparty.api.PartyApi;
import com.kingpixel.cobbleparty.events.DeletePartyEvent;
import com.kingpixel.cobbleutils.CobbleUtils;
import com.kingpixel.cobbleutils.api.PermissionApi;
import com.kingpixel.cobbleutils.util.AdventureTranslator;
import com.kingpixel.cobbleutils.util.PlayerUtils;
import com.kingpixel.cobbleutils.util.TypeMessage;
import com.kingpixel.cobbleutils.util.Utils;
import lombok.Data;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.MutableText;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author Carlos Varas Alonso - 19/02/2025 4:05
 */
@Data
public class PartyData {
  private UUID id = UUID.randomUUID();
  private String name;
  private List<UserParty> members = new ArrayList<>();
  private List<UUID> invites = new ArrayList<>();

  public PartyData(String name, ServerPlayerEntity owner) {
    this.name = name;
    this.members.add(new UserParty(owner, Role.OWNER));
  }

  public void addMember(ServerPlayerEntity player, Role role) {
    this.members.add(new UserParty(player, role));
  }

  public void acceptInvite(ServerPlayerEntity player) {
    this.invites.remove(player.getUuid());
    this.addMember(player, Role.MEMBER);
  }


  public Result leaveParty(ServerPlayerEntity player) {
    this.members.removeIf(userParty -> userParty.getPlayerUUID().equals(player.getUuid()));
    if (this.members.isEmpty()) {
      DeletePartyEvent.DELETE_PARTY_EVENT.emit(this);
      return Result.WITHOUT_MEMBERS;
    }
    UserParty party = this.members.get(Utils.RANDOM.nextInt(this.members.size()));
    party.setRole(Role.OWNER);
    return Result.LEAVE_SUCCESS;
  }

  public Result invitePlayer(ServerPlayerEntity player, ServerPlayerEntity target) {
    if (player.equals(target)) {
      PlayerUtils.sendMessage(
        player,
        CobbleParty.language.getMessagePlayerCantInviteHimSelf(),
        CobbleParty.language.getPrefix(),
        TypeMessage.CHAT
      );
      return Result.CANT_INVITE_HIMSELF;
    }
    PartyData targetParty = PartyApi.getParty(target);
    if (targetParty != null) {
      PlayerUtils.sendMessage(
        player,
        CobbleParty.language.getMessagePlayerYetInParty(),
        CobbleParty.language.getPrefix(),
        TypeMessage.CHAT
      );
      return Result.YET_IN_PARTY;
    }

    if (this.invites.contains(target.getUuid())) {
      PlayerUtils.sendMessage(
        player,
        CobbleParty.language.getMessagePlayerYetInvited(),
        CobbleParty.language.getPrefix(),
        TypeMessage.CHAT
      );
      return Result.YET_INVITED;
    }

    PlayerUtils.sendMessage(
      target,
      CobbleParty.language.getMessagePlayerInvited()
        .replace("%player%", player.getGameProfile().getName())
        .replace("%party%", this.name),
      CobbleParty.language.getPrefix(),
      TypeMessage.CHAT
    );

    PlayerUtils.sendMessage(
      player,
      CobbleParty.language.getMessagePlayerWasInvited()
        .replace("%player%", target.getGameProfile().getName())
        .replace("%party%", this.name),
      CobbleParty.language.getPrefix(),
      TypeMessage.CHAT
    );
    this.invites.add(target.getUuid());
    return Result.INVITE_SUCCESS;
  }

  public void declineInvite(ServerPlayerEntity player) {
    this.invites.remove(player.getUuid());
  }

  public void kickPlayer(ServerPlayerEntity player, UserParty member) {
    if (member.getRole().equals(Role.OWNER)) return;
    UserParty userParty = this.members.stream().filter(userParty1 -> userParty1.getPlayerUUID().equals(player.getUuid())).findFirst().orElse(null);
    if (userParty == null) return;
    if (userParty.getRole().equals(Role.MEMBER)) {
      return;
    }
    ServerPlayerEntity target = CobbleParty.server.getPlayerManager().getPlayer(member.getPlayerUUID());
    if (target != null) {
      PlayerUtils.sendMessage(
        target,
        CobbleParty.language.getMessagePlayerKickedBy()
          .replace("%player%", player.getGameProfile().getName())
          .replace("%party%", this.name),
        CobbleParty.language.getPrefix(),
        TypeMessage.CHAT
      );
    }
    PlayerUtils.sendMessage(
      player,
      CobbleParty.language.getMessagePlayerKicked()
        .replace("%player%", target != null ? player.getGameProfile().getName() : CobbleUtils.language.getUnknown())
        .replace("%party%", this.name),
      CobbleParty.language.getPrefix(),
      TypeMessage.CHAT
    );
    this.members.remove(member);
  }

  public void partyMessage(ServerPlayerEntity player, String message) {
    MutableText text = AdventureTranslator.toNativeComponent(CobbleParty.language.getFormatPartyChat()
      .replace("%party%", this.name)
      .replace("%player%", player.getGameProfile().getName())
      .replace("%message%", message)
      .replace("%prefixSpy%", ""));
    MutableText textSpy = AdventureTranslator.toNativeComponent(CobbleParty.language.getFormatPartyChat()
      .replace("%party%", this.name)
      .replace("%player%", player.getGameProfile().getName())
      .replace("%message%", message)
      .replace("%prefixSpy%", CobbleParty.language.getPrefixSpy()));
    for (ServerPlayerEntity target : CobbleParty.server.getPlayerManager().getPlayerList()) {
      boolean send = this.members.stream().anyMatch(userParty -> userParty.getPlayerUUID().equals(target.getUuid()));
      boolean ss = PermissionApi.hasPermission(target, "cobbleparty.admin.socialspy", 4);
      if (send) {
        target.sendMessage(text);
      } else if (ss) {
        target.sendMessage(textSpy);
      }
    }
  }
}

