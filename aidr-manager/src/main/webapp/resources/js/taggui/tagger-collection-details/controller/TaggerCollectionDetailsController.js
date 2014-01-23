Ext.define('TAGGUI.tagger-collection-details.controller.TaggerCollectionDetailsController', {
    extend: 'Ext.app.Controller',

    views: [
        'TaggerCollectionDetailsPanel'
    ],

    init: function () {

        this.control({

            'tagger-collection-details-view': {
                beforerender: this.beforeRenderView
            },

            "#crisisDelete": {
                click: function (btn, e, eOpts) {
                    this.crisisDelete();
                }
            },

            "#crisisSave": {
                click: function (btn, e, eOpts) {
                    this.crisisSave();
                }
            },

            "#goToCollector": {
                click: function (btn, e, eOpts) {
                    this.goToCollector();
                }
            },

            "#addNewClassifier": {
                click: function (btn, e, eOpts) {
                    this.addNewClassifier();
                }
            },

            "#goFetchLink": {
                click: function (btn, e, eOpts) {
                    this.goToFetchLink();
                }
            },

            "#goRealtimeLink": {
                click: function (btn, e, eOpts) {
                    this.goToRealtTimeLink();
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
            }

        });

    },

    beforeRenderView: function (component, eOpts) {
        AIDRFMFunctions.initMessageContainer();

        this.mainComponent = component;
        taggerCollectionDetailsController = this;
        this.getTemplateStatus();
        this.updateDownloadPanel();

        var me = this;
    },

    crisisDelete: function () {
        Ext.MessageBox.confirm('Confirm Crisis Delete', 'Do you want to delete <b>"' + CRISIS_NAME + '"</b>?',
            function (buttonId) {
            if (buttonId === 'yes') {
                AIDRFMFunctions.setAlert("Ok", 'Will be implemented later');
            }
        });
    },

    crisisSave: function () {
        var me = this;

        var crisisTypeId = me.mainComponent.crysisTypesCombo.getValue();
        var crisisTypeName = me.mainComponent.crisisTypesStore.findRecord("crisisTypeID", crisisTypeId).data.name;

        Ext.Ajax.request({
            url: BASE_URL + '/protected/tagger/updateCrisis.action',
            method: 'POST',
            params: {
                crisisID: CRISIS_ID,
                crisisTypeID: crisisTypeId,
                crisisTypeName: Ext.String.trim( crisisTypeName )
            },
            headers: {
                'Accept': 'application/json'
            },
            success: function (resp) {
                var response = Ext.decode(resp.responseText);
                if (response.success) {
                    me.mainComponent.saveButton.hide();
                    CRISIS_TYPE_ID = crisisTypeId;
                } else {
                    AIDRFMFunctions.setAlert("Error", 'Error while saving crisis.');
                }
            }
        });
    },

    goToFetchLink: function() {
        document.location.href ='http://aidr-dev.qcri.org/AIDROutput/aidrTaggerLatest.html?crisisCode=' + CRISIS_CODE ;
    },

    goToRealtTimeLink: function() {
        document.location.href ='http://aidr-dev.qcri.org/AIDROutput/aidrTaggerStream.html?crisisCode=' + CRISIS_CODE ;
    },

    goToCollector: function() {
        document.location.href = BASE_URL + '/protected/' + CRISIS_CODE +'/collection-details';
    },

    addNewClassifier: function() {
        document.location.href = BASE_URL + "/protected/" + CRISIS_CODE + '/predict-new-attribute';
    },

    getTemplateStatus: function() {
        var me = this;

        Ext.Ajax.request({
            url: BASE_URL + '/protected/tagger/getTemplateStatus.action',
            method: 'GET',
            params: {
                code: CRISIS_CODE
            },
            headers: {
                'Accept': 'application/json'
            },
            success: function (response) {
                var resp = Ext.decode(response.responseText);
                if (resp.success && resp.data) {
                    try {
                        var data = Ext.JSON.decode(resp.data);
                        if (data && data.status) {
                            if (data.status == 'ready') {
                                me.mainComponent.pyBossaLink.setText('<div class="gray-backgrpund"><a href="' + data.url + '"><i>' + data.url + '</i></a></div>', false);
                            } else if (data.status == 'not_ready') {
                                me.mainComponent.pyBossaLink.setText('<div class="gray-backgrpund"><i>' + data.message + '</i></div>', false);
                            }
                        }
                    } catch (e) {
                        me.mainComponent.pyBossaLink.setText('<div class="gray-backgrpund"><i>Initializing crowdsourcing task. Please come back in a few minutes.</i></div>', false);
                    }
                } else {
                    me.mainComponent.pyBossaLink.setText('<div class="gray-backgrpund"><i>Initializing crowdsourcing task. Please come back in a few minutes.</i></div>', false);
                }
            },
            failure: function () {
                me.mainComponent.pyBossaLink.setText('<div class="gray-backgrpund"><i>Initializing crowdsourcing task. Please come back in a few minutes.</i></div>', false);
            }
        });
    },

    updateDownloadPanel: function () {
        var downloadTabText = '<div class="styled-text">You can read the collected tweets from:<br><br>' +
            '<b>1.</b>&nbsp;&nbsp;Tweets in JSON format: <a href="http://scd1.qcri.org/aidr/data/persister/' + CRISIS_CODE + '">http://scd1.qcri.org/aidr/data/persister/' + CRISIS_CODE + '/</a><br>' +
            '<b>2.</b>&nbsp;&nbsp;Redis queue FetcherChannel.' + CRISIS_CODE + ' on host scd1.qcri.org port 6379<br></div>';

        this.mainComponent.downloadText.setText(downloadTabText, false);
    },

    generateCSVLink: function(btn) {
        var me = this;
        btn.setDisabled(true);
        me.mainComponent.CSVLink.setText('<div class="loading-block"></div>', false);

        Ext.Ajax.request({
            url: BASE_URL + '/protected/tagger/taggerGenerateCSVLink.action',
            method: 'GET',
            params: {
                code: CRISIS_CODE
            },
            headers: {
                'Accept': 'application/json'
            },
            success: function (response) {
                btn.setDisabled(false);
                var resp = Ext.decode(response.responseText);
                if (resp.success) {
                    if (resp.data && resp.data != '') {
                        me.mainComponent.CSVLink.setText('<div class="styled-text download-link"><a href="' + resp.data + '">' + resp.data + '</a></div>', false);
                    } else {
                        me.mainComponent.CSVLink.setText('', false);
                        AIDRFMFunctions.setAlert("Error", "Generate CSV service returned empty url. For further inquiries please contact admin.");
                    }
                } else {
                    me.mainComponent.CSVLink.setText('', false);
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
        me.mainComponent.tweetsIdsLink.setText('<div class="loading-block"></div>', false);

        Ext.Ajax.request({
            url: BASE_URL + '/protected/tagger/taggerGenerateTweetIdsLink.action',
            method: 'GET',
            params: {
                code: CRISIS_CODE
            },
            headers: {
                'Accept': 'application/json'
            },
            success: function (response) {
                btn.setDisabled(false);
                var resp = Ext.decode(response.responseText);
                if (resp.success) {
                    if (resp.data && resp.data != '') {
                        me.mainComponent.tweetsIdsLink.setText('<div class="styled-text download-link"><a href="' + resp.data + '">' + resp.data + '</a></div>', false);
                    } else {
                        me.mainComponent.tweetsIdsLink.setText('', false);
                        AIDRFMFunctions.setAlert("Error", "Generate Tweet Ids service returned empty url. For further inquiries please contact admin.");
                    }
                } else {
                    me.mainComponent.tweetsIdsLink.setText('', false);
                    AIDRFMFunctions.setAlert("Error", resp.message);
                }
            },
            failure: function () {
                btn.setDisabled(false);
            }
        });
    }

});