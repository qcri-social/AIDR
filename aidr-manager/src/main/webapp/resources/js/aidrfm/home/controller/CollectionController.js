Ext.define('AIDRFM.home.controller.CollectionController', {
    extend: 'Ext.app.Controller',

    views: [
//        'CollectionPanel'
        'NewCollectionPanel'
    ],

    init: function () {

        this.control({

//            'collection-view': {
            'collection-view-new': {
                beforerender: this.beforeRenderView
            },

            "#newCollection": {
                click: function (btn, e, eOpts) {
                    document.location.href = BASE_URL + '/protected/collection-create';
                }
            },

            "#refreshBtn": {
                click: function (btn, e, eOpts) {
                    collectionController.refreshButtonAction();
                }
            },

            "#manageCrisis": {
                click: function (btn, e, eOpts) {
                    document.location.href = BASE_URL + '/protected/home';
                }
            },

            "#goToAdminSection": {
                click: function (btn, e, eOpts) {
                    document.location.href = BASE_URL + '/protected/administration/admin-console';
                }
            }

        });

    },

    beforeRenderView: function (component, eOpts) {
        AIDRFMFunctions.initMessageContainer();

        this.mainComponent = component;
        collectionController = this;

        this.applyUserPermissions();

        var me = this;

        var isFirstRun = true;
        Ext.TaskManager.start({
            run: function () {
                if (!isFirstRun) {
                    me.refreshButtonAction();
                }
                isFirstRun = false;
            },
//            5 minutes
            interval: 5 * 60 * 1000
        });
    },

    startCollectionCheck: function(id, name, ownerName, ownerId) {
        var me = this;

        var mask = AIDRFMFunctions.getMask(true, 'Starting collection ...');
        mask.show();

        Ext.Ajax.request({
            url: 'protected/collection/getRunningCollectionStatusByUser.action',
            method: 'GET',
            params: {
                id: ownerId
            },
            headers: {
                'Accept': 'application/json'
            },
            success: function (resp) {
                var response = Ext.decode(resp.responseText);
                mask.hide();
                if (response.success) {
                    if (response.data) {
                        var collectionData = response.data;
                        var collectionName = collectionData.name;
                        Ext.MessageBox.confirm('Confirm', 'The collection <b>' + collectionName + '</b> is already running for user <b>' + ownerName + '</b>. ' +
                            'Do you want to stop <b>' + collectionName + '</b>  and start <b>' + name + ' </b>?', function (buttonId) {
                            if (buttonId === 'yes') {
                                me.refreshButtonAction();
                                me.startCollection(id);
                            }
                        });
                    } else {
                        me.startCollection(id);
                    }
                } else {
                    AIDRFMFunctions.setAlert(
                        "Error",
                        ['Error while starting Collection .',
                            'Please try again later or contact Support']
                    );
                    AIDRFMFunctions.reportIssue(resp);
                }
            }
        });

    },

    stopCollection: function(id) {
        var me = this;

        var mask = AIDRFMFunctions.getMask(true, 'Stopping collection ...');
        mask.show();

        Ext.Ajax.request({
            url: 'protected/collection/stop.action',
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
                    me.updateLastRefreshDate();
                    if (resp.data) {
                        var data = resp.data;
                        me.updateCollectionInfo(data);
                    }
                } else {
                    AIDRFMFunctions.setAlert("Error", resp.message);
                    AIDRFMFunctions.reportIssue(response);
                }
            }
        });
    },

    startCollection: function (id) {
        var mask = AIDRFMFunctions.getMask(true, 'Starting collection ...');
        mask.show();

        var me = this;
        Ext.Ajax.request({
            url: 'protected/collection/start.action',
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
                    me.updateLastRefreshDate();
                    if (resp.data) {
                        var data = resp.data;
                        me.updateCollectionInfo(data);
                    }

                    var ranOnce = false;
                    var task = Ext.TaskManager.start({
                        run: function () {
                            if (ranOnce) {
                                me.refreshButtonAction();
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
            }});
    },

    updateCollectionInfo: function(data) {
        var id = data.id
        if (id) {
            var statusField = document.getElementById("statusField_" + id),
                docCountField = document.getElementById("docCountField_" + id),
                lastDocField = document.getElementById("lastDocField_" + id),
                docCount = data.count ? Ext.util.Format.number(data.count,'0,000') : 0,
                lastDoc = data.lastDocument ? data.lastDocument : "<span class='na-text'>N/A</span>";
                
            statusField.innerHTML = AIDRFMFunctions.getStatusWithStyle(data.status);
            docCountField.innerHTML = 'Downloaded ' + this.getItemName(data.collectionType, true) + ':&nbsp;&nbsp;&nbsp' + docCount;
            lastDocField.innerHTML = 'Last downloaded ' + this.getItemName(data.collectionType) + ':&nbsp;&nbsp;&nbsp;<span class="tweet">' + lastDoc + '</span>';

            this.updateStartStopButtonsState(data.status, id);
        }
    },

    getItemName: function (r, plural) {
        if (plural)
            return r == 'Twitter' ? "tweets" : "sms";
        else
            return r == 'Twitter' ? "tweet" : "sms";
    },

    updateLastRefreshDate: function() {
        this.mainComponent.collectionDescription.setText('Status as of ' + Ext.Date.format(new Date(), 'F j, Y, g:i:s A'));
    },

    updateStartStopButtonsState: function(status, id) {
        var buttonStart = document.getElementById("buttonStart_" + id),
            buttonStop = document.getElementById("buttonStop_" + id);

        if (status == 'RUNNING_WARNING' || status == 'RUNNING' || status == 'INITIALIZING' || status == 'WARNING'){
            buttonStart.className = 'btn btn-green hidden';
            buttonStop.className = 'btn btn-red';
        } else {
            buttonStart.className = 'btn btn-green';
            buttonStop.className = 'btn btn-red  hidden';
        }
    },

    refreshButtonAction: function() {
        this.mainComponent.collectionStore.load();
    },

    refreshBothCollections: function(){
        var me = this;
        this.mainComponent.collectionStore.load();
        this.mainComponent.collectionTrashedStore.load(function(records, operation, success) {
            if(records.length == 0){
                var tabs = me.mainComponent.tabs;
                tabs.setActiveTab('myCollectionTab');
            }
        });
    },

    applyUserPermissions: function () {
        var me = this;

        Ext.Ajax.request({
            url:  'protected/user/getCurrentUserRoles.action',
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
                                me.mainComponent.goToAdminSection.show();
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

    untrashCollection: function (collectionId, collectionCode) {
        var me = this;
        var id = collectionId;
        var code = collectionCode;

        var mask = AIDRFMFunctions.getMask();
        mask.show();

        Ext.Ajax.request({
            url: 'protected/collection/untrash.action',
            method: 'POST',
            params: {
                id: id,
                code: code
            },
            headers: {
                'Accept': 'application/json'
            },
            success: function (response) {
                mask.hide();
                var resp = Ext.decode(response.responseText);
                if (resp.success) {
                    me.refreshBothCollections();
                } else {
                    AIDRFMFunctions.setAlert("Error", resp.message);
                    AIDRFMFunctions.reportIssue(response);
                }
            },
            failure: function () {
                mask.hide();
            }
        });
    },
    enableTagger: function(crisisTypeID, code, name) {

        if (!crisisTypeID) {
            AIDRFMFunctions.setAlert("Error", "Collection type is not selected. Please select type of the collection and save it.");
            return false;
        }

        Ext.Ajax.request({
            url: 'protected/collection/classifier/enable',
            method: 'POST',
            params: {
                collectionCode: Ext.String.trim( code )
            },
            headers: {
                'Accept': 'application/json'
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
    }

});