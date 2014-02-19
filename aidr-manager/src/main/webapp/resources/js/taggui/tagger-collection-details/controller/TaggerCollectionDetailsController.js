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

            '#gridTrigger' : {
                keyup : this.onTriggerKeyUp,
                triggerClear : this.onTriggerClear
            }

        });

    },

    beforeRenderView: function (component, eOpts) {
        AIDRFMFunctions.initMessageContainer();

        this.mainComponent = component;
        taggerCollectionDetailsController = this;
        this.getTemplateStatus();

        this.generateCSVLink();
        this.generateTweetIdsLink();
        this.loadLatestTweets();

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
                                var title =  "Help us classifying tweets related to " + CRISIS_NAME;
                                var twitterURL = "https://twitter.com/intent/tweet?text="+title+"&url=" + data.url;
                                var facebookURL= "https://www.facebook.com/sharer/sharer.php?t="+title+"&u=" + data.url;
                                var googlePlusURL= "https://plus.google.com/share?url="+data.url;
                                var pinterestURL= "http://www.pinterest.com/pin/create/button/?media=IMAGEURL&description="+title+"&url=" + data.url;
                                me.mainComponent.pyBossaLink.setText('<div class="gray-backgrpund"><a href="' + data.url + '"><i>' + data.url + '</i></a></div>', false);
                                me.mainComponent.twitterLink.setText('<a href="'+ twitterURL +'"><image src="/AIDRFetchManager/resources/img/icons/twitter-icon.png" /></a>', false);
                                me.mainComponent.facebookLink.setText('<a href="'+ facebookURL +'"><image src="/AIDRFetchManager/resources/img/icons/facebook-icon.png" /></a>', false);
                                me.mainComponent.googlePlusLink.setText('<a href="'+ googlePlusURL +'"><image src="/AIDRFetchManager/resources/img/icons/google-icon.png" /></a>', false);
                                me.mainComponent.pinterestLink.setText('<a href="'+ pinterestURL +'"><image src="/AIDRFetchManager/resources/img/icons/pinterest-icon.png" /></a>', false);

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

    generateCSVLink: function() {
        var me = this;
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
                var resp = Ext.decode(response.responseText);
                if (resp.success) {
                    if (resp.data && resp.data != '') {
                        me.mainComponent.CSVLink.setText('<div class="styled-text download-link">&#8226;&nbsp;<a href="' + resp.data + '">Download latest 100,000 tweets</a></div>', false);
                    } else {
                        me.mainComponent.CSVLink.setText('<div class="styled-text download-link">&#8226;&nbsp;Download latest 100,000 tweets - Not yet available for this crisis.</div>', false);
                    }
                } else {
                    me.mainComponent.CSVLink.setText('', false);
                    AIDRFMFunctions.setAlert("Error", resp.message);
                }
            }
        });
    },

    generateTweetIdsLink: function() {
        var me = this;
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
                var resp = Ext.decode(response.responseText);
                if (resp.success) {
                    if (resp.data && resp.data != '') {
                        me.mainComponent.tweetsIdsLink.setText('<div class="styled-text download-link">&#8226;&nbsp;<a href="' + resp.data + '">Download all tweets (tweet-ids only)</a></div>', false);
                    } else {
                        me.mainComponent.tweetsIdsLink.setText('<div class="styled-text download-link">&#8226;&nbsp;Download all tweets (tweet-ids only) - Not yet available for this crisis.</div>', false);
                    }
                } else {
                    me.mainComponent.tweetsIdsLink.setText('', false);
                    AIDRFMFunctions.setAlert("Error", resp.message);
                }
            }
        });
    },

    loadLatestTweets: function () {
        var me = this;

        Ext.Ajax.request({
            url: BASE_URL + '/protected/tagger/loadLatestTweets.action',
            method: 'GET',
            params: {
                code: CRISIS_CODE
//                code: "2014-02-uk_floods"
            },
            headers: {
                'Accept': 'application/json'
            },
            success: function (response) {
                var jsonData = Ext.decode(response.responseText);
                var tweetData = Ext.JSON.decode(jsonData.data);

                var data = me.transformTweetData(tweetData);
                fetchData = data;
                fetchTmpData = Ext.clone(data);

                me.mainComponent.taggerFetchStore.setProxy({
                    type: 'pagingmemory',
                    data: fetchTmpData,
                    reader: {
                        type: 'json',
                        totalProperty: 'totalCount',
                        root: 'data',
                        successProperty: 'success'
                    }
                });
                me.mainComponent.taggerFetchStore.load();
            }
        });
    },

    transformTweetData: function(tweetData) {
        var result = {};
        var data = [];
        Ext.Array.each(tweetData, function(r, index) {
            var row = {};
            row.text = r.text ? r.text : '';
            row.attribute_name = r.nominal_labels[0].attribute_name ? r.nominal_labels[0].attribute_name : '';
            row.label_name = r.nominal_labels[0].label_name ? r.nominal_labels[0].label_name : '';
            row.confidence = r.nominal_labels[0].confidence ? r.nominal_labels[0].confidence : '';
            data.push(row);
        });
        result.data = data;
        result.totalCount = data.length;
        result.success = true;
        return result;
    },

    onTriggerKeyUp : function(t) {
        var me = this;

        var thisRegEx = new RegExp(t.getValue(), "i");
        var grid = me.mainComponent.taggerFetchGrid;
        var records = [];
        Ext.each(fetchData.data, function (record) {
            if (thisRegEx.test(record[grid.columns[0].dataIndex])) {
                if (!grid.filterHidden && grid.columns[0].isHidden()) {
                } else {
                    records.push(record);
                }
            }
        });
        fetchTmpData.data = records;
        fetchTmpData.totalCount = records.length;
        me.mainComponent.taggerFetchStore.load();
    },

    onTriggerClear : function() {
        var me = this;

        fetchTmpData.data = fetchData.data;
        fetchTmpData.totalCount = fetchData.totalCount;
        me.mainComponent.taggerFetchStore.load();
    }

});