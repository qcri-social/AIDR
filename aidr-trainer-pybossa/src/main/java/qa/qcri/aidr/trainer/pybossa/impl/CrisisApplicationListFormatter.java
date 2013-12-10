package qa.qcri.aidr.trainer.pybossa.impl;

import qa.qcri.aidr.trainer.pybossa.entity.Client;
import qa.qcri.aidr.trainer.pybossa.entity.ClientApp;
import qa.qcri.aidr.trainer.pybossa.service.TaskQueueService;

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


    public void format(){

        String url = client.getHostURL() +"/app/" + clientApp.getShortName();
        Integer remainingTask = taskQueueService.getCountTaskQeueByStatusAndClientApp(clientApp.getClientAppID(), 1);

        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("<div class=\"row-fluid well well-small\">");
        stringBuffer.append(getIconColumn().toString()) ;
        stringBuffer.append(getApplicationColumn(clientApp.getName(), remainingTask, url).toString()) ;
        stringBuffer.append("</div>");
    }

    public StringBuffer getApplicationColumn(String applicationName, Integer remainingTask, String url){
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("<div class=\"span9\">");
        stringBuffer.append("<h2><a href=\"");
        stringBuffer.append("\">");
        stringBuffer.append(applicationName);
        stringBuffer.append("</a></h2>");
        stringBuffer.append("<p><i class=\"icon-tasks\"></i>Remaining : <strong>");
        stringBuffer.append(remainingTask);
        stringBuffer.append("</strong> tasks</p>");
        stringBuffer.append("</div>")  ;

        return    stringBuffer;

    }
    public StringBuffer getIconColumn(){
        StringBuffer stringBuffer = new StringBuffer();

        stringBuffer.append("<div class=\"span3\">");
        stringBuffer.append("<img class=\"img-polaroid\" src=\"http://i.imgur.com/lgZAWIc.png\" style=\"max-width:64px\">") ;
        stringBuffer.append("</div>");

        return stringBuffer;
    }
}
