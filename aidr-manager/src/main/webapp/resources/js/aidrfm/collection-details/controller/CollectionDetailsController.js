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
                            'Multi-word queries<br>' +
                            '- If you include two or more words on a query, all of them must be present in the tweet ("Brooklin bridge" does not match a tweet that does not contain "Brooklin" or does not contain "bridge")<br>' +
                            '- The words does not need to be consecutive or in that order ("Brooklin bridge" will match "the bridge to Brooklin")<br><br>' +
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

            "#collectionStart": {
                click: function (btn, e, eOpts) {
                    var mask = AIDRFMFunctions.getMask();
                    mask.show();

                    Ext.Ajax.request({
                        url: BASE_URL + '/protected/collection/getRunningCollectionStatusByUser.action',
                        method: 'GET',
                        headers: {
                            'Accept': 'application/json'
                        },
                        success: function (resp) {
                            var response = Ext.decode(resp.responseText);
                            var name = datailsController.DetailsComponent.currentCollection.name;
                            mask.hide();
                            if (response.success) {
                                if (response.data) {
                                    var collectionData = response.data;
                                    var collectionName = collectionData.name;
                                    Ext.MessageBox.confirm('Confirm', 'The collection <b>' + collectionName + '</b> is already running. ' +
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
                            }
                        },
                        failure: function () {
                            mask.hide();
                        }
                    });

                }
            },

            "#collectionStop": {
                click: function (btn, e, eOpts) {
                    datailsController.stopCollection();
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
                }
            },

            "#enableTagger": {
                click: function (btn, e, eOpts) {
                    this.getAllCrisisTypesFromTagger();
                }
            },

            "#goToTagger": {
                click: function (btn, e, eOpts) {
                    this.goToTagger();
                }
            },

            "#crisesTypeViewId": {
                itemclick: function (view, record, item, index, e, eOpts) {
                    this.crisisTypeSelectHandler(view, record, item, index, e, eOpts);
                }
            },

            "#crisesTypeWin": {
                hide: function (btn, e, eOpts) {
                    AIDRFMFunctions.getMask().hide();
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

            "#addManager": {
                click: function (btn, e, eOpts) {
                    this.addManager(btn);
                }
            },

            'button[action=removeManager]': {
                    click: function (btn, e, eOpts) {
                    alert("Will be implemented soon");
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
                if (resp.success) {
                    if (resp.data) {
                        me.DetailsComponent.gotoTaggerButton.show();
                        me.DetailsComponent.enableTaggerButton.hide();
                    }
                } else {
                    me.DetailsComponent.gotoTaggerButton.hide();
                    me.DetailsComponent.enableTaggerButton.hide();
                }
            }
        });

        var id = COLLECTION_ID;
        this.DetailsComponent.currentCollectionId = COLLECTION_ID;

        if (!id){
            AIDRFMFunctions.setAlert("Error", ["Collection not specified.", "You will be redirected to Home screen."]);

            var maskRedirect = AIDRFMFunctions.getMask(true, 'Redirecting ...');
            maskRedirect.show();

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
            interval: 5 * 60 * 1000
        });

    },

    loadCollection: function (id) {
        var me = this;

        var mask = AIDRFMFunctions.getMask(true);
        mask.show();

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

                me.updateDetailsPanel(jsonData);
                me.updateDownloadPanel(jsonData.code);
                me.updateEditPanel();
                me.setManagers(jsonData.managers);
                me.applyOwnerPermission(jsonData.user);

                me.DetailsComponent.managersStore.loadData(jsonData.managers
                );

                me.DetailsComponent.suspendLayout = false;
                me.DetailsComponent.forceComponentLayout();

                mask.hide();
            },
            failure: function () {
                mask.hide();
            }
        });
    },

    applyOwnerPermission: function(collectionOwner) {
        if (collectionOwner.userName == USER_NAME){
            this.DetailsComponent.addManagersPanel.show();
        } else {
            this.DetailsComponent.messageForNotOwner.show();
        }
    },

    setManagers: function(managers) {
        var result = '';
        Ext.Array.each(managers, function(r, index) {
            result += r.userName + ', '
        });
        result = result.substring(0, result.length - 2);
        this.DetailsComponent.managersL.setText(result);
    },

    updateDetailsPanel: function (r) {
        var p = this.DetailsComponent;
        p.currentCollection = r;

        p.collectionTitle.setText('<b>' + r.name + '</b>', false);

        this.setStatus(r.status);
        this.setStartDate(r.startDate);
        this.setEndDate(r.endDate);

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

        if (r.follow){
            p.followL.setText(r.follow, false);
            p.followContainer.show();
        } else {
            p.followL.setText(this.ns, false);
            p.followContainer.hide();
        }

        p.languageFiltersL.setText(r.langFilters ? r.langFilters : this.ns, false);

        p.createdL.setText(r.createdDate);
        this.setCountOfDocuments(r.count);
        this.setTotalCountOfDocuments(r.totalCount);
        this.setLastDowloadedDoc(r.lastDocument);
    },

    updateDownloadPanel: function (code) {
        var downloadTabText = '<div class="styled-text">You can read the collected tweets from:<br><br>' +
            '<b>1.</b>&nbsp;&nbsp;Tweets in JSON format: <a href="http://scd1.qcri.org/aidr/data/persister/' + code + '">http://scd1.qcri.org/aidr/data/persister/' + code + '/</a><br>' +
            '<b>2.</b>&nbsp;&nbsp;Redis queue FetcherChannel.' + code + ' on host scd1.qcri.org port 6379<br></div>';

        this.DetailsComponent.downloadText.setText(downloadTabText, false);
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
        p.followE.setValue(r.follow ? r.follow : '');
        p.langCombo.setValue(r.langFilters ? r.langFilters.split(',') : '');
    },

    setStatus: function (raw) {
        var statusText = AIDRFMFunctions.getStatusWithStyle(raw);

        if (raw == 'RUNNING-WARNNING' || raw == 'RUNNING' || raw == 'INITIALIZING'){
            this.DetailsComponent.startButton.hide();
            this.DetailsComponent.enableTaggerButton.enable();
            this.DetailsComponent.stopButton.show();
        } else {
            this.DetailsComponent.startButton.show();
            this.DetailsComponent.enableTaggerButton.disable();
            this.DetailsComponent.stopButton.hide();
        } 
        
        this.DetailsComponent.statusL.setText(statusText, false);
    },

    setStartDate: function (raw) {
        var me = this;
        this.DetailsComponent.lastStartedL.setText(raw ? raw : me.na, false);
    },

    setEndDate: function (raw) {
        var me = this;
        this.DetailsComponent.lastStoppedL.setText(raw ? raw : me.na, false);
    },

    setCountOfDocuments: function (raw) {
        this.DetailsComponent.docCountL.setText(raw ? Ext.util.Format.number(raw,'0,000') : 0);
    },

    setTotalCountOfDocuments: function (raw) {
        this.DetailsComponent.totalDocCountL.setText(raw ? Ext.util.Format.number(raw,'0,000') : 0);
    },

    startCollection: function () {
        var mask = AIDRFMFunctions.getMask();
        mask.show();

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
                mask.hide();
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
                }
            },
            failure: function () {
                mask.hide();
            }
        });
    },

    stopCollection: function () {
        var me = this;
        var id = datailsController.DetailsComponent.currentCollection.id;

        var mask = AIDRFMFunctions.getMask();
        mask.show();

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
                mask.hide();
                var resp = Ext.decode(response.responseText);
                if (resp.success) {
                    if (resp.data) {
                        var data = resp.data;
                        me.updateDetailsPanel(data);
                    }
                } else {
                    AIDRFMFunctions.setAlert("Error", resp.message);
                }
            },
            failure: function () {
                mask.hide();
            }
        });
    },

    updateCollection: function (running) {
        var me = this;

        var id = datailsController.DetailsComponent.currentCollection.id;
        var status = datailsController.DetailsComponent.currentCollection.status;
        var startDate = datailsController.DetailsComponent.currentCollection.startDate;
        var endDate = datailsController.DetailsComponent.currentCollection.endDate;

        var form = Ext.getCmp('collectionForm').getForm();
        Ext.Ajax.request({
            url: BASE_URL + '/protected/collection/update.action',
            method: 'POST',
            params: {
                id: id,
                name: Ext.String.trim( form.findField('name').getValue() ),
                code: Ext.String.trim( form.findField('code').getValue() ),
                track: Ext.String.trim( form.findField('track').getValue() ),
                follow: Ext.String.trim( form.findField('follow').getValue() ),
                geo: Ext.String.trim(  form.findField('geo').getValue() ),
                status: status,
                fromDate: startDate,
                endDate: endDate,
                langFilters: form.findField('langFilters').getValue()
            },
            headers: {
                'Accept': 'application/json'
            },
            success: function (response) {
                me.DetailsComponent.tabPanel.setActiveTab(0);
                me.loadCollection(id);
                me.DetailsComponent.collectionLogStore.load();
                if (running){
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
            }
        });
    },

    cancelCollectionEdit: function () {
        this.DetailsComponent.tabPanel.setActiveTab(0);
        this.updateEditPanel();
    },

    refreshStatus: function (id) {
        var me = this;

        this.DetailsComponent.collectionLogStore.load();
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
                        me.setStatus(data.status);
                        me.setStartDate(data.startDate);
                        me.setEndDate(data.endDate);
                        me.setCountOfDocuments(data.count);
                        me.setTotalCountOfDocuments(data.totalCount);
                        me.setLastDowloadedDoc(data.lastDocument);
                    }
                } else {
                    AIDRFMFunctions.setAlert("Error", resp.message);
                }
            }
        });
    },

    getAllCrisisTypesFromTagger: function() {
        var me = this,
            mask = AIDRFMFunctions.getMask();

        mask.show();

        Ext.Ajax.request({
            url: BASE_URL + '/protected/tagger/getAllCrisisTypes.action',
            method: 'GET',
            headers: {
                'Accept': 'application/json'
            },
            success: function (response) {
                var resp = Ext.decode(response.responseText);
                if (resp.success && resp.data && resp.data.length > 0) {
                    var count = resp.data.length;
                    if (count > 0) {
                        me.DetailsComponent.crisesTypeStore.loadData(resp.data);
                        me.DetailsComponent.crisesTypeWin.show();
                    } else {
                        AIDRFMFunctions.setAlert("Error", "Crises types list received from Tagger is empty");
                    }
                } else {
                    mask.hide();
                    AIDRFMFunctions.setAlert("Error", resp.message);
                }
            },
            failure: function () {
                mask.hide();
            }
        });
    },

    crisisTypeSelectHandler: function(view, record, item, index, e, eOpts) {
        var me = this,
            collection = this.DetailsComponent.currentCollection,
            code = collection.code,
            name = collection.name,
            crisisTypeID = record.data.crisisTypeID;

        Ext.Ajax.request({
            url: BASE_URL + '/protected/tagger/createCrises.action',
            method: 'POST',
            params: {
                code: Ext.String.trim( code ),
                name: Ext.String.trim( name ),
                crisisTypeID: crisisTypeID
            },
            headers: {
                'Accept': 'application/json'
            },
            success: function (response) {
                var resp = Ext.decode(response.responseText);
                if (resp.success) {
                    document.location.href = BASE_URL + '/protected/tagger-home';
                } else {
                    AIDRFMFunctions.setAlert("Error", resp.message);
                }
            }
        });
    },

    goToTagger: function() {
        document.location.href = BASE_URL + '/protected/' + COLLECTION_CODE +'/tagger-collection-details';
    },

    beforeUpdateCollection: function() {
        var status = this.DetailsComponent.currentCollection.status;
        if (status == 'RUNNING-WARNNING' || status == 'RUNNING') {
            this.updateCollection(true);
        } else {
            this.updateCollection(false);
        }
    },

    generateCSVLink: function(btn) {
        var me = this;
        btn.setDisabled(true);
        me.DetailsComponent.CSVLink.setText('<div class="loading-block"></div>', false);

        Ext.Ajax.request({
            url: BASE_URL + '/protected/collection/generateCSVLink.action',
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
                    }
                } else {
                    me.DetailsComponent.CSVLink.setText('', false);
                    AIDRFMFunctions.setAlert("Error", resp.message);
                }
            },
            failure: function () {
                btn.setDisabled(false);
            }
        });
    },

    generateTweetIdsLink: function(btn) {
        var me = this;
        btn.setDisabled(true);
        me.DetailsComponent.tweetsIdsLink.setText('<div class="loading-block"></div>', false);

        Ext.Ajax.request({
            url: BASE_URL + '/protected/collection/generateTweetIdsLink.action',
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
                    } else {
                        me.DetailsComponent.tweetsIdsLink.setText('', false);
                        AIDRFMFunctions.setAlert("Error", "Generate Tweet Ids service returned empty url. For further inquiries please contact admin.");
                    }
                } else {
                    me.DetailsComponent.tweetsIdsLink.setText('', false);
                    AIDRFMFunctions.setAlert("Error", resp.message);
                }
            },
            failure: function () {
                btn.setDisabled(false);
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
                    }
                },
                failure: function () {
                    btn.setDisabled(false);
                }
            });
        } else {
            AIDRFMFunctions.setAlert("Info", "Please select user from list");
            btn.setDisabled(false);
        }
    }

});