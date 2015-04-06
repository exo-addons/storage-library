package org.exoplatform.addons.storage.query;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

/**
 * Created by kmenzli on 04/07/2014.
 */
public interface Query <E,V,K> {

    public BasicDBObject addConstraint (E target, V value, K constraint);
}
