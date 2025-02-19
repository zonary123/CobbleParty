package com.kingpixel.cobbleparty.gui;

import ca.landonjw.gooeylibs2.api.UIManager;
import ca.landonjw.gooeylibs2.api.button.Button;
import ca.landonjw.gooeylibs2.api.button.GooeyButton;
import ca.landonjw.gooeylibs2.api.button.linked.LinkType;
import ca.landonjw.gooeylibs2.api.button.linked.LinkedPageButton;
import ca.landonjw.gooeylibs2.api.helpers.PaginationHelper;
import ca.landonjw.gooeylibs2.api.page.GooeyPage;
import ca.landonjw.gooeylibs2.api.template.types.ChestTemplate;
import com.kingpixel.cobbleparty.CobbleParty;
import com.kingpixel.cobbleparty.api.PartyApi;
import com.kingpixel.cobbleparty.database.DataBaseClientFactory;
import com.kingpixel.cobbleparty.models.PartyData;
import com.kingpixel.cobbleutils.Model.ItemModel;
import com.kingpixel.cobbleutils.Model.PanelsConfig;
import com.kingpixel.cobbleutils.features.shops.Shop;
import com.kingpixel.cobbleutils.util.AdventureTranslator;
import com.kingpixel.cobbleutils.util.PlayerUtils;
import com.kingpixel.cobbleutils.util.TypeMessage;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Carlos Varas Alonso - 19/02/2025 6:04
 */
public class MenuInvitations {
  private final String title;
  private final int rows;
  private final ItemModel itemInvites;
  private final ItemModel previous;
  private final ItemModel close;
  private final ItemModel next;
  private final Shop.Rectangle rectangle;
  private final List<PanelsConfig> panels;

  public MenuInvitations() {
    this.title = "Invitations";
    this.rows = 3;
    this.itemInvites = new ItemModel(0, "minecraft:paper", "&aInvitation -> %party%", List.of(
      "&7Right click to accept",
      "&7Left click to decline"
    ), 0);
    this.previous = new ItemModel(18, "minecraft:arrow", "&aPrevious", List.of(
      "&7Go to the previous page"
    ), 0);
    this.close = new ItemModel(22, "minecraft:barrier", "&cClose", List.of(
      "&7Close the menu"
    ), 0);
    this.next = new ItemModel(26, "minecraft:arrow", "&aNext", List.of(
      "&7Go to the next page"
    ), 0);
    this.panels = List.of(
      new PanelsConfig(new ItemModel("minecraft:gray_stained_glass_pane"), rows)
    );
    this.rectangle = new Shop.Rectangle(rows);
  }

  public void open(ServerPlayerEntity player) {
    ChestTemplate template = ChestTemplate
      .builder(rows)
      .build();

    List<PartyData> invitations = PartyApi.getInvitations(player);
    if (invitations.isEmpty()) {
      PlayerUtils.sendMessage(
        player,
        CobbleParty.language.getMessageNoInvitation(),
        CobbleParty.language.getPrefix(),
        TypeMessage.CHAT
      );
      return;
    }

    PanelsConfig.applyConfig(template, panels);
    rectangle.apply(template);

    List<Button> buttons = getButtons(invitations);


    template.set(previous.getSlot(),
      LinkedPageButton.builder()
        .display(previous.getItemStack())
        .linkType(LinkType.Previous)
        .build()
    );

    template.set(next.getSlot(),
      LinkedPageButton.builder()
        .display(next.getItemStack())
        .linkType(LinkType.Next)
        .build()
    );

    template.set(close.getSlot(), close.getButton(action -> {
      CobbleParty.language.getMenu().open(action.getPlayer());
    }));

    GooeyPage page = PaginationHelper.createPagesFromPlaceholders(
      template,
      buttons,
      null
    );

    page.setTitle(
      AdventureTranslator.toNative(title)
    );

    UIManager.openUIForcefully(player, page);

  }

  private List<Button> getButtons( List<PartyData> invitations) {

    List<Button> buttons = new ArrayList<>();
    for (PartyData invitation : invitations) {
      String name = itemInvites.getDisplayname().replace("%party%", invitation.getName());
      List<String> lore = new ArrayList<>(itemInvites.getLore());
      lore.replaceAll(s -> s.replace("%party%", invitation.getName()));
      buttons.add(itemInvites.getButton(1, name, lore,action -> {
        ServerPlayerEntity player = action.getPlayer();
        switch (action.getClickType()){
          case LEFT_CLICK:
            DataBaseClientFactory.INSTANCE.declineInvite(player, invitation);
            break;
          case RIGHT_CLICK:
            DataBaseClientFactory.INSTANCE.acceptInvite(player, invitation);
            break;
        }
      }));
    }
    return buttons;
  }
}
