package org.exoplatform.addons.storage.utils;

import org.exoplatform.addons.storage.model.StatisticsBean;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

/**
 * Created by kmenzli on 04/07/2014.
 */
public class StatisticUtils {

    public static String exportLocation = System.getProperty("java.io.tmpdir");

    /**
     * This method validates the given time stamp in String format
     * @param timestamp
     * @return
     */

    public static long isTimeStampValid(String timestamp) {
        //(Considering that formal will be yyyy-MM-dd HH:mm:ss.SSSSSS )
        //Tokenize string and separate date and time
        boolean time = false;
        try {
            //Tokenize string and separate date and time
            StringTokenizer st = new StringTokenizer(timestamp, " ");

            if (st.countTokens() != 2) {
                return 0;
            }

            String[] dateAndTime = new String[2];

            int i = 0;
            while (st.hasMoreTokens()) {
                dateAndTime[i] = st.nextToken();
                i++;
            }

            String timeToken = dateAndTime[1];

            StringTokenizer timeTokens = new StringTokenizer(timeToken, ":");
            if (timeTokens.countTokens() != 3) {
                return 0;
            }

            String[] timeAt = new String[4];
            int j = 0;
            while (timeTokens.hasMoreTokens()) {
                timeAt[j] = timeTokens.nextToken();
                j++;
            }
            try {
                int HH = Integer.valueOf(timeAt[0].toString());
                int mm = Integer.valueOf(timeAt[1].toString());
                float ss = Float.valueOf(timeAt[2].toString());


                if (HH < 60 && HH >= 0 && mm < 60 && mm >= 0 && ss < 60 && ss >= 0) {
                    time = true;
                } else {
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            //Got Date
            String dateToken = dateAndTime[0];//st.nextToken();

            //Tokenize separated date and separate year-month-day
            StringTokenizer dateTokens = new StringTokenizer(dateToken, "-");
            if (dateTokens.countTokens() != 3) {
                return 0;
            }
            String[] tokenAt = new String[3];

            //This will give token string array with year month and day value.
            int k = 0;
            while (dateTokens.hasMoreTokens()) {
                tokenAt[k] = dateTokens.nextToken();
                k++;
            }

            //Now try to create new date with got value of date
            int dayInt = Integer.parseInt(tokenAt[2]);
            int monthInt = Integer.parseInt(tokenAt[1]);
            int yearInt = Integer.parseInt(tokenAt[0]);
            Calendar cal = new GregorianCalendar();
            cal.setLenient(false);
            cal.set(yearInt, monthInt - 1, dayInt);
            cal.getTime();//If not able to create date it will throw error



        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
        //Here we ll check for correct format is provided else it ll return false
        try {
            Pattern p = Pattern.compile("^\\d{4}[-]?\\d{1,2}[-]?\\d{1,2} \\d{1,2}:\\d{1,2}:\\d{1,2}[.]?\\d{1,6}$");
            if (p.matcher(timestamp).matches()) {
            } else {
                return 0;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
        //Cross checking with simple date format to get correct time stamp only
        SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSS");
        try {
            format.parse(timestamp);
            //return true;

            if (time) {
                return Long.parseLong(timestamp);
            } else {
                return 0;
            }
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        } catch (Exception e) {
            e.printStackTrace();

            return 0;
        }

    }


    /**
     *
     * @param statistics
     */
    public static void xml (List<StatisticsBean> statistics) {
        //--- build the xml structure
        Document doc = structure(statistics);

        //--- Generate the xml file
        XMLOutputter outputter = null;

        long timestamp = System.currentTimeMillis() / 1000;

        try {

            outputter = new XMLOutputter(Format.getPrettyFormat());

            outputter.output(doc, new FileWriter(exportLocation+"/statistics-"+timestamp+".xml"));

        } catch (IOException IOE) {

        }
    }

    /**
     *
     * @param statistics
     */
    public static void json (List<StatisticsBean> statistics) {

        FileWriter file = null;

        long timestamp = System.currentTimeMillis() / 1000;

        try {

            file = new FileWriter(exportLocation+"/statistics-"+timestamp+".json");

            file.write(StatisticsBean.statisticstoJSON(statistics));

            file.flush();

            file.close();

        } catch (IOException IOE) {
        }
    }

    /**
     *
     * @param statistics
     * @return
     */
    public static Document structure(List<StatisticsBean> statistics) {

        Document doc = new Document();

        Element statsListElement = new Element("statistics");

        Element statisticElement = null;

        for (StatisticsBean statisticsBean : statistics) {

            statisticElement = new Element("statistic");

            statisticElement.setAttribute("verb",statisticsBean.getVerb().name());

            Element actorElement = new Element("actor");

            actorElement.addContent(new Element("objectType").addContent(statisticsBean.getActor().getObjectType().name()));

            actorElement.addContent(new Element("userName").addContent(statisticsBean.getActor().getUserName()));

            statsListElement.addContent(statisticElement);

        }

        doc.addContent(statsListElement);

        return doc;

    }

}
