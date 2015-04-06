package org.exoplatform.addons.storage.services.mongodb;

import com.google.gson.Gson;
import com.mongodb.*;
import com.mongodb.util.JSON;
import org.exoplatform.addons.storage.listener.ConnectionManager;
import org.exoplatform.addons.storage.model.*;
import org.exoplatform.addons.storage.services.StatisticsService;
import org.exoplatform.addons.storage.utils.StatisticUtils;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * Created by menzli on 02/05/14.
 */
@Named("statisticsService")
@ApplicationScoped
public class StatisticsServiceImpl implements StatisticsService {

    private static Logger LOG = Logger.getLogger(StatisticsServiceImpl.class.getName());

    private DB db() {

        return ConnectionManager.getInstance().getDB();

    }

    @Override
    public void cleanupStatistics(long timestamp) throws Exception {

        DBCollection coll = db().getCollection(M_STATISTICS);

        DBCursor cursor = null;

        BasicDBObject query = new BasicDBObject();

        if (timestamp > 0) {

            query.put("timestamp", new BasicDBObject("$lt", timestamp));

        }

        try {

            cursor = coll.find(query);

            while (cursor.hasNext())
            {
                DBObject doc = cursor.next();

                coll.remove(doc);
            }
        } finally {

            cursor.close();

        }
    }

    @Override
    public StatisticsBean insert(StatisticsBean statisticsBean) {

        DBCollection coll = db().getCollection(M_STATISTICS);

        BasicDBObject doc = new BasicDBObject();

        doc.put("timestamp", System.currentTimeMillis());

        //--- Persist an Actor Bean
        BasicDBObject actor = new BasicDBObject();

        actor.put("objectType",statisticsBean.getActor().getObjectType().name());
        actor.put("userName",statisticsBean.getActor().getUserName());
        doc.put("actor",actor);

        /** FIN */

        //--- persist the verb
        doc.put("verb", statisticsBean.getVerb().name());


        //--- Persist the Object Bean
        BasicDBObject object = new BasicDBObject();

        object.put("objectType",statisticsBean.getObject().getObjectType().name());
        object.put("displayName",statisticsBean.getObject().getDisplayName());
        object.put("content",statisticsBean.getObject().getContent());
        object.put("spentTime", statisticsBean.getObject().getSpentTime());
        object.put("url",statisticsBean.getObject().getUrl());
        object.put("link",statisticsBean.getObject().getLink());
        doc.put("object", object);
        /** FIN */

        //--- Persist the the Target Bean
        if (statisticsBean.getTarget() != null ) {

            BasicDBObject target = new BasicDBObject();

            target.put("objectType",statisticsBean.getTarget().getObjectType().name());
            target.put("displayName",statisticsBean.getTarget().getDisplayName());

            //---Manage Actor
            BasicDBObject targetActor = new BasicDBObject();
            targetActor.put("objectType",statisticsBean.getTarget().getActorBean().getObjectType().name());
            targetActor.put("userName",statisticsBean.getTarget().getActorBean().getUserName());
            target.put("actor",targetActor);

            doc.put("target", target);
        }
        /** FIN */

        //--- Persist the the Context Bean

        if (statisticsBean.getContext() != null) {

            BasicDBObject context = new BasicDBObject();

            context.put("type",statisticsBean.getContext().getType());
            context.put("value",statisticsBean.getContext().getValue());

            doc.put("context", context);

        }

        doc.put("isPrivate", false);

        //--- add new line to the dababase (what's the difference with save() method)
        coll.insert(doc);

        return null;
    }

    @Override
    public StatisticsBean update (StatisticsBean statisticsBean, String id) {

        return null;
    }

    @Override
    public List<StatisticsBean> filter (StatisticsBean statisticsBean) throws Exception {

        return null;

    }

    @Override
    public int count (long timestamp) throws Exception {

        DBCollection coll = db().getCollection(M_STATISTICS);

        BasicDBObject query = new BasicDBObject();

        if (timestamp > 0 ) {

            //--- get only tuples which timestap >= {{timestamp}}
            query.put("timestamp", new BasicDBObject("$gte", timestamp));

        }

        return coll.find(query).count();

    }

    @Override
    public void export (List<StatisticsBean> statistics, String format) throws Exception {

        if (format.equalsIgnoreCase("json")) {

            StatisticUtils.json(statistics);

        } else if (format.equalsIgnoreCase("xml")) {

            StatisticUtils.xml(statistics);

        } else {

            throw new IllegalArgumentException();

        }
    }

}
