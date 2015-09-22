package qa.qcri.aidr.trainer.api.template;


import qa.qcri.aidr.trainer.api.entity.Client;
import qa.qcri.aidr.trainer.api.entity.ClientApp;
import qa.qcri.aidr.trainer.api.service.TaskQueueService;
import qa.qcri.aidr.trainer.api.store.URLReference;

/**
 * Created with IntelliJ IDEA.
 * User: jlucas
 * Date: 10/26/13
 * Time: 2:30 PM
 * To change this template use File | Settings | File Templates.
 */
public class CrisisApplicationListFormatter {
    private ClientApp clientApp ;
    private Client client;
    private TaskQueueService taskQueueService;

    public CrisisApplicationListFormatter(ClientApp clientApp, Client client, TaskQueueService taskQueueService){
        this.clientApp = clientApp;
        this.client = client;
        this.taskQueueService = taskQueueService;
    }

    public String getURLLink(){
      //  String hostApiUrl = client.getHostURL();
       // hostApiUrl = hostApiUrl.replace("/api","/app/");
        //String url = hostApiUrl + clientApp.getShortName();
         String url = URLReference.PUBLIC_PYBOSSA_URL + "/project/" + clientApp.getShortName();
        return url;
    }

    public Integer getRemaining(){
        Integer remainingTask = taskQueueService.getCountTaskQeueByStatusAndClientApp(clientApp.getClientAppID(), 1);

        return remainingTask;
    }

    public Integer getTotalTaskNumber(){
        Integer totalTask = 0;
        if(taskQueueService.getTaskQueueByClientApp(clientApp.getClientAppID()) != null){
            totalTask =   taskQueueService.getTotalNumberOfTaskQueue(clientApp.getClientAppID()).size();
        }

        return totalTask;
    }


    public String format(){
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("<div>");
        String hostApiUrl = client.getHostURL();
        hostApiUrl = hostApiUrl.replace("/api","/project/");
        stringBuffer.append("<table>");

        String url = hostApiUrl + clientApp.getShortName();
        Integer remainingTask = taskQueueService.getCountTaskQeueByStatusAndClientApp(clientApp.getClientAppID(), 1);

        stringBuffer.append(getIconColumn().toString()) ;
        stringBuffer.append(getApplicationColumn(clientApp.getName(), remainingTask, url).toString()) ;

        stringBuffer.append("</table>");
        stringBuffer.append("</div>");

        return stringBuffer.toString();
    }

    public StringBuffer getApplicationColumn(String applicationName, Integer remainingTask, String url){
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("<td><p>&nbsp;</p><p style='font-size: 12px;'><a href='");
        stringBuffer.append(url);
        stringBuffer.append("'>&nbsp;&nbsp;&nbsp;");
        stringBuffer.append(applicationName);
        stringBuffer.append("</a></p>");
        stringBuffer.append("<p style='font-size: 12px;color: #888282;'>&nbsp;&nbsp;&nbsp;Remaining : ");
        stringBuffer.append(remainingTask);
        stringBuffer.append(" tasks</p>");
        stringBuffer.append("</td></tr>")  ;

        return    stringBuffer;

    }
    public StringBuffer getIconColumn(){
        StringBuffer stringBuffer = new StringBuffer();

        stringBuffer.append("<tr><td>");
        stringBuffer.append("<img src='http://i.imgur.com/lgZAWIc.png' style='max-width:34px'>") ;
        stringBuffer.append("</td>");
        return stringBuffer;
    }
}
