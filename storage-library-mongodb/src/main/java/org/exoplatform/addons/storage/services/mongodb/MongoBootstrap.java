/*
 * Copyright (C) 2012 eXo Platform SAS.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.exoplatform.addons.storage.services.mongodb;

import com.mongodb.*;
import org.exoplatform.addons.storage.utils.PropertyManager;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.logging.Logger;

public class MongoBootstrap {

    private Mongo m;
    private DB db;
    private static Logger log = Logger.getLogger(MongoBootstrap.class.getName());

    private Mongo mongo() {
        if (m==null) {
            try {
                MongoOptions options = new MongoOptions();
                options.connectionsPerHost = 200;
                options.connectTimeout = 60000;
                options.threadsAllowedToBlockForConnectionMultiplier = 10;
                options.autoConnectRetry = true;
                String host = PropertyManager.getProperty(PropertyManager.PROPERTY_SERVER_HOST);
                int port = Integer.parseInt(PropertyManager.getProperty(PropertyManager.PROPERTY_SERVER_PORT));
                m = new Mongo(new ServerAddress(host, port), options);
                m.setWriteConcern(WriteConcern.SAFE);
            } catch (UnknownHostException e) {

            } catch (IOException e) {

            }
        }
        return m;
    }

    public void close() {
        try {
            if (m!=null) {
                m.close();
            }
        } catch (NullPointerException e) {

        }
    }

    public void initialize() {
        this.close();
        this.m = null;
        this.mongo();
    }

    public void dropDB(String dbName) {
        log.info("---- Dropping DB " + dbName);
        mongo().dropDatabase(dbName);
        log.info("-------- DB " + dbName + " dropped!");
    }

    public DB getDB() {
        return getDB(null);
    }

    public DB getDB(String dbName) {
        if (db==null || dbName!=null) {
            if (dbName!=null) {
                db = mongo().getDB(dbName);
            } else {
                db = mongo().getDB(PropertyManager.getProperty(PropertyManager.PROPERTY_DB_NAME));
            }
            boolean authenticate = "true".equals(PropertyManager.getProperty(PropertyManager.PROPERTY_DB_AUTHENTICATION));
            if (authenticate) {
                db.authenticate(PropertyManager.getProperty(PropertyManager.PROPERTY_DB_USER), PropertyManager.getProperty(PropertyManager.PROPERTY_DB_PASSWORD).toCharArray());
            }
            initCollection("statistics");
        }
        return db;
  }

  public void initCappedCollection(String name, int size)
  {
    initCollection(name, true, size);
  }

  private void initCollection(String name)
  {
    initCollection(name, false, 0);
  }

  private void initCollection(String name, boolean isCapped, int size)
  {
    if (getDB().collectionExists(name)) return;

    BasicDBObject doc = new BasicDBObject();
    doc.put("capped", isCapped);
    if (isCapped)
      doc.put("size", size);
    getDB().createCollection(name, doc);

  }
}
