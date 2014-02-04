package qa.qcri.aidr.trainer.pybossa.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created with IntelliJ IDEA.
 * User: jilucas
 * Date: 10/6/13
 * Time: 11:58 AM
 * To change this template use File | Settings | File Templates.
 */
public class DateTimeConverter {

    public static String convertToDefault(String input) throws ParseException {
        DateFormat utcFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        utcFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

        Date date = utcFormat.parse(input);

        DateFormat currentFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
        currentFormat.setTimeZone(TimeZone.getDefault());

       // System.out.println(currentFormat.format(date));

        return currentFormat.format(date);
    }

    public static String utcToDefault(String input) throws ParseException {



        DateFormat currentFormat =  DateFormat.getDateInstance();
        Date date = currentFormat.parse(input);

        return date.toString();
    }

    public static long getHourDifference(Date oldDate, Date newDate){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long diffHours = 0;
        if(newDate == null){
            newDate = new Date();
        }

        try {
            Date dbDate = sdf.parse(oldDate.toString());
            Date nowDate = sdf.parse(sdf.format(newDate));
            long diff = Math.abs(nowDate.getTime() - dbDate.getTime());
            diffHours = diff / (60 * 60 * 1000);

        } catch (ParseException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        return diffHours;
    }

    public static String reformattedCurrentDate(){
        DateFormat currentFormat =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        return currentFormat.format(new Date());
    }

    public static String reformattedCurrentDateForFileName(){
        DateFormat currentFormat =  new SimpleDateFormat("yyyyMMddHHmm");
        return currentFormat.format(new Date());
    }
}
