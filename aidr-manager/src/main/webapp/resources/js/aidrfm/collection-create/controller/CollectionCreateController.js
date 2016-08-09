Ext.define('AIDRFM.collection-create.controller.CollectionCreateController', {
    extend: 'Ext.app.Controller',

    views: [
        'CollectionCreatePanel'
    ],

    init: function () {

        this.control({

            'collection-create': {
                afterrender: this.afterRenderCollectionCreateView
            },

            "#collectionNameInfo": {
                render: function (infoPanel, eOpts) {
                    var tip = Ext.create('Ext.tip.ToolTip', {
                        trackMouse: true,
                        html: 'Give a name to your collection. For example, Hurricane Sandy, Earthquake Japan.',
                        target: infoPanel.el,
                        dismissDelay: 0
                    });
                }
            },


            "#collectionkeywordsInfo": {
                render: function (infoPanel, eOpts) {
                	var infoText = 'This field represents comma separated keywords to filter the Twitter stream.<br>' +
                    'General rules:<br>' +
                    '- Not case-sensitive ("bridge" matches "Bridge").<br>' +
                    '- Whole words match ("bridge" does not match "damagedbridge").<br><br>' +
                    'Multi-word queries:<br>' +
                    '- If you include two or more words on a query, all of them must be present in the tweet ("Brooklyn bridge" does not match a tweet that does not contain "Brooklyn" or does not contain "bridge")<br>' +
                    '- The words does not need to be consecutive or in that order ("Brooklyn bridge" will match "the bridge to Brooklyn")<br><br>' +
                    'Queries with or without hashtags:<br>' +
                    '- If you don\'t include \'#\', you also match hashtags ("bridge" matches "#bridge")<br>' +
                    '- If you do include \'#\', you only match hashtags ("#bridge" does not match "bridge")<br>';

                	if(SIGNED_IN_PROVIDER === 'facebook'){
                		infoText = 'This field represents comma separated keywords to filter the Facebook page/group/event.<br>' +
                        'General rules:<br>' +
                        '- No hashTag search allowed.<br>' +
                        '- Not case-sensitive ("bridge" matches "Bridge").<br>' +
                        '- If you include two or more keywords in a query, all of them must be present in the page/group/event ("Brooklyn,bridge" does not match a page/group/event that does not contain "Brooklyn" or does not contain "bridge")<br><br>' +
                        'Multi-word queries:<br>' +
                        '- If you include two or more words on a query, all of them must be present in the page/group/event ("Brooklyn bridge" does not match a page/group/event that does not contain "Brooklyn" or does not contain "bridge")<br>' +
                        '- The words does not need to be consecutive or in that order ("Brooklyn bridge" will match "the bridge to Brooklyn")<br>';
                	}

                    var tip = Ext.create('Ext.tip.ToolTip', {
                        trackMouse: true,
                        html: infoText,
                        target: infoPanel.el,
                        dismissDelay: 0
                    });
                }
            },

            "#collectionGeoInfo": {
                render: function (infoPanel, eOpts) {
                    var tip = Ext.create('Ext.tip.ToolTip', {
                        trackMouse: true,
                        html: 'This field represents a comma-separated pairs of longitude and latitude. A valid geo location represents a bounding box with southwest corner of the box coming first. Note that if you specify a geographical region, all messages posted from within that region will be collected, independently of whether they contain the keywords or not.',
                        target: infoPanel.el,
                        dismissDelay: 0
                    });
                }
            },

            "#collectionFollowInfo": {
                render: function (infoPanel, eOpts) {
                    var tip = Ext.create('Ext.tip.ToolTip', {
                        trackMouse: true,
                        html: "Include all tweets from this list of Twitter users. Separate multiple users by commas. Do not include the @-sign. Alternatively, you can also use numerical user-ids",
                        target: infoPanel.el,
                        dismissDelay: 0
                    });
                }
            },

            "#collectionDurationInfo": {
                render: function (infoPanel, eOpts) {
                    var tip = Ext.create('Ext.tip.ToolTip', {
                        trackMouse: true,
                        html: "Collection duration specifies the length in days after which the collection will be automatically stopped. An increase in duration up to 30days can be requested from AIDR admin.",
                        target: infoPanel.el,
                        dismissDelay: 0
                    });
                }
            },

            "#fetchIntervalInfo": {
                render: function (infoPanel, eOpts) {
                    var tip = Ext.create('Ext.tip.ToolTip', {
                        trackMouse: true,
                        html: "Collection fetch interval specifies the duration after which collection will fetch new data.",
                        target: infoPanel.el,
                        dismissDelay: 0
                    });
                }
            },

			"#fetchFromInfo": {
                render: function (infoPanel, eOpts) {
                    var tip = Ext.create('Ext.tip.ToolTip', {
                        trackMouse: true,
                        html: "Collection fetch from specifies that from how long you want to collect data.",
                        target: infoPanel.el,
                        dismissDelay: 0
                    });
                }
            },

            "#collectionLangInfo": {
                render: function (infoPanel, eOpts) {
                    var tip = Ext.create('Ext.tip.ToolTip', {
                        trackMouse: true,
                        html: "This field is used to set a comma separated list of language codes to filter results only to the specified languages. The language codes must be a valid BCP 47 language identifier. Language filter is not a mandatory field, but it is strongly recommended if you intend to use the automatic tagger.",
                        target: infoPanel.el,
                        dismissDelay: 0
                    });
                }
            },

            "#collectionTypeInfo": {
                render: function (infoPanel, eOpts) {
                    var tip = Ext.create('Ext.tip.ToolTip', {
                        trackMouse: true,
                        html: "Collection Type.",
                        target: infoPanel.el,
                        dismissDelay: 0
                    });
                }
            },

            "#crisisTypesInfo": {
                render: function (infoPanel, eOpts) {
                    var tip = Ext.create('Ext.tip.ToolTip', {
                        trackMouse: true,
                        html: "Collection type specifies a type of the crisis.",
                        target: infoPanel.el,
                        dismissDelay: 0
                    });
                }
            },

            "#keywordsComboInfo": {
                render: function (infoPanel, eOpts) {
                    var tip = Ext.create('Ext.tip.ToolTip', {
                        trackMouse: true,
                        html: "Collection type specifies a type of the crisis.",
                        target: infoPanel.el,
                        dismissDelay: 0
                    });
                }
            },

            "#CollectionLang":{
                change: function(field, newValue, oldValue){
                     var selectedValue = newValue;
                     if(field.value.length == 0){
                         this.CollectionCreateComponent.langNote.setText('<span class="redInfo">*</span> Note: if you want to use the automatic tagger, it is best that you collect for a specific language.', false);
                         this.CollectionCreateComponent.langNote.show();
                     }
                     else if(field.value.length > 1){
                         this.CollectionCreateComponent.langNote.setText('<span class="redInfo">*</span> Note: if you want to use the automatic tagger, it is best that you collect for one specific language.', false);
                         this.CollectionCreateComponent.langNote.show();
                     }
                    else{
                         this.CollectionCreateComponent.langNote.setText('');
                         this.CollectionCreateComponent.langNote.hide();
                     }

                }
            },

            "#collectionCancelCreate": {
                click: function (btn, e, eOpts) {
                    document.location.href = BASE_URL + '/protected/home';
                }
            },

            "#collectionCreate": {
                click: function (btn, e, eOpts) {
                    CollectionCreateController.initNameAndCodeValidation();
                }
            },

            "#nameTextField": {
                blur: function (field, eOpts) {
                    CollectionCreateController.generateCollectionCode(field.getValue());
                }
            },

            "#addProfile": {
                click: function (btn, e, eOpts) {
                    CollectionCreateController.addProfiles();
                }
            },

            "#CollectionType":{
				render: function(field, newValue, oldValue){
					if(SIGNED_IN_PROVIDER == 'facebook'){
						Ext.getCmp('CollectionType').setValue('Facebook');
					}else{
						Ext.getCmp('CollectionType').setValue('Twitter');
					}
				},
                change: function(field, newValue, oldValue){
                     if(newValue === 'SMS'){
                         Ext.getCmp('keywordsPanel').hide();
                         Ext.getCmp('langPanel').hide();
                         Ext.getCmp('geoPanel').hide();
                         Ext.getCmp('geoRPanel').hide();
                         Ext.getCmp('followPanel').hide();
                         Ext.getCmp('durationDescription').hide();
                         Ext.getCmp('geoDescription').hide();
                         Ext.getCmp('fetchIntervalPanel').hide();
						 Ext.getCmp('fetchFromPanel').hide();
                     } else if(newValue === 'Twitter'){
                         Ext.getCmp('keywordsPanel').show();
                         Ext.getCmp('langPanel').show();
                         Ext.getCmp('geoPanel').show();
                         Ext.getCmp('geoRPanel').show();
                         Ext.getCmp('followPanel').show();
                         Ext.getCmp('durationDescription').show();
                         Ext.getCmp('geoDescription').show();
                         Ext.getCmp('fetchIntervalPanel').hide();
						             Ext.getCmp('fetchFromPanel').hide();
                     }
                     else if(newValue === 'Facebook'){
                         Ext.getCmp('keywordsPanel').show();
                         Ext.getCmp('langPanel').hide();
                         Ext.getCmp('geoPanel').hide();
                         Ext.getCmp('geoRPanel').hide();
                         Ext.getCmp('followPanel').hide();

                         Ext.getCmp('subscriptionPanel').show();
                         Ext.getCmp('subscribeResult').show();
                         Ext.getCmp('fetchIntervalPanel').show();
						             Ext.getCmp('fetchFromPanel').show();
                         Ext.getCmp('durationDescription').show();
                         Ext.getCmp('geoDescription').hide();
                     }
                }
            }
        });
    },

    afterRenderCollectionCreateView: function (component, eOpts) {
        AIDRFMFunctions.initMessageContainer();
        this.CollectionCreateComponent = component;
        CollectionCreateController = this;
    },

    saveCollection: function () {

      var fb_profiles = this.CollectionCreateComponent.profiles;

    	var purpose='Using collection for humanitarian and crisis response purpose only.';

    	Ext.MessageBox.confirm('Confirm', 'Are you going to use this collection for humanitarian and crisis response purposes only?', function (buttonId) {
			if (buttonId === 'yes') {
				checkRunningStatus();
			}
			else{
				var message = 'Please enter your purpose for creating the collection (Max. 1000 chars):';
				Ext.MessageBox.show({
		    	    title: 'Purpose for creating collection',
		    	    msg: message,
		    	    width: 500,
		    	    buttons: Ext.Msg.OKCANCEL,
		    	    multiline: true,
		    	    fn: function(btn, text, cfg){
		    	    	text = Ext.String.trim(text);
		    	    	if (btn == 'ok' && Ext.isEmpty(text)){
		    	    		var newMsg = message+'</br><span style="color:red">Collection purpose is mandatory !!!</span>';
		        	    	Ext.MessageBox.show(Ext.apply({}, {msg:newMsg}, cfg));
		    	    	}
		    	    	else if(btn == 'ok' && text.length>1000){
		    	    		var newMsg = message+'</br><span style="color:red">Collection purpose should not be greater than 1000 characters!!!</span>';
		        	    	Ext.MessageBox.show(Ext.apply({}, {msg:newMsg}, cfg));
		    	    	}
		    	    	else if(btn == 'ok'){
		        	    	purpose = text;
		        	    	checkRunningStatus();
		        	    }
		    	    }
		       	});
		    }
        });


    	function checkRunningStatus(){
    		var form = Ext.getCmp('collectionForm').getForm();
    		if(form.findField('collectionType').getValue() ==='Facebook'){
    			follow = [];
    			for(id in fb_profiles) {
    				var item = fb_profiles[id];
    				follow.push(item);
    			}
	            follow = JSON.stringify(follow);
	            form.findField('follow').setValue(follow);
    		}
    		
	        if (AIDRFMFunctions.mandatoryFieldsEntered()) {
	
	            Ext.getBody().mask('Loading...');
	
	            //Check if some collection already is running for current user
	            Ext.Ajax.request({
	                url: BASE_URL + '/protected/collection/getRunningCollectionStatusByUser.action',
	                method: 'GET',
	                params: {
	                    id: USER_ID
	                },
	                headers: {
	                    'Accept': 'application/json'
	                },
	                success: function (resp) {
	                    var response = Ext.decode(resp.responseText);
	                    var name = form.findField('name').getValue();
	                    Ext.getBody().unmask();
	                    if (response.success) {
	                        if (response.data) {
	                            var collectionData = response.data;
	                            var collectionName = collectionData.name;
	                            Ext.MessageBox.confirm('Confirm', 'The collection <b>' + collectionName + '</b> is already running for user <b>' + USER_NAME + '</b>. ' +
	                                'Do you want to stop <b>' + collectionName + '</b>  and start <b>' + name + ' </b>?', function (buttonId) {
	                                if (buttonId === 'yes') {
	                                    //Create collection and run after creating
	                                    createCollection(true)
	                                } else {
	                                    //Create collection without running
	                                    createCollection(false)
	                                }
	                            });
	                        } else {
	                            createCollection(true)
	                        }
	                    } else {
	                        AIDRFMFunctions.setAlert(
	                            "Error",
	                            ['Error while starting Collection .',
	                                'Please try again later or contact Support']
	                        );
	                        AIDRFMFunctions.reportIssue(resp);
	                    }
	                },
	                failure: function () {
	                    Ext.getBody().unmask();
	
	                }
	            });
	
	            /**
	             * Creates collection
	             * @param shouldRun - decides whether run collection after creating. If true then created collection will be
	             * started after creating.
	             */
	            function createCollection(shouldRun) {
	
	                Ext.getBody().mask('Saving collection ...');
	                var fi =0, ff = 0, follow;
	
	                if(form.findField('collectionType').getValue() ==='Facebook'){
	                	fi = form.findField('fetchInterval').getValue();
						        ff = form.findField('fetchFrom').getValue();
	                }
	
	                Ext.Ajax.request({
	                    url: 'collection/create' + (shouldRun ? '?runAfterCreate=true' : ''),
	                    method: 'POST',
	                    params: {
	                        name: Ext.String.trim( form.findField('name').getValue() ),
	                        code: Ext.String.trim( form.findField('code').getValue() ),
	                        track: Ext.String.trim( form.findField('track').getValue() ),
	                        follow: Ext.String.trim( form.findField('follow').getValue() ),
	                        geo: Ext.String.trim( form.findField('geo').getValue() ),
	                        geoR: Ext.String.trim(  form.findField('geoR').getValue().geoR1 ),
	                        langFilters: form.findField('langFilters').getValue(),
	                        fetchInterval: fi,
							fetchFrom: ff,
	                        durationHours: form.findField('durationHours').getValue(),
	                        crisisType: form.findField('crisisType').getValue(),
	                        provider: form.findField('collectionType').getValue(),
	                        purpose: Ext.String.trim(purpose)
	                    },
	                    headers: {
	                        'Accept': 'application/json'
	                    },
	                    success: function (resp) {
	                        Ext.getBody().unmask();
	                        var response = Ext.decode(resp.responseText);
	                        if (response.success) {
	                            AIDRFMFunctions.setAlert("Info", ["Collection created successfully.", "You will be redirected to the collection details page."]);
	                            Ext.getBody().mask('Redirecting ...');
	
	                            //wait for 3 sec to let user read information box
	                            var isFirstRun = true;
	                            Ext.TaskManager.start({
	                                run: function () {
	                                    if (!isFirstRun) {
	                                        document.location.href = BASE_URL + '/protected/'+ form.findField('code').getValue() +'/collection-details';
	                                    }
	                                    isFirstRun = false;
	                                },
	                                interval: 3 * 1000
	                            });
	                    	} else {
	                        	AIDRFMFunctions.setAlert("Error", ["Error in creating collection.", "Please try again later."]);
	                        }
	
	                    },
	                    failure: function(response) {
	                    	Ext.getBody().unmask();
	                    }
	                });
	            }
	        }
    }
    },

    isExist: function () {
        var me = this;

        var form = Ext.getCmp('collectionForm').getForm();
        var code = form.findField('code');
        Ext.Ajax.request({
            url: 'collection/exist.action',
            method: 'GET',
            params: {
                code: code.getValue()
            },
            headers: {
                'Accept': 'application/json'
            },
            success: function (resp) {
                me.checkCount--;

                var response = Ext.decode(resp.responseText);
                if (response.data) {
                    AIDRFMFunctions.setAlert('Error', 'Collection Code already exist. Please select another code');
                    code.markInvalid("Collection Code already exist. Please select another code");
                } else {
                    if (me.checkCount == 0) {
                        me.saveCollection();
                    }
                }
            }
        });
    },

    isExistName: function () {
        var me = this;

        var form = Ext.getCmp('collectionForm').getForm();
        var name = form.findField('name');
        Ext.Ajax.request({
            url: 'collection/existName.action',
            method: 'GET',
            params: {
                name: name.getValue()
            },
            headers: {
                'Accept': 'application/json'
            },
            success: function (resp) {
                me.checkCount--;

                var response = Ext.decode(resp.responseText);
                if (response.data) {
                    AIDRFMFunctions.setAlert('Error', [
                        'The name of the collection you have selected is already taken. Please enter a more specific name for your collection indicating a more specific time, location, and/or purpose.',
                        '&nbsp;',
                        '<b>Examples:</b>',
                        '&quot;Earthquake in Chile in Feb. 2014&quot;',
                        '&quot;Earthquake in Concepcion, Chile in 2014&quot;',
                        '&quot;Consequences of earthquake in Concepcion, Chile in 2014&quot;'
                    ]);

                    name.markInvalid("Collection Name already exist. Please select another name");
                } else {
                    if (me.checkCount == 0) {
                        me.saveCollection();
                    }
                }
            }
        });
    },

    agreeTOS: function () {
    	var me = this;

    	Ext.MessageBox.confirm('Confirm', 'Are you going to use this collection for humanitarian and crisis response purposes only?', function (buttonId) {
                if (buttonId === 'yes') {
                }
                else{
                	Ext.MessageBox.show({
                	    title: 'Purpose for creating collection',
                	    message: 'Please enter your purpose for creating the collection:',
                	    width: 500,
                	    buttons: Ext.Msg.OKCANCEL,
                	    multiline: true,
                	    fn: function(btn, text){
                    	    if (btn == 'ok'){
                    	        console.log(text);
                    	    }
                    	    else{
                    	    	console.log("cancel");
                    	    }
                    	},
                	});
                }
            });
    },

    addProfiles: function() {
      var profiles = this.CollectionCreateComponent.profiles;
      if(this.CollectionCreateComponent.profilesCombo.valueCollection.items) {
        this.CollectionCreateComponent.profilesCombo.valueCollection.items.forEach(function (item, index) {
          profiles[item.id] = item.data;
        });
        this.CollectionCreateComponent.profilesCombo.setValue(null);
      }
      CollectionCreateController.renderProfiles(this.CollectionCreateComponent.profiles);
    },

    renderProfiles: function(profiles) {
      var temp_html = '<div>';
      for(id in profiles) {
        var item = profiles[id];
        var name = AIDRFMFunctions.applyEllipsis(item.name, 10);
        var likes;
        if(item.fans) {
          likes = item.fans.toLocaleString();
        }
        temp_html+= '<div class="profile-div"><div style="float: left"><img src="'+ item.imageUrl +'"/></div>';
        if(item.type == 'PAGE') {
            temp_html+= '<div style="float: left; padding: 8px"><b>'+ name +'</b><br/>'+ likes +' Likes</div>';
        } else {
            temp_html+= '<div style="float: left; padding: 8px"><b>'+ name +'</b><br/>'+ item.type +'</div>';
        }
        temp_html+= '<div style="float: right;padding: -5px;margin: -5px;color: #3c7ac6"><i class="fa fa-external-link link" onclick="CollectionCreateController.openProfile(\''+ item.link +'\')"></i> <i class="fa fa-times-circle-o link" onclick="CollectionCreateController.removeProfile('+ item.id +')"></i></div>';
        //temp_html+= '<span class="tooltiptext">'+ item.name +'</span></div>';
        temp_html+= '</div>';
      }
      temp_html+= '</div>';
      Ext.getCmp('subscribeResult').update(temp_html);
    },

    removeProfile: function(id) {
      delete this.CollectionCreateComponent.profiles[id];
      CollectionCreateController.renderProfiles(this.CollectionCreateComponent.profiles);
    },

    openProfile: function(link) {
      window.open(link, '_blank');
    },

    initNameAndCodeValidation: function() {
    	this.checkCount = 2;
    	this.isExist();
    	this.isExistName();
    },

    generateCollectionCode: function(value) {
        var me = this;

//        var currentCode = me.CollectionCreateComponent.codeE.getValue();
//        if (currentCode != ''){
//            return false;
//        }

        var v = Ext.util.Format.trim(value);
        v = v.replace(/ /g, '_');
        v = Ext.util.Format.lowercase(v);

        var date = Ext.Date.format(new Date(), "ymdHis");
        date = Ext.util.Format.lowercase(date);

        var length = value.length;


        if(length > 64){
            v =  Ext.util.Format.substr(v, 0, length - date.length ) ;
        }

        var result = date + "_" + v;

        if(result.length > 64){
            return false;
        }
        else{
            me.isExistForGenerated(result);
        }
    },

    isExistForGenerated: function (code, attempt) {
        var me = this;

        Ext.Ajax.request({
            url: 'collection/exist.action',
            method: 'GET',
            params: {
                code: code
            },
            headers: {
                'Accept': 'application/json'
            },
            success: function (response) {
                var response = Ext.decode(response.responseText);
                if (response.data) {
                    if (attempt) {
                        me.modifyGeneratedCode(code, attempt);
                    } else {
                        me.modifyGeneratedCode(code, 0);
                    }
                } else {
                    me.CollectionCreateComponent.codeE.setValue(code);
                }
            }
        });
    },

    modifyGeneratedCode: function(oldCode, attempt) {
        var me = this;

        var date = Ext.util.Format.substr(oldCode, oldCode.length - 7, oldCode.length),
            code = Ext.util.Format.substr(oldCode, 0, oldCode.length - 9);

        var result = code + '_' + attempt + date;
        me.isExistForGenerated(result, attempt + 1);
    }

});
