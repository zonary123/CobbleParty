package com.kingpixel.cobbleparty.gui;

import ca.landonjw.gooeylibs2.api.UIManager;
import ca.landonjw.gooeylibs2.api.button.Button;
import ca.landonjw.gooeylibs2.api.button.linked.LinkType;
import ca.landonjw.gooeylibs2.api.button.linked.LinkedPageButton;
import ca.landonjw.gooeylibs2.api.helpers.PaginationHelper;
import ca.landonjw.gooeylibs2.api.page.GooeyPage;
import ca.landonjw.gooeylibs2.api.template.types.ChestTemplate;
import com.kingpixel.cobbleparty.CobbleParty;
import com.kingpixel.cobbleparty.api.PartyApi;
import com.kingpixel.cobbleparty.database.DataBaseClientFactory;
import com.kingpixel.cobbleparty.models.UserParty;
import com.kingpixel.cobbleutils.Model.ItemModel;
import com.kingpixel.cobbleutils.Model.PanelsConfig;
import com.kingpixel.cobbleutils.Model.Rectangle;
import com.kingpixel.cobbleutils.util.AdventureTranslator;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Carlos Varas Alonso - 19/02/2025 6:04
 */
public class MenuMembers {
  private final String title;
  private final int rows;
  private final ItemModel itemMember;
  private final ItemModel previous;
  private final ItemModel close;
  private final ItemModel next;
  private final Rectangle rectangle;
  private final List<PanelsConfig> panels;

  public MenuMembers() {
    this.title = "Invitations";
    this.rows = 3;
    this.itemMember = new ItemModel(0, "minecraft:paper", "&aMember -> %member%", List.of(
      "&7Right click to kick",
      "&7Left click to promote"
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
    this.rectangle = new Rectangle(rows);
  }

  public void open(ServerPlayerEntity player) {
    ChestTemplate template = ChestTemplate
      .builder(rows)
      .build();

    List<UserParty> members = PartyApi.getParty(player).getMembers();

    PanelsConfig.applyConfig(template, panels);
    rectangle.apply(template);

    List<Button> buttons = getButtons(members);


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

  private List<Button> getButtons(List<UserParty> members) {

    List<Button> buttons = new ArrayList<>();
    for (UserParty member : members) {
      String name = replace(member, itemMember.getDisplayname());
      List<String> lore = new ArrayList<>(itemMember.getLore());
      lore.replaceAll(s -> replace(member, s));

      buttons.add(itemMember.getButton(1, name, lore, action -> {
        DataBaseClientFactory.INSTANCE.kickPlayer(action.getPlayer(), member);
      }));

    }
    return buttons;
  }

  private String replace(UserParty member, String message) {
    return message.replace("%member%", member.getPlayerName());
  }
}
