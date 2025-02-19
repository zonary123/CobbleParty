package com.kingpixel.cobbleparty.events;

import com.kingpixel.cobbleparty.models.PartyData;

/**
 * @author Carlos Varas Alonso - 28/06/2024 8:45
 */
public interface CreatePartyListener {
  void onCreateParty(PartyData partyData);
}
