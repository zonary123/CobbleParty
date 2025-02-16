package com.kingpixel.fabric.cobbleparty;

import com.kingpixel.cobbleparty.CobbleParty;
import net.fabricmc.api.ModInitializer;

public class CobblePartyFabric implements ModInitializer {

  @Override
  public void onInitialize() {
    CobbleParty.init();
  }

}
