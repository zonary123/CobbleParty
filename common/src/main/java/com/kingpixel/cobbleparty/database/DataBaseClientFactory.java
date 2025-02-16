package com.kingpixel.cobbleparty.database;

import com.kingpixel.cobbleutils.Model.DataBaseConfig;

/**
 * @author Carlos Varas Alonso - 27/07/2024 7:36
 */
public class DataBaseClientFactory {
  public static DataBaseClient INSTANCE;

  public static DataBaseClient create(DataBaseConfig config) {
    if (INSTANCE != null) {
      INSTANCE.disconnect();
    }
    switch (config.getType()) {
      case JSON -> INSTANCE = new DataBaseJson();
      case MONGODB -> INSTANCE = new DataBaseMongoDB();
      case SQLITE -> INSTANCE = new DataBaseSQLITE();
      default -> INSTANCE = new DataBaseJson();
    }
    INSTANCE.connect();
    return INSTANCE;
  }

}
