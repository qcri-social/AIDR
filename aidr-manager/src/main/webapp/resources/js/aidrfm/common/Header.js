Ext.onReady(function() {
        Ext.BLANK_IMAGE_URL = 'http://extjs.cachefly.net/ext-3.2.1/resources/images/default/s.gif';
        App.Demo.init();
    });


    Ext.ns('App2');
    App2.Demo = {
        init:function()
        {
            
        },

        viewProfile:function(url)
        {
            var win = new Ext.Window({
                width:330,
                minHeight:115,
                cls: 'popWindow',
                resizable :false,
                
                autoScroll:true,
                modal:true
            });


            Ext.Ajax.request({
    url: 'http://google.com',
    method: "get",
         
    success: function(response){
        var responseText = response.responseText;
        console.log(hey);
        //DO STUFF  
    },
    failure: function(response ) {
            // DO STUFF
            console.log("hey");
            var name="sushant";
            var key="f65b49da-9373-11e5-8994-feff819cdc9f";
            var dh = Ext.DomHelper;
            dh.append('name',name);
            dh.append('key',key);
          
            }
});
            var div=
            	'<div id="content1" class="modal-body" >'+
     '<p class="text-right" id="name"><span><img width="24" height="24" id="twitter" src="' + BASE_URL + '/resources/img/User-Profile-24.png"/></span></p>'+
           
       ' </div>'+
        '<div id="content2" class="modal-body" >'+
           
          ' <p class="text-right" id="key"><span><img width="24" height="24" id="twitter" src="' + BASE_URL + '/resources/img/key96.png"/></span></p>'+

        '</div>'+
         '<div class="modal-body" >'
            
            

            // show first
            win.show();
            // then iframe
            Ext.DomHelper.insertFirst(win.body, div)

        }

    }
      Ext.ns('App1');
    App1.Demo1 = {
        init:function()
        {
            
        },

        updateProfile:function(url)
        {
            var win = new Ext.Window({
                width:370,
                minHeight:220,
                cls: 'popWindow',
                resizable:false,
                
                autoScroll:true,
                modal:true
            });
            

            Ext.Ajax.request({
    url: 'http:google.com',
    method: "JSONP",
         
    success: function(response){
        var responseText = response.responseText;
        console.log("hey");
        var name="sushant";
            var key="f65b49da-9373-11e5-8994-feff819cdc9f";
            var dh = Ext.DomHelper;
            dh.append('name',name);
            dh.append('key',key);
        //DO STUFF  
    },
    failure: function(response ) {
            // DO STUFF
            console.log("hey");
            console.log("hey");
        var name="sushant";
            var key="f65b49da-9373-11e5-8994-feff819cdc9f";
            var dh = Ext.DomHelper;
            dh.append('name',name);
            dh.append('key',key);
            }
});
            var div={
            	html:'<div id="content1" class="modal-body" >'+
     '<p class="text-right" id="name"><span><img width="24" height="24" id="twitter" src="' + BASE_URL + '/resources/img/User-Profile-24.png"/></span></p>'+
           
       ' </div>'+
        '<div id="content1" class="modal-body" >'+
           
          ' <p class="text-right" id="key"><span><img width="24" height="24" id="twitter" src="' + BASE_URL + '/resources/img/key96.png"/></span></p>'+

        '</div>'+
         '<div id="content2" class="modal-body" >'+
'<form action="demo_form.asp" method="get">'+
         ' <p class="text-right" id="name"><span><img width="24" height="24" id="twitter" src="' + BASE_URL + '/resources/img/email5.png"/> <input class="inputForm" type="text" name="fname"></span></p>'
        +
          
         ' <p class="text-right" id="name"><span><img width="24" height="24" id="twitter" src="' + BASE_URL + '/resources/img/character.png"/> <input class="inputForm" type="text" name="lname"></span></p>'+'<input class="btn btn-rddish1" type="button"onclick="window.location.replace("http://localhost:8084/AIDRFetchManager/public.jsp#")" value="Cancel"><input class="btn btn-bluish1" type="submit" value="Submit"> '
         +'</form>'

            }

            // show first
            win.show();
            // then iframe
            Ext.DomHelper.insertFirst(win.body, div)

        }

    }
Ext.define('AIDRFM.common.Header', {
    extend: 'Ext.container.Container',
    alias: 'widget.aidr-header',

    width: '100%',
    html: '<div class="headerWrapper">' +
        '<div class="header"><a href="http://aidr.qcri.org"><img src="' + BASE_URL + '/resources/img/AIDR/aidr_logo_240x90.png"></a>'+'</div>'+
        '</div>'+'<div class="dropdown">'+

  
   ' <button><img src="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACAAAAAgBAMAAACBVGfHAAAAMFBMVEUAAAD///////////////////////////////////////////////////////////87TQQwAAAAD3RSTlMAAQ8TFB4fSV6Ag4Wt6/uDY192AAAAQElEQVQoz2NgIAiEXVCAAsP5/yjgOwMq//8vdIHfDO9RBX4y9KEKXGOgCUCz5SqmOzBciuEXDN9ihAc1wGANZAAX+vf+Xu8gUwAAAABJRU5ErkJggg=="/> </button>'+

  
    '<ul class="dropdown-menu">'+
        '<li><a  onclick="App2.Demo.viewProfile()">View Profile</a></li>'+
        '<li><a  onclick="App1.Demo1.updateProfile()">Update Profile</a></li>'+
        
    '</ul>'+
'</div>' 
 
});
// this.menu=Ext.create('Ext.Button', {
//     text      : 'Menu button',
//     margin: '-1400 700 100 600',
//     renderTo  : Ext.getBody(),
//     arrowAlign: 'bottom',
//     menu      : [
//         {text: 'View Profile'},
//         {text: 'Update Profile'}
        
//     ],
//     listeners: {
//         mouseover: function () {
//             this.showMenu();
//         }
//     }
// });
// <a class="svg-menu" href="#" onclick="App.Demo.openWindow()"><img src="http://f.cl.ly/items/1U2c3b1215383h3a2T2r/icon-menu.svg"/></a>
// <div class="dropdown">

//     <!-- trigger button -->
//     <button>Navigate</button>

//     <!-- dropdown menu -->
//     <ul class="dropdown-menu">
//         <li><a href="#home">Home</a></li>
//         <li><a href="#about">About</a></li>
//         <li><a href="#contact">Contact</a></li>
//     </ul>
// </div>