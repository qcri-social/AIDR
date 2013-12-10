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


    public static String reformattedCurrentDate(){
        DateFormat currentFormat =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        return currentFormat.format(new Date());
    }

    public static String reformattedCurrentDateForFileName(){
        DateFormat currentFormat =  new SimpleDateFormat("yyyyMMddHHmm");
        return currentFormat.format(new Date());
    }
}
