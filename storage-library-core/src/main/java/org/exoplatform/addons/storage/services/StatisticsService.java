package org.exoplatform.addons.storage.services;

import org.exoplatform.addons.storage.model.*;
import org.jdom.Document;

import java.util.List;

/**
 * Created by menzli on 21/04/14.
 */
public interface StatisticsService {

    public static final String M_STATISTICS = "statistics";

    public void cleanupStatistics(long timestamp) throws Exception;

    public StatisticsBean insert(StatisticsBean statisticsBean) throws Exception;

    public StatisticsBean update (StatisticsBean statisticsBean, String id) throws Exception;

    public List<StatisticsBean> filter (StatisticsBean statisticsBean) throws Exception;

    public int count (long timestamp) throws Exception;

    public void export(List<StatisticsBean> statistics, String format) throws Exception;

}
