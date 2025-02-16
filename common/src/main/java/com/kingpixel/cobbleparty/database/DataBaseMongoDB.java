package com.kingpixel.cobbleparty.database;

import com.kingpixel.cobbleparty.CobbleParty;
import com.kingpixel.cobbleutils.CobbleUtils;
import com.kingpixel.cobbleutils.bson.Document;
import com.kingpixel.cobbleutils.mongodb.client.MongoClient;
import com.kingpixel.cobbleutils.mongodb.client.MongoClients;
import com.kingpixel.cobbleutils.mongodb.client.MongoCollection;
import com.kingpixel.cobbleutils.mongodb.client.MongoDatabase;

public class DataBaseMongoDB implements DataBaseClient {
  private final MongoClient mongoClient;
  private final MongoDatabase database;
  private MongoCollection<Document> collectionPartyInfo;

  private final String COLLECTION_PARTYINFO = "UserInfo";

  public DataBaseMongoDB() {
    this.mongoClient = MongoClients.create(CobbleParty.config.getDataBaseConfig().getUrl());
    this.database = mongoClient.getDatabase(CobbleParty.config.getDataBaseConfig().getDatabase());
  }

  @Override public void connect() {
    CobbleUtils.LOGGER.info(CobbleParty.MOD_ID, "Connected to MongoDB");

    try {
      createTable(COLLECTION_PARTYINFO);
      this.collectionPartyInfo = database.getCollection(COLLECTION_PARTYINFO);
    } catch (Exception e) {
      CobbleUtils.LOGGER.error(CobbleParty.MOD_ID, "Error while connecting to MongoDB: " + e.getMessage());
    }
  }

  @Override public void disconnect() {
    mongoClient.close();
  }

  private void createTable(String tableName) {
    database.getCollection(tableName);
  }

}