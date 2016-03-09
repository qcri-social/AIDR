Ext.define('AIDRPUBLIC.interactive-view-download.controller.InteractiveViewDownloadController', {
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
        this.mainComponent.downloadTweetsDescription.setText('The entire collection has approximately ' + COLLECTION_COUNT + ' items in total.');
        this.loadLatestTweets();
        this.getAttributesAndLabelsByCrisisId();
    },

    onTriggerKeyUp : function(t) {
        var me = this;

        var thisRegEx = new RegExp(t.getValue(), "i");
        var grid = me.mainComponent.tweetsGrid;
        var records = [];
        Ext.each(tweetsData.data, function (record) {
            if (thisRegEx.test(record[grid.columns[1].dataIndex])) {
                if (!grid.filterHidden && grid.columns[1].isHidden()) {
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
        Ext.getBody().mask('Loading...');
        
        Ext.Ajax.timeout = 900000;
        Ext.override(Ext.form.Basic, {timeout: Ext.Ajax.timeout/1000});
        Ext.override(Ext.data.proxy.Server, {timeout: Ext.Ajax.timeout});
        Ext.override(Ext.data.Connection, {timeout: Ext.Ajax.timeout});
        Ext.Ajax.request({
            url: BASE_URL + '/public/collection/loadLatestTweets.action',
            timeout: 900000,
            method: 'GET',
            params: {
                code: CRISIS_CODE,
                constraints: me.mainComponent.constraintsString
            },
            headers: {
                'Accept': 'application/json'
            },
            success: function (response) {
                var jsonData = Ext.decode(response.responseText);
                if (jsonData.data == ""){
                    Ext.getBody().unmask();
                    return true;
                }
                var tweetData = Ext.JSON.decode(jsonData.data);

                  var columnsFromTweetData = me.getColumnsModelAndStoreKeysFromTweetData(tweetData);
                  var data = me.transformTweetData(tweetData);
                 data.metaData.fields = columnsFromTweetData.fields;
                tweetsData = data;
                tweetsTmpData = Ext.clone(data);

                me.mainComponent.tweetsStore.setProxy({
                    type: 'memory',
                    data: tweetsTmpData,
                    enablePaging:true,
                    reader: {
                        type: 'json',
                        totalProperty: 'totalCount',
                        rootProperty: 'data',
                        successProperty: 'success'
                    }
                });
                me.mainComponent.tweetsGrid.reconfigure(me.mainComponent.tweetsStore, columnsFromTweetData.columns);
                me.mainComponent.tweetsStore.load();
                Ext.getBody().unmask();
                return true;
            },
            failure: function () {
                Ext.getBody().unmask();
            }
        });
    },

     transformTweetData : function(tweetData) {
         var rv = {
             data: [],
             metaData: {}
         };
         Ext.Array.each(tweetData, function(tw, idx) {
             var row = {};
             row.text  = tw.text ? tw.text : '';
             if (tw.nominal_labels && tw.nominal_labels.length) {
                 for (var i = 0; i < tw.nominal_labels.length; i++) {
                     var classifier = tw.nominal_labels[i];
                     row[classifier.attribute_code] = classifier.label_name;
                     if((classifier.confidence * 100).toFixed(0) != 0){
                         row[classifier.attribute_code + '_confidence'] = (classifier.confidence * 100).toFixed(0) + '%';
                     }
                 }
             }
             if (tw.created_at) {
                 row.createdAt = moment(tw.created_at).format("YYYY-MM-DD HH:mm Z");
             } else {
                 row.createdAt = "<span class='na-text'>Not specified</span>";
             }
             rv.data.push(row);
         });
         rv.totalCount = rv.data.length;
         rv.success = true;
         return rv;
     },
     getColumnsModelAndStoreKeysFromTweetData: function (obj) {
         var columns = [
             {
                 xtype: 'gridcolumn', dataIndex: 'createdAt', text: 'Date/Time', width: 185
             },
             {
                 xtype: 'gridcolumn', dataIndex: 'text', text: Ext.util.Format.capitalize(COLLECTION_TYPES[TYPE]["plural"]), flex: 1,
                 renderer: function (value, meta, record) {
                    var tooltipText = value + "<br/>";
                     Ext.Object.each(record.data, function(key, val){
                         var attributeName = getAttributeNameByAttributeCode(key);
                         if(key != 'createdAt' && key != 'text' && key.indexOf('_confidence') == -1 && !Ext.isEmpty(checkAndGetVal(attributeName))){
                             tooltipText += "<br/>" + attributeName + ": " + val + " (" + record.data[key + '_confidence'] + ")";
                         }
                     });
                     if(tooltipText.indexOf("\"") !== -1) {
                         tooltipText = tooltipText.replace(/\"/g, "");
                     }
                     meta.tdAttr = 'data-qtip="' + tooltipText + '"';
                     return value;
                 }
             }
         ];
         var storeFields = [{name: 'createdAt'}, {name: 'text'}];
         if (obj && obj.length) {
             var tweet = obj[0];//Get only first tweet as amount of classifiers are the same for every tweet.
             if (tweet.nominal_labels && tweet.nominal_labels.length) {
                 for (var i = 0; i < tweet.nominal_labels.length; i++) {
                       var classifier = tweet.nominal_labels[i];
                       var classifierCol = {
                         xtype: 'gridcolumn',
                         text: checkAndGetVal(classifier.attribute_name),
                         dataIndex: classifier.attribute_code
                     }
                     var confidenceColumn = {
                         xtype: 'gridcolumn',
                         text: 'Conf.',
                         dataIndex: classifier.attribute_code + '_confidence',
                         renderer: function (value, meta, record) {
                             meta.style = "float:right; padding-top: 9px;";
                             return value;
                         }
                     }
                     storeFields.push({name:classifier.attribute_code});
                     storeFields.push({name:classifier.attribute_code + '_confidence'});
                     columns.push(classifierCol);
                     columns.push(confidenceColumn);
                 }
             }
         }
         function checkAndGetVal(val){
            return Ext.isEmpty(val) || val === "null" || val === "undefined" ? "" : val
         }
         function getAttributeNameByAttributeCode(attrCode){
             var rv = "";
             if (obj && obj.length) {
                 var tweet = obj[0];//Get only first tweet as amount of classifiers are the same for every tweet.
                 if (tweet.nominal_labels && tweet.nominal_labels.length) {
                     for (var i = 0; i < tweet.nominal_labels.length; i++) {
                        if(attrCode == tweet.nominal_labels[i].attribute_code){
                            rv = tweet.nominal_labels[i].attribute_name;
                         }
                     }
                 }
             }
             return rv;
         }
         return {columns:  columns, fields : storeFields};
     },
 /*
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

 */
    loadCollection: function () {
        var me = this;
       
        Ext.Ajax.request({
            url: BASE_URL + '/public/collection/findById.action',
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
                    me.mainComponent.updateLayout();
                }
                //Check whether the logged in user can download full tweets or not
                Ext.DOWNLOAD_ENABLED = me.isCurrentUserCanDownloadFullTweets(managers);
                if(!Ext.DOWNLOAD_ENABLED){
                	me.mainComponent.downloadContents.hide();
                }
                me.updateStatusInfo(jsonData.status, jsonData.endDate, jsonData.collectionType);
            }
        });
    },

    updateStatusInfo: function(status, endDate, collectionType){
        var statusText = '';

        if (status == 'RUNNING_WARNING' || status == 'RUNNING' || status == 'INITIALIZING'){
            var status = AIDRFMFunctions.getStatusWithStyle(status, collectionType);
            statusText = '<div class="styled-text">Status: <small>' + status +'</small></div>';
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
    
    isCurrentUserCanDownloadFullTweets: function(managers){
        var result = false;
        Ext.Array.each(managers, function(r) {
            if (r.userName == USER_NAME){
            	if(r.downloadPermitted){
            		 result = true;
            	}
            	return false; // to stop loop iteration
            }
        });
        return result;
    },

    downloadButtonHandler: function(btn){
        var me = this;
        var format = me.mainComponent.downloadFormat.getValue().format;
        var contents = me.mainComponent.downloadContents.getValue().contents;
        var url = '';
        var params = {
            code: CRISIS_CODE,
            count: Ext.getCmp('limit').value,
            removeRetweet: Ext.getCmp('retweet').value,
            queryString: me.mainComponent.constraintsString
        };

        if(!Ext.DOWNLOAD_ENABLED) {
        	contents = 'ids;'
        }
        
        if(format == 'csv'){
            if (contents == 'full') {
                url = '/protected/tagger/taggerGenerateCSVFilteredLink.action';
            } else {
                url = '/protected/tagger/taggerGenerateTweetIdsFilteredLink.action';
            }
        } else {
            params.jsonType = format;
            if (contents == 'full') {
                url = '/protected/tagger/taggerGenerateJSONFilteredLink.action';
            } else {
                url = '/protected/tagger/taggerGenerateJsonTweetIdsFilteredLink.action';
            }
        }

        btn.setDisabled(true);
        me.mainComponent.downloadLink.show();
        Ext.suspendLayouts();
        me.mainComponent.downloadLink.setText('<div class="loading-block"></div>', false);

        Ext.Ajax.timeout = 900000;
        Ext.override(Ext.form.Basic, {timeout: Ext.Ajax.timeout/1000});
        Ext.override(Ext.data.proxy.Server, {timeout: Ext.Ajax.timeout});
        Ext.override(Ext.data.Connection, {timeout: Ext.Ajax.timeout});
        Ext.Ajax.request({
            url: BASE_URL + url,
            timeout: 900000,
            method: 'POST',
            params: params,
            headers: {
                'Accept': 'application/json'
            },
            success: function (response) {
                btn.setDisabled(false);
                var resp = Ext.decode(response.responseText);
                if (resp.success) {
                    if (resp.data && resp.data != '') {
                        me.mainComponent.downloadLink.setText('<div class="styled-text download-link"><a target="_blank" href="' + resp.data + '">Download File</a></div>', false);
                    } else {
                        me.mainComponent.downloadLink.setText('', false);
                        AIDRFMFunctions.setAlert("Error", "Generate Tweet Ids service returned empty url. For further inquiries please contact admin.");
                        AIDRFMFunctions.reportIssue(response);
                    }
                } else {
                    me.mainComponent.downloadLink.setText('', false);
                    AIDRFMFunctions.setAlert("Error", resp.message);
                    //AIDRFMFunctions.reportIssue(response);
                }
            },
            failure: function () {
                btn.setDisabled(false);
            }
        });
    },

    applyFilterButtonHandler: function(){
    	this.mainComponent.downloadLink.hide();
        this.mainComponent.constraintsString = this.getAllFilters();
        this.mainComponent.downloadTweetsDescription.setText('The entire collection has approximately ' + COLLECTION_COUNT + ' items in total, which will be filtered using your criteria above.');
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
        this.mainComponent.downloadTweetsDescription.setText('The entire collection has approximately ' + COLLECTION_COUNT + ' items in total.');
        this.loadLatestTweets();

        me.mainComponent.suspendLayout = false;
        me.mainComponent.updateLayout();
        me.mainComponent.downloadLink.hide();
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
        me.mainComponent.updateLayout();
    },

    getAttributesAndLabelsByCrisisId: function () {
        var me = this;

        Ext.Ajax.request({
            url: BASE_URL + '/public/collection/getAttributesAndLabelsByCrisisId.action',
            method: 'GET',
            params: {
                id: CRISIS_ID
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
                me.mainComponent.updateLayout();
                 if(!Ext.isEmpty(data.curatorInfo)) {
                     me.mainComponent.curatorInfoR.setText(data.curatorInfo, false);
                     me.mainComponent.curatorInfoR.show();
                     me.mainComponent.contactOwnerL.hide();
                 } else {
                     me.mainComponent.curatorInfoR.hide();
                     me.mainComponent.contactOwnerL.show();
                 }
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