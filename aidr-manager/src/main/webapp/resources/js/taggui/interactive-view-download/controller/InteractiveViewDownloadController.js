Ext.define('TAGGUI.interactive-view-download.controller.InteractiveViewDownloadController', {
    extend: 'Ext.app.Controller',

    views: [
        'InteractiveViewDownloadPanel'
    ],

    init: function () {

        this.control({

            'interactive-view-download-view': {
                beforerender: this.beforeRenderView
            },

            '#gridTrigger' : {
                keyup : this.onTriggerKeyUp,
                triggerClear : this.onTriggerClear
            },

            "#downloadButton": {
                click: function (btn, e, eOpts) {
                    this.downloadButtonHandler(btn);
                }
            },

            "#applyFilterButton": {
                click: function (btn, e, eOpts) {
                    this.applyFilterButtonHandler(btn);
                }
            }

        });

    },

    beforeRenderView: function (component, eOpts) {
        AIDRFMFunctions.initMessageContainer();
        this.mainComponent = component;

        this.loadCollection();
        this.loadLatestTweets();
    },

    onTriggerKeyUp : function(t) {
        var me = this;

        var thisRegEx = new RegExp(t.getValue(), "i");
        var grid = me.mainComponent.tweetsGrid;
        var records = [];
        Ext.each(tweetsData.data, function (record) {
            if (thisRegEx.test(record[grid.columns[0].dataIndex])) {
                if (!grid.filterHidden && grid.columns[0].isHidden()) {
                } else {
                    records.push(record);
                }
            }
        });
        tweetsTmpData.data = records;
        tweetsTmpData.totalCount = records.length;
        me.mainComponent.tweetsStore.load();
    },

    onTriggerClear : function() {
        var me = this;

        tweetsTmpData.data = tweetsData.data;
        tweetsTmpData.totalCount = tweetsData.totalCount;
        me.mainComponent.tweetsStore.load();
    },

    loadLatestTweets: function () {
        var me = this;

        Ext.Ajax.request({
            url: BASE_URL + '/protected/tagger/loadLatestTweets.action',
            method: 'GET',
            params: {
                code: CRISIS_CODE
//                code: "2014-02-uk_floods"
//                code: "2014-03-mh370"
            },
            headers: {
                'Accept': 'application/json'
            },
            success: function (response) {
                var jsonData = Ext.decode(response.responseText);
                var tweetData = Ext.JSON.decode(jsonData.data);

                var data = me.transformTweetData(tweetData);
                tweetsData = data;
                tweetsTmpData = Ext.clone(data);

                me.mainComponent.tweetsStore.setProxy({
                    type: 'pagingmemory',
                    data: tweetsTmpData,
                    reader: {
                        type: 'json',
                        totalProperty: 'totalCount',
                        root: 'data',
                        successProperty: 'success'
                    }
                });
                me.mainComponent.tweetsStore.load();
            }
        });
    },

    transformTweetData: function(tweetData) {
        var result = {};
        var data = [];
        Ext.Array.each(tweetData, function(r, index) {
            if (r.text && r.nominal_labels) {
                var row = {};
                row.text = r.text ? r.text : '';
                row.attribute_name = r.nominal_labels[0].attribute_name ? r.nominal_labels[0].attribute_name : '';
                row.label_name = r.nominal_labels[0].label_name ? r.nominal_labels[0].label_name : '';
                row.confidence = r.nominal_labels[0].confidence ? r.nominal_labels[0].confidence : '';
                data.push(row);
            }
        });
        result.data = data;
        result.totalCount = data.length;
        result.success = true;
        return result;
    },

    loadCollection: function () {
        var me = this;

        Ext.Ajax.request({
            url: BASE_URL + '/protected/collection/findById.action',
            method: 'GET',
            params: {
                id: COLLECTION_ID
            },
            headers: {
                'Accept': 'application/json'
            },
            success: function (response) {
                var jsonData = Ext.decode(response.responseText);

                var managers = jsonData.managers;
                var ownerUserName = jsonData.user.userName;
                if (!me.isCurrentUserManagerOrOwner(managers, ownerUserName)){
                    me.mainComponent.suspendLayout = true;
                    me.mainComponent.downloadPanel.hide();

                    var contactToText = 'Contact <a target="_blank" href="https://twitter.com/' + ownerUserName + '">&#64;' + ownerUserName + '</a> to get download permissions.';
                    me.mainComponent.contactOwnerL.setText(contactToText, false);

                    me.mainComponent.contactOwnerPanel.show();
                    me.mainComponent.suspendLayout = false;
                    me.mainComponent.forceComponentLayout();
                }
            }
        });
    },

    isCurrentUserManagerOrOwner: function(managers, ownerUserName){
        if (ownerUserName == USER_NAME){
            return true;
        }
        var result = false;
        Ext.Array.each(managers, function(r) {
            if (r.userName == USER_NAME){
                result = true;
                return false; // to stop loop iteration
            }
        });
        return result;
    },

    downloadButtonHandler: function(){
        alert("Will be implemented soon.");
    },

    applyFilterButtonHandler: function(){
        alert("Will be implemented soon.");
    }

});