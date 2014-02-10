Ext.define('AIDRFM.home.controller.CollectionController', {
    extend: 'Ext.app.Controller',

    views: [
        'CollectionPanel'
    ],

    init: function () {

        this.control({

            'collection-view': {
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
                    document.location.href = BASE_URL + '/protected/tagger-home';
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
            url: 'collection/getRunningCollectionStatusByUser.action',
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
                }
            }
        });

    },

    stopCollection: function(id) {
        var me = this;

        var mask = AIDRFMFunctions.getMask(true, 'Stopping collection ...');
        mask.show();

        Ext.Ajax.request({
            url: 'collection/stop.action',
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
                }
            }
        });
    },

    startCollection: function (id) {
        var mask = AIDRFMFunctions.getMask(true, 'Starting collection ...');
        mask.show();

        var me = this;
        Ext.Ajax.request({
            url: 'collection/start.action',
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
            docCountField.innerHTML = 'Downloaded items:&nbsp;&nbsp;&nbsp' + docCount;
            lastDocField.innerHTML = 'Last downloaded item:&nbsp;&nbsp;&nbsp;<span class="tweet">' + lastDoc + '</span>';

            this.updateStartStopButtonsState(data.status, id);
        }
    },

    updateLastRefreshDate: function() {
        this.mainComponent.collectionDescription.setText('Status as of ' + Ext.Date.format(new Date(), "Y F d h:i:s A"));
    },

    updateStartStopButtonsState: function(status, id) {
        var buttonStart = document.getElementById("buttonStart_" + id),
            buttonStop = document.getElementById("buttonStop_" + id);

        if (status == 'RUNNING-WARNNING' || status == 'RUNNING' || status == 'INITIALIZING'){
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

    applyUserPermissions: function () {
        var me = this;

        Ext.Ajax.request({
            url:  BASE_URL + '/protected/user/getCurrentUserRoles.action',
            method: 'GET',
            headers: {
                'Accept': 'application/json'
            },
            success: function (response) {
                var response = Ext.decode(response.responseText);
                if (response.data && response.data.roles) {
                    var roles = response.data.roles;
                    if (Ext.isArray(roles)) {
                        Ext.each(roles, function (role) {
                            if (role.name && role.name == 'ADMIN'){
                                me.mainComponent.goToAdminSection.show();
                            }
                        })
                    }
                } else {
                    AIDRFMFunctions.setAlert('Error', 'Collection Code already exist. Please select another code');
                }
            }
        });
    }

});