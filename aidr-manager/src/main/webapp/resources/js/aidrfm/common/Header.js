Ext.onReady(function() {
		Ext.BLANK_IMAGE_URL = 'http://extjs.cachefly.net/ext-3.2.1/resources/images/default/s.gif';

		    Ext.define('User', {
		        extend: 'Ext.data.Model',
		        fields: [
		                 { name: 'userName', type: 'string' },
		                 { name: 'apiKey', type: 'string' },
		                 { name: 'provider', type: 'string' },
		                 { name: 'locale', type: 'string' },
		                 { name: 'email', type: 'string' }
		          ]
		    });

		    Ext.USER = new Ext.data.Store({
		        model: 'User',
		        proxy: {
		            type: 'ajax',
		            url : BASE_URL + '/protected/user/loggedin',
					method : "GET",
		            reader: {
		                type: 'json',
		                root : 'data'
		            }
		        }
		    });
		    Ext.USER.load();
		    
		    UserController.service.applyUserPermissions();

});

Ext.ns('UserController');
UserController.service = {
	init : function() {},

	viewProfileModal : function(url) {
		var win = new Ext.Window({
			width : 330,
			minHeight : 115,
			cls : 'popWindow',
			resizable : false,
			autoScroll : true,
			modal : true,
		});

		var div = '<div id="content1" class="modal-body" >'
				+ '<p class="text-right" id="name"><span><img width="24" height="24" id="twitter" src="'
				+ BASE_URL
				+ '/resources/img/User-Profile-24.png"/> ' + Ext.USER.data.items[0].data.userName + '</span></p>'
				+ ' </div>'
				+ '<div id="content2" class="modal-body" >'
				+ ' <p class="text-right" id="key"><span><img width="24" height="24" id="twitter" src="'
				+ BASE_URL + '/resources/img/key96.png"/>' + Ext.USER.data.items[0].data.apiKey + '</span></p>'
				+ '</div>' + '<div class="modal-body" >'
		// show first
		win.show();

		// then iframe
		Ext.DomHelper.insertFirst(win.body, div)
	},
	
	updateProfileModal : function(url) {
		var win = new Ext.Window({
			width : 370,
			minHeight : 220,
			cls : 'popWindow',
			resizable : false,
			autoScroll : true,
			modal : true
		});

		var div = {
			html : 	'<form id="updateForm">'
				+ '<div id="content1" class="modal-body" >'
				+ '<p class="text-right" id="name"><span><img width="24" height="24" id="twitter" src="'
				+ BASE_URL
				+ '/resources/img/User-Profile-24.png"/>' +Ext.USER.data.items[0].data.userName + '</span></p>'
				+' </div>'
				+ '<div id="content1" class="modal-body" >'
				+ ' <p class="text-right" id="key"><span><img width="24" height="24" id="twitter" src="'
				+ BASE_URL
				+ '/resources/img/key96.png"/>' + Ext.USER.data.items[0].data.apiKey + '</span></p>'
				+ '</div>'
				+ '<div id="content2" class="modal-body" >'

				+ ' <p class="text-right" id="name"><span><img width="24" height="24" id="twitter" src="'
				+ BASE_URL
				+ '/resources/img/email5.png"/> <input class="inputForm" type="text" name="email"></span></p>'
				+ ' <p class="text-right" id="name"><span><img width="24" height="24" id="twitter" src="'
				+ BASE_URL
				+ '/resources/img/character.png"/> <input class="inputForm" type="text" name="locale"></span></p>'
				+ '<input class="btn btn-rddish1" type="button" type="reset" value="Cancel"><input class="btn btn-bluish1" onclick = "UserController.service.updateUser()" type="button" value="Save"> '
				+ '</form>'
		}

		// show first
		win.show();
		// then iframe
		Ext.DomHelper.insertFirst(win.body, div)
	},
	
    updateUser: function () {

		var form = Ext.getCmp('updateForm').getForm();
        var userName = Ext.USER.data.items[0].data.userName;
        var email = form.findField('email').getValue();
        var locale = form.findField('locale').getValue();
        var mask = AIDRFMFunctions.getMask();
        mask.show();

        //Check if some collection already is running for current user
        Ext.Ajax.request({
            url: BASE_URL + '/protected/user/update',
            method: 'POST',
            params: {
                userName: userName,
                email: email,
                locale: locale
            },
            headers: {
                'Accept': 'application/json'
            },
            success: function (resp) {
            },
            failure: function (resp) {
                mask.hide();
            }
        });
    },
    
    applyUserPermissions: function () {
        var me = this;

        Ext.Ajax.request({
            url:  BASE_URL + '/protected/user/getCurrentUserRoles.action',
            method: 'GET',
            headers: {
                'Accept': 'application/json'
            },
            success: function (resp) {
                var response = Ext.decode(resp.responseText);
                if (response.data) {
                    var roles = response.data;
                    if (Ext.isArray(roles)) {
                        Ext.each(roles, function (role) {
                            if (role && role == 'ADMIN'){
                                Ext.get('adminButton').show();
                            }
                        })
                    }
                } else {
                    AIDRFMFunctions.setAlert('Error', 'Collection Code already exist. Please select another code');
                    AIDRFMFunctions.reportIssue(resp);
                }
            }
        });
    },
    
    goToAdminSection: function() {
    	document.location.href = BASE_URL + '/protected/administration/admin-console';
    }
}

