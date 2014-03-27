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
            }

        });

    },

    beforeRenderView: function (component, eOpts) {
        AIDRFMFunctions.initMessageContainer();
        this.mainComponent = component;

//        this.loadCollectionData();
        this.loadLatestTweets();
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
    }

});