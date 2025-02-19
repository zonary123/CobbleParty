package com.kingpixel.cobbleparty;

import com.kingpixel.cobbleparty.command.CommandTree;
import com.kingpixel.cobbleparty.config.Config;
import com.kingpixel.cobbleparty.config.Lang;
import com.kingpixel.cobbleparty.database.DataBaseClientFactory;
import com.kingpixel.cobbleparty.events.CreatePartyEvent;
import com.kingpixel.cobbleparty.events.DeletePartyEvent;
import com.kingpixel.cobbleparty.utils.PlaceHolderUtils;
import com.kingpixel.cobbleutils.CobbleUtils;
import dev.architectury.event.events.common.CommandRegistrationEvent;
import dev.architectury.event.events.common.LifecycleEvent;
import dev.architectury.event.events.common.PlayerEvent;
import net.minecraft.server.MinecraftServer;

/**
 * @author Carlos Varas Alonso - 28/04/2024 23:50
 */
public class CobbleParty {
  public static final String MOD_ID = "cobbleparty";
  public static final String MOD_NAME = "cobbleparty";
  public static final String PATH = "/config/cobbleparty/";
  public static final String PATH_LANG = PATH + "lang/";
  public static final String PATH_DATA = PATH + "data/";
  public static MinecraftServer server;
  public static Config config = new Config();
  public static Lang language = new Lang();

  public static void init() {
    CobbleUtils.LOGGER.info(MOD_ID, "Initializing " + MOD_NAME);
    events();
  }

  public static void load() {
    files();
    DataBaseClientFactory.create(config.getDatabase());
  }

  private static void files() {
    config.init();
    language.init();
  }

  private static void events() {
    files();

    LifecycleEvent.SERVER_STARTED.register(server -> load());

    CommandRegistrationEvent.EVENT.register((dispatcher, registry, selection) -> CommandTree.register(dispatcher));

    PlayerEvent.PLAYER_JOIN.register(player -> {

    });

    PlayerEvent.PLAYER_QUIT.register(player -> {

    });

    LifecycleEvent.SERVER_STOPPING.register(server -> {
      CreatePartyEvent.CREATE_PARTY_EVENT.clear();
      DeletePartyEvent.DELETE_PARTY_EVENT.clear();
    });

    LifecycleEvent.SERVER_LEVEL_LOAD.register(level -> server = level.getServer());

    // PlaceHolders
    PlaceHolderUtils.register();

  }
}