Ext.define('AIDRFM.common.Header', {
					extend : 'Ext.container.Container',
					alias : 'widget.aidr-header',
					id : 'header',
					width : '100%',
		            /*items: [
		                    {
		                    	xtype: 'container',
		                    	cls: 'header',
		                    	items:[
										{
											xtype: 'component',
										    autoEl: {
										        tag: 'a',
										        href: 'http://aidr.qcri.org',
										        html: '<img src="' + BASE_URL
													+ '/resources/img/AIDR/aidr_logo_240x90.png">'
										    }
										},
										{
											xtype: 'container',
											cls: 'dropdown',
											items: [
											        {
											        	xtype: 'button',
											        	html: '<img src="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACAAAAAgBAMAAACBVGfHAAAAMFBMVEUAAAD///////////////////////////////////////////////////////////87TQQwAAAAD3RSTlMAAQ8TFB4fSV6Ag4Wt6/uDY192AAAAQElEQVQoz2NgIAiEXVCAAsP5/yjgOwMq//8vdIHfDO9RBX4y9KEKXGOgCUCz5SqmOzBciuEXDN9ihAc1wGANZAAX+vf+Xu8gUwAAAABJRU5ErkJggg=="/>',
											        	cls: 'dropdown-button'
											        },
											        {
											        	xtype: 'box',
											        	id: 'menu',
											            html: '<ul class="dropdown-menu"> ' 
											            		+ '<li><a onclick="UserController.service.viewProfileModal()">VIEW PROFILE</a></li>'
																+ '<li><a onclick="UserController.service.updateProfileModal()">UPDATE PROFILE</a></li>'
																+ '<li id="adminButton" hidden="true"><a onclick="goToAdmin()">ADMIN CONSOLE</a></li>'+
																+ '</ul>',
											        }
											        ]
										}
		                    	       ]
									}]
		                    ,*/
		                   
					html : '<div class="headerWrapper">'
							+ '<div class="header"><a href="http://aidr.qcri.org"><img src="'
							+ BASE_URL
							+ '/resources/img/AIDR/aidr_logo_240x90.png"></a>'
							+ '<div class="dropdown">'
							+ ' <button><img src="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACAAAAAgBAMAAACBVGfHAAAAMFBMVEUAAAD///////////////////////////////////////////////////////////87TQQwAAAAD3RSTlMAAQ8TFB4fSV6Ag4Wt6/uDY192AAAAQElEQVQoz2NgIAiEXVCAAsP5/yjgOwMq//8vdIHfDO9RBX4y9KEKXGOgCUCz5SqmOzBciuEXDN9ihAc1wGANZAAX+vf+Xu8gUwAAAABJRU5ErkJggg=="/> </button>'
							+ '<ul class="dropdown-menu">'
							+ '<li><span onclick="UserController.service.viewProfileModal()">VIEW PROFILE</span></li>'
							+ '<li><span onclick="UserController.service.updateProfileModal()">UPDATE PROFILE</span></li>'
							+ '<li id="adminButton" hidden="true"><span onclick="UserController.service.goToAdminSection()">ADMIN CONSOLE</span></li></ul></div></div></div>'
				});
