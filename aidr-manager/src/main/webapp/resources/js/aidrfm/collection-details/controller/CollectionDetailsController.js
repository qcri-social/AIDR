Ext.define('AIDRFM.collection-details.controller.CollectionDetailsController', {
    extend: 'Ext.app.Controller',

    views: [
        'CollectionDetailsPanel'
    ],

    init: function () {
        this.control({

            'collection-details-view': {
                afterrender: this.afterRenderDetailsView
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

            "#collectionCodeInfo": {
                render: function (infoPanel, eOpts) {
                    var tip = Ext.create('Ext.tip.ToolTip', {
                        trackMouse: true,
                        html: ' Collection code consists of alpha-numeric short code name to a collection. ' +
                            'Spaces are not allowed in the code name. For example, Sandy2012, EQJapan2013 are valid code names',
                        target: infoPanel.el,
                        dismissDelay: 0
                    });
                }
            },

            "#collectionkeywordsInfo": {
                render: function (infoPanel, eOpts) {
                    var tip = Ext.create('Ext.tip.ToolTip', {
                        trackMouse: true,
                        html: 'This field represents comma separated keywords to filter the Twitter stream.<br>' +
                            'General rules:<br>' +
                            '- Not case-sensitive ("bridge" matches "Bridge").<br>' +
                            '- Whole words match ("bridge" does not match "damagedbridge").<br><br>' +
                            'Multi-word queries:<br>' +
                            '- If you include two or more words on a query, all of them must be present in the tweet ("Brooklyn bridge" does not match a tweet that does not contain "Brooklyn" or does not contain "bridge")<br>' +
                            '- The words does not need to be consecutive or in that order ("Brooklyn bridge" will match "the bridge to Brooklyn")<br><br>' +
                            'Queries with or without hashtags:<br>' +
                            '- If you don\'t include \'#\', you also match hashtags ("bridge" matches "#bridge")<br>' +
                            '- If you do include \'#\', you only match hashtags ("#bridge" does not match "bridge")<br>',
                        target: infoPanel.el,
                        dismissDelay: 0
                    });
                }
            },

            "#collectionGeoInfo": {
                render: function (infoPanel, eOpts) {
                    var tip = Ext.create('Ext.tip.ToolTip', {
                        trackMouse: true,
                        html: 'This field represents a comma-separated pairs of longitude and latitude. A valid geo location represents a bounding box with southwest corner of the box coming first. Visit <a href="http://boundingbox.klokantech.com/" target="_blank">http://boundingbox.klokantech.com</a> to get a valid bounding box.',
                        target: infoPanel.el,
                        dismissDelay: 0
                    });
                }
            },

            "#collectionFollowInfo": {
                render: function (infoPanel, eOpts) {
                    var tip = Ext.create('Ext.tip.ToolTip', {
                        trackMouse: true,
                        html: "Follow represents a comma-separated list twitter user-ids to be followed. A valid twitter user id must be in the numeric format.",
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

            "#collectionTypeInfo": {
                render: function (infoPanel, eOpts) {
                    var tip = Ext.create('Ext.tip.ToolTip', {
                        trackMouse: true,
                        html: "Collection type.",
                        target: infoPanel.el,
                        dismissDelay: 0
                    });
                }
            },

            "#collectionStart": {
                click: function (btn, e, eOpts) {
                    Ext.getBody().mask('Loading...');

                    Ext.Ajax.request({
                        url: BASE_URL + '/protected/collection/getRunningCollectionStatusByUser.action',
                        method: 'GET',
                        params: {
                            id: OWNER_ID
                        },
                        headers: {
                            'Accept': 'application/json'
                        },
                        success: function (resp) {
                            var response = Ext.decode(resp.responseText);
                            var name = datailsController.DetailsComponent.currentCollection.name;
                            Ext.getBody().unmask();
                            if (response.success) {
                                if (response.data) {
                                    var collectionData = response.data;
                                    var collectionName = collectionData.name;
                                    Ext.MessageBox.confirm('Confirm', 'The collection <b>' + collectionName + '</b> is already running for user <b>' + OWNER_NAME + '</b>. ' +
                                        'Do you want to stop <b>' + collectionName + '</b>  and start <b>' + name + ' </b>?', function (buttonId) {
                                        if (buttonId === 'yes') {
                                            datailsController.startCollection();
                                        }
                                    });
                                } else {
                                    datailsController.startCollection();
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

                }
            },

            "#collectionStop": {
                click: function (btn, e, eOpts) {
                    datailsController.stopCollection();
                }
            },
            
            "#collectionTrash": {
                click: function (btn, e, eOpts) {
                    datailsController.trashCollectionHandler();
                }
            },

            "#collectionUntrash": {
                click: function (btn, e, eOpts) {
                    datailsController.untrashCollection();
                    
                }
            },

            '#collectionUpdate': {
                click: function (btn, e, eOpts) {
                    if (AIDRFMFunctions.mandatoryFieldsEntered()) {
                        datailsController.beforeUpdateCollection();
                    }
                }
            },

            '#collectionEditCancel': {
                click: function (btn, e, eOpts) {
                    datailsController.cancelCollectionEdit();
                }
            },

            "#refreshBtn": {
                click: function (btn, e, eOpts) {
                    var id = datailsController.DetailsComponent.currentCollection.id;
                    this.refreshStatus(id);
                    this.DetailsComponent.collectionLogStore.load();
                }
            },

            "#enableTagger": {
                click: function (btn, e, eOpts) {
                    this.enableTagger();
                }
            },

            "#goToTagger": {
                click: function (btn, e, eOpts) {
                    this.goToTagger();
                }
            },

            "#generateCSVLink": {
                click: function (btn, e, eOpts) {
                    this.generateCSVLink(btn);
                }
            },

            "#generateTweetIdsLink": {
                click: function (btn, e, eOpts) {
                    this.generateTweetIdsLink(btn);
                }
            },
            
            "#generateJSONLink": {
                click: function (btn, e, eOpts) {
                    this.generateJSONLink(btn);
                }
            },

            "#generateJsonTweetIdsLink": {
                click: function (btn, e, eOpts) {
                    this.generateJsonTweetIdsLink(btn);
                }
            },
            
            "#addManager": {
                click: function (btn, e, eOpts) {
                    this.addManager(btn);
                }
            },

            'button[action=removeManager]': {
                click: function (btn, e, eOpts) {
                    this.removeManager(btn);
                }
            },

            "#toMyClassifiersToDownload": {
                click: function (btn, e, eOpts) {
                    this.goToTagger(btn);
                }
            }
        });
    },

    afterRenderDetailsView: function (component, eOpts) {
        AIDRFMFunctions.initMessageContainer();

        this.DetailsComponent = component;
        datailsController = this;
        var me = this;

        Ext.Ajax.request({
            url: BASE_URL + '/protected/tagger/crisis-exists.action',
            method: 'GET',
            params: {
                code: COLLECTION_CODE
            },
            headers: {
                'Accept': 'application/json'
            },
            success: function (response) {
                var resp = Ext.decode(response.responseText);
                var cmp = me.DetailsComponent;
                if (resp.success) {
                    if (resp.data) {
                        console.log('crisis exists', true);
                        cmp.gotoTaggerButton.show();
                        cmp.enableTaggerButton.hide();
                        //cmp.downloadExportTaggerDisabledPanel.hide();
                        //cmp.downloadExportTaggerEnabledPanel.show();
                    }
                } else {
                    console.log('crisis exists', false);
                    cmp.gotoTaggerButton.hide();
                    cmp.enableTaggerButton.hide();
                    //cmp.downloadExportTaggerDisabledPanel.hide();
                    //cmp.downloadExportTaggerEnabledPanel.hide();
                }
            }
        });

        var id = COLLECTION_ID;
        this.DetailsComponent.currentCollectionId = COLLECTION_ID;

        if (!id){
            AIDRFMFunctions.setAlert("Error", ["Collection not specified.", "You will be redirected to Home screen."]);

			Ext.getBody().mask('Redirecting ...');

//            wait for 3 sec to let user read information box
            var isFirst = true;
            Ext.TaskManager.start({
                run: function () {
                    if (!isFirst) {
                        document.location.href = BASE_URL + '/protected/home';
                    }
                    isFirst = false;
                },
                interval: 3 * 1000
            });
        }

        me.na = "<span class='na-text'>N/A</span>";
        me.ns = "<span class='na-text'>Not specified</span>";

        this.loadCollection(id);

        var isFirstRun = true;
        Ext.TaskManager.start({
            run: function () {
                if (!isFirstRun) {
                    me.refreshStatus(id);
                }
                isFirstRun = false;
            },
//            5 minutes
//            interval: 5 * 60 * 1000,

//            15 seconds
            interval: 15 * 1000
        });

    },

    loadCollection: function (id) {
        var me = this;

		Ext.getBody().mask('Loading...');

        Ext.Ajax.request({
            url: BASE_URL + '/protected/collection/findById.action',
            method: 'GET',
            params: {
                id: id
            },
            headers: {
                'Accept': 'application/json'
            },
            success: function (response) {
                var jsonData = Ext.decode(response.responseText);

                me.DetailsComponent.suspendLayout = true;

                OWNER_ID = jsonData.user.id;
                OWNER_NAME = jsonData.user.userName;

                me.updateDetailsPanel(jsonData);
                me.updateEditPanel();
                me.setManagers(jsonData.managers);

                me.DetailsComponent.managersStore.loadData(jsonData.managers);

                me.DetailsComponent.suspendLayout = false;
                me.DetailsComponent.updateLayout();

                Ext.getBody().unmask();
                
                var collectionType = jsonData.collectionType;
                
                if(collectionType === 'SMS'){
                    Ext.getCmp('keywordsPanel').hide();
                    Ext.getCmp('keywords').hide();
                    Ext.getCmp('configurationsL').hide();
                    Ext.getCmp('durationDescription').hide();
                    Ext.getCmp('fetchIntervalPanel').hide();
                    Ext.getCmp('iconPanel').update('<img src="/AIDRFetchManager/resources/img/sms_icon.png"/>');
                    Ext.getCmp('endpointLabel').show();
                } else if(collectionType === 'Twitter'){
                	Ext.getCmp('Language').show();
                	Ext.getCmp('langPanel').show();
                    Ext.getCmp('geoPanel').show();
                    Ext.getCmp('AdvancedConfiguration').show();
                    Ext.getCmp('geoRPanel').show();
                    Ext.getCmp('followPanel').show();
                    Ext.getCmp('geoDescription').show();
                    Ext.getCmp('fetchIntervalPanel').hide();
                    Ext.getCmp('iconPanel').update('<img src="/AIDRFetchManager/resources/img/twitter_icon.png"/>');
                }else if(collectionType === 'Facebook'){
                    Ext.getCmp('iconPanel').update('<img src="/AIDRFetchManager/resources/img/facebook_icon.png"/>');
                }

               Ext.getCmp('downloadLabel').setText('Downloaded ' + COLLECTION_TYPES[collectionType]['plural'] + ' <br/> (since last re-start):',false);
               Ext.getCmp('totalDownloadLabel').setText('Total downloaded ' + COLLECTION_TYPES[collectionType]['plural'] + ':');
               Ext.getCmp('lastDownloadLabel').setText('Last downloaded ' + COLLECTION_TYPES[collectionType]['plural'] + ':');

            },
            failure: function () {
                Ext.getBody().unmask();
            }
        });
    },

    setManagers: function(managers) {
        var result = '';
        Ext.Array.each(managers, function(r, index) {
            result += AIDRFMFunctions.getUserNameWithProviderIcon(r.userName, false) + ', '
        });
        result = result.substring(0, result.length - 2);
       // this.DetailsComponent.managersL.setText(result);
		
		this.DetailsComponent.managersL.setHtml(result);
    },

    updateDetailsPanel: function (r) {
        var p = this.DetailsComponent;
        p.currentCollection = r;
        
        p.breadcrumbs.setHtml('<div class="bread-crumbs"><a href="' + BASE_URL + '/protected/home">My Collections</a><span>&nbsp;>&nbsp;'+r.name+'</span></div>');
        p.collectionTitle.setText('<b>' + r.name + '</b>', false);

        this.setStatus(r.status, r.collectionType);

        this.setStartDate(r.startDate);
        this.setEndDate(r.endDate, r.status);
        this.setWillStoppedDate(r.status, r.startDate, r.durationHours);

        COLLECTION_CODE = r.code;
        p.codeL.setText(r.code);
        p.keywordsL.setText(r.track ? r.track : this.na, false);
        
        if(r.collectionType === 'Facebook'){
        	if(r.fetchInterval > 0){
        		var storeIndex = p.fetchIntervalStore.findExact("val", r.fetchInterval);
            	var fi = "";
                if (storeIndex == -1){
            		if(r.fetchInterval >= 24){
                		fi = Math.floor(r.fetchInterval/24) +" days ";
                	}
                	if(r.fetchInterval % 24 > 0){
                		fi += r.fetchInterval%24 +" hours ";
                	}
                	if(r.fetchInterval > 0){
                		p.fetchIntervalStore.add({
                            "val": r.fetchInterval,
                            "label": fi
                       });
                	}
                }
                else{
                	fi = p.fetchIntervalStore.getAt(storeIndex).data.label;
                }

                p.fetchIntervalL.setText(fi, false);
                p.fetchIntervalContainer.show();
        	}
        }
        
        if (r.geo){
            p.geoL.setText(r.geo, false);
            if (r.geoR === 'approximate') {
                p.geoR.items.items[0].items.items[1].setValue(true);
            }
            else if (r.geoR === 'strict') {
                p.geoR.items.items[0].items.items[2].setValue(true);
            }
            p.geoContainer.show();
        } else {
            p.geoL.setText(this.ns, false);
            p.geoR.items.items[0].items.items[0].setValue(true);
            p.geoContainer.hide();
        }

        if (r.follow){
            p.followL.setText(r.follow, false);
            p.followContainer.show();
        } else {
            p.followL.setText(this.ns, false);
            p.followContainer.hide();
        }
        var languageFull = r.langFilters;
        if(languageFull != ''){
            for(var i=0; i < LANG.length; i++){
                var s = LANG[i];
                var a = s[0] + s[1];
                if(s[1] == languageFull) {
                    languageFull = s[0];
                }
            }
        }

        p.languageFiltersL.setText(languageFull ? languageFull : this.na, false);

        p.createdL.setText(moment(r.createdDate).calendar());

        this.setCountOfDocuments(r.count);
        this.setTotalCountOfDocuments(r.totalCount);
        this.setLastDowloadedDoc(r.lastDocument);
        if(r.hasTaggerOutput) {
        	this.DetailsComponent.gotoTaggerButton.show();
        	this.DetailsComponent.enableTaggerButton.hide();
    	} else {
    		this.DetailsComponent.gotoTaggerButton.hide();
        	this.DetailsComponent.enableTaggerButton.show();
    	}
        
    },
    
    updateTrashedDetailsPanel: function (r) {
        var p = this.DetailsComponent;
        p.currentCollection = r;

        p.collectionTitle.setText('<b>' + r.name + '</b>', false);

        this.setStatus('TRASHED');

        this.setStartDate('');
        this.setEndDate('');
        this.setWillStoppedDate('TRASHED','',0);

        COLLECTION_CODE = r.code;
        p.codeL.setText(r.code);
        p.keywordsL.setText(r.track);

        if (r.geo){
            p.geoL.setText(r.geo, false);
            p.geoContainer.show();
        } else {
            p.geoL.setText(this.ns, false);
            p.geoContainer.hide();
        }

        if(r.collectionType === 'Facebook'){
        	p.fetchIntervalL.setText(r.fetchInterval +"hours", false);
            p.fetchIntervalContainer.show();
        }
        
        p.followL.setText(this.ns, false);
        p.followContainer.hide();
        
        var languageFull = r.langFilters;
        if(languageFull != ''){
            for(var i=0; i < LANG.length; i++){
                var s = LANG[i];
                var a = s[0] + s[1];
                if(s[1] == languageFull) {
                    languageFull = s[0];
                }
            }
        }

        p.languageFiltersL.setText(languageFull ? languageFull : this.ns, false);

        p.createdL.setText('');

        this.setCountOfDocuments(0);
        this.setTotalCountOfDocuments(0);
        this.setLastDowloadedDoc('');
    },

    setLastDowloadedDoc: function(raw) {
        var p = this.DetailsComponent;
        p.lastDocL.setText(raw ? raw : this.na, false);
    },

    updateEditPanel: function () {
        var p = this.DetailsComponent;
        var r = p.currentCollection;

         p.codeE.setValue(r.code);
        p.nameE.setValue(r.name);
        p.keywordsE.setValue(r.track);

        p.geoE.setValue(r.geo ? r.geo : '');
        if (r.geo){
            if (r.geoR === 'approximate') {
                p.geoR.items.items[0].items.items[1].setValue(true);
            }
            else if (r.geoR === 'strict') {
                p.geoR.items.items[0].items.items[2].setValue(true);
            }
        } else {
            p.geoR.items.items[0].items.items[0].setValue(true);
        }
        p.followE.setValue(r.follow ? r.follow : '');
//        default duration is 2 days (48 hours)
        var duration = r.durationHours ? r.durationHours : 48;

        var storeIndex = p.durationStore.findExact("val", duration);
        if (storeIndex == -1){
            p.durationStore.add({
                "val": duration,
                "label": duration / 24 + ' days'
            });
        }
        
        var fetchInterval = r.fetchInterval ? r.fetchInterval : 0;
        p.fetchInterval.setValue(fetchInterval);
        p.duration.setValue(duration);
        p.langCombo.setValue(r.langFilters ? r.langFilters.split(',') : '');
        if(r.crisisType!=null){
        	p.crisisTypesCombo.setValue(r.crisisType.id);
        }
        var collectionForm = Ext.getCmp('collectionForm').getForm();
		collectionForm.findField('collectionType').setValue(r.collectionType);
		
		p.collectionTypeL.setText(r.collectionType);
        if(r.collectionType === 'SMS'){
           Ext.getCmp('iconPanel').update('<img src="/AIDRFetchManager/resources/img/sms_icon.png"/>');
           Ext.getCmp('downloadLabel').setText('Downloaded ' + COLLECTION_TYPES[r.collectionType]['plural'] + ' <br/> (since last re-start):',false);
           Ext.getCmp('totalDownloadLabel').setText('Total downloaded ' + COLLECTION_TYPES[r.collectionType]['plural'] + ':');
           Ext.getCmp('lastDownloadLabel').setText('Last downloaded ' + COLLECTION_TYPES[r.collectionType]['plural'] + ':');
           Ext.getCmp('endpointLabel').show();
        }
    },

    setStatus: function (raw, collectionType) {
        var statusText = AIDRFMFunctions.getStatusWithStyle(raw, collectionType);

        if (raw == 'RUNNING_WARNING' || raw == 'RUNNING' || raw == 'INITIALIZING' || raw == 'WARNING'){
            console.log('Set status ', raw);
            this.DetailsComponent.startButton.hide();
//            this.DetailsComponent.enableTaggerButton.show(); TODO: Fix for https://www.pivotaltracker.com/s/projects/969960/stories/74910442
            this.DetailsComponent.enableTaggerButton.enable();
            this.DetailsComponent.stopButton.show();
            this.DetailsComponent.trashButton.show();
            this.DetailsComponent.untrashButton.hide();
        } else if (raw == 'TRASHED') {
            this.DetailsComponent.startButton.hide();
            this.DetailsComponent.stopButton.hide();
            this.DetailsComponent.enableTaggerButton.hide();
            this.DetailsComponent.enableTaggerButton.disable();
            this.DetailsComponent.trashButton.hide();
            this.DetailsComponent.untrashButton.show();
        } else {
            if (raw == 'NOT_RUNNING') {
                statusText += ' (Click on "Start" to start this collection.)';
            }
            this.DetailsComponent.startButton.show();
            this.DetailsComponent.enableTaggerButton.disable();
            this.DetailsComponent.stopButton.hide();
            this.DetailsComponent.trashButton.show();
            this.DetailsComponent.untrashButton.hide();
        } 
        
        if(raw == 'WARNING')
        	this.DetailsComponent.statusMsgL.setText("Disconnected from Twitter a moment ago, trying to re-connect", false);
        else
        	this.DetailsComponent.statusMsgL.setText("", false);
        
        this.DetailsComponent.statusL.setText(statusText, false);
    },

    setStartDate: function (raw) {
        var me = this;

        this.DetailsComponent.lastStartedL.setText(raw ? moment(raw).calendar() : me.na, false);
    },
    
    setEndDate: function (raw, status) {
        var me = this;
        if (status == "RUNNING" || status == "RUNNING_WARNING" || status == "INITIALIZING" || status == 'WARNING') {
        	this.DetailsComponent.lastStoppedL.setText(raw ? moment(raw).calendar() : 'N/A', false);
        	//this.DetailsComponent.lastStoppedL.setText('Still running', false);
        }
        else {
        	this.DetailsComponent.lastStoppedL.setText(raw ? moment(raw).calendar() : me.na, false);
        }
    },

    setWillStoppedDate: function (status, startDate, duration) {
        var me = this;

        if (status == "RUNNING" || status == "RUNNING_WARNING" || status == "INITIALIZING" || status == 'WARNING'){
            var willEndDate = moment(startDate);
            willEndDate.add('h', duration);

//            round to the next hour
            willEndDate.second(0);
            if (willEndDate.minute() > 0) {
                willEndDate.add('h', 1);
                willEndDate.minute(0);
            }

            willEndDate = moment(willEndDate).calendar();
            this.DetailsComponent.willStoppedL.setText(willEndDate ? willEndDate : me.na, false);
            this.DetailsComponent.willStoppedContainer.show();
        } else {
            this.DetailsComponent.willStoppedContainer.hide();
        }
    },

    setCountOfDocuments: function (raw) {
        this.DetailsComponent.docCountL.setText(raw ? Ext.util.Format.number(raw,'0,000') : 0);
    },

    setTotalCountOfDocuments: function (raw) {
        this.DetailsComponent.totalDocCountL.setText(raw ? Ext.util.Format.number(raw,'0,000') : 0);
    },

    startCollection: function () {
       
		Ext.getBody().mask('Loading...');

        var me = this;
        var id = datailsController.DetailsComponent.currentCollection.id;
        Ext.Ajax.request({
            url: BASE_URL + '/protected/collection/start.action',
            method: 'GET',
            params: {
                id: id
            },
            headers: {
                'Accept': 'application/json'
            },
            success: function (response) {
                Ext.getBody().unmask();
                var resp = Ext.decode(response.responseText);
                if (resp.success) {
                    if (resp.data) {
                        var data = resp.data;
                        me.updateDetailsPanel(data);
                    }

                    var ranOnce = false;
                    var task = Ext.TaskManager.start({
                        run: function () {
                            if (ranOnce) {
                                me.refreshStatus(id);
                                Ext.TaskManager.stop(task);
                            }
                            ranOnce = true;
                        },
                        interval: 5000
                    });
                } else {
                    AIDRFMFunctions.setAlert("Error", resp.message);
                    AIDRFMFunctions.reportIssue(response);
                }
            },
            failure: function () {
                Ext.getBody().unmask();
            }
        });
    },

    stopCollection: function () {
        var me = this;
        var id = datailsController.DetailsComponent.currentCollection.id;

        Ext.getBody().mask('Loading...');

        Ext.Ajax.request({
            url: BASE_URL + '/protected/collection/stop.action',
            method: 'GET',
            params: {
                id: id
            },
            headers: {
                'Accept': 'application/json'
            },
            success: function (response) {
                Ext.getBody().unmask();
                var resp = Ext.decode(response.responseText);
                if (resp.success) {
                    if (resp.data) {
                        var data = resp.data;
                        me.updateDetailsPanel(data);
                    }
                } else {
                    AIDRFMFunctions.setAlert("Error", resp.message);
                    AIDRFMFunctions.reportIssue(response);
                }
            },
            failure: function () {
                Ext.getBody().unmask();
            }
        });
    },

    trashCollectionHandler: function () {
        var me = this;
        Ext.MessageBox.confirm('Confirm Collection Delete', 'Are you sure you want to move this collection to the trash bin?',
            function (buttonId) {
                if (buttonId === 'yes') {
                    me.trashCollection();
                }
            });
    },

    trashCollection: function () {
        var me = this;
        var id = datailsController.DetailsComponent.currentCollection.id;

		Ext.getBody().mask('Loading...');

        Ext.Ajax.request({
            url: BASE_URL + '/protected/collection/trash.action',
            method: 'POST',
            params: {
                id: id
            },
            headers: {
                'Accept': 'application/json'
            },
            success: function (response) {
                Ext.getBody().unmask();
                var resp = Ext.decode(response.responseText);
                if (resp.success) {
                    if (resp.data) {
                        var data = resp.data;
//                        me.updateTrashedDetailsPanel(data);
                        document.location.href = BASE_URL + '/protected/home';
                    }
                } else {
                    AIDRFMFunctions.setAlert("Error", resp.message);
                    AIDRFMFunctions.reportIssue(response);
                }
            },
            failure: function () {
                Ext.getBody().unmask();
            }
        });
    },

    untrashCollection: function () {
        var me = this;
        var id = datailsController.DetailsComponent.currentCollection.id;
        var code = datailsController.DetailsComponent.currentCollection.code;
		
		Ext.getBody().mask('Loading...');

        Ext.Ajax.request({
            url: BASE_URL + '/protected/collection/untrash.action',
            method: 'POST',
            params: {
                id: id,
                code: code
            },
            headers: {
                'Accept': 'application/json'
            },
            success: function (response) {
                Ext.getBody().unmask();
                var resp = Ext.decode(response.responseText);
                if (resp.success) {
                	var statusText = AIDRFMFunctions.getStatusWithStyle("NOT_RUNNING", TYPE);
                	datailsController.DetailsComponent.statusL.setText(statusText, false);
                    me.refreshStatus(id);
                    window.location.reload();
                    history.go(0);
                    window.location.href=window.location.href;
                } else {
                    AIDRFMFunctions.setAlert("Error", resp.message);
                    //AIDRFMFunctions.reportIssue(response);
                }
            },
            failure: function () {
                Ext.getBody().unmask();
            }
        });
    },

    updateCollection: function (running) {
        var me = this;

        var id = datailsController.DetailsComponent.currentCollection.id;
        var status = datailsController.DetailsComponent.currentCollection.status;

        var startDate = null;
        if(datailsController.DetailsComponent.currentCollection.startDate) {
            startDate = Ext.Date.format( new Date(datailsController.DetailsComponent.currentCollection.startDate ), 'Y-m-d' );
        }

        var endDate = null;
        if(datailsController.DetailsComponent.currentCollection.endDate) {
            endDate = Ext.Date.format( new Date(datailsController.DetailsComponent.currentCollection.endDate ), 'Y-m-d' );
        }

        var cmp = Ext.getCmp('collectionForm');
        var form = cmp.getForm();

        var editPanelEl = cmp.up('panel').getEl();
        editPanelEl.mask("Updating...");
        
        var fi = 0;
        
        if(TYPE === 'Facebook'){
        	var fi = form.findField('fetchInterval').getValue();
        }
        
        Ext.Ajax.request({
            url: BASE_URL + '/protected/collection/update',
            method: 'POST',
            params: {
                id: id,
                name: Ext.String.trim( form.findField('name').getValue() ),
                code: Ext.String.trim( form.findField('code').getValue() ),
                track: Ext.String.trim( form.findField('track').getValue() ),
                follow: Ext.String.trim( form.findField('follow').getValue() ),
                geo: Ext.String.trim(  form.findField('geo').getValue() ),
                geoR: Ext.String.trim(  form.findField('geoR').getValue().geoR1 ),
                status: status,
                fromDate: startDate,
                endDate: endDate,
                langFilters: form.findField('langFilters').getValue(),
                durationHours: form.findField('durationHours').getValue(),
                fetchInterval: fi,
                crisisType: form.findField('crisisType').getValue(),
                provider: form.findField('collectionType').getValue()
            },
            headers: {
                'Accept': 'application/json'
            },
            success: function (response) {
                var respObj = Ext.decode(response.responseText);

                if (respObj.success) {

                    me.DetailsComponent.tabPanel.setActiveTab(0);
                    me.loadCollection(id);
                 //   me.DetailsComponent.collectionLogStore.load();
                    if (running) {
                        var ranOnce = false;
                        var task = Ext.TaskManager.start({
                            run: function () {
                                if (ranOnce) {
                                    me.refreshStatus(id);
                                    Ext.TaskManager.stop(task);
                                }
                                ranOnce = true;
                            },
                            interval: 5000
                        });
                    }

                    AIDRFMFunctions.setAlert("Info", ["Collection updated successfully."]);

                } else {
                    AIDRFMFunctions.setAlert("Error", ["An error occurred while updating the collection."]);
                    //AIDRFMFunctions.reportIssue(response);
                }

                editPanelEl.unmask();
            },

            failure: function(response, opts) {
                AIDRFMFunctions.setAlert("Error", ["An error occurred while updating the collection."]);
                AIDRFMFunctions.reportIssue(response);
                editPanelEl.unmask();
            }
        });
    },

    cancelCollectionEdit: function () {
        this.DetailsComponent.tabPanel.setActiveTab(0);
        this.updateEditPanel();
    },

    refreshStatus: function (id) {
        var me = this;

      //  this.DetailsComponent.collectionLogStore.load();
        
        if(!(me.DetailsComponent.statusL.html =="<b class='warningFont'>TRASHED </b>")){
        	Ext.Ajax.request({
                url: BASE_URL + '/protected/collection/refreshCount.action',
                method: 'GET',
                params: {
                    id: id
                },
                headers: {
                    'Accept': 'application/json'
                },
                success: function (response) {
                    var resp = Ext.decode(response.responseText);
                    if (resp.success ) {
                        if (resp.data) {
                            var data = resp.data;

                            me.DetailsComponent.currentCollection.status = data.status;
                            Ext.suspendLayouts();
                            me.setStatus(data.status, data.collectionType);
                            me.setStartDate(data.startDate);
                            me.setEndDate(data.endDate, data.status);
                            me.setWillStoppedDate(data.status, data.startDate, data.durationHours);
                            me.setCountOfDocuments(data.count);
                            me.setTotalCountOfDocuments(data.totalCount);
                            me.setLastDowloadedDoc(data.lastDocument);
                            me.setManagers(data.managers);
                            Ext.resumeLayouts();
                        }
                    } else {
                        AIDRFMFunctions.setAlert("Error", resp.message);
                        //AIDRFMFunctions.reportIssue(response);
                    }
                }
            });
        }
        
    },

    enableTagger: function(view, record, item, index, e, eOpts) {
        var collection = this.DetailsComponent.currentCollection;

        var crisisTypeID = collection.crisisType;
        if (!crisisTypeID) {
            AIDRFMFunctions.setAlert("Error", "Collection type is not selected. Please select type of the collection and save it.");
            return false;
        }

        var code = collection.code;
        var name = collection.name;

        
        Ext.Ajax.request({
            url: BASE_URL + '/protected/collection/classifier/enable',
            method: 'POST',
            params: {
            	collectionCode: Ext.String.trim( code ),
            },
            success: function (response) {
                var resp = Ext.decode(response.responseText);
                if (resp.success) {
                    document.location.href = BASE_URL + '/protected/' + code + '/tagger-collection-details';
                } else {
                    AIDRFMFunctions.setAlert("Error", resp.message);
                    AIDRFMFunctions.reportIssue(response);
                }
            }
        });
    },

    goToTagger: function() {
        document.location.href = BASE_URL + '/protected/' + COLLECTION_CODE +'/tagger-collection-details';
    },

    beforeUpdateCollection: function() {
        var status = this.DetailsComponent.currentCollection.status;
        if (status == 'RUNNING_WARNING' || status == 'RUNNING') {
            this.updateCollection(true);
        } else {
            this.updateCollection(false);
        }
    },

    generateCSVLink: function(btn) {
        var me = this;
        btn.setDisabled(true);
        me.DetailsComponent.CSVLink.setText('<div class="loading-block"></div>', false);
        Ext.Ajax.timeout = 900000;
        Ext.override(Ext.form.Basic, {timeout: Ext.Ajax.timeout/1000});
        Ext.override(Ext.data.proxy.Server, {timeout: Ext.Ajax.timeout});
        Ext.override(Ext.data.Connection, {timeout: Ext.Ajax.timeout});
        Ext.Ajax.request({
            url: BASE_URL + '/protected/collection/generateCSVLink.action',
            timeout: 900000,
            method: 'GET',
            params: {
                code: COLLECTION_CODE
            },
            headers: {
                'Accept': 'application/json'
            },
            success: function (response) {
                btn.setDisabled(false);
                var resp = Ext.decode(response.responseText);
                if (resp.success) {
                    if (resp.data && resp.data != '') {
                        me.DetailsComponent.CSVLink.setText('<div class="styled-text download-link"><a href="' + resp.data + '">' + resp.data + '</a></div>', false);
                    } else {
                        me.DetailsComponent.CSVLink.setText('', false);
                        AIDRFMFunctions.setAlert("Error", "Generate CSV service returned empty url. For further inquiries please contact admin.");
                        AIDRFMFunctions.reportIssue(response);
                    }
                } else {
                    me.DetailsComponent.CSVLink.setText('', false);
                    AIDRFMFunctions.setAlert("Error", resp.message);
                    AIDRFMFunctions.reportIssue(response);
                }
                //Ext.Ajax.timeout = 30000;
                //Ext.override(Ext.form.Basic, {timeout: Ext.Ajax.timeout/1000});
                //Ext.override(Ext.data.proxy.Server, {timeout: Ext.Ajax.timeout});
                //Ext.override(Ext.data.Connection, {timeout: Ext.Ajax.timeout});
            },
            failure: function () {
                btn.setDisabled(false);
                Ext.Ajax.timeout = 30000;
                Ext.override(Ext.form.Basic, {timeout: Ext.Ajax.timeout/1000});
                Ext.override(Ext.data.proxy.Server, {timeout: Ext.Ajax.timeout});
                Ext.override(Ext.data.Connection, {timeout: Ext.Ajax.timeout});
                
            }
        });
    },

    generateTweetIdsLink: function(btn) {
        var me = this;
        btn.setDisabled(true);
        me.DetailsComponent.tweetsIdsLink.setText('<div class="loading-block"></div>', false);

        Ext.Ajax.timeout = 900000;
        Ext.override(Ext.form.Basic, {timeout: Ext.Ajax.timeout/1000});
        Ext.override(Ext.data.proxy.Server, {timeout: Ext.Ajax.timeout});
        Ext.override(Ext.data.Connection, {timeout: Ext.Ajax.timeout});
        Ext.Ajax.request({
            url: BASE_URL + '/protected/collection/generateTweetIdsLink.action',
            timeout: 900000,
            method: 'GET',
            params: {
                code: COLLECTION_CODE
            },
            headers: {
                'Accept': 'application/json'
            },
            success: function (response) {
                btn.setDisabled(false);
                var resp = Ext.decode(response.responseText);
                if (resp.success) {
                    if (resp.data && resp.data != '') {
                        me.DetailsComponent.tweetsIdsLink.setText('<div class="styled-text download-link"><a href="' + resp.data + '">' + resp.data + '</a></div>', false);
                        if (resp.message) {
                     	   AIDRFMFunctions.setAlert("Error", resp.message); 
                        } 
                    } else {
                        me.DetailsComponent.tweetsIdsLink.setText('', false);
                        AIDRFMFunctions.setAlert("Error", "Generate Tweet Ids service returned empty url. For further inquiries please contact admin.");
                        AIDRFMFunctions.reportIssue(response);
                    }
                } else {
                    me.DetailsComponent.tweetsIdsLink.setText('', false);
                    AIDRFMFunctions.setAlert("Error", resp.message);
                    AIDRFMFunctions.reportIssue(response);
                }
                //Ext.Ajax.timeout = 30000;
                //Ext.override(Ext.form.Basic, {timeout: Ext.Ajax.timeout/1000});
                //Ext.override(Ext.data.proxy.Server, {timeout: Ext.Ajax.timeout});
                //Ext.override(Ext.data.Connection, {timeout: Ext.Ajax.timeout});
            },
            failure: function () {
                btn.setDisabled(false);
                Ext.Ajax.timeout = 30000;
                Ext.override(Ext.form.Basic, {timeout: Ext.Ajax.timeout/1000});
                Ext.override(Ext.data.proxy.Server, {timeout: Ext.Ajax.timeout});
                Ext.override(Ext.data.Connection, {timeout: Ext.Ajax.timeout});
            }
        });
    },
    
    generateJSONLink: function(btn) {
        var me = this;
        btn.setDisabled(true);
        me.DetailsComponent.JSONLink.setText('<div class="loading-block"></div>', false);
        Ext.Ajax.timeout = 900000;
        Ext.override(Ext.form.Basic, {timeout: Ext.Ajax.timeout/1000});
        Ext.override(Ext.data.proxy.Server, {timeout: Ext.Ajax.timeout});
        Ext.override(Ext.data.Connection, {timeout: Ext.Ajax.timeout});
        Ext.Ajax.request({
            url: BASE_URL + '/protected/collection/generateJSONLink.action',
            timeout: 900000,
            method: 'GET',
            params: {
                code: COLLECTION_CODE
            },
            headers: {
                'Accept': 'application/json'
            },
            success: function (response) {
                btn.setDisabled(false);
                var resp = Ext.decode(response.responseText);
                if (resp.success) {
                    if (resp.data && resp.data != '') {
                        me.DetailsComponent.JSONLink.setText('<div class="styled-text download-link"><a href="' + resp.data + '">' + resp.data + '</a></div>', false);
                    } else {
                        me.DetailsComponent.JSONLink.setText('', false);
                        AIDRFMFunctions.setAlert("Error", "Generate JSON service returned empty url. For further inquiries please contact admin.");
                        AIDRFMFunctions.reportIssue(response);
                    }
                } else {
                    me.DetailsComponent.JSONLink.setText('', false);
                    AIDRFMFunctions.setAlert("Error", resp.message);
                    AIDRFMFunctions.reportIssue(response);
                }
                //Ext.Ajax.timeout = 30000;
                //Ext.override(Ext.form.Basic, {timeout: Ext.Ajax.timeout/1000});
                //Ext.override(Ext.data.proxy.Server, {timeout: Ext.Ajax.timeout});
                //Ext.override(Ext.data.Connection, {timeout: Ext.Ajax.timeout});
            },
            failure: function () {
                btn.setDisabled(false);
                Ext.Ajax.timeout = 30000;
                Ext.override(Ext.form.Basic, {timeout: Ext.Ajax.timeout/1000});
                Ext.override(Ext.data.proxy.Server, {timeout: Ext.Ajax.timeout});
                Ext.override(Ext.data.Connection, {timeout: Ext.Ajax.timeout});
            }
        });
    },

    generateJsonTweetIdsLink: function(btn) {
        var me = this;
        btn.setDisabled(true);
        me.DetailsComponent.JsonTweetsIdsLink.setText('<div class="loading-block"></div>', false);

        Ext.Ajax.timeout = 900000;
        Ext.override(Ext.form.Basic, {timeout: Ext.Ajax.timeout/1000});
        Ext.override(Ext.data.proxy.Server, {timeout: Ext.Ajax.timeout});
        Ext.override(Ext.data.Connection, {timeout: Ext.Ajax.timeout});
        Ext.Ajax.request({
            url: BASE_URL + '/protected/collection/generateJsonTweetIdsLink.action',
            timeout: 900000,
            method: 'GET',
            params: {
                code: COLLECTION_CODE
            },
            headers: {
                'Accept': 'application/json'
            },
            success: function (response) {
                btn.setDisabled(false);
                var resp = Ext.decode(response.responseText);
                if (resp.success) {
                    if (resp.data && resp.data != '') {
                        me.DetailsComponent.JsonTweetsIdsLink.setText('<div class="styled-text download-link"><a href="' + resp.data + '">' + resp.data + '</a></div>', false);
                        if (resp.message) {
                     	   AIDRFMFunctions.setAlert("Error", resp.message); 
                        } 
                    } else {
                        me.DetailsComponent.JsonTweetsIdsLink.setText('', false);
                        AIDRFMFunctions.setAlert("Error", "Generate JSON Tweet Ids service returned empty url. For further inquiries please contact admin.");
                        AIDRFMFunctions.reportIssue(response);
                    }
                } else {
                    me.DetailsComponent.JsonTweetsIdsLink.setText('', false);
                    AIDRFMFunctions.setAlert("Error", resp.message);
                    AIDRFMFunctions.reportIssue(response);
                }
                //Ext.Ajax.timeout = 30000;
                //Ext.override(Ext.form.Basic, {timeout: Ext.Ajax.timeout/1000});
                //Ext.override(Ext.data.proxy.Server, {timeout: Ext.Ajax.timeout});
                //Ext.override(Ext.data.Connection, {timeout: Ext.Ajax.timeout});
            },
            failure: function () {
                btn.setDisabled(false);
                Ext.Ajax.timeout = 30000;
                Ext.override(Ext.form.Basic, {timeout: Ext.Ajax.timeout/1000});
                Ext.override(Ext.data.proxy.Server, {timeout: Ext.Ajax.timeout});
                Ext.override(Ext.data.Connection, {timeout: Ext.Ajax.timeout});
            }
        });
    },

    addManager: function(btn) {
        var me = this;
        btn.setDisabled(true);
        var userId = me.DetailsComponent.usersCombo.getValue();

        if (typeof userId == 'number') {
            Ext.Ajax.request({
                url: BASE_URL + '/protected/user/addManagerToCollection.action',
                method: 'GET',
                params: {
                    code: COLLECTION_CODE,
                    userId: userId
                },
                headers: {
                    'Accept': 'application/json'
                },
                success: function (response) {
                    btn.setDisabled(false);
                    var resp = Ext.decode(response.responseText);
                    if (resp.success && resp.data) {
                        me.DetailsComponent.managersStore.add(resp.data);
                    } else {
                        AIDRFMFunctions.setAlert("Error", resp.message);
                        AIDRFMFunctions.reportIssue(response);
                    }
                },
                failure: function () {
                    btn.setDisabled(false);
                }
            });
        } else {
            AIDRFMFunctions.setAlert("Error", "Please select user from list");
            btn.setDisabled(false);
        }
    },

    removeManager: function(btn) {
        var me = this;

        btn.setDisabled(true);
        var userId = btn.exampleId;

        Ext.Ajax.request({
            url: BASE_URL + '/protected/user/removeManagerFromCollection.action',
            method: 'GET',
            params: {
                code: COLLECTION_CODE,
                userId: userId
            },
            headers: {
                'Accept': 'application/json'
            },
            success: function (response) {
                btn.setDisabled(false);
                var resp = Ext.decode(response.responseText);
                if (resp.success && resp.data) {
                    me.DetailsComponent.managersStore.loadData(resp.data);
                } else {
                    AIDRFMFunctions.setAlert("Error", resp.message);
                    AIDRFMFunctions.reportIssue(response);
                }
            },
            failure: function () {
                btn.setDisabled(false);
            }
        });
    }

});
