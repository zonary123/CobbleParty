package com.kingpixel.cobbleparty.gui;

import ca.landonjw.gooeylibs2.api.UIManager;
import ca.landonjw.gooeylibs2.api.page.GooeyPage;
import ca.landonjw.gooeylibs2.api.template.types.ChestTemplate;
import com.kingpixel.cobbleparty.CobbleParty;
import com.kingpixel.cobbleparty.api.PartyApi;
import com.kingpixel.cobbleparty.database.DataBaseClientFactory;
import com.kingpixel.cobbleutils.Model.ItemModel;
import com.kingpixel.cobbleutils.Model.PanelsConfig;
import com.kingpixel.cobbleutils.util.AdventureTranslator;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.List;

/**
 * @author Carlos Varas Alonso - 19/02/2025 5:34
 */
public class Menu {
  private String title;
  private int rows;
  private ItemModel itemInvites;
  private ItemModel itemMembers;
  private ItemModel itemLeave;
  private ItemModel close;
  private List<PanelsConfig> panels;

  public Menu() {
    this.title = "Menu";
    this.rows = 3;
    this.itemInvites = new ItemModel(10, "minecraft:paper", "&aInvites", List.of(
      "&7View and accept invites"
    ), 0);
    this.itemMembers = new ItemModel(12, "minecraft:player_head", "&aMembers", List.of(
      "&7View and manage members"
    ), 0);
    this.itemLeave = new ItemModel(16, "minecraft:barrier", "&cLeave", List.of(
      "&7Leave the party"
    ), 0);
    this.close = new ItemModel(22, "minecraft:barrier", "&cClose", List.of(
      "&7Close the menu"
    ), 0);
    this.panels = List.of(
      new PanelsConfig(new ItemModel("minecraft:gray_stained_glass_pane"), rows)
    );
  }

  public void open(ServerPlayerEntity player) {
    ChestTemplate template = ChestTemplate
      .builder(rows)
      .build();

    PanelsConfig.applyConfig(template, panels);


    if (PartyApi.hasParty(player)) {
      template.set(itemMembers.getSlot(), itemMembers.getButton(action -> CobbleParty.language.getMenuMembers().open(action.getPlayer())));

      template.set(itemLeave.getSlot(), itemLeave.getButton(action -> DataBaseClientFactory.INSTANCE.leaveParty(action.getPlayer())));
    } else {
      template.set(itemInvites.getSlot(), itemInvites.getButton(action -> CobbleParty.language.getMenuInvitations().open(action.getPlayer())));
    }


    template.set(close.getSlot(), close.getButton(action -> UIManager.closeUI(player)));


    GooeyPage page = GooeyPage
      .builder()
      .title(AdventureTranslator.toNative(title))
      .template(template)
      .build();

    UIManager.openUIForcefully(player, page);

  }
}
