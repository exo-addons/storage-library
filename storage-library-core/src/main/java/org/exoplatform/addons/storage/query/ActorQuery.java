package org.exoplatform.addons.storage.query;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.apache.commons.lang3.StringUtils;
import org.exoplatform.addons.storage.model.ObjectType;

/**
 * Created by kmenzli on 04/07/2014.
 */
public class ActorQuery implements Query <Object,String,String> {

    private BasicDBObject actor = null;

    public ActorQuery() {

        this.actor = new BasicDBObject ();
    }

    public BasicDBObject addConstraint (Object target, String value, String constraint) {

        if (StringUtils.isNotEmpty(constraint)) {

            actor.append(target.toString(), new BasicDBObject(constraint,value));

        } else {

            actor.append(target.toString(),value);

        }

        return actor;

    }



}
