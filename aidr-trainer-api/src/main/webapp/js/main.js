// The root URL for the RESTful services

var rootURL = "/AIDRTrainerAPI/rest/template/JSONP/crisis/code/";
var desc = "<b>Hi!</b>&nbsp;&nbsp;Thanks a lot for helping us in classifying the tweets collected on " ;
// Retrieve wine list when application starts
findByID();

function findByID() {

    var searchKey = $.url().param('code');

    if(typeof searchKey !='undefined' ){
        url = "http://" + window.location.host + rootURL + searchKey;
        $.ajax({
            type: 'GET',
            url: url,
            dataType: 'jsonp',
            success: renderList,
            error: FailedRenderList,
            jsonp: false,
            jsonpCallback: "jsonp"
        });
    }
    else{
        FailedRenderList();
    }
}

function FailedRenderList() {
    var defaultTextHtml =  "<h2><b>Initializing trainer task. Please come back in a few minutes.</b></h2>";
    var $defaultTextHtml = $(defaultTextHtml);
    $('#title').append($defaultTextHtml);
    $('#title').listview('refresh');
}

function renderList(data) {
    //console.log(data);

    var list = data == null ? [] : (data instanceof Array ? data : [data]);


    if(typeof list !='undefined' || list!= null ){

        var appList =  list[0].app== null ? [] : (list[0].app instanceof Array ? list[0].app : [list[0].app]);
        var template = '';
        var crisisTitle = '<h2>' + list[0].crisisName +'</h2>';
        document.title = "Help us classifying tweets related to " + list[0].crisisName + " - AIDR/MicroMappers";
        $.each(appList, function(index, item) {
            var respURL = item.url.split("/");
            if(respURL.length > 0){
              var shortName = respURL[respURL.length - 1];
              setPybossaCookie (shortName);
            }

            template = template + '<li data-theme="d">';
            if(item.remaining > 0){
                template = template + '<a href="' + item.url + '/newtask">';
            }
            template = template + '<img src="image/lgZAWIc.png" style="margin-left: 15px;"/>';
            template = template +  '<h3>' + item.name + '</h3>';
            template = template +  '<p>Volunteers have classified <b><font color="#13a89e">'+ item.totaltaskNumber +'</font></b> tweets so far.';
            if(item.remaining > 0){
                template = template +  ' There are <b><font color="#8d222c">' + item.remaining + '</font></b> tweets waiting to be classified.' + '</p>';
                if(item.remaining > 0){
                    template = template + '</a>';
                }
                template = template + '</li>';
            }
            else{
                template = template +  ' Waiting for more tweets, please come back in a few minutes.';
            }



        });
        desc = desc + list[0].crisisName;
        $('#title').append(crisisTitle);
        $('#description').append(desc);
        $('#crisisAppList').append(template);
        $('#crisisAppList').listview('refresh');
    }
    else{
        FailedRenderList();

    }


}


function setPybossaCookie(cookieName){
    var tutorialCookie = cookieName + "tutorial";
    if($.cookie(cookieName) === null || typeof $.cookie(cookieName) == 'undefined'){

        $.cookie(cookieName, 'seen', {
            path    : '/',
            domain  : 'http://clickers.micromappers.org/'
        });

        $.cookie(tutorialCookie, 'seen', {
            path    : '/',
            domain  : 'http://clickers.micromappers.org/'
        });

        $.cookie(cookieName, 'seen', {
            path    : '/'
        });

        $.cookie(tutorialCookie, 'seen', {
            path    : '/'
        });
    }

}

