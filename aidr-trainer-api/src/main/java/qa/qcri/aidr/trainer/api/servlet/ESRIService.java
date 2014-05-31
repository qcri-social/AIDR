package qa.qcri.aidr.trainer.api.servlet;

import org.springframework.beans.factory.annotation.Autowired;
import qa.qcri.aidr.trainer.api.service.GeoService;
import qa.qcri.aidr.trainer.api.util.Communicator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: jlucas
 * Date: 5/18/14
 * Time: 12:35 PM
 * To change this template use File | Settings | File Templates.
 */
public class ESRIService extends HttpServlet {

    @Autowired
    GeoService geoService;

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        processRequest(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        processRequest(request, response);
    }

    @SuppressWarnings("unchecked")
    public void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            String name = "qdate";
            String value = request.getParameter(name);
            String url = "http://localhost:8084/AIDRTrainerAPI/rest/geo/JSON/geoMap";
            if (value != null) {
                if(isValidDateFormat(value)){
                    url = "http://localhost:8084/AIDRTrainerAPI/rest/geo/JSON/geoMap/qdate/" + value;
                    //url = "http://pybossa-dev.qcri.org/AIDRTrainerAPI/rest/geo/JSON/geoMap/qdate/2014-02-26 13:44:48";
                    System.out.println(url);
                }
            }

            Communicator com = new Communicator();
            com.sendGet(url);
            String returnValue =  com.sendGet(url);
            System.out.println("esri: " + returnValue);
            final byte[] content = returnValue.getBytes("UTF-8");

            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.setContentLength(content.length);

            final OutputStream out = response.getOutputStream();
            out.write(content);


        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    private boolean isValidDateFormat(String lastupdated){
        try{
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            //String dateInString = "2014-01-26 13:44:48";
            Date queryDate = sdf.parse(lastupdated);
            return true;
        }
        catch(Exception e){
            System.out.println(e.getMessage());
            return false;
        }

    }


}
