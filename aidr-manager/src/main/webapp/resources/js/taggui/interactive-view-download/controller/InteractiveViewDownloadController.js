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
            },

            "#resetFilters": {
                click: function (btn, e, eOpts) {
                    this.resetFiltersHandler(btn);
                }
            },

            "#addFilter": {
                click: function (btn, e, eOpts) {
                    this.addFilterHandler(btn);
                }
            },

            "#filterBlock": {
                filterchange: function (isValid) {
                    this.reflectFilterBlockState(isValid);
                }
            }

        });

    },

    beforeRenderView: function (component, eOpts) {
        AIDRFMFunctions.initMessageContainer();
        this.mainComponent = component;

        this.loadCollection();
        this.mainComponent.constraintsString = '{"constraints":[]}';
        this.loadLatestTweets();
        this.getAttributesAndLabelsByCrisisId();
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
        var mask = AIDRFMFunctions.getMask(true);
        mask.show();

        Ext.Ajax.request({
            url: BASE_URL + '/protected/tagger/loadLatestTweets.action',
            method: 'GET',
            params: {
                code: CRISIS_CODE,
//                code: "2014-02-uk_floods"
//                code: "2014-03-mh370"
//                code: "2014-04-mers"
                constraints: me.mainComponent.constraintsString
            },
            headers: {
                'Accept': 'application/json'
            },
            success: function (response) {
                var jsonData = Ext.decode(response.responseText);
                if (jsonData.data == ""){
                    return true;
                }
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
                mask.hide();
            },
            failure: function () {
                mask.hide();
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
                if (r.nominal_labels[0].confidence) {
                    var confidence = (r.nominal_labels[0].confidence * 100).toFixed(2);
                    row.confidence = confidence + '%';
                } else {
                    row.confidence = '0%';
                }
                if (r.created_at) {
                    row.createdAt = moment(r.created_at).format("YYYY-MM-DD HH:mm Z");
                } else {
                    row.createdAt = "<span class='na-text'>Not specified</span>";
                }
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
                me.updateStatusInfo(jsonData.status, jsonData.endDate);
            }
        });
    },

    updateStatusInfo: function(status, endDate){
        var statusText = '';

        if (status == 'RUNNING-WARNNING' || status == 'RUNNING' || status == 'INITIALIZING'){
            statusText = '<div class="styled-text">Status: running</div>';
        } else {
            if (endDate) {
                statusText = '<div class="styled-text">This collection is not running since ' + moment(endDate).calendar() +'.</div>';
            } else {
                statusText = '<div class="styled-text">This collection was never running.</div>';
            }
        }

        this.mainComponent.statusL.setText(statusText, false);
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

    downloadButtonHandler: function(btn){
        var me = this;
        var value = me.mainComponent.downloadType.getValue();

        if (!value && !value.type) {
            AIDRFMFunctions.setAlert("Error", "Type of the download not selected.");
        }

//        TODO finish this section - use correct ajax with constraints
//        var constraints = me.mainComponent.constraintsString;
//        console.log("Constraints: " + constraints);

        if (value.type == '1000_TWEETS'){
            console.log('1000_TWEETS');
        } else if (value.type == '100000_TWEETS') {
            console.log('100000_TWEETS');
        } else {
            console.log('ALL');
            me.generateTweetIdsLink(btn)
        }
    },

    applyFilterButtonHandler: function(){
        this.mainComponent.constraintsString = this.getAllFilters();
        this.loadLatestTweets();
        this.mainComponent.resetFiltersButton.enable();
    },

    getAllFilters: function(){
        var me = this;
        var constraints = [];
        Ext.each(me.mainComponent.filterBlock.items.items, function (r) {
            constraints.push(r.getValue());
        });
        var result = {
            constraints: constraints
        };

        return Ext.JSON.encode(result);
    },

    resetFiltersHandler: function (btn) {
        var me = this;
        me.mainComponent.suspendLayout = true;

        me.mainComponent.filterBlock.removeAll(true);
        me.mainComponent.filterBlock.insert(0, {
            rawData: me.mainComponent.attributesAndLabels
        });
        btn.disable();
//        Disable Apply Filter Button as we added new filter which is not valid yet
        me.mainComponent.applyFilterButton.disable();
        this.mainComponent.constraintsString = '{"constraints":[]}';
        this.loadLatestTweets();

        me.mainComponent.suspendLayout = false;
        me.mainComponent.forceComponentLayout();
    },

    addFilterHandler: function(){
        var me = this;
        me.mainComponent.suspendLayout = true;

        var position = me.mainComponent.filterBlock.items.length;
        me.mainComponent.filterBlock.insert(position, {
            rawData : me.mainComponent.attributesAndLabels
        });

//        Disable Apply Filter Button as we added new filter which is not valid yet
        me.mainComponent.applyFilterButton.disable();

        me.mainComponent.suspendLayout = false;
        me.mainComponent.forceComponentLayout();
    },

    generateTweetIdsLink: function(btn) {
        var me = this;

        btn.setDisabled(true);
        me.mainComponent.downloadLink.setText('<div class="loading-block"></div>', false);

        Ext.Ajax.request({
            url: BASE_URL + '/protected/collection/generateTweetIdsLink.action',
            method: 'GET',
            params: {
                code: CRISIS_CODE,
//                code: "2014-03-mh370",
//                code: "2014-04-mers",
                constraints: me.mainComponent.constraintsString
            },
            headers: {
                'Accept': 'application/json'
            },
            success: function (response) {
                btn.setDisabled(false);
                var resp = Ext.decode(response.responseText);
                if (resp.success) {
                    if (resp.data && resp.data != '') {
                        me.mainComponent.downloadLink.setText('<div class="styled-text download-link"><a href="' + resp.data + '">' + resp.data + '</a></div>', false);
                    } else {
                        me.mainComponent.downloadLink.setText('', false);
                        AIDRFMFunctions.setAlert("Error", "Generate Tweet Ids service returned empty url. For further inquiries please contact admin.");
                    }
                } else {
                    me.mainComponent.downloadLink.setText('', false);
                    AIDRFMFunctions.setAlert("Error", resp.message);
                }
            },
            failure: function () {
                btn.setDisabled(false);
            }
        });
    },

    getAttributesAndLabelsByCrisisId: function () {
        var me = this;

        Ext.Ajax.request({
            url: BASE_URL + '/protected/tagger/getAttributesAndLabelsByCrisisId.action',
            method: 'GET',
            params: {
                id: CRISIS_ID
//                id: 117
//                id: 71
//                id: 149
            },
            headers: {
                'Accept': 'application/json'
            },
            success: function (response) {
                var jsonData = Ext.decode(response.responseText);
                var data = Ext.JSON.decode(jsonData.data);
                me.mainComponent.attributesAndLabels = data;
                me.mainComponent.addFilterButton.enable();

                me.mainComponent.suspendLayout = true;
                me.mainComponent.filterBlock.insert(0, {
                    rawData : data
                });
                me.mainComponent.suspendLayout = false;
                me.mainComponent.forceComponentLayout();
            }
        });
    },

    reflectFilterBlockState: function(filterBlock){
        var isValid = true;

        Ext.each(filterBlock.items.items, function (r) {
            var v = r.isValid();
            if (!v){
                isValid = false;
            }
        });

        var me = this;
        if (isValid){
            me.mainComponent.applyFilterButton.enable();
        } else {
            me.mainComponent.applyFilterButton.disable();
        }
    }

});