package com.kingpixel.cobbleparty.neoforge;

import com.kingpixel.cobbleparty.CobbleParty;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(CobbleParty.MOD_ID)
public class CobblePartyNeoForge {

  public CobblePartyNeoForge(IEventBus modBus) {
    CobbleParty.init();
  }
}
