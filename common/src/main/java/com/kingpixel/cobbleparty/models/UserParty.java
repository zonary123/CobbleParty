package com.kingpixel.cobbleparty.models;

import lombok.Data;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.UUID;

/**
 * @author Carlos Varas Alonso - 19/02/2025 4:05
 */
@Data
public class UserParty {
  private UUID playerUUID;
  private String playerName;
  private Role role;

  public UserParty(ServerPlayerEntity player, Role role) {
    this.playerUUID = player.getUuid();
    this.playerName = player.getGameProfile().getName();
    this.role = role;
  }
}
